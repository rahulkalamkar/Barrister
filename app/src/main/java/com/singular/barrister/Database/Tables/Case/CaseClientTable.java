package com.singular.barrister.Database.Tables.Case;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by rahul.kalamkar on 12/4/2017.
 */

@DatabaseTable(tableName = "CaseClientTable")
public class CaseClientTable {

    @DatabaseField(columnName = "id", generatedId = true)
    int id;

    @DatabaseField(columnName = "client_id")
    String client_id;
    @DatabaseField(columnName = "first_name", canBeNull = true)
    String first_name;
    @DatabaseField(columnName = "last_name", canBeNull = true)
    String last_name;

    @DatabaseField(columnName = "country_code", canBeNull = true)
    String country_code;

    @DatabaseField(columnName = "mobile", canBeNull = true)
    String mobile;

    @DatabaseField(columnName = "email", canBeNull = true)
    String email;

    @DatabaseField(columnName = "address", canBeNull = true)
    String address;


    public CaseClientTable() {

    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CaseClientTable(String client_id, String first_name, String last_name, String country_code, String mobile, String email, String address) {
        this.client_id = client_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.country_code = country_code;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
    }
}
