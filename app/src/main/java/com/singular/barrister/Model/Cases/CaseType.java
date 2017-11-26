package com.singular.barrister.Model.Cases;

import com.singular.barrister.Util.IModel;

import java.io.Serializable;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CaseType implements IModel,Serializable{
    String case_type_id;

    public String getCase_type_id() {
        return case_type_id;
    }

    public void setCase_type_id(String case_type_id) {
        this.case_type_id = case_type_id;
    }

    public String getCase_type_name() {
        return case_type_name;
    }

    public void setCase_type_name(String case_type_name) {
        this.case_type_name = case_type_name;
    }

    String case_type_name;
}
