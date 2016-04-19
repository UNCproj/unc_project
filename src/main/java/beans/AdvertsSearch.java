package beans;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;

import javax.ejb.Local;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Денис on 04.04.2016.
 */
@Local
public interface AdvertsSearch {
    SearchResponse advertsSearch(QueryBuilder query,
                                 String adCategoryId, String adCategoryName, String sortingParam,
                                 String sortingOrder, String[] additionalAttributes,
                                 String adsStartingNum, String adsCount)
            throws PropertyVetoException, SQLException, IOException;

    ArrayList[] getAllCategories() throws PropertyVetoException, SQLException, IOException;
    ArrayList<String[]> getAttributes(String adCategoryId) throws IOException, SQLException, PropertyVetoException;
    ArrayList<String[]> getFirstLevelCategories(String adCategoryId, String adCategoryName) throws SQLException, IOException, PropertyVetoException;
    ArrayList<String> getParentCategories(String adCategoryId, String adCategoryName) throws SQLException, IOException, PropertyVetoException;

    void runIndexer();
}
