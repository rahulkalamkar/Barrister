package com.singular.barrister.Database.Tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by rahul.kalamkar on 12/1/2017.
 */

@DatabaseTable(tableName = "CourtTypeTable")
public class CourtTypeTable {

    @DatabaseField(generatedId = true, columnName = "id")
    int id;

    @DatabaseField(columnName = "court_type", unique = true)
    String courtType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourtType() {
        return courtType;
    }

    public void setCourtType(String courtType) {
        this.courtType = courtType;
    }

    public CourtTypeTable() {

    }


    public CourtTypeTable(String type) {
        courtType = type;
    }
}
