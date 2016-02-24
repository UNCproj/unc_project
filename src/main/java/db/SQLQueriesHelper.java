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

    static public String selectFullObjectInformationByName(String[] types, String objectName) {
        StringBuffer query = new StringBuffer(
                        "select  o.object_id,\n" +
                        "        o.object_name,\n" +
                        "        ot.ot_name as type,\n" +
                        "        a.attr_name as attr_name,\n" +
                        "        case\n" +
                        "          when p.value is not null\n" +
                        "            then p.value\n" +
                        "          when r.object_reference_id is not null\n" +
                        "            then to_char(r.object_reference_id)\n" +
                        "          else\n" +
                        "            to_char(p.date_value)\n" +
                        "        end as value,\n" +
                        "        aot.attr_group_id as group\n" +
                        "  from  unc_objects o\n" +
                        "        left join unc_attr_object_types aot\n" +
                        "          on o.object_type_id = aot.ot_id\n" +
                        "        left join unc_attributes a\n" +
                        "          on a.attr_id = aot.attr_id\n" +
                        "        left join unc_params p\n" +
                        "          on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n" +
                        "        left join unc_references r\n" +
                        "          on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n" +
                        "        join unc_object_types ot\n" +
                        "          on ot.ot_id = o.object_type_id\n");

        query.append("where (o.object_type_id = " + types[0]);

        for (int i = 1; i < types.length; i++) {
            query.append(" or o.object_type_id = " + types[i]);
        }

        query.append(") and o.object_name = '" + objectName + "'");
        query.append(  " order by o.object_id,\n" +
                        "         a.attr_id");

        String queryString = query.toString();
        return queryString;
    }

    static public String selectFullObjectInformationById(String[] types, String[] ids) {
        StringBuffer query = new StringBuffer(
                "select  o.object_id,\n" +
                        "        o.object_name,\n" +
                        "        ot.ot_name as type,\n" +
                        "        a.attr_name as attr_name,\n" +
                        "        case\n" +
                        "          when p.value is not null\n" +
                        "            then p.value\n" +
                        "          when r.object_reference_id is not null\n" +
                        "            then to_char(r.object_reference_id)\n" +
                        "          else\n" +
                        "            to_char(p.date_value)\n" +
                        "        end as value,\n" +
                        "        aot.attr_group_id as group\n" +
                        "  from  unc_objects o\n" +
                        "        left join unc_attr_object_types aot\n" +
                        "          on o.object_type_id = aot.ot_id\n" +
                        "        left join unc_attributes a\n" +
                        "          on a.attr_id = aot.attr_id\n" +
                        "        left join unc_params p\n" +
                        "          on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n" +
                        "        left join unc_references r\n" +
                        "          on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n" +
                        "        join unc_object_types ot\n" +
                        "          on ot.ot_id = o.object_type_id\n");

        query.append("where (o.object_type_id = " + types[0]);

        for (int i = 1; i < types.length; i++) {
            query.append(" or o.object_type_id = " + types[i]);
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

    static public String selectParams(String[] types, String[] ids, String[] paramsNames, String name) {
        StringBuffer query = new StringBuffer(
                "select  o.object_id,\n" +
                        "        o.object_name,\n" +
                        "        ot.ot_name as type,\n" +
                        "        a.attr_name as attr_name,\n" +
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
                        "          on o.object_type_id = aot.ot_id\n" +
                        "        left join unc_attributes a\n" +
                        "          on a.attr_id = aot.attr_id\n" +
                        "        left join unc_params p\n" +
                        "          on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n" +
                        "        left join unc_references r\n" +
                        "          on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n" +
                        "        join unc_object_types ot\n" +
                        "          on ot.ot_id = o.object_type_id\n");

        query.append("where (o.object_type_id = " + types[0]);

        for (int i = 1; i < types.length; i++) {
            query.append(" or o.object_type_id = " + types[i]);
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
                " where object_id = " + id + "and attr_id = " + attrId;

        return query;
    }

    static public String getAllAttributes(String type) {
        String query = "select a.attr_name, uao.attr_group_id as group " +
                       "from unc_attr_object_types uao " +
                       "left join unc_attributes a " +
                       "on uao.attr_id = a.attr_id " +
                       "where uao.ot_id = " + type;
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

    static public String getTypeId(String typeName) {
        String query = "select ot_id as id from unc_object_types where ot_name = " + typeName;
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
    
}
