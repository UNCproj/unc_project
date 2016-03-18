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

/**
 * Created by Денис on 24.02.2016.
 */
public class UncObject {
    private String id;
    private String type;
    private String parentType;
    private String name;
    private ArrayList<Param> params;
    private ArrayList<String> attributeGroups;

    public UncObject() {
        params = new ArrayList<>();
        attributeGroups = new ArrayList<>();
    }

    public UncObject(String id, String name) {
        this();
        this.id = id;
        this.name = name;
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

    public void insertIntoDB() throws PropertyVetoException, SQLException, IOException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            ResultSet results = (statement.executeQuery(SQLQueriesHelper.newId()));
            results.next();
            id = results.getString("id");
            statement.executeUpdate(SQLQueriesHelper.insertObject(id, type, name));

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

    public void updateInDB() throws PropertyVetoException, SQLException, IOException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
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
                        statement.executeUpdate(SQLQueriesHelper.updateParamByName(
                                new BigDecimal(id), param.getName(), param.getValue(), param.getDateValue()
                        ));
                    }
                    else {
                        statement.executeUpdate(SQLQueriesHelper.updateParam(
                                new BigDecimal(id), param.getAttrId(), param.getValue(), param.getDateValue()
                        ));
                    }
                }
            }
            connection.commit();
        }
    }

    public void selectFromDB() throws PropertyVetoException, SQLException, IOException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            params = new ArrayList<>();

            ResultSet results = statement.executeQuery(
                    (id == null || id.length() == 0) ?
                            SQLQueriesHelper.selectFullObjectInformationByName(name) :
                            SQLQueriesHelper.selectFullObjectInformationById(new String[]{id})

            );

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
                for (Param param: params) {
                    if (param.getName().equals(currentParamName)) {
                        param.setValue(results.getString("value"));
                        param.setRuName(results.getString("attr_name_ru"));
                        param.setGroup(results.getString("attr_group"));
                        param.setType(results.getString("attr_type"));
                        isAdded = true;
                        break;
                    }
                }

                if (!isAdded) {
                    params.add(new Param(currentParamName,
                                            results.getString("value"),
                                            results.getString("attr_name_ru"),
                                            results.getString("attr_group"),
                                            results.getString("attr_type"))
                    );
                }
            }
        }
    }

    public void loadAttributesListFromDB() throws PropertyVetoException, SQLException, IOException {
        try(Connection connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement())
        {
            if (type == null || type.length() == 0) {
                ResultSet results = statement.executeQuery(SQLQueriesHelper.getTypeIdByObjectId(id));
                results.next();
                type = results.getString("type_id");
                ResultSet results1 = statement.executeQuery(SQLQueriesHelper.getParentTypeIdByObjectTypeId(type));
                results1.next();
                parentType = ""+results1.getInt("praperent");
                
            }
            
            if (parentType.equals(SQLQueriesHelper.ADVERT_TYPE_ID)){
                statement.execute(SQLQueriesHelper.insertAdStat(id));
                connection.commit();
            }
            
            ResultSet results = statement.executeQuery(SQLQueriesHelper.getAllHierarchyAttributes(type));

            while (results.next()) {
                String group = results.getString("attr_group_id");

                if (!attributeGroups.contains(group)) {
                    attributeGroups.add(group);
                }

                params.add(new Param(results.getString("attr_name"), results.getString("attr_name_ru"), null, group));
            }
            statement.close();
            connection.close();
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
                if (param.getGroup().equals(groupName)) {
                    paramsInCurrentGroups.add(param);
                    break;
                }
            }
        }

        return paramsInCurrentGroups;
    }

    public ArrayList<String> getAttributeGroups() {
        return attributeGroups;
    }
}
