package com.singular.barrister.Model.Cases;

import com.singular.barrister.Util.IModel;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CaseHearing implements IModel{
    String id;
    String case_id;
    String case_hearing_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getCase_hearing_date() {
        return case_hearing_date;
    }

    public void setCase_hearing_date(String case_hearing_date) {
        this.case_hearing_date = case_hearing_date;
    }

    public String getCase_decision() {
        return case_decision;
    }

    public void setCase_decision(String case_decision) {
        this.case_decision = case_decision;
    }

    String case_decision;
}
