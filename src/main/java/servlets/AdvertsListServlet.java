package servlets;

import beans.AdvertsManager;
import beans.AdvertsSearchBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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
    private AdvertsSearchBean searchBean;
    @EJB
    private AdvertsManager advertsManagerBean;

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

        SearchResponse searchResponse = searchBean.advertsSearch(query, adCategoryId,
                adCategoryName, additionalAttributes);

        SearchHit[] searchHits = searchResponse.getHits().getHits();

        List<Map<String, Object>> foundedAdverts = new ArrayList<>();

        for (SearchHit res : searchHits) {
            Map<String, Object> mappedRes = res.getSource();
            foundedAdverts.add(mappedRes);
        }

        foundedAdverts = applySortAndCount(
                foundedAdverts,
                (o1, o2) -> {
                    String sortingParamValue1 = (String) o1.get(sortingParam);
                    String sortingParamValue2 = (String) o2.get(sortingParam);

                    int compared;

                    try {
                        int sortingParamIntValue1 = Integer.parseInt(sortingParamValue1);
                        int sortingParamIntValue2 = Integer.parseInt(sortingParamValue2);

                        compared = sortingParamIntValue1 - sortingParamIntValue2;
                    }
                    catch (Exception e) {
                        compared = sortingParamValue1.compareTo(sortingParamValue2);
                    }

                    return sortingOrder.equalsIgnoreCase("desc") ? -compared : compared;
                },
                adsStartingNum,
                adsCount
        );


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
        String adNamePattern = request.getParameter("adNamePattern");
        String additionalAttributes[] = request.getParameterValues("additionalAttributes");

        QueryBuilder query = QueryBuilders.matchPhrasePrefixQuery("name", adNamePattern).maxExpansions(10);

        SearchResponse searchResponse = searchBean.advertsSearch(query, adCategoryId, adCategoryName, additionalAttributes);

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
        ArrayList<String>[] allCategories = advertsManagerBean.getAllCategories();

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

        ArrayList<String[]> categories = advertsManagerBean.getFirstLevelCategories(adCategoryId,adCategoryName);

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

        SearchResponse searchResponse = null;
        try {
            searchResponse = searchBean.advertsSearch(query, adCategoryId, adCategoryName, additionalAttributes);
        }
        catch (Exception e) {
            e.getMessage();
        }

        Integer foundedAdsCount = searchResponse.getHits().getHits().length;
        int parameteredAdsCount = Integer.parseInt(adsCount);

        if (foundedAdsCount > parameteredAdsCount) {
            foundedAdsCount = parameteredAdsCount;
        }

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

        ArrayList<String[]> attributes = advertsManagerBean.getAttributes(adCategoryId);

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
        String attrName = request.getParameter("attrName");
        String attrValuePattern = request.getParameter("attrValuePattern");
        String additionalAttributes[] = request.getParameterValues("additionalAttributes");

        QueryBuilder query = QueryBuilders.matchPhrasePrefixQuery(attrName, attrValuePattern).maxExpansions(10);

        SearchResponse searchResponse = searchBean.advertsSearch(query, adCategoryId,
                adCategoryName, additionalAttributes);

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

        ArrayList<String> parentsCategories = advertsManagerBean.getParentCategories(adCategoryId, adCategoryName);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String categoriesJson = gson.toJson(parentsCategories, parentsCategories.getClass());

        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(categoriesJson);
        }
    }

    private <T> List<T> applySortAndCount(List<T> srcArray, Comparator<T> comparator,
                                          String adsStartingNum, String adsCount) {
        return srcArray
                .stream()
                .sorted(comparator)
                .skip(Integer.parseInt(adsStartingNum))
                .limit(Integer.parseInt(adsCount))
                .collect(Collectors.toList());
    }
}
