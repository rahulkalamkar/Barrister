package com.singular.barrister.Model;

import com.singular.barrister.Util.IModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahul.kalamkar on 11/29/2017.
 */

public class CasesTypeData implements IModel, Serializable {
    String case_type_id;
    String case_type_name;
    ArrayList<CasesSubType> subCaseData;

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

    public ArrayList<CasesSubType> getSubCaseData() {
        return subCaseData;
    }

    public void setSubCaseData(ArrayList<CasesSubType> subCaseData) {
        this.subCaseData = subCaseData;
    }
}
