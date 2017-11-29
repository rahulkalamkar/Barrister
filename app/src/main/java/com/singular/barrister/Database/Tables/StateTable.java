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
public class StateTable implements Serializable{
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public StateTable()
    {}

    @DatabaseField(columnName = "id")
    String id;

    @DatabaseField(columnName = "parent_id",canBeNull = true)
    String parent_id;

    @DatabaseField(columnName = "external_id",canBeNull = true)
    String external_id;

    @DatabaseField(columnName = "name")
    String name;

    @DatabaseField(columnName = "location_type",canBeNull = true)
    String location_type;

    @DatabaseField(columnName = "pin",canBeNull = true)
    String pin;
}
