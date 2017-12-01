package com.singular.barrister.Database.Tables;

import android.content.Context;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.singular.barrister.Database.DB.DatabaseHelper;

import java.io.Serializable;

/**
 * Created by rahulbabanaraokalamkar on 11/29/17.
 */
@DatabaseTable(tableName = "StateTable")
public class StateTable implements Serializable {
    private static final long serialVersionUID = -222864131214757024L;

    @DatabaseField(columnName = "id", unique = true)
    String id;

    @DatabaseField(columnName = "parent_id", canBeNull = true)
    String parent_id;

    @DatabaseField(columnName = "external_id", canBeNull = true)
    String external_id;

    @DatabaseField(columnName = "name")
    String name;

    @DatabaseField(columnName = "location_type", canBeNull = true)
    String location_type;

    @DatabaseField(columnName = "pin", canBeNull = true)
    String pin;

    public StateTable() {
    }

    public StateTable(String id, String parent_id, String external_id, String name, String location_type, String pin) {
        this.id = id;
        this.pin = pin;
        this.name = name;
        this.location_type = location_type;
        this.parent_id = parent_id;
        this.external_id = external_id;
    }

}
