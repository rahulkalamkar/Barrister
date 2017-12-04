package com.singular.barrister.Database.Tables.Case;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by rahul.kalamkar on 12/4/2017.
 */

@DatabaseTable(tableName = "CasePerson")
public class CasePerson {
    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getOpp_name() {
        return opp_name;
    }

    public void setOpp_name(String opp_name) {
        this.opp_name = opp_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @DatabaseField(columnName = "id", generatedId = true)
    int id;

    @DatabaseField(columnName = "case_id", canBeNull = true)
    String case_id;
    @DatabaseField(columnName = "opp_name", canBeNull = true)
    String opp_name;
    @DatabaseField(columnName = "opp_mobile", canBeNull = true)
    String mobile;
    @DatabaseField(columnName = "country_code", canBeNull = true)
    String country_code;
    @DatabaseField(columnName = "type", canBeNull = true)
    String type;

    public CasePerson(String case_id, String opp_name, String mobile, String country_code, String type) {
        this.case_id = case_id;
        this.opp_name = opp_name;
        this.mobile = mobile;
        this.country_code = country_code;
        this.type = type;
    }

    public CasePerson() {
    }
}
