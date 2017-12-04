package com.singular.barrister.Database.Tables.Case;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by rahul.kalamkar on 12/4/2017.
 */

@DatabaseTable(tableName = "CasesSubCaseType")
public class CasesSubCaseType {
    @DatabaseField(columnName = "id", generatedId = true)
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSub_case_type_id() {
        return sub_case_type_id;
    }

    public void setSub_case_type_id(String sub_case_type_id) {
        this.sub_case_type_id = sub_case_type_id;
    }

    public String getSub_case_type_name() {
        return sub_case_type_name;
    }

    public void setSub_case_type_name(String sub_case_type_name) {
        this.sub_case_type_name = sub_case_type_name;
    }
/*
    public String getCase_type_id() {
        return case_type_id;
    }

    public void setCase_type_id(String case_type_id) {
        this.case_type_id = case_type_id;
    }*/

    @DatabaseField(columnName = "sub_case_type_id", canBeNull = true)
    String sub_case_type_id;

    @DatabaseField(columnName = "sub_case_type_name", canBeNull = true)
    String sub_case_type_name;

   /* @DatabaseField(columnName = "case_type_id", canBeNull = true)
    String case_type_id;*/

    public CasesSubCaseType(String sub_case_type_id, String sub_case_type_name) {
        this.sub_case_type_id = sub_case_type_id;
        this.sub_case_type_name = sub_case_type_name;
    }

    public CasesSubCaseType() {
    }

}
