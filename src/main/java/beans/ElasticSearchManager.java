package beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import db.SQLQueriesHelper;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import serializers.AdvertSerializer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
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
    @Resource(name = "elastic.reindex-while-starting")
    private Boolean isNeedReindexWhileStarting;

    @PostConstruct
    private void createClientInstance() {
        try {
            client = TransportClient.builder().build().addTransportAddress(
                    new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300)
            );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        if (isNeedReindexWhileStarting) {
            reindex();
        }
    }

    @PreDestroy
    private void cleanUpResources() {
        this.client.close();
    }

    public Client getClient() {
        if (client == null) {
            try {
                client = TransportClient.builder().build().addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300)
                );
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

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

        JsonObject mappings = new JsonObject();
        JsonObject advert = new JsonObject();
        JsonObject properties = new JsonObject();
        JsonObject map_coordinates = new JsonObject();

        map_coordinates.addProperty("type", "geo_point");
        properties.add("map_coordinates", map_coordinates);
        advert.add("properties", properties);
        mappings.add("advert", advert);

        client.admin().indices()
                .prepareCreate("adverts")
                .addMapping("advert", gson.toJson(mappings))
                .execute()
                .actionGet();

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
