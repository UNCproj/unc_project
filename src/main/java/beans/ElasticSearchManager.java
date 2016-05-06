package beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import db.SQLQueriesHelper;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import serializers.AdvertSerializer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Денис on 10.04.2016.
 */
@Startup
@Singleton
public class ElasticSearchManager {
    private Client client;
    @EJB
    private AdvertsManager advertsManager;

    @PostConstruct
    private void createClientInstance() throws UnknownHostException {
        client = TransportClient.builder().build().addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("vinokurov2.no-ip.biz"), 9300)
        );
        reindex();
    }

    @PreDestroy
    private void cleanUpResources() {
        this.client.close();
    }

    public Client getClient() {
        return client;
    }

    public void reindex() {
        deleteIndex();
        createIndex();
    }

    public void createIndex() {
        ArrayList<AdvertBean> indexingAdverts = new ArrayList<>();

        try {
            indexingAdverts = advertsManager.getAdverts(SQLQueriesHelper.ADVERT_TYPE_ID, true);
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.getMessage();
            //TODO: logging
        }

        HashMap<String, String> attrsPseudonims = new HashMap<>();
        attrsPseudonims.put("object_id", "id");
        attrsPseudonims.put("object_name", "name");
        attrsPseudonims.put("type_id", "category");

        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(AdvertBean.class, new AdvertSerializer(attrsPseudonims))
                .create();

        client.admin().indices().prepareCreate("adverts").setSettings(
                Settings.builder().loadFromSource(
                        "{\"analysis\": {\n" +
                        "            \"analyzer\": {\n" +
                        "                \"ru\": {\n" +
                        "                    \"type\": \"custom\",\n" +
                        "                    \"tokenizer\": \"standard\",\n" +
                        "                    \"filter\": [\"lowercase\", \"russian_morphology\", \"english_morphology\", \"ru_stopwords\"]\n" +
                        "                }\n" +
                        "            },\n" +
                        "            \"filter\": {\n" +
                        "                \"ru_stopwords\": {\n" +
                        "                    \"type\": \"stop\",\n" +
                        "                    \"stopwords\": \"а,без,более,бы,был,была,были,было,быть,в,вам,вас,весь,во,вот,все,всего,всех,вы,где,да,даже,для,до,его,ее,если,есть,еще,же,за,здесь,и,из,или,им,их,к,как,ко,когда,кто,ли,либо,мне,может,мы,на,надо,наш,не,него,нее,нет,ни,них,но,ну,о,об,однако,он,она,они,оно,от,очень,по,под,при,с,со,так,также,такой,там,те,тем,то,того,тоже,той,только,том,ты,у,уже,хотя,чего,чей,чем,что,чтобы,чье,чья,эта,эти,это,я,a,an,and,are,as,at,be,but,by,for,if,in,into,is,it,no,not,of,on,or,such,that,the,their,then,there,these,they,this,to,was,will,with\"\n" +
                        "                },\n" +
                        "                \"ru_stemming\": {\n" +
                        "                    \"type\": \"snowball\",\n" +
                        "                    \"language\": \"Russian\"\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }}"
                )
        ).execute().actionGet();

        for (AdvertBean adv: indexingAdverts) {
            String advertJson = gson.toJson(adv);

            IndexResponse response = client
                    .prepareIndex("adverts", "advert")
                    .setSource(advertJson)
                    .get();

            String id = response.getId();
        }
    }

    public void deleteIndex() {
        DeleteIndexResponse delete = client.admin()
                .indices()
                .delete(
                        new DeleteIndexRequest("_all")
                )
                .actionGet();
    }
}
