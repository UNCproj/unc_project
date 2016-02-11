package db;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

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

    static public final String LOGIN_ATTR_ID = "1";
    static public final String PASSWORD_ATTR_ID = "2";
    static public final String REG_DATE_ATTR_ID = "3";
    static public final String LAST_VISIT_DATE_ATTR_ID = "4";
    static public final String EMAIL_ATTR_ID = "5";
    static public final String USER_PIC_FILE_ATTR_ID = "6";

    static public final String DEFAULT_USER_PIC_FILE = "/unc-project/resources/img/user-pics/default.png";

    static public String selectFullObjectInformationByName(String[] types, String name) {
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

        query.append(") and o.object_name = '" + name + "'");
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

    static public String insertNewUser(String login) {
        String query = "insert into unc_objects(object_id, object_type_id, object_name) values(" +
                                    "GetID(), 1, '" + login + "')";

        return query;
    }

    static public String insertParam(BigDecimal id, String attrId, String value, Date dateValue)
            throws Exception
    {
        if (value != null && dateValue != null) {
            throw new Exception("Value or dateValue must be null!");
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String query = "insert into unc_params(object_id, attr_id, value, date_value) values(" +
                id + ", " +
                attrId + ", " +
                (value == null ? "null" : "'" + value + "'") + ", " +
                (value == null ? "to_date('" + df.format(dateValue) + "', 'yyyy-MM-dd HH24:mi:ss')" : "null") + ")";

        return query;
    }

    static public String updateParam(BigDecimal id, String attrId, String value, Date dateValue)
        throws Exception
    {
        if (value != null && dateValue != null) {
            throw new Exception("Value or dateValue must be null!");
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String query = "update unc_params set " +
                "value = " + (value == null ? "null" : "'" + value + "'") + ", " +
                "date_value = " + (value == null ? "to_date('" + df.format(dateValue) + "', 'yyyy-MM-dd HH24:mi:ss')" : "null") +
                "where object_id = " + id + "and attr_id = " + attrId;

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
