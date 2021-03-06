package com.singular.barrister.Database.Tables;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.singular.barrister.Model.Cases.SubCaseType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by rahul.kalamkar on 12/1/2017.
 */

@DatabaseTable(tableName = "CaseTypeTable")
public class CaseTypeTable implements Serializable {
    @DatabaseField(columnName = "id", generatedId = true)
    int id;

    @DatabaseField(columnName = "case_type_id", canBeNull = true, unique = true)
    String case_type_id;

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

    @DatabaseField(columnName = "case_type_name", canBeNull = true)
    String case_type_name;

    public CaseTypeTable() {

    }

    public CaseTypeTable(String case_type_id, String case_type_name) {
        this.case_type_id = case_type_id;
        this.case_type_name = case_type_name;
    }
}
