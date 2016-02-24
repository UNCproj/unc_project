package unc.helpers;

import java.util.Date;

/**
 * Created by Денис on 24.02.2016.
 */
public class Param {
    private String attrId;
    private String name;
    private String value;
    private Date dateValue;
    private String group;
    private boolean isReference;

    public Param() {

    }

    public Param(String name, String value) {
        this.name = name;
        this.value = value;
        this.group = null;
    }

    public Param(String name, String value, String group) {
        this.name = name;
        this.value = value;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setReference(boolean reference) {
        isReference = reference;
    }

    public boolean isReference() {
        return isReference;
    }
}
