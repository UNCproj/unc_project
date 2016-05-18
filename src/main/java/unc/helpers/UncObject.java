package unc.helpers;

import db.DataSource;
import db.SQLQueriesHelper;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

/**
 * Created by Денис on 24.02.2016.
 */
public class UncObject {
    public final Logger log = Logger.getLogger("unc_log");
    private String id;
    private String type;
    private String parentType;
    private String name;
    private ArrayList<Param> params;
    private ArrayList<String> attributeGroups;
    private String userId;
    private String advertId;

    public UncObject() {
        params = new ArrayList<>();
        attributeGroups = new ArrayList<>();
    }

    public UncObject(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public UncObject (String type){
        this();
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement()){
            ResultSet results = statement.executeQuery(SQLQueriesHelper.getTypeIdByTypeName(type));
            results.next();
            this.type = results.getString("id");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
//        this.type = type; //Название типа
    }

    public UncObject(String id, String name, boolean isNeedLoadAllAttributes) {
        this(id, name);
        if (isNeedLoadAllAttributes) {
            try {
                loadAttributesListFromDB();
            } catch (PropertyVetoException|SQLException|IOException e) {
                e.printStackTrace();
            }
        }
    }

    public UncObject(String id, String name, String typeId, String userId){
        this.id = id;
        this.name = name;
        this.type = typeId;
        attributeGroups = new ArrayList<>();
        params = new ArrayList<>();
        this.type = typeId;
        this.userId = userId;
    }
    
    public boolean isVip() throws IOException, SQLException, PropertyVetoException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            ResultSet results = statement.executeQuery(SQLQueriesHelper.isVip(id));
            while (results.next()) {
                String id = results.getString("VALUE");
                if ("Gold".equals(id)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public void vipAdvert(String id_advert) throws SQLException, IOException, PropertyVetoException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            statement.executeUpdate(SQLQueriesHelper.setVipAdvert(id_advert));
            connection.commit();
        }
    }
    
    public String MD5(String[] mass) throws NoSuchAlgorithmException{
        //params
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mass.length; i++) {
            sb.append(mass[i]);
            if (i != mass.length - 1) {
                sb.append(":");
            }
        }
        String result = sb.toString();
    	
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(result.getBytes());
        
        byte byteData[] = md.digest();
        sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
     
        return sb.toString();
    }

    public void insertIntoDB() throws PropertyVetoException, SQLException, IOException {
        try (Connection connection = DataSource.getInstance().getConnection();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            ResultSet results = (statement.executeQuery(SQLQueriesHelper.newId()));
            results.next();
            id = results.getString("id");
            advertId = id;
            statement.executeUpdate(SQLQueriesHelper.insertObject(id, type, name));

            if (userId != null) {
                Statement statementReference = connection.createStatement();
                System.out.println(type);
                if (Integer.parseInt(type) > 388) {
                    statementReference.executeUpdate(SQLQueriesHelper.insertParam(
                            new BigDecimal(id), "39", userId, null
                    ));
                    statementReference.executeUpdate(SQLQueriesHelper.insertParam(
                            new BigDecimal(id), "38", null, null
                    ));
                } else {
                    statementReference.executeUpdate(SQLQueriesHelper.newReference(id, userId, SQLQueriesHelper.USERS_ADVERT_ATTR_ID));
                }
                if (type.equals("388")) {
                    statementReference.executeUpdate(SQLQueriesHelper.insertParam(
                            new BigDecimal(id), "42", userId, null
                    ));
                }
                Statement statementAllAttrId = connection.createStatement();
                ResultSet resultsAllAttrId = statementAllAttrId.executeQuery(SQLQueriesHelper.getAllAttributes(type));
                while (resultsAllAttrId.next()) {
                    for (Param p : params) {
                        if (p.getName().equals(resultsAllAttrId.getString("attr_name"))) {
                            p.setAttrId(resultsAllAttrId.getString("attr_id"));
                        }
                    }
                }
                if (Integer.parseInt(type) < 388) {
                    Statement statementRegDate = connection.createStatement();
                    statementRegDate.executeUpdate(SQLQueriesHelper.insertRegDateById(id));
                }
            }

            for (Param param : params) {
                if (param.isReference()) {
                    statement.executeUpdate(SQLQueriesHelper.newReference(
                            id, param.getValue(), param.getAttrId()
                    ));
                } else {
                    statement.executeUpdate(SQLQueriesHelper.insertParam(
                            new BigDecimal(id), param.getAttrId(), param.getValue(), param.getDateValue()
                    ));
                }
            }
            connection.commit();
        }
    }
    
    public ArrayList<String[]> lisrReferences()  throws IOException, SQLException, PropertyVetoException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            ArrayList<String[]> list = new ArrayList<>();
            ResultSet results = statement.executeQuery(SQLQueriesHelper.getListReferences(id));
            while (results.next()) {
                String[] mass = new String[3];
                mass[0] = results.getString("OBJECT_ID");
                mass[1] = results.getString("OBJECT_NAME");
                mass[2] = results.getString("invalid");
                list.add(mass);
            }
            return list;
        }
    }

    public void updateInDB() throws PropertyVetoException, SQLException, IOException {
        Connection connection = null;
        Statement statement = null;

        try
        {
            connection = DataSource.getInstance().getConnection();
            statement = connection.createStatement();

            connection.setAutoCommit(false);
            if (type != null && type.length() > 0) {
                ResultSet results = statement.executeQuery(SQLQueriesHelper.getTypeIdByTypeName(type));
                results.next();
                String typeId = results.getString("id");
                statement.executeUpdate(SQLQueriesHelper.changeType(new BigDecimal(id), typeId));
            }

            if (name != null && name.length() > 0) {
                statement.executeUpdate(SQLQueriesHelper.changeName(new BigDecimal(id), name));
            }

            if (params != null) {
                for (Param param : params) {
                    if (param.getAttrId() == null) {
                        statement.executeUpdate(SQLQueriesHelper.mergeParamByName(
                                new BigDecimal(id), param.getName(), param.getValue(), param.getDateValue()
                        ));
                    }
                    else {
                        statement.executeUpdate(SQLQueriesHelper.mergeParam(
                                new BigDecimal(id), param.getAttrId(), param.getValue(), param.getDateValue()
                        ));
                    }
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
        }
        catch (SQLException e) {
            connection.rollback();
        }
        finally {
            if (connection != null) {
                connection.close();
            }

            if (statement != null) {
                statement.close();
            }
        }
    }

    public void selectFromDB() throws PropertyVetoException, SQLException, IOException {
        try(Connection connection = DataSource.getInstance().getConnection())
        {
            params = new ArrayList<>();
            PreparedStatement statement;
            if (id == null || id.length() == 0) { 
                statement = connection.prepareStatement(SQLQueriesHelper.selectFullObjectInformationByName());
                statement.setString(1, name);
            }
            else {
                String[] mass = new String[]{id};
                statement = connection.prepareStatement(SQLQueriesHelper.selectFullObjectInformationById(mass));
                int j = 1;
                for (String i : mass) {
                    statement.setString(j++, i);
                }
            }
            System.out.println(statement.toString());
            ResultSet results = statement.executeQuery();
            

            while (results.next()) {
                if (id == null || id.length() == 0) {
                    id = results.getString("object_id");
                }

                if (type == null || type.length() == 0) {
                    type = results.getString("type");
                }

                if (name == null || name.length() == 0) {
                    name = results.getString("object_name");
                }

                String currentParamName = results.getString("attr_name");
                boolean isAdded = false;
                /*for (Param param: params) {
                    if (param.getName().equals(currentParamName)) {
                        param.setValue(results.getString("value"));
                        param.setRuName(results.getString("attr_name_ru"));
                        param.setGroup(results.getString("attr_group"));
                        param.setType(results.getString("attr_type"));
                        isAdded = true;
                        break;
                    }
                }*/

                if (!isAdded) {
                    params.add(new Param(currentParamName,
                                            results.getString("value"),
                                            results.getString("attr_name_ru"),
                                            results.getString("attr_group"),
                                            results.getString("attr_type"))
                    );
                }
            }
            statement.close();
        }
    }
    
    public ArrayList<String> selectCategory(String type) throws IOException, SQLException, PropertyVetoException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            ArrayList<String> list = new ArrayList<>();
            ResultSet results = statement.executeQuery(SQLQueriesHelper.selectCategories(type));
            while (results.next()) {
                list.add(results.getString("OT_NAME"));
            }
            return list;
        }
    }

    public void loadAttributesListFromDB() throws PropertyVetoException, SQLException, IOException {
        try(Connection connection = DataSource.getInstance().getConnection())
        {
            PreparedStatement statement;
            if (type == null || type.length() == 0) {
                statement = connection.prepareStatement(SQLQueriesHelper.getTypeIdByObjectId());
                statement.setBigDecimal(1, new BigDecimal(id));
                ResultSet results = statement.executeQuery();
                results.next();
                type = results.getString("type_id");
                statement.close();
                statement = connection.prepareStatement(SQLQueriesHelper.getParentTypeIdByObjectTypeId());
                statement.setInt(1, new Integer(type));
                
                
                ResultSet results1 = statement.executeQuery();
                results1.next();
                parentType = ""+results1.getInt("praperent");
                statement.close();
                
            }
            
            if (parentType!=null && parentType.equals(SQLQueriesHelper.ADVERT_TYPE_ID) && 
                    id!=null && ((getParam("is_invalid")!= null && !getParam("is_invalid").getValue().equals("true")) || getParam("is_invalid")==null)){
                statement = connection.prepareStatement(SQLQueriesHelper.insertAdStat());
                statement.setBigDecimal(1, new BigDecimal(id));
                statement.execute();
                statement.close();
                connection.commit();
            }
            statement = connection.prepareStatement(SQLQueriesHelper.getAllHierarchyAttributes());
            statement.setInt(1, new Integer(type));
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                String group = results.getString("attr_group_id");
                if ((!attributeGroups.contains(group))&&(group!=null)) {
                    attributeGroups.add(group);
                }

                params.add(new Param(results.getString("attr_name"), results.getString("attr_name_ru"), null, group));
            }
            statement.close();
            connection.close();
        }
        
    }
    
    public boolean isAdvert() throws SQLException, IOException, PropertyVetoException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {

            ResultSet results = statement.executeQuery(SQLQueriesHelper.isAdvert(type));

            while (results.next()) {
                String id = results.getString("OT_ID");

                if ("4".equals(id)) {
                    return true;
                }
            }
            return false;
        }
    }

    public String getId() {
        return id;
    }
    
    public String getParentType() {
        return parentType;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParam(String name, String value) {
        params.add(new Param(name, value));
    }

    public ArrayList<Param> getParams() {
        return params;
    }

    public ArrayList<Param> getParams(String groupName) {
        return getParams(new String[] { groupName });
    }

    public ArrayList<Param> getParams(String[] groupsNames) {
        ArrayList<Param> paramsInCurrentGroups = new ArrayList<>();

        for (Param param: params) {
            for (String groupName: groupsNames) {
                if (param.getGroup()!=null){
                    if (param.getGroup().equals(groupName)) {
                        paramsInCurrentGroups.add(param);
                        break;
                    }
                }
            }
        }

        return paramsInCurrentGroups;
    }

    public ArrayList<String> getAttributeGroups() {
        return attributeGroups;
    }
    
    public Param getParam(String name){
        ArrayList<Param> prms = getParams();
        for (Param p : prms){
            if (p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }
    public String getAdvertId (){
        return advertId;
    }
}
