package db;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;
import java.util.jar.Attributes;

/**
 * Created by Денис on 15.12.2015.
 */
public class SQLQueriesHelper {
    static public final String USER_TYPE_ID = "1";
    static public final String MODERATOR_TYPE_ID = "2";
    static public final String ADVERT_TYPE_ID = "4";

    static public final String LOGIN_ATTR = "login";
    static public final String PASSWORD_ATTR = "password";
    static public final String REG_DATE_ATTR = "registration_date";
    static public final String LAST_VISIT_DATE_ATTR = "last_visit_date";
    static public final String EMAIL_ATTR = "email";
    static public final String USER_PIC_FILE_ATTR = "user_pic_file";
    static public final String DESCRIPTION_ATTR = "description";
    static public final String CATEGORY_ADVERT_ATTR = "category";
    static public final String CITY_ADVERT_ATTR = "city";
    static public final String PRICE_ADVERT_ATTR = "price";
    static public final String USERS_ADVERT_ATTR = "user_advert";
    static public final String FIRST_NAME_ATTR = "first_name";
    static public final String SECOND_NAME_ATTR = "second_name";
    static public final String SURNAME_ATTR = "surname";
    static public final String PHONE_ATTR = "phone";
    static public final String STREET_AND_HOUSE_NAME_ATTR = "street_and_house";
    static public final String COUNTRY_ATTR = "country";
    static public final String ADDITIONAL_INFO_ATTR = "additional_info";
    static public final String BOOKMARK_ATTR = "bookmark";

    static public final String LOGIN_ATTR_ID = "1";
    static public final String PASSWORD_ATTR_ID = "2";
    static public final String REG_DATE_ATTR_ID = "3";
    static public final String LAST_VISIT_DATE_ATTR_ID = "4";
    static public final String EMAIL_ATTR_ID = "5";
    static public final String USER_PIC_FILE_ATTR_ID = "6";
    static public final String DESCRIPTION_ATTR_ID = "7";
    static public final String CATEGORY_ADVERT_ATTR_ID = "8";
    static public final String CITY_ADVERT_ATTR_ID = "9";
    static public final String PRICE_ADVERT_ATTR_ID = "10";
    static public final String USERS_ADVERT_ATTR_ID = "11";
    static public final String FIRST_NAME_ATTR_ID = "12";
    static public final String SECOND_NAME_ATTR_ID = "13";
    static public final String SURNAME_ATTR_ID = "14";
    static public final String PHONE_ATTR_ID = "15";
    static public final String STREET_AND_HOUSE_NAME_ATTR_ID = "16";
    static public final String COUNTRY_ATTR_ID = "17";
    static public final String ADDITIONAL_INFO_ATTR_ID = "18";
    static public final String BOOKMARK_ATTR_ID = "19";

    //static public final String DEFAULT_USER_PIC_FILE = "/unc-project/resources/img/user-pics/default.png";

    static public String selectFullObjectInformationByName(String[] typesIds, String objectName) {
        StringBuffer query = new StringBuffer(
                        "select  o.object_id,\n" +
                        "        o.object_name,\n" +
                        "        ot.ot_name as type,\n" +
                        "        a.attr_name as attr_name,\n" +
                        "        a.attr_type,\n" +
                        "        case\n" +
                        "          when p.value is not null\n" +
                        "            then p.value\n" +
                        "          when r.object_reference_id is not null\n" +
                        "            then to_char(r.object_reference_id)\n" +
                        "          else\n" +
                        "            to_char(p.date_value)\n" +
                        "        end as value,\n" +
                        "        aot.attr_group_id as attr_group\n" +
                        "  from  unc_objects o\n" +
                        "        left join unc_attr_object_types aot\n" +
                                "          on aot.ot_id in (select ot_id" +
                                "                             from unc_object_types\n" +
                                "                            start with ot_id = o.object_type_id\n" +
                                "                          connect by ot_id = prior parent_id)\n" +
                        "        left join unc_attributes a\n" +
                        "          on a.attr_id = aot.attr_id\n" +
                        "        left join unc_params p\n" +
                        "          on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n" +
                        "        left join unc_references r\n" +
                        "          on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n" +
                        "        join unc_object_types ot\n" +
                        "          on ot.ot_id = o.object_type_id\n");

        query.append("where (o.object_type_id = " + typesIds[0]);

        for (int i = 1; i < typesIds.length; i++) {
            query.append(" or o.object_type_id = " + typesIds[i]);
        }

        query.append(") and o.object_name = '" + objectName + "'");
        query.append(  " order by o.object_id,\n" +
                        "         a.attr_id");

        String queryString = query.toString();
        return queryString;
    }
    
    static public String selectCategories(String objectTypeId) {
        StringBuffer query = new StringBuffer("SELECT  OT_NAME\n" +
                        "  FROM  UNC_OBJECT_TYPES\n" +
                        "  where PARENT_ID is not null\n" +
                        "  CONNECT BY PRIOR PARENT_ID = OT_ID\n" +
                        "  START WITH OT_ID = ");
        query.append(objectTypeId);
        query.append(" ORDER BY PARENT_ID");
        String queryString = query.toString();
        return queryString;
    }
    
    static public String isAdvert(String objectTypeId) {
        StringBuffer query = new StringBuffer("SELECT  OT_ID\n" +
                        "  FROM  UNC_OBJECT_TYPES\n" +
                        "  where PARENT_ID is null\n" +
                        "  CONNECT BY PRIOR PARENT_ID = OT_ID\n" +
                        "  START WITH OT_ID = ");
        query.append(objectTypeId);
        String queryString = query.toString();
        return queryString;
    }
    
    static public String getListReferences(String objectId) {
        StringBuffer query = new StringBuffer("select  OBJECT_ID\n" +
                        "  from  unc_references\n" +
                        "  where OBJECT_REFERENCE_ID = ");
        query.append(objectId);
        String queryString = query.toString();
        return queryString;
    }

    static public String selectFullObjectInformationByName(String objectName) {
        StringBuffer query = new StringBuffer(
                "select  o.object_id,\n" +
                        "        o.object_name,\n" +
                        "        ot.ot_name as type,\n" +
                        "        ot.ot_id as type_id,\n" +
                        "        a.attr_name as attr_name,\n" +
                        "        aot.attr_name_ru as attr_name_ru,\n" +
                        "        a.attr_type,\n" +
                        "        case\n" +
                        "          when p.value is not null\n" +
                        "            then p.value\n" +
                        "          when r.object_reference_id is not null\n" +
                        "            then to_char(r.object_reference_id)\n" +
                        "          else\n" +
                        "            to_char(p.date_value)\n" +
                        "        end as value,\n" +
                        "        aot.attr_group_id as attr_group\n" +
                        "  from  unc_objects o\n" +
                        "        left join unc_attr_object_types aot\n" +
                        "          on aot.ot_id in (select ot_id" +
                        "                             from unc_object_types\n" +
                        "                            start with ot_id = o.object_type_id\n" +
                        "                          connect by ot_id = prior parent_id)\n" +
                        "        left join unc_attributes a\n" +
                        "          on a.attr_id = aot.attr_id\n" +
                        "        left join unc_params p\n" +
                        "          on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n" +
                        "        left join unc_references r\n" +
                        "          on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n" +
                        "        join unc_object_types ot\n" +
                        "          on ot.ot_id = o.object_type_id\n");

        query.append("where (o.object_name = '" + objectName + "'");
        query.append(  " order by o.object_id,\n" +
                "         a.attr_id");

        String queryString = query.toString();
        return queryString;
    }

    static public String selectFullObjectInformationById(String[] typesIds, String[] ids) {
        StringBuffer query = new StringBuffer(
                "select  o.object_id,\n" +
                        "        o.object_name,\n" +
                        "        ot.ot_name as type,\n" +
                        "        a.attr_name as attr_name,\n" +
                        "        nvl(aot.attr_name_ru, a.attr_name) as attr_name_ru,\n" +
                        "        a.attr_type,\n" +
                        "        case\n" +
                        "          when p.value is not null\n" +
                        "            then p.value\n" +
                        "          when r.object_reference_id is not null\n" +
                        "            then to_char(r.object_reference_id)\n" +
                        "          else\n" +
                        "            to_char(p.date_value)\n" +
                        "        end as value,\n" +
                        "        aot.attr_group_id as attr_group\n" +
                        "  from  unc_objects o\n" +
                        "        left join unc_attr_object_types aot\n" +
                        "          on aot.ot_id in (select ot_id" +
                        "                             from unc_object_types\n" +
                        "                            start with ot_id = o.object_type_id\n" +
                        "                          connect by ot_id = prior parent_id)\n" +
                        "        left join unc_attributes a\n" +
                        "          on a.attr_id = aot.attr_id\n" +
                        "        left join unc_params p\n" +
                        "          on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n" +
                        "        left join unc_references r\n" +
                        "          on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n" +
                        "        join unc_object_types ot\n" +
                        "          on ot.ot_id = o.object_type_id\n");

        query.append("where (o.object_type_id = " + typesIds[0]);

        for (int i = 1; i < typesIds.length; i++) {
            query.append(" or o.object_type_id = " + typesIds[i]);
        }

        query.append(") and (o.object_id = " + ids[0]);

        for (int i = 1; i < ids.length; i++) {
            query.append(" or o.object_id = " + ids[i]);
        }

        query.append(  ") order by o.object_id,\n" +
                "         a.attr_id");

        String queryString = query.toString();
        return queryString;
    }

    static public String selectFullObjectInformationById(String[] ids) {
        StringBuffer query = new StringBuffer(
                "select  o.object_id,\n" +
                        "        o.object_name,\n" +
                        "        ot.ot_name as type,\n" +
                        "        ot.ot_id as type_id,\n" +
                        "        a.attr_name as attr_name,\n" +
                        "        nvl(aot.attr_name_ru, a.attr_name) as attr_name_ru,\n" +
                        "        a.attr_type,\n" +
                        "        case\n" +
                        "          when p.value is not null\n" +
                        "            then p.value\n" +
                        "          when r.object_reference_id is not null\n" +
                        "            then to_char(r.object_reference_id)\n" +
                        "          else\n" +
                        "            to_char(p.date_value)\n" +
                        "        end as value,\n" +
                        "        aot.attr_group_id as attr_group\n" +
                        "  from  unc_objects o\n" +
                        "        left join unc_attr_object_types aot\n" +
                        "          on aot.ot_id in (select ot_id" +
                        "                             from unc_object_types\n" +
                        "                            start with ot_id = o.object_type_id\n" +
                        "                          connect by ot_id = prior parent_id)\n" +
                        "        left join unc_attributes a\n" +
                        "          on a.attr_id = aot.attr_id\n" +
                        "        left join unc_params p\n" +
                        "          on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n" +
                        "        left join unc_references r\n" +
                        "          on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n" +
                        "        join unc_object_types ot\n" +
                        "          on ot.ot_id = o.object_type_id\n");

        query.append("where (o.object_id = " + ids[0]);

        for (int i = 1; i < ids.length; i++) {
            query.append(" or o.object_id = " + ids[i]);
        }

        query.append(  ") order by o.object_id,\n" +
                "         a.attr_id");

        String queryString = query.toString();
        return queryString;
    }

    static public String selectParams(String[] typesIds, String[] ids, String[] paramsNames, String name) {
        StringBuffer query = new StringBuffer(
                "select  o.object_id,\n" +
                        "        o.object_name,\n" +
                        "        ot.ot_name as type,\n" +
                        "        a.attr_name as attr_name,\n" +
                        "        a.attr_type,\n" +
                        "        case\n" +
                        "          when p.value is not null\n" +
                        "            then p.value\n" +
                        "          when r.object_reference_id is not null\n" +
                        "            then to_char(r.object_reference_id)\n" +
                        "          else\n" +
                        "            to_char(p.date_value)\n" +
                        "        end as value\n" +
                        "  from  unc_objects o\n" +
                        "        left join unc_attr_object_types aot\n" +
                        "          on aot.ot_id in (select ot_id" +
                        "                             from unc_object_types\n" +
                        "                            start with ot_id = o.object_type_id\n" +
                        "                          connect by ot_id = prior parent_id)\n" +
                        "        left join unc_attributes a\n" +
                        "          on a.attr_id = aot.attr_id\n" +
                        "        left join unc_params p\n" +
                        "          on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n" +
                        "        left join unc_references r\n" +
                        "          on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n" +
                        "        join unc_object_types ot\n" +
                        "          on ot.ot_id = o.object_type_id\n");

        query.append("where (o.object_type_id = " + typesIds[0]);

        for (int i = 1; i < typesIds.length; i++) {
            query.append(" or o.object_type_id = " + typesIds[i]);
        }

        query.append(") and (a.attr_name = '" + paramsNames[0] + "'");

        for (int i = 1; i < paramsNames.length; i++) {
            query.append(" or a.attr_name = '" + paramsNames[i] + "'");
        }

        if (ids != null && ids.length > 0) {
            query.append(") and (o.object_id = " + ids[0]);

            for (int i = 1; i < ids.length; i++) {
                query.append(" or o.object_id = " + ids[i]);
            }
        }

        if (name != null && name.length() > 0) {
            query.append(") and o.object_name = '" + name + "'");
        }

        query.append(  ") order by o.object_id,\n" +
                "         a.attr_id");

        String queryString = query.toString();
        return queryString;
    }

    static public String insertObject(String id, String type, String name) {
        String query = "insert into unc_objects(object_id, object_type_id, object_name) values(" +
                id + "," + type + "'" + name + "')";

        return query;
    }

    static public String insertObject(String type, String name) {
        String query = "insert into unc_objects(object_id, object_type_id, object_name) values(" +
                "GetID()," + type + "'" + name + "')";

        return query;
    }

    static public String insertNewUser(String login) {
        String query = "insert into unc_objects(object_id, object_type_id, object_name) values(" +
                                    "GetID(), 1, '" + login + "')";

        return query;
    }

    static public String insertParam(BigDecimal object_id, String attrId, String value, Date dateValue)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String query = "insert into unc_params(object_id, attr_id, value, date_value) values(" +
                object_id + ", " +
                attrId + ", " +
                (value == null ? "null" : "'" + value + "'") + ", " +
                (value == null ? "to_date('" + df.format(dateValue) + "', 'yyyy-MM-dd HH24:mi:ss')" : "null") + ")";

        return query;
    }

    static public String updateParam(BigDecimal id, String attrId, String value, Date dateValue)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String query = "update unc_params set " +
                "value = " + (value == null ? "null" : "'" + value + "'") + ", " +
                "date_value = " + (value == null ? "to_date('" + df.format(dateValue) + "', 'yyyy-MM-dd HH24:mi:ss')" : "null") +
                " where object_id = " + id + " and attr_id = " + attrId;

        return query;
    }

    static public String updateParamByName(BigDecimal id, String attrName, String value, Date dateValue)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String query = "update unc_params set " +
                "value = " + (value == null ? "null" : "'" + value + "'") + ", " +
                "date_value = " + (value == null ? "to_date('" + df.format(dateValue) + "', 'yyyy-MM-dd HH24:mi:ss')" : "null") +
                " where object_id = " + id + " and attr_id in " +
                "(select attr_id from unc_attributes where attr_name = '" + attrName + "')";

        return query;
    }

    static public String getAllAttributes(String type) {
        String query = "select a.attr_id, a.attr_name, uao.attr_group_id, uao.attr_name_ru, a.attr_type " +
                       "from unc_attr_object_types uao " +
                       "left join unc_attributes a " +
                       "on uao.attr_id = a.attr_id " +
                       "where uao.ot_id = " + type +
                       " order by uao.attr_order nulls last";
        return query;
    }

    static public String getAllHierarchyAttributes(String typeId) {
        String query =  "select a.attr_name, uao.attr_group_id, uao.attr_name_ru " +
                        "  from unc_attr_object_types uao " +
                        "  left join unc_attributes a " +
                        "    on uao.attr_id = a.attr_id " +
                        " where uao.ot_id in (select ot_id " +
                        "                       from unc_object_types " +
                        "                      start with ot_id = " + typeId +
                        "                    connect by ot_id = prior parent_id)" +
                        " order by uao.attr_order nulls last";
        return query;
    }

    static public String changeName(BigDecimal objectId, String newName) {
        String query = "update unc_objects set object_name = '" + newName + "' where object_id = " + objectId;
        return query;
    }

    static public String changeType(BigDecimal objectId, String newTypeId) {
        String query = "update unc_objects set object_type_id = '" + newTypeId + "' where object_id = " + objectId;
        return query;
    }

    static public String getTypeIdByObjectId(String objectId) {
        String query = "select o.object_type_id as type_id\n" +
                        "from unc_objects o\n" +
                        "where o.object_id = " + objectId;
        return query;
    }

    static public String getParentTypeIdByObjectTypeId(String objectType) {
        String query =  "SELECT  CONNECT_BY_ROOT uot.OT_ID as praperent\n" +
                "FROM unc_object_types uot\n" +
                "where uot.ot_id = " + objectType +"\n" +
                "START WITH uot.PARENT_ID is null\n" +
                "CONNECT BY PRIOR ot_id = parent_id";
        return query;
    }

    static public String insertAdStat(String adId) {
        String query =  "insert into unc_stat(ID,OBJ_ID, VISIT_DATE) values(getid, "+ adId + "," +
                "to_date('"+new SimpleDateFormat("dd.MM.yy").format(new Date()) + "', 'DD.MM.YY')" +
                ")";
        return query;
    }

    static public String getTypeIdByTypeName(String typeName) {
        String query = "select ot_id as id from unc_object_types where lower(ot_name) = '" + typeName.toLowerCase() + "'";
        return query;
    }

    static public String newId (){
        String query = "select GetID() as id from dual";
        return query;
    }
    static public String newReference (String objectId, String referenceId, String attrId){
        String query = "insert into unc_references(OBJECT_ID, OBJECT_REFERENCE_ID, ATTR_ID) values (" +
                objectId +","+referenceId+","+attrId+")";
        return query;
    }
    static public String newAdvert(String id, String name){
        String query = "insert into unc_objects(object_id, object_type_id, object_name) values (" +
                id +", 4,'"+name+"')";
        return query;
    }
    static  public String insertAdvertsParams (String id, String param, String attr){
        String query = "insert into unc_params(object_id, attr_id, value) values (" + id +", "+attr+",'"+param+"')";
        return query;
    }
    static public String insertDate (String id, String param){
        String query = "insert into unc_params(object_id, attr_id, date_value) values (" + id +",'3', to_date('" + param + "','yyyy:mm:dd hh24:mi:ss'))";
        return query;
    }

    static public String getTypeChildren(String typeId) {
        String query =  "select  ot_id, ot_name\n" +
                "  from  unc_object_types\n" +
                " start  with parent_id = " + typeId + "\n" +
                "connect by prior ot_id = parent_id";

        return query;
    }

    static public String getTypeFirstLevelChildren(String typeId) {
        String query =  "select  ot_id, ot_name\n" +
                "  from  unc_object_types uot\n" +
                " where  uot.parent_id = " + typeId;

        return query;
    }

    static public String getAdvertsList(String[] categories, String startingNum, String count,
                                        String sortingParam, String sortingOrder, String namePattern) {
        StringBuilder query = new StringBuilder(
                "select  *\n" +
                        "  from  (\n" +
                        "       select row_number () over (order by " +
                        (sortingParam == null ? "object_name" : sortingOrder) +
                        "       ) rn, adv.*\n" +
                        "       from (\n" +
                        "          select  object_id,\n" +
                        "                  max(object_name) as object_name,\n" +
                        "                  max(type) as type,\n" +
                        "                  max(price) as price,\n" +
                        "                  max(description) as description,\n" +
                        "                  max(pic) as pic,\n" +
                        "                  max(city) as city,\n" +
                        "                  max(registration_date) as registration_date\n" +
                        "            from  (\n" +
                        "                  select  object_id,\n" +
                        "                          object_name,\n" +
                        "                          type,\n" +
                        "                          case\n" +
                        "                            when attr_name = 'price'\n" +
                        "                              then attr_value\n" +
                        "                            else\n" +
                        "                              null\n" +
                        "                          end as price,\n" +
                        "                          case\n" +
                        "                            when attr_name = 'description'\n" +
                        "                              then attr_value\n" +
                        "                            else\n" +
                        "                              null\n" +
                        "                          end as description,\n" +
                        "                          case\n" +
                        "                            when attr_name = 'category'\n" +
                        "                              then attr_value\n" +
                        "                            else\n" +
                        "                              null\n" +
                        "                          end as category,\n" +
                        "                          case\n" +
                        "                            when attr_name = 'city'\n" +
                        "                              then attr_value\n" +
                        "                            else\n" +
                        "                              null\n" +
                        "                          end as city,\n" +
                        "                          case\n" +
                        "                            when attr_name = 'user_pic_file'\n" +
                        "                              then attr_value\n" +
                        "                            else\n" +
                        "                              null\n" +
                        "                          end as pic,\n" +
                        "                          case\n" +
                        "                            when attr_name = 'registration_date'\n" +
                        "                              then attr_value\n" +
                        "                            else\n" +
                        "                              null\n" +
                        "                          end as registration_date\n" +
                        "                    from  (\n" +
                        "                  select  o.object_id,\n" +
                        "                          o.object_name,\n" +
                        "                          ot.ot_name as type,\n" +
                        "                          a.attr_name as attr_name,\n" +
                        "                          a.attr_type,\n" +
                        "                          case\n" +
                        "                            when p.value is not null\n" +
                        "                              then p.value\n" +
                        "                            when r.object_reference_id is not null\n" +
                        "                              then to_char(r.object_reference_id)\n" +
                        "                            else\n" +
                        "                              to_char(p.date_value)\n" +
                        "                          end as attr_value\n" +
                        "                    from  unc_objects o\n" +
                        "                          left join unc_attr_object_types aot\n" +
                        "                            on aot.ot_id in (select ot_id\n" +
                        "                                           from unc_object_types\n" +
                        "                                          start with ot_id = o.object_type_id\n" +
                        "                                        connect by ot_id = prior parent_id)\n" +
                        "                          left join unc_attributes a\n" +
                        "                            on a.attr_id = aot.attr_id\n" +
                        "                          left join unc_params p\n" +
                        "                            on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n" +
                        "                          left join unc_references r\n" +
                        "                            on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n" +
                        "                          join unc_object_types ot\n" +
                        "                            on ot.ot_id = o.object_type_id\n" +
                        "                    where (o.object_type_id = 4"
        );

        if (categories != null && categories.length > 0) {
            for (String category: categories) {
                query.append(" or o.object_type_id = ").append(category);
            }
        }

        query.append(")))\n")
                .append("group by object_id\n")
                .append("order by ")
                .append(sortingParam == null ? "object_name" : sortingParam)
                .append(" ")
                .append(sortingOrder != null && sortingOrder.equals("desc") ? "desc" : "asc")
                .append(") adv\n");

        if (namePattern != null) {
            query.append("where lower(object_name) like '%" + namePattern.toLowerCase() + "%'");
        }

        query.append(")")
                .append(" where rn > ").append(startingNum == null ? startingNum = "0" : startingNum);

        if (count != null) {
            query.append(" and rn <= ").append(Integer.valueOf(startingNum) + Integer.valueOf(count));
        }

        return query.toString();
    }
}
