package com.singular.barrister.Model;

import com.singular.barrister.Util.IModel;

import java.io.Serializable;

/**
 * Created by rahul.kalamkar on 11/29/2017.
 */

public class CasesSubType implements IModel, Serializable {
    String subcase_type_id;
    String subcase_type_name;

    public String getSubcase_type_id() {
        return subcase_type_id;
    }

    public void setSubcase_type_id(String subcase_type_id) {
        this.subcase_type_id = subcase_type_id;
    }

    public String getSubcase_type_name() {
        return subcase_type_name;
    }

    public void setSubcase_type_name(String subcase_type_name) {
        this.subcase_type_name = subcase_type_name;
    }

    public CasesSubType(String subcase_type_id, String subcase_type_name) {
        this.subcase_type_id = subcase_type_id;
        this.subcase_type_name = subcase_type_name;
    }
}
