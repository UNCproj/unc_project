package unc.helpers;

import java.util.Date;

/**
 * Created by Денис on 24.02.2016.
 */
public class Param {
    private String attrId;
    private String name;
    private String ruName;
    private String value;
    private Date dateValue;
    private String group;
    private String type;
    private boolean isReference;

    public Param() {

    }

    public Param(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Param(String name, String value, String ruName, String group) {
        this(name, value);
        this.ruName = ruName;
        this.group = group;
    }

    public Param(String name, String value, String ruName, String group, String type) {
        this(name, value, ruName, group);
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuName() {
        return ruName;
    }

    public void setRuName(String ruName) {
        this.ruName = ruName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReference(boolean reference) {
        isReference = reference;
    }

    public boolean isReference() {
        return isReference;
    }
}
