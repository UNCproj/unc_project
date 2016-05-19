package servlets;

import beans.AdvertsManager;
import beans.AdvertsSearchBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
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
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * get_adverts_geo_query - получить список объявлений, находящихся на определенном расстоянии от заданного
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
            } else if (action.equals("get_parent_categories")) {
                getParentCategories(request, response);
            } else if (action.equals("get_adverts_geo_query")) {
                getAdvertsGeoQuery(request, response);
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

        SearchResponse searchResponse = searchBean.advertsSearch(query, adCategoryId, adCategoryName);

        SearchHit[] searchHits = searchResponse.getHits().getHits();

        List<Map<String, Object>> foundedAdverts = new ArrayList<>();

        for (SearchHit res : searchHits) {
            Map<String, Object> mappedRes = res.getSource();
            foundedAdverts.add(mappedRes);
        }

        foundedAdverts = applyFiltering(foundedAdverts, additionalAttributes);

        foundedAdverts = applySortAndCount(
                foundedAdverts,
                createAdvertsComparator(sortingParam, sortingOrder),
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

        SearchResponse searchResponse = searchBean.advertsSearch(query, adCategoryId, adCategoryName);

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
            searchResponse = searchBean.advertsSearch(query, adCategoryId, adCategoryName);
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

        SearchResponse searchResponse = searchBean.advertsSearch(query, adCategoryId, adCategoryName);

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

    private void getAdvertsGeoQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String adCategoryId = request.getParameter("adCategoryId");
        String adCategoryName = request.getParameter("adCategoryName");
        String adCenterLat = request.getParameter("adCenterCoords[lat]");
        String adCenterLng = request.getParameter("adCenterCoords[lng]");
        String radius = request.getParameter("radius");
        String adsCount = request.getParameter("adsCount");

        SearchResponse searchResponse = searchBean.geoQuery(Double.parseDouble(adCenterLat),
                Double.parseDouble(adCenterLng), Double.parseDouble(radius));

        SearchHit[] searchHits = searchResponse.getHits().getHits();

        List<Map<String, Object>> foundedAdverts = new ArrayList<>();

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

    private <T> List<T> applySortAndCount(List<T> srcArray, Comparator<T> comparator,
                                          String adsStartingNum, String adsCount) {
        adsStartingNum = adsStartingNum == null ? "0" : adsStartingNum;
        adsCount = adsCount == null ? "5000" : adsCount;

        return srcArray
                .stream()
                .sorted(comparator)
                .skip(Integer.parseInt(adsStartingNum))
                .limit(Integer.parseInt(adsCount))
                .collect(Collectors.toList());
    }

    private <T extends Map<String, Object>> List<T> applyFiltering(List<T> srcArray, String[] additionalAttributes) {
        List<T> outputArray = srcArray;

        if (additionalAttributes != null) {
            Gson inputAttrsJson = new GsonBuilder().setPrettyPrinting().create();

            Type type = Object.class;

            for (String additionalAttribute : additionalAttributes) {
                LinkedTreeMap attributeInfo = inputAttrsJson.fromJson(additionalAttribute, type);

                String attrName = (String) attributeInfo.get("name");
                String attrType = (String) attributeInfo.get("type");
                ArrayList<String> attrValues = (ArrayList<String>) attributeInfo.get("values");

                if (attrValues == null || attrValues.size() == 0) {
                    continue;
                }

                if (attrType != null) {
                    switch (attrType) {
                        case "discrete_multi":
                            List<String> filtrationValues = attrValues.stream()
                                    .filter(val -> val != null && !val.equals(""))
                                    .collect(Collectors.toList());

                            if (filtrationValues != null && filtrationValues.size() > 0) {
                                outputArray = outputArray.stream()
                                        .filter(t -> {
                                            String attrValue =  (String) t.get(attrName);

                                            for (String filtrationValue: filtrationValues) {
                                                if (attrValue.equals(filtrationValue)) {
                                                    return true;
                                                }
                                            }

                                            return false;
                                        })
                                        .collect(Collectors.toList());
                            }
                            break;

                        case "continous":
                            outputArray = outputArray.stream()
                                    .filter(t -> {
                                        String attrValue =  (String)t.get(attrName);

                                        Integer attrDigitValue = tryParseInt(attrValue);

                                        if (attrDigitValue == null) {
                                            Date attrDateValue = tryParseDate(attrValue);

                                            if (attrDateValue == null) {
                                                return isBetween(attrValue, attrValues.get(0), attrValues.get(1));
                                            }
                                            else {
                                                Date fromDateValue = tryParseDate(attrValues.get(0));
                                                Date toDateValue = tryParseDate(attrValues.get(1));

                                                return isBetween(attrDateValue, fromDateValue, toDateValue);
                                            }
                                        }
                                        else {
                                            Integer fromDigitValue = tryParseInt(attrValues.get(0));
                                            Integer toDigitValue = tryParseInt(attrValues.get(1));

                                            return isBetween(attrDigitValue, fromDigitValue, toDigitValue);
                                        }
                                    })
                                    .collect(Collectors.toList());
                            break;
                    }
                }
            }
        }

        return outputArray;
    }

    private <T extends Comparable<T>> boolean isBetween(T inputValue, T fromValue, T toValue) {
        if (fromValue != null && inputValue.compareTo(fromValue) < 0) {
            return false;
        }

        if (toValue != null && inputValue.compareTo(toValue) > 0) {
            return false;
        }

        return true;
    }

    private <T extends Map<String, Object>> Comparator<T> createAdvertsComparator(String sortingParam, String sortingOrder) {
        final String SORTING_PARAM = sortingParam == null ? "registration_date" : sortingParam;
        final String SORTING_ORDER = sortingOrder == null ? "asc" : sortingOrder;

        return (o1, o2) -> {
            String sortingParamValue1 = (String) o1.get(SORTING_PARAM);
            String sortingParamValue2 = (String) o2.get(SORTING_PARAM);

            if (sortingParamValue1 == null) {
                return -1;
            }

            if (sortingParamValue2 == null) {
                return 1;
            }

            int compared;

            Integer sortingParamIntValue1 = tryParseInt(sortingParamValue1);

            if (sortingParamIntValue1 == null) {
                Date sortingParamDateValue1 = tryParseDate(sortingParamValue1);

                if (sortingParamDateValue1 == null) {
                    compared = sortingParamValue1.compareTo(sortingParamValue2);
                }
                else {
                    Date sortingParamDateValue2 = tryParseDate(sortingParamValue2);
                    compared = sortingParamDateValue1.compareTo(sortingParamDateValue2);
                }
            }
            else {
                Integer sortingParamIntValue2 = tryParseInt(sortingParamValue2);
                compared = sortingParamIntValue1 - sortingParamIntValue2;
            }

            return SORTING_ORDER.equalsIgnoreCase("desc") ? -compared : compared;
        };
    }

    private Integer tryParseInt(String inputString) {
        try {
            return Integer.parseInt(inputString);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private Date tryParseDate(String inputString) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            return dateFormat.parse(inputString);
        }
        catch (ParseException e) {
            return null;
        }
    }
}
