package beans;

import javax.ejb.Local;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Денис on 20.04.2016.
 */
@Local
public interface AdvertsManager {
    ArrayList[] getAllCategories() throws PropertyVetoException, SQLException, IOException;
    ArrayList<String[]> getAttributes(String adCategoryId) throws IOException, SQLException, PropertyVetoException;
    ArrayList<String[]> getFirstLevelCategories(String adCategoryId, String adCategoryName) throws SQLException, IOException, PropertyVetoException;
    ArrayList<String> getParentCategories(String adCategoryId, String adCategoryName) throws SQLException, IOException, PropertyVetoException;
    ArrayList<String> getSubCategories(String adCategoryId) throws PropertyVetoException, SQLException, IOException;
    String getAdCategoryId(String adCategoryName) throws SQLException, IOException, PropertyVetoException;
}
