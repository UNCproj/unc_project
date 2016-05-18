package db;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ????? on 15.12.2015.
 */
public class SQLQueriesHelper {

    static public final String USER_TYPE_ID = "1";
    static public final String MODERATOR_TYPE_ID = "2";
    static public final String ADVERT_TYPE_ID = "4";
    static public final String MESSAGE_TYPE_ID = "3";
    static public final String FORUM_TOPIC = "388";

    static public final String LOGIN_ATTR = "login";
    static public final String MODER_ATTR = "is_moderator";
    static public final String ADMIN_ATTR = "is_admin";
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
    static public final String TEXT_MESSAGE = "31";
    static public final String DATE_MESSAGE = "32";
    static public final String ID_SENDER = "33";
    static public final String ID_RECIPIENT = "34";
    static public final String INVALID_ATTR_ID = "37";
    static public final String DATE_CREATION = "38";
    static public final String PERSON_CREATION = "39";
    static public final String DEL_MSG_ATTR_ID = "40";
    static public final String DEL_ID_ATTR_ID = "41";
    static public final String MODER_ATTR_ID = "35";
    static public final String ADMIN_ATTR_ID = "36";
    static public final String READ_ATTR_ID = "44";
    //static public final String DEFAULT_USER_PIC_FILE = "/unc-project/resources/img/user-pics/default.png";

    static public String selectFullObjectInformationByName(String[] typesIds, String objectName) {
        StringBuffer query = new StringBuffer(
                "select  o.object_id,\n"
                + "        o.object_name,\n"
                + "        ot.ot_name as type,\n"
                + "        a.attr_name as attr_name,\n"
                + "        a.attr_type,\n"
                + "        case\n"
                + "          when p.value is not null\n"
                + "            then p.value\n"
                + "          when r.object_reference_id is not null\n"
                + "            then to_char(r.object_reference_id)\n"
                + "          else\n"
                + "            to_char(p.date_value)\n"
                + "        end as value,\n"
                + "        aot.attr_group_id as attr_group\n"
                + "  from  unc_objects o\n"
                + "        left join unc_attr_object_types aot\n"
                + "          on aot.ot_id in (select ot_id"
                + "                             from unc_object_types\n"
                + "                            start with ot_id = o.object_type_id\n"
                + "                          connect by ot_id = prior parent_id)\n"
                + "        left join unc_attributes a\n"
                + "          on a.attr_id = aot.attr_id\n"
                + "        left join unc_params p\n"
                + "          on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n"
                + "        left join unc_references r\n"
                + "          on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n"
                + "        join unc_object_types ot\n"
                + "          on ot.ot_id = o.object_type_id\n");

        query.append("where (o.object_type_id = " + typesIds[0]);

        for (int i = 1; i < typesIds.length; i++) {
            query.append(" or o.object_type_id = " + typesIds[i]);
        }

        query.append(") and o.object_name = '" + objectName + "'");
        query.append(" order by o.object_id,\n"
                + "         a.attr_id");

        String queryString = query.toString();
        return queryString;
    }

    static public String selectCategories(String objectTypeId) {
        StringBuffer query = new StringBuffer("SELECT  OT_NAME\n"
                + "  FROM  UNC_OBJECT_TYPES\n"
                + "  where PARENT_ID is not null\n"
                + "  CONNECT BY PRIOR PARENT_ID = OT_ID\n"
                + "  START WITH OT_ID = ");
        query.append(objectTypeId);
        query.append(" ORDER BY PARENT_ID");
        String queryString = query.toString();
        return queryString;
    }

    static public String isAdvert(String objectTypeId) {
        StringBuffer query = new StringBuffer("SELECT  OT_ID\n"
                + "  FROM  UNC_OBJECT_TYPES\n"
                + "  where PARENT_ID is null\n"
                + "  CONNECT BY PRIOR PARENT_ID = OT_ID\n"
                + "  START WITH OT_ID = ");
        query.append(objectTypeId);
        String queryString = query.toString();
        return queryString;
    }

    static public String setVipAdvert(String id_advert) {
        StringBuffer query = new StringBuffer("insert into UNC_PARAMS values( " + id_advert + ", 20, 'Gold', sysdate, null)");
        String queryString = query.toString();
        return queryString;
    }

    static public String isVip(String objectId) {
        StringBuffer query = new StringBuffer("select VALUE from UNC_PARAMS where ATTR_ID = 20 and OBJECT_ID = ");
        query.append(objectId);
        String queryString = query.toString();
        return queryString;
    }

    static public String getListReferences(String objectId) {
        String inv_id = INVALID_ATTR_ID;
        StringBuffer query = new StringBuffer("select  r.OBJECT_ID,\n"
                + "        o.OBJECT_NAME,\n"
                + "        nvl(\n"
                + "          (select value\n"
                + "          from unc_params\n"
                + "          where attr_id=" + inv_id + " and\n"
                + "                object_id=o.OBJECT_ID\n"
                + "          ),\n"
                + "          'false') as invalid\n"
                + "from  unc_references r\n"
                + "  left join UNC_OBJECTS o\n"
                + "    on r.OBJECT_ID = o.OBJECT_ID\n"
                + "where r.OBJECT_REFERENCE_ID =  " + objectId + " union \n"
                + "select  rr.OBJECT_REFERENCE_ID,\n"
                + "        oo.OBJECT_NAME,\n"
                + "        nvl(\n"
                + "          (select value\n"
                + "          from unc_params\n"
                + "          where attr_id=" + inv_id + " and\n"
                + "                object_id=oo.OBJECT_ID\n"
                + "          ),\n"
                + "          'false') as invalid\n"
                + "from  unc_references rr\n"
                + "  left join UNC_OBJECTS oo\n"
                + "    on rr.OBJECT_REFERENCE_ID = oo.OBJECT_ID\n"
                + "where rr.OBJECT_ID = "
        );
        query.append(objectId);
        String queryString = query.toString();
        return queryString;
    }

    static public String selectFullObjectInformationByName() {
        StringBuffer query = new StringBuffer(
                "select  o.object_id,\n"
                + "        o.object_name,\n"
                + "        ot.ot_name as type,\n"
                + "        ot.ot_id as type_id,\n"
                + "        a.attr_name as attr_name,\n"
                + "        aot.attr_name_ru as attr_name_ru,\n"
                + "        a.attr_type,\n"
                + "        case\n"
                + "          when p.value is not null\n"
                + "            then p.value\n"
                + "          when r.object_reference_id is not null\n"
                + "            then to_char(r.object_reference_id)\n"
                + "          else\n"
                + "            to_char(p.date_value)\n"
                + "        end as value,\n"
                + "        aot.attr_group_id as attr_group\n"
                + "  from  unc_objects o\n"
                + "        left join unc_attr_object_types aot\n"
                + "          on aot.ot_id in (select ot_id"
                + "                             from unc_object_types\n"
                + "                            start with ot_id = o.object_type_id\n"
                + "                          connect by ot_id = prior parent_id)\n"
                + "        left join unc_attributes a\n"
                + "          on a.attr_id = aot.attr_id\n"
                + "        left join unc_params p\n"
                + "          on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n"
                + "        left join unc_references r\n"
                + "          on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n"
                + "        join unc_object_types ot\n"
                + "          on ot.ot_id = o.object_type_id\n");

        query.append("where (o.object_name = ?");
        query.append(" order by o.object_id,\n"
                + "         a.attr_id");

        String queryString = query.toString();
        return queryString;
    }

    static public String selectAllaboutUser() {
        return "";
    }

    static public String selectFullObjectInformationById(String[] typesIds, String[] ids) {
        StringBuffer query = new StringBuffer(
                "select  o.object_id,\n"
                + "        o.object_name,\n"
                + "        ot.ot_name as type,\n"
                + "        a.attr_name as attr_name,\n"
                + "        nvl(aot.attr_name_ru, a.attr_name) as attr_name_ru,\n"
                + "        a.attr_type,\n"
                + "        case\n"
                + "          when p.value is not null\n"
                + "            then p.value\n"
                + "          when r.object_reference_id is not null\n"
                + "            then to_char(r.object_reference_id)\n"
                + "          else\n"
                + "            to_char(p.date_value)\n"
                + "        end as value,\n"
                + "        aot.attr_group_id as attr_group\n"
                + "  from  unc_objects o\n"
                + "        left join unc_attr_object_types aot\n"
                + "          on aot.ot_id in (select ot_id"
                + "                             from unc_object_types\n"
                + "                            start with ot_id = o.object_type_id\n"
                + "                          connect by ot_id = prior parent_id)\n"
                + "        left join unc_attributes a\n"
                + "          on a.attr_id = aot.attr_id\n"
                + "        left join unc_params p\n"
                + "          on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n"
                + "        left join unc_references r\n"
                + "          on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n"
                + "        join unc_object_types ot\n"
                + "          on ot.ot_id = o.object_type_id\n");

        query.append("where ");

        if (typesIds != null) {
            query.append("(o.object_type_id = " + typesIds[0]);

            for (int i = 1; i < typesIds.length; i++) {
                query.append(" or o.object_type_id = " + typesIds[i]);
            }

            query.append(") and");
        }

        query.append(" (o.object_id = " + ids[0]);

        for (int i = 1; i < ids.length; i++) {
            query.append(" or o.object_id = " + ids[i]);
        }

        query.append(") order by aot.ATTR_ORDER,o.object_id,\n"
                + "         a.attr_id");

        String queryString = query.toString();
        return queryString;
    }

    static public String selectFullObjectInformationById(String[] ids) {
        StringBuffer query = new StringBuffer(
                "select  o.object_id,\n"
                + "        o.object_name,\n"
                + "        ot.ot_name as type,\n"
                + "        ot.ot_id as type_id,\n"
                + "        a.attr_name as attr_name,\n"
                + "        nvl(aot.attr_name_ru, a.attr_name) as attr_name_ru,\n"
                + "        a.attr_type,\n"
                + "        case\n"
                + "          when p.value is not null\n"
                + "            then p.value\n"
                + "          when r.object_reference_id is not null\n"
                + "            then to_char(r.object_reference_id)\n"
                + "          else\n"
                + "            to_char(p.date_value)\n"
                + "        end as value,\n"
                + "        aot.attr_group_id as attr_group\n"
                + "  from  unc_objects o\n"
                + "        left join unc_attr_object_types aot\n"
                + "          on aot.ot_id in (select ot_id"
                + "                             from unc_object_types\n"
                + "                            start with ot_id = o.object_type_id\n"
                + "                          connect by ot_id = prior parent_id)\n"
                + "        left join unc_attributes a\n"
                + "          on a.attr_id = aot.attr_id\n"
                + "        left join unc_params p\n"
                + "          on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n"
                + "        left join unc_references r\n"
                + "          on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n"
                + "        join unc_object_types ot\n"
                + "          on ot.ot_id = o.object_type_id\n");

        query.append("where (o.object_id = ?");

        for (int i = 1; i < ids.length; i++) {
            query.append(" or o.object_id = ?");
        }

        query.append(") order by aot.ATTR_ORDER,o.object_id,\n"
                + "         a.attr_id");

        String queryString = query.toString();
        System.out.println(queryString);
        return queryString;
    }

    static public String selectParams(String[] typesIds, String[] ids, String[] paramsNames, String name) {
        StringBuffer query = new StringBuffer(
                "select  o.object_id,\n"
                + "        o.object_name,\n"
                + "        ot.ot_name as type,\n"
                + "        a.attr_name as attr_name,\n"
                + "        a.attr_type,\n"
                + "        case\n"
                + "          when p.value is not null\n"
                + "            then p.value\n"
                + "          when r.object_reference_id is not null\n"
                + "            then to_char(r.object_reference_id)\n"
                + "          else\n"
                + "            to_char(p.date_value)\n"
                + "        end as value\n"
                + "  from  unc_objects o\n"
                + "        left join unc_attr_object_types aot\n"
                + "          on aot.ot_id in (select ot_id"
                + "                             from unc_object_types\n"
                + "                            start with ot_id = o.object_type_id\n"
                + "                          connect by ot_id = prior parent_id)\n"
                + "        left join unc_attributes a\n"
                + "          on a.attr_id = aot.attr_id\n"
                + "        left join unc_params p\n"
                + "          on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n"
                + "        left join unc_references r\n"
                + "          on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n"
                + "        join unc_object_types ot\n"
                + "          on ot.ot_id = o.object_type_id\n");

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

        query.append(") order by o.object_id,\n"
                + "         a.attr_id");

        String queryString = query.toString();
        return queryString;
    }

    static public String insertObject(String id, String type, String name) {
        String query = "insert into unc_objects(object_id, object_type_id, object_name) values("
                + id + "," + type + ",'" + name + "')";

        return query;
    }

    static public String insertObject(String type, String name) {
        String query = "insert into unc_objects(object_id, object_type_id, object_name) values("
                + "GetID()," + type + "'" + name + "')";

        return query;
    }

    static public String insertNewUser(String login) {
        String query = "insert into unc_objects(object_id, object_type_id, object_name) values("
                + "GetID(), 1, '" + login + "')";

        return query;
    }

    static public String insertParam(BigDecimal object_id, String attrId, String value, Date dateValue) {
        System.out.println("object_id" + object_id + "attrId" + attrId + "value" + value + "dateValue" + dateValue);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (value == null && dateValue == null && (attrId.equals(SQLQueriesHelper.DATE_MESSAGE)
                || attrId.equals(SQLQueriesHelper.DATE_CREATION))) {
            String query = "insert into unc_params(object_id, attr_id, date_value) values(" + object_id + ", "
                    + attrId + ", systimestamp)";
            return query;
        }
        String query = "insert into unc_params(object_id, attr_id, value, date_value) values("
                + object_id + ", "
                + attrId + ", "
                + (value == null ? "null" : "'" + value + "'") + ", "
                + (value == null ? "to_date('" + df.format(dateValue) + "', 'yyyy-MM-dd HH24:mi:ss')" : "null") + ")";
        if (attrId.equals("20")) {
            query = "insert into unc_params(object_id, attr_id, value, date_value) values(" + object_id + ", "
                    + attrId + ", '" + value + "', systimestamp)";
        }

        System.out.println(query);
        return query;
    }

    static public String updateParam(BigDecimal id, String attrId, String value, Date dateValue) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String query = "update unc_params set "
                + "value = " + (value == null ? "null" : "'" + value + "'") + ", "
                + "date_value = " + (dateValue != null ? "to_date('" + df.format(dateValue) + "', 'yyyy-MM-dd HH24:mi:ss')" : "null")
                + " where object_id = " + id + " and attr_id = " + attrId;

        return query;
    }

    static public String updateParamByName(BigDecimal id, String attrName, String value, Date dateValue) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String query = "update unc_params set "
                + "value = " + (value == null ? "null" : "'" + value + "'") + ", "
                + "date_value = " + (value == null ? "to_date('" + df.format(dateValue) + "', 'yyyy-MM-dd HH24:mi:ss')" : "null")
                + " where object_id = " + id + " and attr_id in "
                + "(select attr_id from unc_attributes where attr_name = '" + attrName + "')";

        return query;
    }

    static public String mergeParam(BigDecimal id, String attrId, String value, Date dateValue) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String query = "merge  into unc_params p\n"
                + "using  (select " + id + " object_id, "
                + attrId + " attr_id, "
                + (value != null && value.length() > 0 ? "'" + value + "'" : null) + " value, "
                + (dateValue != null ? "to_date('" + df.format(dateValue) + "', 'yyyy-MM-dd HH24:mi:ss')" : "null") + " date_value \n"
                + "          from dual) s\n"
                + "        on (p.object_id = s.object_id and p.attr_id = s.attr_id)\n"
                + " when  matched then update set p.value = s.value, p.date_value = s.date_value\n"
                + " when  not matched then insert (object_id, attr_id, value, date_value) \n"
                + "                        values (s.object_id, s.attr_id, s.value, s.date_value)";

        return query;
    }

    static public String mergeParamByName(BigDecimal id, String attrName, String value, Date dateValue) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String query = "merge  into unc_params p\n"
                + "using  (select " + id + " object_id,\n"
                + "              '" + attrName + "' attr_name,\n"
                + "               " + (value != null && value.length() > 0 ? "'" + value + "'" : null) + " value,\n"
                + "               " + (dateValue != null ? "to_date('" + df.format(dateValue) + "', 'yyyy-MM-dd HH24:mi:ss')" : "null") + " date_value\n"
                + "          from dual) s\n"
                + "       on (p.object_id = s.object_id and p.attr_id in \n"
                + "                        (select attr_id\n"
                + "                           from unc_attributes a\n"
                + "                          where a.attr_name = s.attr_name))\n"
                + "when  matched then update set p.value = s.value, p.date_value = s.date_value\n"
                + "when  not matched then insert (object_id, attr_id, value, date_value)\n"
                + "                       values (\n"
                + "                               s.object_id, \n"
                + "                               (select attr_id\n"
                + "                                  from unc_attributes a\n"
                + "                                 where a.attr_name = '" + attrName + "'), \n"
                + "                               s.value, \n"
                + "                               s.date_value\n"
                + "                              )";
        System.out.println(query);
        return query;
    }
	
	static public String deleteParam(String objectId, String attrId, String value) {
        StringBuilder query = new StringBuilder(
                "delete unc_params where object_id = " + objectId +
                                   " and attr_id = "   + attrId
        );

        if (value != null) {
            query.append(" and value = '");
            query.append(value);
            query.append("'");
        }

        return query.toString();
    }

    static public String deleteReference(String objectId, String attrId, String value) {
        StringBuilder query = new StringBuilder(
                "delete unc_references where object_id = " + objectId +
                        " and attr_id = "   + attrId
        );

        if (value != null) {
            query.append(" and object_reference_id = ");
            query.append(value);
        }

        return query.toString();
    }

    static public String getAllAttributes(String type) {
        String query = "select * "
                + "  from unc_attr_object_types uao "
                + "  left join unc_attributes a "
                + "    on uao.attr_id = a.attr_id "
                + " where uao.ot_id in (select ot_id "
                + "                       from unc_object_types "
                + "                      start with ot_id = " + type
                + "                    connect by ot_id = prior parent_id)"
                + " order by uao.attr_order nulls last";
        return query;
    }

    static public String getAllHierarchyAttributes() {
        String query = "select a.attr_name, uao.attr_group_id, uao.attr_name_ru "
                + "  from unc_attr_object_types uao "
                + "  left join unc_attributes a "
                + "    on uao.attr_id = a.attr_id "
                + " where uao.ot_id in (select ot_id "
                + "                       from unc_object_types "
                + "                      start with ot_id = ?"
                + "                    connect by ot_id = prior parent_id)"
                + " order by uao.attr_order nulls last";
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

    static public String getTypeIdByObjectId() {
        String query = "select o.object_type_id as type_id\n"
                + "from unc_objects o\n"
                + "where o.object_id = ?";
        return query;
    }

    static public String getParentTypeIdByObjectTypeId() {
        String query = "SELECT  CONNECT_BY_ROOT uot.OT_ID as praperent\n"
                + "FROM unc_object_types uot\n"
                + "where uot.ot_id = ?\n"
                + "START WITH uot.PARENT_ID is null\n"
                + "CONNECT BY PRIOR ot_id = parent_id";
        return query;
    }

    static public String insertAdStat() {
        String query = "insert into unc_stat(ID,OBJ_ID, VISIT_DATE) values(getid, ?,"
                + "to_date('" + new SimpleDateFormat("dd.MM.yy").format(new Date()) + "', 'DD.MM.YY')"
                + ")";
        return query;
    }

    static public String getTypeIdByTypeName(String typeName) {
        String query = "select ot_id as id from unc_object_types where lower(ot_name) = '" + typeName.toLowerCase() + "'";
        return query;
    }

    static public String newId() {
        String query = "select GetID() as id from dual";
        return query;
    }

    static public String newReference(String objectId, String referenceId, String attrId) {
        String query = "insert into unc_references(OBJECT_ID, OBJECT_REFERENCE_ID, ATTR_ID) values ("
                + objectId + "," + referenceId + "," + attrId + ")";
        return query;
    }

    static public String newAdvert(String id, String name) {
        String query = "insert into unc_objects(object_id, object_type_id, object_name) values ("
                + id + ", 4,'" + name + "')";
        return query;
    }

    static public String insertAdvertsParams(String id, String param, String attr) {
        String query = "insert into unc_params(object_id, attr_id, value) values (" + id + ", " + attr + ",'" + param + "')";
        return query;
    }

    static public String insertDate(String id, String param) {
        String query = "insert into unc_params(object_id, attr_id, date_value) values (" + id + ",'3', to_date('" + param + "','yyyy:mm:dd hh24:mi:ss'))";
        return query;
    }

    static public String getTypeChildren(String typeId) {
        String query = "select  ot_id, ot_name\n"
                + "  from  unc_object_types\n"
                + " start  with parent_id = " + typeId + "\n"
                + "connect by prior ot_id = parent_id";

        return query;
    }

    static public String getTypeFirstLevelChildren(String typeId) {
        String query = "select  ot_id, ot_name\n"
                + "  from  unc_object_types uot\n"
                + " where  uot.parent_id = " + typeId;

        return query;
    }

    static public String getAdvertsList(String[] categories, String startingNum, String count,
            String sortingParam, String sortingOrder, String namePattern) {
        StringBuilder query = new StringBuilder(
                "select  *\n"
                + "  from  (\n"
                + "       select row_number () over (order by "
                + (sortingParam == null ? "object_name" : sortingOrder)
                + "       ) rn, adv.*\n"
                + "       from (\n"
                + "          select  object_id,\n"
                + "                  max(object_name) as object_name,\n"
                + "                  max(type) as type,\n"
                + "                  max(type_id) as type_id,\n"
                + "                  max(price) as price,\n"
                + "                  max(description) as description,\n"
                + "                  max(pic) as pic,\n"
                + "                  max(city) as city,\n"
                + "                  max(registration_date) as registration_date,\n"
                + "                  max(is_invalid) as is_invalid,\n"
                + "                  max(map_coordinates) as map_coordinates\n"
                + "            from  (\n"
                + "                  select  object_id,\n"
                + "                          object_name,\n"
                + "                          type_id,\n"
                + "                          type,\n"
                + "                          case\n"
                + "                            when attr_name = 'price'\n"
                + "                              then attr_value\n"
                + "                            else\n"
                + "                              null\n"
                + "                          end as price,\n"
                + "                          case\n"
                + "                            when attr_name = 'description'\n"
                + "                              then attr_value\n"
                + "                            else\n"
                + "                              null\n"
                + "                          end as description,\n"
                + "                          case\n"
                + "                            when attr_name = 'category'\n"
                + "                              then attr_value\n"
                + "                            else\n"
                + "                              null\n"
                + "                          end as category,\n"
                + "                          case\n"
                + "                            when attr_name = 'city'\n"
                + "                              then attr_value\n"
                + "                            else\n"
                + "                              null\n"
                + "                          end as city,\n"
                + "                          case\n"
                + "                            when attr_name = 'user_pic_file'\n"
                + "                              then attr_value\n"
                + "                            else\n"
                + "                              null\n"
                + "                          end as pic,\n"
                + "                          case\n"
                + "                            when attr_name = 'registration_date'\n"
                + "                              then attr_value\n"
                + "                            else\n"
                + "                              null\n"
                + "                          end as registration_date,\n"
                + "                          case\n"
                + "                            when attr_name = 'is_invalid'\n"
                + "                              then attr_value\n"
                + "                            else\n"
                + "                              null\n"
                + "                          end as is_invalid,\n"
                + "                          case\n"
                + "                            when attr_name = 'map_coordinates'\n"
                + "                              then attr_value\n"
                + "                            else\n"
                + "                              null\n"
                + "                          end as map_coordinates\n"
                + "                    from  (\n"
                + "                  select  o.object_id,\n"
                + "                          o.object_name,\n"
                + "                          ot.ot_id as type_id,\n"
                + "                          ot.ot_name as type,\n"
                + "                          a.attr_name as attr_name,\n"
                + "                          a.attr_type,\n"
                + "                          case\n"
                + "                            when p.value is not null\n"
                + "                              then p.value\n"
                + "                            when r.object_reference_id is not null\n"
                + "                              then to_char(r.object_reference_id)\n"
                + "                            else\n"
                + "                              to_char(p.date_value)\n"
                + "                          end as attr_value\n"
                + "                    from  unc_objects o\n"
                + "                          left join unc_attr_object_types aot\n"
                + "                            on aot.ot_id in (select ot_id\n"
                + "                                           from unc_object_types\n"
                + "                                          start with ot_id = o.object_type_id\n"
                + "                                        connect by ot_id = prior parent_id)\n"
                + "                          left join unc_attributes a\n"
                + "                            on a.attr_id = aot.attr_id\n"
                + "                          left join unc_params p\n"
                + "                            on (aot.attr_id = p.attr_id) and (o.object_id = p.object_id)\n"
                + "                          left join unc_references r\n"
                + "                            on (aot.attr_id = r.attr_id) and (o.object_id = r.object_id)\n"
                + "                          join unc_object_types ot\n"
                + "                            on ot.ot_id = o.object_type_id\n"
                + "                    where (o.object_type_id = 4"
        );

        if (categories != null && categories.length > 0) {
            for (String category : categories) {
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

    static public String findIdNamed(String name) {
        String query = "select object_id\n"
                + "  from unc_objects\n"
                + " where object_name = '" + name + "'";
        return query;
    }

    public static String selectTypes(String type) {
        String query = "select ot_name "
                + "from unc_object_types "
                + "where parent_id = ( "
                + "                    select ot_id "
                + "                    from unc_object_types "
                + "                    where ot_name='" + type + "' "
                + "                  ) "
                + "order by ot_id ";
        return query;
    }

    public static String selectTypes(String type1, String type2, String type3, String type4) {
        String query = "";
        if (type1 != null && type2 == null && type3 == null && type4 == null) {
            query = "select t2.ot_name, "
                    + "(select count(*) "
                    + "from unc_object_types t1 "
                    + "left join unc_object_types t2 "
                    + "on t2.PARENT_ID=t1.OT_ID "
                    + "where t1.ot_name = '" + type1 + "' "
                    + "group by t2.parent_id "
                    + ") as sizeSelect "
                    + "from unc_object_types t1 "
                    + "left join unc_object_types t2 "
                    + "on t2.PARENT_ID=t1.OT_ID "
                    + "where t1.ot_name = '" + type1 + "'"
                    + "order by t2.ot_id";
        }
        if (type1 != null && type2 != null && type3 == null && type4 == null) {
            query = "select t3.ot_name, "
                    + "(select count(*) "
                    + "from unc_object_types t1 "
                    + "left join unc_object_types t2 "
                    + "on t2.PARENT_ID=t1.OT_ID "
                    + "left join unc_object_types t3 "
                    + "on t3.PARENT_ID=t2.OT_ID "
                    + "where t1.ot_name = '" + type1 + "' and "
                    + "t2.ot_name = '" + type2 + "' "
                    + "group by t3.parent_id "
                    + ") as sizeSelect "
                    + "from unc_object_types t1 "
                    + "left join unc_object_types t2 "
                    + "on t2.PARENT_ID=t1.OT_ID "
                    + "left join unc_object_types t3 "
                    + "on t3.PARENT_ID=t2.OT_ID "
                    + "where t1.ot_name = '" + type1 + "' and "
                    + "t2.ot_name = '" + type2 + "'"
                    + "order by t3.ot_id";
        }
        if (type1 != null && type2 != null && type3 != null && type4 == null) {
            query = "select t4.ot_name, "
                    + "(select count(*) "
                    + "from unc_object_types t1 "
                    + "left join unc_object_types t2 "
                    + "on t2.PARENT_ID=t1.OT_ID "
                    + "left join unc_object_types t3 "
                    + "on t3.PARENT_ID=t2.OT_ID "
                    + "left join unc_object_types t4 "
                    + "on t4.PARENT_ID=t3.OT_ID "
                    + "where t1.ot_name = '" + type1 + "' and "
                    + "t2.ot_name = '" + type2 + "' and "
                    + "t3.ot_name = '" + type3 + "' "
                    + "group by t4.parent_id "
                    + ") as sizeSelect "
                    + "from unc_object_types t1 "
                    + "left join unc_object_types t2 "
                    + "on t2.PARENT_ID=t1.OT_ID "
                    + "left join unc_object_types t3 "
                    + "on t3.PARENT_ID=t2.OT_ID "
                    + "left join unc_object_types t4 "
                    + "on t4.PARENT_ID=t3.OT_ID "
                    + "where t1.ot_name = '" + type1 + "' and "
                    + "t2.ot_name = '" + type2 + "' and "
                    + "t3.ot_name = '" + type3 + "'"
                    + "order by t4.ot_id";
        }
        System.out.println(query);
        return query;
    }

    static public String newMessage(String type, String data) {
        String query = "insert into unc_objects(object_id, object_type_id) values(" + data + ", " + type + ")";
        return query;
    }

    static public String outputMessages(String lastId, String senderId, String recipientId) {
        if (lastId == null || lastId.equals("") || lastId.equals("undefined")) {
            String query = "select  t1.* " +
                    "        , p20.value as name, p21.value as surname " +
                    "from  ( " +
                    "select  o1.object_id as id_message, " +
                    "p1.value as text_message, " +
                    "p2.date_value as date_message, " +
                    "p3.value as sender_message, " +
                    "p4.value as recipient_message, " +
                    "o2.object_name, " +
//                    "case " +
//                    "when p3.value = " + senderId + " then " +
//                    "  null " +
//                    "else " +
//                    "  p5.value " +
//                    "end as read_status " +
                    " p5.value as read_status " +
                    "from  unc_objects o1 " +
                    "left join unc_params p1 " +
                    "on p1.OBJECT_ID = o1.OBJECT_ID " +
                    "left join unc_params p2 " +
                    "on p2.OBJECT_ID = o1.OBJECT_ID " +
                    "left join unc_params p3 " +
                    "on p3.OBJECT_ID = o1.OBJECT_ID " +
                    "left join unc_params p4 " +
                    "on p4.OBJECT_ID = o1.OBJECT_ID " +
                    "left join unc_params p5 " +
                    "on p5.OBJECT_ID = o1.OBJECT_ID " +
                    "left join unc_objects o2 " +
                    "on o2.object_id = p3.value " +
                    "where  o1.OBJECT_TYPE_ID = 3 and " +
                    "p1.attr_id = 31 and " +
                    "p2.attr_id = 32 and " +
                    "p3.attr_id = 33 and " +
                    "p4.attr_id = 34 and " +
                    "p5.attr_id = 44 and " +
                    "((p3.value =  " + senderId +  " and p4.value =  " + recipientId +  ") or  " +
                    "(p3.value =  " + recipientId +  " and p4.value =  " + senderId +  "))  " +
                    "order by p2.date_value desc" +
                    ") " +
                    "t1 " +
                    "        left join unc_params p20 " +
                    "          on p20.object_id = t1.sender_message and p20.attr_id = 12 " +
                    "          left join unc_params p21 " +
                    "          on p21.object_id = t1.sender_message and p21.attr_id = 14 " +
//                    "where  ROWNUM < 21 " +
                    "order by date_message desc";
            System.out.println("!!!!!!!!!!!!!!!" + query);
            return query;
        } else {
            String query = "select  t1.* " +
                    "        , p20.value as name, p21.value as surname " +
                    "from  ( " +
                    "select  o1.object_id as id_message, " +
                    "p1.value as text_message, " +
                    "p2.date_value as date_message, " +
                    "p3.value as sender_message, " +
                    "p4.value as recipient_message, " +
                    "o2.object_name, " +
//                    "case " +
//                    "when p3.value = " + senderId + " then " +
//                    "  null " +
//                    "else " +
//                    "  p5.value " +
//                    "end as read_status " +
                    " p5.value as read_status " +
                    "from  unc_objects o1 " +
                    "left join unc_params p1 " +
                    "on p1.OBJECT_ID = o1.OBJECT_ID " +
                    "left join unc_params p2 " +
                    "on p2.OBJECT_ID = o1.OBJECT_ID " +
                    "left join unc_params p3 " +
                    "on p3.OBJECT_ID = o1.OBJECT_ID " +
                    "left join unc_params p4 " +
                    "on p4.OBJECT_ID = o1.OBJECT_ID " +
                    "left join unc_params p5 " +
                    "on p5.OBJECT_ID = o1.OBJECT_ID " +
                    "left join unc_objects o2 " +
                    "on o2.object_id = p3.value " +
                    "where  o1.OBJECT_TYPE_ID = 3 and " +
                    "p1.attr_id = 31 and " +
                    "p2.attr_id = 32 and " +
                    "p3.attr_id = 33 and " +
                    "p4.attr_id = 34 and " +
                    "p5.attr_id = 44 and " +
                    "((p3.value =  " + senderId +  " and p4.value =  " + recipientId +  ") or  " +
                    "(p3.value =  " + recipientId +  " and p4.value =  " + senderId +  "))  " +
                    "and p2.date_value > (select date_value from unc_params where object_id = " + lastId + " and attr_id = 32)" +
                    "order by p2.date_value " +
                    ") " +
                    "t1 " +
                    "        left join unc_params p20 " +
                    "          on p20.object_id = t1.sender_message and p20.attr_id = 12 " +
                    "          left join unc_params p21 " +
                    "          on p21.object_id = t1.sender_message and p21.attr_id = 14 ";
            System.out.println("@@@@@@@@@@@@@@@@@@@@" + query);
            return query;
        }
    }

    public static String selectAttrs(String type, String type1, String type2, String type3, String type4) {
        String query = "";

        query = "select a.attr_id, aot.attr_group_id, aot.attr_order, a.attr_name, aot.attr_name_ru, a.attr_type, aot.ot_id  "
                + "from unc_attr_object_types aot  "
                + "left join unc_attributes a  "
                + "on aot.attr_id = a.attr_id  ";
        if (type != null) {
            query += "where aot.ot_id in ( "
                    + "(select ot.ot_id "
                    + "from unc_object_types ot "
                    + "where ot.ot_name='" + type + "') ";
            if (type1 != null) {
                query += "union "
                        + "(select ot1.ot_id "
                        + "from unc_object_types ot "
                        + "left join unc_object_types ot1 "
                        + "on ot1.parent_id=ot.ot_id "
                        + "where ot.ot_name='" + type + "'  "
                        + "and ot1.ot_name='" + type1 + "') ";
                if (type2 != null) {
                    query += "union "
                            + "(select ot2.ot_id "
                            + "from unc_object_types ot "
                            + "left join unc_object_types ot1 "
                            + "on ot1.parent_id=ot.ot_id "
                            + "left join unc_object_types ot2 "
                            + "on ot2.parent_id=ot1.ot_id "
                            + "where ot.ot_name='" + type + "'  "
                            + "and ot1.ot_name='" + type1 + "' "
                            + "and ot2.ot_name='" + type2 + "') ";
                    if (type3 != null) {
                        query += "union "
                                + "(select ot3.ot_id "
                                + "from unc_object_types ot "
                                + "left join unc_object_types ot1 "
                                + "on ot1.parent_id=ot.ot_id  "
                                + "left join unc_object_types ot2 "
                                + "on ot2.parent_id=ot1.ot_id "
                                + "left join unc_object_types ot3 "
                                + "on ot3.parent_id=ot2.ot_id "
                                + "where ot.ot_name='" + type + "'  "
                                + "and ot1.ot_name='" + type1 + "' "
                                + "and ot2.ot_name='" + type2 + "' "
                                + "and ot3.ot_name='" + type3 + "') ";
                        if (type4 != null) {
                            query += "union "
                                    + "(select ot4.ot_id "
                                    + "from unc_object_types ot "
                                    + "left join unc_object_types ot1 "
                                    + "on ot1.parent_id=ot.ot_id "
                                    + "left join unc_object_types ot2 "
                                    + "on ot2.parent_id=ot1.ot_id "
                                    + "left join unc_object_types ot3 "
                                    + "on ot3.parent_id=ot2.ot_id "
                                    + "left join unc_object_types ot4 "
                                    + "on ot4.parent_id=ot3.ot_id "
                                    + "where ot.ot_name='" + type + "'  "
                                    + "and ot1.ot_name='" + type1 + "' "
                                    + "and ot2.ot_name='" + type2 + "' "
                                    + "and ot3.ot_name='" + type3 + "' "
                                    + "and ot4.ot_name='" + type4 + "') ";
                        }
                    }
                }
            }
            query += " ) order by attr_order, attr_id ";
        }
        System.out.println(query);
        return query;
    }

    public static String selectChildTypeIdByTypeNames(String type, String type1, String type2, String type3, String type4) {
        String query = "";
        if (type != null && type1 == null && type2 == null && type3 == null && type4 == null) {
            query = "select t.ot_id "
                    + "from  unc_object_types t "
                    + "where t.ot_name = '" + type + "'";
        }
        if (type != null && type1 != null && type2 == null && type3 == null && type4 == null) {
            query = "select t1.ot_id "
                    + "from  unc_object_types t "
                    + "left join unc_object_types t1 "
                    + "on t1.parent_id = t.ot_id "
                    + "where t.ot_name = '" + type + "' and "
                    + "t1.ot_name = '" + type1 + "'";
        }
        if (type != null && type1 != null && type2 != null && type3 == null && type4 == null) {
            query = "select t2.ot_id "
                    + "from  unc_object_types t "
                    + "left join unc_object_types t1 "
                    + "on t1.parent_id = t.ot_id "
                    + "left join unc_object_types t2 "
                    + "on t2.parent_id = t1.ot_id "
                    + "where t.ot_name = '" + type + "' and "
                    + "t1.ot_name = '" + type1 + "' and "
                    + "t2.ot_name = '" + type2 + "'";
        }
        if (type != null && type1 != null && type2 != null && type3 != null && type4 == null) {
            query = "select t3.ot_id "
                    + "from  unc_object_types t "
                    + "left join unc_object_types t1 "
                    + "on t1.parent_id = t.ot_id "
                    + "left join unc_object_types t2 "
                    + "on t2.parent_id = t1.ot_id "
                    + "left join unc_object_types t3 "
                    + "on t3.parent_id = t2.ot_id "
                    + "where t.ot_name = '" + type + "' and "
                    + "t1.ot_name = '" + type1 + "' and "
                    + "t2.ot_name = '" + type2 + "' and "
                    + "t3.ot_name = '" + type3 + "' ";
        }
        if (type != null && type1 != null && type2 != null && type3 != null && type4 != null) {
            query = "select t4.ot_id "
                    + "from  unc_object_types t "
                    + "left join unc_object_types t1 "
                    + "on t1.parent_id = t.ot_id "
                    + "left join unc_object_types t2 "
                    + "on t2.parent_id = t1.ot_id "
                    + "left join unc_object_types t3 "
                    + "on t3.parent_id = t2.ot_id "
                    + "left join unc_object_types t4 "
                    + "on t4.parent_id = t3.ot_id "
                    + "where t.ot_name = '" + type + "' and "
                    + "t1.ot_name = '" + type1 + "' and "
                    + "t2.ot_name = '" + type2 + "' and "
                    + "t3.ot_name = '" + type3 + "' and "
                    + "t4.ot_name = '" + type4 + "' ";
        }
        System.out.println(query);
        return query;
    }

    public static String insertRegDateById(String id) {
        String query = "insert into unc_params(object_id, attr_id, value, date_value, lv_id) values ("
                + id + "," + REG_DATE_ATTR_ID + ", null, sysdate, null)";
        return query;
    }

    static public String outputUsers(String id) {
        String query = "select object_name\n"
                + "  from unc_objects \n"
                + "  where object_type_id=1 and\n"
                + "        object_id !=  " + id + "\n"
                + " order by object_name";
        return query;
    }

    static public String selectVipAdverts(String size) {
        String query = "select * "
                + "from ( "
                + "select * "
                + "from ( "
                + "select o.object_id, "
                + "o.object_name, "
                + "p2.VALUE , " +
                "  p4.value as files "
                + "from unc_objects o "
                + "left join unc_params p "
                + "on o.object_id=p.object_id "
                + "left join unc_params p2 "
                + "on p.object_id=p2.object_id " +
                " left join unc_params p4 " +
                " on p4.object_id = o.object_id and p4.attr_id = 6 and substr(p4.value,-6) = '-0.png' "
                + "where p.attr_id=20 and  "
                + "p2.attr_id=10 and  "
                + "p.date_value+7>systimestamp ) "
                + "ORDER BY dbms_random.value ) "
                + "WHERE rownum <" + size;
        System.out.println("vip" + query);
        return query;
    }

    static public String selectVipAdverts(String size, String type) {
        String query = "select * "
                + "from ( "
                + "select * "
                + "from ( "
                + "select o.object_id, "
                + "o.object_name, "
                + "p2.VALUE , " +
                " p4.value as files "
                + "from unc_objects o "
                + "left join unc_params p "
                + "on o.object_id=p.object_id "
                + "left join unc_params p2 "
                + "on p.object_id=p2.object_id " +
                " left join unc_params p4 " +
                " on p4.object_id = o.object_id and p4.attr_id = 6 and substr(p4.value,-6) = '-0.png' "
                + "where p.attr_id=20 and "
                + "p2.attr_id=10 and "
                + "p.date_value+7>systimestamp and "
                + "o.object_type_id in ( "
                + "select  ot_id "
                + "from  unc_object_types "
                + "start  with ot_id = ( "
                + "select ot_id "
                + "from unc_object_types "
                + "where OT_NAME='" + type + "' "
                + ") "
                + "connect by prior ot_id = parent_id "
                + ")) "
                + "ORDER BY dbms_random.value ) "
                + "WHERE rownum <" + size;
        System.out.println("vip" + query);
        return query;
    }

    static public String getIdByName(String name) {
        String query = "select object_id\n"
                + "  from unc_objects \n"
                + "  where object_name=" + name + " and\n"
                + " order by object_name";
        return query;
    }

    static public String getParamValue(String object_id, String attr_id) {
        String query = "select value\n"
                + "  from unc_params \n"
                + "  where object_id=" + object_id + " and\n"
                + "  attr_id=" + attr_id;
        return query;
    }

    static public String verificationUser(String id) {
        String query = "select case  "
                + "when object_type_id = 1 then "
                + "object_id "
                + "else "
                + "( "
                + "  select object_reference_id "
                + "  from UNC_REFERENCES "
                + "  where OBJECT_ID = " + id
                + " ) "
                + "end as object_id "
                + "from unc_objects "
                + "where object_id = " + id;
        return query;
    }

    static public String selectForumTopics() {
        String query = "select * "
                + "from unc_object_types uot "
                + "where uot.parent_id = " + FORUM_TOPIC;
        return query;
    }

    static public String selectselectForumTopicsWithAttrs(String type) {
        String query = "select o.object_id, "
                + "o.object_name, "
                + "p1.value as description, "
                + "to_char(p2.DATE_VALUE,'DD:MM:YYYY HH24:MI:SS') as creation_date, "
                + "p3.value as creation_id, "
                + "o2.object_name as creation_login , "
                + "count(o.object_id) as numb_comments "
                + "from unc_objects o "
                + "left join unc_params p1 "
                + "on p1.object_id = o.object_id "
                + "left join unc_params p2 "
                + "on p2.object_id = o.object_id "
                + "left join unc_params p3 "
                + "on p3.object_id = o.object_id "
                + "left join unc_objects o2 "
                + "on o2.OBJECT_ID = p3.VALUE "
                + "left join unc_params p4 "
                + "on p4.value = o.object_id and p4.attr_id = 42 "
                + "where o.object_type_id = ( "
                + "select ot.ot_id  "
                + "from unc_object_types ot  "
                + "where lower(ot.ot_name) =  "
                + "lower('" + type + "') "
                + ") and p1.attr_id = " + DESCRIPTION_ATTR_ID
                + " and p2.attr_id =  " + DATE_CREATION
                + " and p3.attr_id =  " + PERSON_CREATION
                + " group by o.object_id, o.object_name, p1.value, to_char(p2.DATE_VALUE,'DD:MM:YYYY HH24:MI:SS'), p3.value, o2.object_name "
                + "order by creation_date desc";
        System.out.println(query);
        return query;
    }

    public static String selectForumsDiscussion(String id) {
        String query = "select o.object_id, "
                + "o.object_name, "
                + "p1.value as description, "
                + "to_char(p2.DATE_VALUE,'DD:MM:YYYY HH24:MI:SS') as creation_date, "
                + "p3.value as creation_id, "
                + "o2.object_name as creation_login "
                + "from unc_objects o "
                + "left join unc_params p1 "
                + "on p1.object_id = o.object_id "
                + "left join unc_params p2 "
                + "on p2.object_id = o.object_id "
                + "left join unc_params p3 "
                + "on p3.object_id = o.object_id "
                + "left join unc_objects o2 "
                + "on o2.OBJECT_ID = p3.VALUE "
                + "where o.object_id = " + id
                + " and p1.attr_id = " + DESCRIPTION_ATTR_ID
                + " and p2.attr_id =  " + DATE_CREATION
                + " and p3.attr_id =  " + PERSON_CREATION
                + " order by p2.DATE_VALUE ";
        System.out.println(query);
        return query;
    }

    public static String selectForumComments(String forumTopicId) {
        String query = "select  o.object_name, "
                + "to_char(p1.date_value, 'DD:MM:YYYY HH24:MI:SS') as date_creation, "
                + "p2.value as id_creation, "
                + "o2.object_name as login_creation "
                + "from unc_objects o "
                + "left join unc_params p1 "
                + "on p1.object_id = o.object_id "
                + "left join unc_params p2 "
                + "on p2.object_id = o.object_id "
                + "left join unc_params p3 "
                + "on p3.object_id = o.object_id "
                + "left join unc_objects o2 "
                + "on o2.object_id = p2.value "
                + "where o.object_type_id = 392 and "
                + "p1.attr_id = 38 and "
                + "p2.attr_id = 39 and "
                + "p3.attr_id = 42 and "
                + "p3.value = " + forumTopicId;
        return query;
    }

    static String insertUser(String id, String name) {
        String query = "insert into UNC_OBJECTS values(" + id + ", 1, '" + name + "', null)";
        return query;
    }

    static String insertParam(String id, String type, String value, String date) {
        StringBuilder query = new StringBuilder("insert into UNC_PARAMS values(" + id + ", " + type + ", ");
        query.append(value == null ? "null, " : "'" + value + "'");
        query.append(date == null ? "null, " : "TO_DATE('" + date + "', 'dd.mm.yy')");
        query.append("null)");
        return query.toString();
    }

    static String getTypeIdByName(String name) {
        StringBuilder query = new StringBuilder("select  ATTR_ID\n"
                + "  from  UNC_ATTRIBUTES\n"
                + "  where ATTR_NAME = ");
        query.append("'" + name + "'");
        return query.toString();
    }

    static String insertUser(BigDecimal id, String name) {
        String query = "insert into UNC_OBJECTS values(" + id + ", 1, '" + name + "', null)";
        System.out.println(query);
        return query;
    }

    static String checkLogin(String login) {
        StringBuilder query = new StringBuilder("select OBJECT_ID\n"
                + "  from UNC_OBJECTS\n"
                + "  where OBJECT_TYPE_ID = 1 and OBJECT_NAME = ");
        query.append("'" + login + "'");
        System.out.println(query.toString());
        return query.toString();
    }

    static String getTypePropertyAdvertByTypeNameWithSubCategory(String category, String subcategory, String action) {
        StringBuilder query = new StringBuilder("select  ctg.OT_ID\n"
                + "  from (select  u.OT_NAME,\n"
                + "                u.OT_ID,\n"
                + "                u.PARENT_ID\n"
                + "          from UNC_OBJECT_TYPES u\n"
                + "          CONNECT BY prior u.OT_ID = u.PARENT_ID\n"
                + "          start with u.OT_NAME = '" + category + "') ctg\n"
                + "  where ctg.OT_NAME = '" + subcategory + "'\n"
                + "  CONNECT BY prior ctg.OT_ID = ctg.PARENT_ID\n"
                + "  start with ctg.OT_NAME = '" + action + "'");
        System.out.println(query.toString());
        return query.toString();
    }

    static String getTypePropertyAdvertByTypeNameWithOutSubCategory(String category, String action) {
        StringBuilder query = new StringBuilder("select  ctg.OT_ID\n"
                + "  from  UNC_OBJECT_TYPES ctg\n"
                + "  where ctg.OT_NAME = '" + action + "'\n"
                + "  CONNECT BY prior ctg.OT_ID = ctg.PARENT_ID\n"
                + "  start with ctg.OT_NAME = '" + category + "'");
        System.out.println(query.toString());
        return query.toString();
    }

    static String insertPropertyAdvert(BigDecimal id, String type, String name) {
        String query = "insert into UNC_OBJECTS values(" + id + ", " + type + ", '" + name + "', null)";
        System.out.println(query);
        return query;
    }

    public static String mergeParamValue(String id, String attr_id, String newValue) {
        String comm = "MERGE INTO unc_params up\n"
                + "USING (SELECT " + id + " object_id, " + attr_id + " attr_id, '" + newValue + "' value, null date_value, null lv_id from dual) s\n"
                + "ON ((up.object_id = s.object_id) and (up.attr_id = s.attr_id))\n"
                + "WHEN MATCHED THEN UPDATE SET up.value = s.value\n"
                + "WHEN NOT MATCHED THEN INSERT (object_id, attr_id, value, date_value, lv_id) VALUES (s.object_id, s.attr_id, s.value, s.date_value, s.lv_id)";

        return comm;
    }

    static public String selectAllUsersDialog (String id){
        String query = "select  t3.mess_id, " +
                "t2.sender, " +
                "t2.recipient, " +
                "t3.mess_text, " +
                "t2.mess_date, " +
                "o3.object_name as login, " +
                "p9.value as name, " +
                "p10.value as surname, " +
                "t3.read_status " +
                "from  ( " +
                "select  t1.sender,  " +
                "t1.recipient, " +
                "max(t1.mess_date) as mess_date " +
                "from  ( " +
                "select  o1.object_id as mess_id, " +
                "p2.date_value as mess_date, " +
                "case  " +
                "when p3.value != " + id + " then " +
                "p4.value " +
                "else " +
                "p3.value " +
                "end as sender, " +
                "case " +
                "when p4.value = " + id + " then " +
                "p3.value " +
                "else " +
                "p4.value " +
                "end as recipient " +
                "from  unc_objects o1 " +
                "left join unc_params p2 " +
                "on p2.OBJECT_ID = o1.OBJECT_ID " +
                "left join unc_params p3 " +
                "on p3.OBJECT_ID = o1.OBJECT_ID " +
                "left join unc_params p4 " +
                "on p4.OBJECT_ID = o1.OBJECT_ID " +
                "where  o1.object_type_id = 3 and " +
                "p2.ATTR_ID = 32 and " +
                "p3.ATTR_ID = 33 and " +
                "p4.ATTR_ID = 34 and " +
                "(p3.value = " + id + " or p4.value =  " + id +  ") " +
                ") t1 " +
                "group  by t1.sender, t1.recipient " +
                ") t2 " +
                "left join ( " +
                "select  o2.object_id as mess_id, " +
                "p5.value as mess_text, " +
                "p6.date_value as mess_date, " +
                "p7.value as sender, " +
                "p8.value as recipient, " +
                "case " +
                "when  p7.value = " + id +  " then  " +
                "'yes' " +
                "else " +
                "p11.value  " +
                "end as read_status " +
                "from  unc_objects o2 " +
                "left join unc_params p5 " +
                "on p5.OBJECT_ID = o2.OBJECT_ID " +
                "left join unc_params p6 " +
                "on p6.OBJECT_ID = o2.OBJECT_ID " +
                "left join unc_params p7 " +
                "on p7.OBJECT_ID = o2.OBJECT_ID " +
                "left join unc_params p8 " +
                "on p8.OBJECT_ID = o2.OBJECT_ID " +
                "left join unc_params p11 " +
                "on p11.OBJECT_ID = o2.OBJECT_ID " +
                "where  o2.object_type_id = 3 and " +
                "p5.ATTR_ID = 31 and " +
                "p6.ATTR_ID = 32 and " +
                "p7.ATTR_ID = 33 and " +
                "p8.ATTR_ID = 34 and " +
                "p11.attr_id = 44 and " +
                "(p7.value =  " + id +  " or p8.value =  " + id +  ") " +
                ") t3 " +
                "on t3.mess_date = t2.mess_date and " +
                "t2.sender in (t3.sender, t3.recipient) and " +
                "t2.recipient in (t3.sender, t3.recipient)  " +
                "left join unc_objects o3 " +
                "on o3.object_id = t2.recipient " +
                "left join unc_params p9 " +
                "on t2.recipient = p9.object_id and p9.ATTR_ID = 12 " +
                "left join unc_params p10 " +
                "on t2.recipient = p10.object_id and p10.ATTR_ID = 14 ";
        return query;
    }
    public static String updateReadStatus (String messId){
        String query = "update unc_params " +
                "set value = 'yes' " +
                "where object_id = " + messId + " and attr_id = " + READ_ATTR_ID;
        return query;
    }
    public static String getUsersIds() {
        return "select object_id\n"
                + "from  UNC_OBJECTS\n"
                + "where (OBJECT_TYPE_ID = \n"
                + "      (select CONNECT_BY_ROOT(uot.OT_ID) from UNC_OBJECT_TYPES uot\n"
                + "      connect by prior uot.OT_ID = uot.PARENT_ID\n"
                + "      start with uot.OT_ID = " + USER_TYPE_ID + "))";
    }
    public static String selectCountNewMessage (String userId){
        String query = "select  count(*) as new_message " +
                " from  unc_params p1 " +
                " left join unc_params p2 " +
                " on p2.object_id = p1.object_id  " +
                " where  p1.attr_id = 34 and p1.value = " + userId + " " +
                " and p2.attr_id = 44 and p2.value = 'no'";
        return query;
    }
    public static String selectPreviousMessages (String messId, String sendId, String recId){
        String query = "select  p1.object_id as mess_id, " +
                "p1.value as mess_text, " +
                "p2.date_value as mess_date, " +
                "p3.value as sender, " +
                "p4.value as recipient, " +
                "p5.value as read_status, " +
                "o1.object_name as login, " +
                "p6.value as name, " +
                "p7.value as surname " +
                "from  unc_params p1 " +
                "left join unc_params p2 " +
                "on p2.object_id = p1.object_id " +
                "left join unc_params p3 " +
                "on p3.object_id = p1.object_id " +
                "left join unc_params p4 " +
                "on p4.object_id = p1.object_id " +
                "left join unc_params p5 " +
                "on p5.object_id = p1.object_id " +
                "left join unc_objects o1 " +
                "on o1.object_id = p3.value " +
                "left join unc_params p6 " +
                "on p6.object_id = p3.value and p6.attr_id = 12 " +
                "left join unc_params p7 " +
                "on p7.object_id = p3.value and p7.attr_id = 14 " +
                "where  p1.attr_id = 31 and p2.attr_id = 32 and p5.attr_id = 44 and " +
                "p3.attr_id = 33 and p4.attr_id = 34 and  " +
                "((p3.value = " + sendId +" and p4.value = " +recId+") or " +
                "(p3.value = "+recId+" and p4.value = "+sendId+" )) and " +
                "p2.date_value<(select date_value from unc_params where attr_id = 32 and object_id = "+messId+")  " +
                "order by p2.date_value desc";
        System.out.println(query);
        return query;
    }

    public static String deleteVipAdverts() {
        String query = "delete unc_params up where sysdate - 1 > up.date_value";
        return query;
    }
}
