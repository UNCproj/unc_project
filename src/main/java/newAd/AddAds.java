package newAd;

import db.DataSource;
import db.SQLQueriesHelper;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddAds {

    private String category;
    private String city;
    private String price;
    private String name;
    private String description;
    private String userId;
    private String advertId;


    public boolean inputNewAdvert(String cat, String cit, String pri, String nam, String des, String idd) throws SQLException, IOException, PropertyVetoException {

        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss");
        String date =  dateFormate.format(new Date());

        category = cat;
        city = cit;
        price = pri;
        name = nam;
        description = des;
        userId = idd;

        if (city != null && price !=null && name != null && userId != null) {
            Connection connection = null;
            try {
                connection = DataSource.getInstance().getConnection();

                Statement statementId = connection.createStatement();
                ResultSet resultSetId = statementId.executeQuery(SQLQueriesHelper.newId());
                resultSetId.next();
                advertId = resultSetId.getString("id");

                Statement statementAdvert =  connection.createStatement();
                statementAdvert.executeUpdate(SQLQueriesHelper.newAdvert(advertId, name));

                Statement statementReference = connection.createStatement();
                statementReference.executeUpdate(SQLQueriesHelper.newReference(advertId, userId, "11"));

                Statement statementAdvertCategory = connection.createStatement();
                statementAdvertCategory.executeUpdate(SQLQueriesHelper.insertAdvertsParams(advertId,category, "8"));

                Statement statementAdvertCity = connection.createStatement();
                statementAdvertCity.executeUpdate(SQLQueriesHelper.insertAdvertsParams(advertId,city,"9"));

                Statement statementAdvertPrice = connection.createStatement();
                statementAdvertPrice.executeUpdate(SQLQueriesHelper.insertAdvertsParams(advertId,price,"10"));

                Statement statementAdvertDescription = connection.createStatement();
                statementAdvertDescription.executeUpdate(SQLQueriesHelper.insertAdvertsParams(advertId, description, "7"));

                Statement statementAdvertDate = connection.createStatement();
                statementAdvertDate.executeUpdate((SQLQueriesHelper.insertDate(advertId, date)));

            }finally {
                connection.close();
            }
            return true;
        }

        return false;
    }
}
