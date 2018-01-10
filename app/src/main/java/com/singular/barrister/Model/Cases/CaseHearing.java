package com.singular.barrister.Model.Cases;

import com.singular.barrister.Util.IModel;

import java.io.Serializable;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CaseHearing implements IModel, Serializable {
    String id;
    String case_id;
    String case_hearing_date;
    String case_decision;
    String case_note;
    String case_disposed;

    public String getCase_notes() {
        return case_note;
    }

    public void setCase_notes(String case_notes) {
        this.case_note = case_notes;
    }


    public String getCase_disposed() {
        return case_disposed;
    }

    public void setCase_disposed(String case_disposed) {
        this.case_disposed = case_disposed;
    }


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


    public CaseHearing(String id, String case_id, String case_hearing_date, String case_decision, String case_notes, String case_disposed) {
        this.id = id;
        this.case_id = case_id;
        this.case_note = case_notes;
        this.case_hearing_date = case_hearing_date;
        this.case_decision = case_decision;
        this.case_disposed = case_disposed;
    }
}
