package beans;

import db.SQLQueriesHelper;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Денис on 18.05.2016.
 */
@Singleton
public class ManageVipAdvertsBean {

    @Schedule(hour = "23", minute = "59", second = "00")
    public void runReports() {
        try (Connection connection = db.DataSource.getInstance().getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQLQueriesHelper.deleteVipAdverts());
        } catch (SQLException | PropertyVetoException | IOException e) {
            e.printStackTrace();
        }
    }
}
