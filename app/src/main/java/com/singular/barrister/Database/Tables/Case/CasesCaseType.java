package com.singular.barrister.Database.Tables.Case;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by rahul.kalamkar on 12/4/2017.
 */

@DatabaseTable(tableName = "Cases_CaseType")
public class CasesCaseType {
    @DatabaseField(columnName = "id", generatedId = true)
    int id;

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

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DatabaseField(columnName = "case_type_id", canBeNull = true)
    String case_type_id;

    @DatabaseField(columnName = "case_type_name", canBeNull = true)
    String case_type_name;

    public CasesCaseType() {

    }

    public CasesCaseType(String case_type_id, String case_type_name) {
        this.case_type_id = case_type_id;
        this.case_type_name = case_type_name;
    }

}
