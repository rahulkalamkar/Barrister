package com.singular.barrister.Model.Cases;

import com.singular.barrister.Util.IModel;

import java.io.Serializable;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CasePersons implements IModel,Serializable {
    String case_id;
    String opp_name;
    String mobile;
    String country_code;

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

    String type;
}
