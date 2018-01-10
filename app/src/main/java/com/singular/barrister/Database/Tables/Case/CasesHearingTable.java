package com.singular.barrister.Database.Tables.Case;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by rahulbabanaraokalamkar on 12/4/17.
 */

@DatabaseTable(tableName = "CasesHearingTable")
public class CasesHearingTable {

    @DatabaseField(columnName = "id", generatedId = true)
    int id;
    @DatabaseField(columnName = "hearing_id", canBeNull = true)
    String hearing_id;

    public String getHearing_id() {
        return hearing_id;
    }

    public void setHearing_id(String hearing_id) {
        this.hearing_id = hearing_id;
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

    @DatabaseField(columnName = "case_id", canBeNull = true)
    String case_id;
    @DatabaseField(columnName = "case_hearing_date", canBeNull = true)
    String case_hearing_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DatabaseField(columnName = "case_decision", canBeNull = true)

    String case_decision;

    public String getCase_notes() {
        return case_notes;
    }

    public void setCase_notes(String case_notes) {
        this.case_notes = case_notes;
    }

    @DatabaseField(columnName = "case_notes", canBeNull = true)

    String case_notes;

    @DatabaseField(columnName = "case_disposed", canBeNull = true)
    String case_disposed;

    public CasesHearingTable() {
    }

    public String getCase_disposed() {
        return case_disposed;
    }

    public void setCase_disposed(String case_disposed) {
        this.case_disposed = case_disposed;
    }

    public CasesHearingTable(String id, String case_id, String case_hearing_date, String case_decision, String case_disposed, String case_notes) {
        this.hearing_id = id;
        this.case_id = case_id;
        this.case_hearing_date = case_hearing_date;
        this.case_notes = case_notes;
        this.case_decision = case_decision;
        this.case_disposed = case_disposed;
    }
}
