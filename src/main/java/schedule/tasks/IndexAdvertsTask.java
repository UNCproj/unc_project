package schedule.tasks;

import beans.Advert;
import beans.AdvertBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import db.DataSource;
import db.SQLQueriesHelper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;

import javax.servlet.ServletException;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.elasticsearch.node.NodeBuilder.*;

/**
 * Created by Денис on 21.03.2016.
 */
public class IndexAdvertsTask implements Runnable {

    private boolean isIndexed = false;

    @Override
    public void run() {
        indexAdverts();
    }

    public void indexAdverts() {
        Node node = nodeBuilder()
                .settings(Settings.settingsBuilder().put("http.enabled", false))
                .client(true)
                .node();

        Client client = node.client();

        ArrayList<AdvertBean> indexingAdverts = new ArrayList<>();

        try {
            indexingAdverts = getAdverts("4", "0", null, null, null, null);
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.getMessage();
            //TODO: logging
        }

        for (AdvertBean adv: indexingAdverts) {
            Gson gson = new GsonBuilder().create();
            String advertJson = gson.toJson(adv);

            IndexResponse response = client.prepareIndex("adverts", "advert")
                    .setSource(advertJson)
                    .get();

            String id = response.getId();
            id.charAt(0);
        }

        isIndexed = true;
        node.close();
    }

    public boolean isIndexed() {
        return isIndexed;
    }


    private ArrayList<AdvertBean> getAdverts(String adCategoryId, String adsStartingNum, String adsCount,
                                             String sortingParam, String sortingOrder, String adNamePattern)
            throws IOException, SQLException, PropertyVetoException {
        ArrayList<AdvertBean> adverts = new ArrayList<>();

        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement()
        ) {
            try (ResultSet childrenTypesResults = statement.executeQuery(SQLQueriesHelper.getTypeChildren(adCategoryId))) {
                ArrayList<String> categories = new ArrayList<>();

                while (childrenTypesResults.next()) {
                    categories.add(childrenTypesResults.getString("ot_id"));
                }

                try (
                        ResultSet advListResults = statement.executeQuery(
                                SQLQueriesHelper.getAdvertsList(categories.toArray(new String[0]),
                                        adsStartingNum, adsCount, sortingParam, sortingOrder, adNamePattern)
                        )
                ) {
                    while (advListResults.next()) {
                        AdvertBean adv = new AdvertBean();

                        adv.setId(advListResults.getString("object_id"));
                        adv.setName(advListResults.getString("object_name"));
                        adv.setCategory(advListResults.getString("type_id"));
                        adv.setDescription(advListResults.getString(SQLQueriesHelper.DESCRIPTION_ATTR));
                        adv.setCity(advListResults.getString(SQLQueriesHelper.CITY_ADVERT_ATTR));
                        adv.setPic(advListResults.getString("pic"));
                        adv.setPrice(advListResults.getString(SQLQueriesHelper.PRICE_ADVERT_ATTR));
                        adv.setRegistrationDate(advListResults.getString(SQLQueriesHelper.REG_DATE_ATTR));

                        adverts.add(adv);
                    }
                }
            }
        }

        return adverts;
    }
}
