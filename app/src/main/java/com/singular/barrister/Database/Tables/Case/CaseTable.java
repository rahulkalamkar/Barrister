package com.singular.barrister.Database.Tables.Case;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.singular.barrister.Model.Cases.CaseClient;
import com.singular.barrister.Model.Cases.CasePersons;
import com.singular.barrister.Model.Cases.CaseType;
import com.singular.barrister.Model.Cases.SubCaseType;
import com.singular.barrister.Model.Court.CourtData;

import java.util.List;

/**
 * Created by rahul.kalamkar on 12/4/2017.
 */

@DatabaseTable(tableName = "CaseTable")
public class CaseTable {

    @DatabaseField(columnName = "case_id", unique = true)
    String case_id;
    @DatabaseField(columnName = "client_id")
    String client_id;
    @DatabaseField(columnName = "client_type")
    String client_type;
    @DatabaseField(columnName = "court_id")
    String court_id;
    @DatabaseField(columnName = "case_cnr_number")
    String case_cnr_number;
    @DatabaseField(columnName = "case_register_number")
    String case_register_number;
    @DatabaseField(columnName = "case_register_date")
    String case_register_date;
    @DatabaseField(columnName = "case_type")
    String case_type;
    @DatabaseField(columnName = "case_sub_type")
    String case_sub_type;
    @DatabaseField(columnName = "case_status")
    String case_status;

    @DatabaseField(columnName = "client_details", canBeNull = true, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    CaseClientTable client;

    @DatabaseField(columnName = "Case_case_type", canBeNull = true, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    CasesCaseType casesCaseType;

    @DatabaseField(columnName = "Case_case_sub_type", canBeNull = true, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    CasesSubCaseType casesSubCaseType;

    @DatabaseField(columnName = "case_court", canBeNull = true, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    CaseCourtTable court;

    @DatabaseField(columnName = "diff", canBeNull = true)
    String diff;

    public CaseTable() {

    }

    public void setClient(CaseClientTable client) {
        this.client = client;
    }

    public CaseTable(String case_id, String client_id, String case_cnr_number, String case_register_number, String case_register_date, String case_status, String case_type, String case_sub_type,
                     String client_type, String court_id, CaseClientTable client, CasesCaseType casesCaseType, CasesSubCaseType casesSubCaseType, CaseCourtTable caseCourtTable, String diff) {
        this.case_id = case_id;
        this.client_id = client_id;
        this.case_cnr_number = case_cnr_number;
        this.case_register_number = case_register_number;
        this.case_register_date = case_register_date;
        this.case_status = case_status;
        this.case_type = case_type;
        this.case_sub_type = case_sub_type;
        this.client_type = client_type;
        this.court_id = court_id;
        this.client = client;
        this.casesCaseType = casesCaseType;
        this.casesSubCaseType = casesSubCaseType;
        this.court = caseCourtTable;
        this.diff = diff;

    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
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

    public CasesCaseType getCasesCaseType() {
        return casesCaseType;
    }

    public void setCasesCaseType(CasesCaseType casesCaseType) {
        this.casesCaseType = casesCaseType;
    }

    public CasesSubCaseType getCasesSubCaseType() {
        return casesSubCaseType;
    }

    public void setCasesSubCaseType(CasesSubCaseType casesSubCaseType) {
        this.casesSubCaseType = casesSubCaseType;
    }

    public CaseCourtTable getCourt() {
        return court;
    }

    public void setCourt(CaseCourtTable court) {
        this.court = court;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }
}
