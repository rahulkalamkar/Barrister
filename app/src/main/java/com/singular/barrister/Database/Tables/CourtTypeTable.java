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

    public CourtTypeTable() {

    }


    public CourtTypeTable(String type) {
        courtType = type;
    }
}
