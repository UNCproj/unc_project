package servlets;

import beans.AdvertsSearch;
import beans.AdvertsSearchBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import db.DataSource;
import db.SQLQueriesHelper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * Возможные значения параметра action:
 * get_adverts - получить список объявлений с заданными параметрами
 * get_adverts_autocomplete - получить список объявлений с параметрами для автодополнения
 * get_all_categories - получить список всех категорий (дети заданного типа и всех его потомков)
 * get_first_lvl_categories - получить список главных категорий (дети заданного типа)
 * get_adverts_count - получить количество объявлений с заданными параметрами
 * get_adverts_attributes - получить атрибуты заданного типа объявлений
 * get_attributes_autocomplete - получить автодополнение для атрибута
 * get_parent_categories - получить иерархию категорий от advert до заданного типа
 */
@WebServlet(name = "AdvertsListServlet", urlPatterns = "/advertsList")
public class AdvertsListServlet extends HttpServlet {
    @EJB
    private AdvertsSearch searchBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action.equals("get_adverts")) {
                getAdverts(request, response);
            } else if (action.equals("get_adverts_autocomplete")) {
                getAdvertsAutocomplete(request, response);
            } else if (action.equals("get_all_categories")) {
                getAllCategories(request, response);
            } else if (action.equals("get_first_lvl_categories")) {
                getFirstLevelCategories(request, response);
            } else if (action.equals("get_adverts_count")) {
                getAdvertsCount(request, response);
            } else if (action.equals("get_adverts_attributes")) {
                getAdvertsAttributes(request, response);
            } else if (action.equals("get_attributes_autocomplete")) {
                getAttributesAutocomplete(request, response);
            }else if (action.equals("get_parent_categories")) {
                getParentCategories(request, response);
            }
        } catch (SQLException | PropertyVetoException e) {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void getAdverts(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PropertyVetoException, SQLException {
        String adCategoryId = request.getParameter("adCategoryId");
        String adCategoryName = request.getParameter("adCategoryName");
        String adsStartingNum = request.getParameter("adsStartingNum");
        String adsCount = request.getParameter("adsCount");
        String sortingParam = request.getParameter("sortingParam");
        String sortingOrder = request.getParameter("sortingOrder");
        String adNamePattern = request.getParameter("adNamePattern");
        String[] additionalAttributes = request.getParameterValues("additionalAttributes");

        QueryBuilder query =
                QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchPhraseQuery("name", adNamePattern)
                                .fuzziness(Fuzziness.TWO))
                        .should(QueryBuilders.matchPhraseQuery("description", adNamePattern)
                                .fuzziness(Fuzziness.TWO));

        if (adNamePattern == null || adNamePattern.equals("")) {
            query = null;
        }

        SearchResponse searchResponse = searchBean.advertsSearch(query, adCategoryId, adCategoryName, sortingParam,
                sortingOrder, additionalAttributes, adsStartingNum, adsCount);

        SearchHit[] searchHits = searchResponse.getHits().getHits();

        ArrayList<Map<String, Object>> foundedAdverts = new ArrayList<>();

        for (SearchHit res : searchHits) {
            Map<String, Object> mappedRes = res.getSource();
            foundedAdverts.add(mappedRes);
        }


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String advListJson = gson.toJson(foundedAdverts, foundedAdverts.getClass());

        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(advListJson);
        }
    }

    private void getAdvertsAutocomplete(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PropertyVetoException, SQLException {
        String adCategoryId = request.getParameter("adCategoryId");
        String adCategoryName = request.getParameter("adCategoryName");
        String adsStartingNum = request.getParameter("adsStartingNum");
        String adsCount = request.getParameter("adsCount");
        String sortingParam = request.getParameter("sortingParam");
        String sortingOrder = request.getParameter("sortingOrder");
        String adNamePattern = request.getParameter("adNamePattern");
        String additionalAttributes[] = request.getParameterValues("additionalAttributes");

        QueryBuilder query = QueryBuilders.matchPhrasePrefixQuery("name", adNamePattern).maxExpansions(10);

        SearchResponse searchResponse = searchBean.advertsSearch(query, adCategoryId, adCategoryName, sortingParam,
                sortingOrder, additionalAttributes, adsStartingNum, adsCount);

        SearchHit[] searchHits = searchResponse.getHits().getHits();

        Set<String> foundedTitles = new HashSet<>();

        for (SearchHit res: searchHits) {
            Map<String, Object> mappedRes = res.getSource();
            foundedTitles.add((String)mappedRes.get("name"));
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String advListJson = gson.toJson(foundedTitles, foundedTitles.getClass());

        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(advListJson);
        }
    }

    private void getAllCategories(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PropertyVetoException, SQLException {
        ArrayList<String>[] allCategories = searchBean.getAllCategories();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String categoriesJson = gson.toJson(allCategories, allCategories.getClass());

        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(categoriesJson);
        }
    }

    private void getFirstLevelCategories(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PropertyVetoException, SQLException {
        String adCategoryId = request.getParameter("adCategoryId");
        String adCategoryName = request.getParameter("adCategoryName");

        ArrayList<String[]> categories = searchBean.getFirstLevelCategories(adCategoryId,adCategoryName);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String categoriesJson = gson.toJson(categories, categories.getClass());

        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(categoriesJson);
        }
    }

    private void getAdvertsCount(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PropertyVetoException, SQLException {
        String adCategoryId = request.getParameter("adCategoryId");
        String adCategoryName = request.getParameter("adCategoryName");
        String adsCount = request.getParameter("adsCount");
        String sortingParam = request.getParameter("sortingParam");
        String sortingOrder = request.getParameter("sortingOrder");
        String adNamePattern = request.getParameter("adNamePattern");
        String[] additionalAttributes = request.getParameterValues("additionalAttributes");

        QueryBuilder query =
                QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchPhraseQuery("name", adNamePattern)
                                .fuzziness(Fuzziness.TWO))
                        .should(QueryBuilders.matchPhraseQuery("description", adNamePattern)
                                .fuzziness(Fuzziness.TWO));

        if (adNamePattern == null || adNamePattern.equals("")) {
            query = null;
        }

        SearchResponse searchResponse = searchBean.advertsSearch(query, adCategoryId, adCategoryName, sortingParam,
                sortingOrder, additionalAttributes, null, adsCount);

        Integer foundedAdsCount = searchResponse.getHits().getHits().length;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String advListJson = gson.toJson(foundedAdsCount, foundedAdsCount.getClass());

        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(advListJson);
        }
    }

    private void getAdvertsAttributes(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PropertyVetoException, SQLException {
        String adCategoryId = request.getParameter("adCategoryId");

        ArrayList<String[]> attributes = searchBean.getAttributes(adCategoryId);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String attrJson = gson.toJson(attributes, attributes.getClass());

        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(attrJson);
        }
    }

    private void getAttributesAutocomplete(HttpServletRequest request, HttpServletResponse response)
            throws PropertyVetoException, IOException, SQLException {
        String adCategoryId = request.getParameter("adCategoryId");
        String adCategoryName = request.getParameter("adCategoryName");
        String adsStartingNum = request.getParameter("adsStartingNum");
        String adsCount = request.getParameter("adsCount");
        String sortingParam = request.getParameter("sortingParam");
        String sortingOrder = request.getParameter("sortingOrder");
        String attrName = request.getParameter("attrName");
        String attrValuePattern = request.getParameter("attrValuePattern");
        String additionalAttributes[] = request.getParameterValues("additionalAttributes");

        QueryBuilder query = QueryBuilders.matchPhrasePrefixQuery(attrName, attrValuePattern).maxExpansions(10);

        SearchResponse searchResponse = searchBean.advertsSearch(query, adCategoryId, adCategoryName, sortingParam,
                sortingOrder, additionalAttributes, adsStartingNum, adsCount);

        SearchHit[] searchHits = searchResponse.getHits().getHits();

        Set<String> foundedTitles = new HashSet<>();

        for (SearchHit res: searchHits) {
            Map<String, Object> mappedRes = res.getSource();
            foundedTitles.add((String)mappedRes.get(attrName));
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String advListJson = gson.toJson(foundedTitles, foundedTitles.getClass());

        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(advListJson);
        }
    }

    private void getParentCategories(HttpServletRequest request, HttpServletResponse response)
            throws IOException, PropertyVetoException, SQLException {
        String adCategoryId = request.getParameter("adCategoryId");
        String adCategoryName = request.getParameter("adCategoryName");

        ArrayList<String> parentsCategories = searchBean.getParentCategories(adCategoryId, adCategoryName);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String categoriesJson = gson.toJson(parentsCategories, parentsCategories.getClass());

        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(categoriesJson);
        }
    }
}
