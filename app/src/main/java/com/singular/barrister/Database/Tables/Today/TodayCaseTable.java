package com.singular.barrister.Database.Tables.Today;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by rahul.kalamkar on 12/4/2017.
 */

@DatabaseTable(tableName = "TodayCaseTable")
public class TodayCaseTable {
    @DatabaseField(columnName = "id", generatedId = true)
    int id;

    @DatabaseField(columnName = "case_id", unique = true)
    String case_id;
    @DatabaseField(columnName = "client_id", canBeNull = true)
    String client_id;
    @DatabaseField(columnName = "client_type", canBeNull = true)
    String client_type;
    @DatabaseField(columnName = "court_id", canBeNull = true)
    String court_id;
    @DatabaseField(columnName = "case_cnr_number", canBeNull = true)
    String case_cnr_number;
    @DatabaseField(columnName = "case_register_number", canBeNull = true)
    String case_register_number;
    @DatabaseField(columnName = "case_register_date", canBeNull = true)
    String case_register_date;
    @DatabaseField(columnName = "case_type", canBeNull = true)
    String case_type;
    @DatabaseField(columnName = "case_sub_type", canBeNull = true)
    String case_sub_type;
    @DatabaseField(columnName = "case_status", canBeNull = true)
    String case_status;

    public TodayCasesHearingTable getHearingTable() {
        return hearingTable;
    }

    public void setHearingTable(TodayCasesHearingTable hearingTable) {
        this.hearingTable = hearingTable;
    }

    @DatabaseField(columnName = "case_hearing", canBeNull = true, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    TodayCasesHearingTable hearingTable;

    public TodayCaseClientTable getClient() {
        return client;
    }

    @DatabaseField(columnName = "client_details", canBeNull = true, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    TodayCaseClientTable client;

    @DatabaseField(columnName = "Case_case_type", canBeNull = true, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    TodayCasesCaseType casesCaseType;

    @DatabaseField(columnName = "Case_case_sub_type", canBeNull = true, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    TodayCasesSubCaseType casesSubCaseType;

    @DatabaseField(columnName = "case_court", canBeNull = true, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    TodayCaseCourtTable court;

    @DatabaseField(columnName = "diff", canBeNull = true)
    String diff;

    public TodayCaseTable() {

    }

    public void setClient(TodayCaseClientTable client) {
        this.client = client;
    }

    public TodayCaseTable(String case_id, String client_id, String case_cnr_number, String case_register_number, String case_register_date, String case_status, String case_type, String case_sub_type,
                     String client_type, String court_id, TodayCaseClientTable client, TodayCasesCaseType casesCaseType, TodayCasesSubCaseType casesSubCaseType,
                          TodayCaseCourtTable caseCourtTable, String diff, TodayCasesHearingTable hearingTable) {
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
        this.hearingTable = hearingTable;

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

    public TodayCasesCaseType getCasesCaseType() {
        return casesCaseType;
    }

    public void setCasesCaseType(TodayCasesCaseType casesCaseType) {
        this.casesCaseType = casesCaseType;
    }

    public TodayCasesSubCaseType getCasesSubCaseType() {
        return casesSubCaseType;
    }

    public void setCasesSubCaseType(TodayCasesSubCaseType casesSubCaseType) {
        this.casesSubCaseType = casesSubCaseType;
    }

    public TodayCaseCourtTable getCourt() {
        return court;
    }

    public void setCourt(TodayCaseCourtTable court) {
        this.court = court;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }
}
