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
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Денис on 24.02.2016.
 */
public class UncObject {
    private String id;
    private String type;
    private String name;
    private ArrayList<Param> params;
    private ArrayList<String> attributeGroups;

    public UncObject() {

    }

    public UncObject(String name) {
        this.name = name;
    }

    public UncObject(String name, boolean isNeedLoadAllAttributes) {
        this(name);

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
            Statement statement = connection.createStatement())
        {
            connection.setAutoCommit(false);
            ResultSet results = (statement.executeQuery(SQLQueriesHelper.newId()));
            results.next();
            id = results.getString("id");
            statement.executeUpdate(SQLQueriesHelper.insertObject(id, type, name));

            if (params != null) {
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
                ResultSet results = statement.executeQuery(SQLQueriesHelper.getTypeId(type));
                results.next();
                String typeId = results.getString("id");
                statement.executeUpdate(SQLQueriesHelper.changeType(new BigDecimal(id), typeId));
            }

            if (name != null && name.length() > 0) {
                statement.executeUpdate(SQLQueriesHelper.changeName(new BigDecimal(id), name));
            }

            if (params != null) {
                for (Param param : params) {
                    statement.executeUpdate(SQLQueriesHelper.updateParam(
                            new BigDecimal(id), param.getAttrId(), param.getValue(), param.getDateValue()
                    ));
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
                            SQLQueriesHelper.selectFullObjectInformationById(new String[]{type}, new String[]{id}) :
                            SQLQueriesHelper.selectFullObjectInformationByName(new String[]{type}, name)
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
                        param.setGroup(results.getString("group"));
                        isAdded = true;
                        break;
                    }
                }

                if (!isAdded) {
                    params.add(new Param(currentParamName,
                                            results.getString("value"),
                                            results.getString("group"))
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
                selectFromDB();
            }

            ResultSet results = statement.executeQuery(SQLQueriesHelper.getAllAttributes(type));

            while (results.next()) {
                String group = results.getString("group");

                if (!attributeGroups.contains(group)) {
                    attributeGroups.add(group);
                }

                params.add(new Param(results.getString("attr_name"), null, group));
            }
        }
    }

    public String getId() {
        return id;
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
