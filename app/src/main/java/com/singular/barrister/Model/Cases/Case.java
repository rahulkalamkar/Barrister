package com.singular.barrister.Model.Cases;

import com.singular.barrister.Model.Court.CourtData;
import com.singular.barrister.Util.IModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class Case implements IModel, Serializable {
    String id;
    String client_id;
    String client_type;
    String court_id;
    String case_cnr_number;
    String case_register_number;
    String case_register_date;
    String case_type;
    String case_sub_type;
    String case_status;
    CaseClient client;
    List<CasePersons> persons;
    CaseType casetype;
    SubCaseType subCasetype;
    CourtData court;
    String diff;

    public Case(String id, String client_id, String client_type, String court_id, String case_cnr_number, String case_register_number, String case_register_date,
                String case_type, String case_sub_type, String case_status, CaseClient client, List<CasePersons> persons, CaseType caseType, SubCaseType subCaseType,
                CourtData court, String diff, CaseHearing hearing) {
        this.id = id;
        this.client_id = client_id;
        this.client_type = client_type;
        this.court_id = court_id;
        this.case_cnr_number = case_cnr_number;
        this.case_register_number = case_register_number;
        this.case_register_date = case_register_date;
        this.case_type = case_type;
        this.case_sub_type = case_sub_type;
        this.case_status = case_status;
        this.client = client;
        this.case_type = case_type;
        this.case_sub_type = case_sub_type;
        this.subCasetype = subCaseType;
        this.casetype = caseType;
        this.persons = persons;
        this.court = court;
        this.diff = diff;
        this.hearing = hearing;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getCourt_id() {
        return court_id;
    }

    public void setCourt_id(String court_id) {
        this.court_id = court_id;
    }

    public String getCase_cnr_number() {
        return case_cnr_number;
    }

    public void setCase_cnr_number(String case_cnr_number) {
        this.case_cnr_number = case_cnr_number;
    }

    public String getCase_register_number() {
        return case_register_number;
    }

    public void setCase_register_number(String case_register_number) {
        this.case_register_number = case_register_number;
    }

    public String getCase_register_date() {
        return case_register_date;
    }

    public void setCase_register_date(String case_register_date) {
        this.case_register_date = case_register_date;
    }

    public String getCase_type() {
        return case_type;
    }

    public void setCase_type(String case_type) {
        this.case_type = case_type;
    }

    public String getCase_sub_type() {
        return case_sub_type;
    }

    public void setCase_sub_type(String case_sub_type) {
        this.case_sub_type = case_sub_type;
    }

    public String getCase_status() {
        return case_status;
    }

    public void setCase_status(String case_status) {
        this.case_status = case_status;
    }

    public CaseClient getClient() {
        return client;
    }

    public void setClient(CaseClient client) {
        this.client = client;
    }

    public List<CasePersons> getPersons() {
        return persons;
    }

    public void setPersons(List<CasePersons> persons) {
        this.persons = persons;
    }

    public CaseType getCasetype() {
        return casetype;
    }

    public void setCasetype(CaseType casetype) {
        this.casetype = casetype;
    }

    public SubCaseType getSubCasetype() {
        return subCasetype;
    }

    public void setSubCasetype(SubCaseType subCasetype) {
        this.subCasetype = subCasetype;
    }

    public CourtData getCourt() {
        return court;
    }

    public void setCourt(CourtData court) {
        this.court = court;
    }

    public CaseHearing getHearing() {
        return hearing;
    }

    public void setHearing(CaseHearing hearing) {
        this.hearing = hearing;
    }

    CaseHearing hearing;
}
