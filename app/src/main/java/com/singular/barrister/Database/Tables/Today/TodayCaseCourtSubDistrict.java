package com.singular.barrister.Database.Tables.Today;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by rahulbabanaraokalamkar on 12/2/17.
 */

@DatabaseTable(tableName = "TodayCaseCourtSubDistrict")
public class TodayCaseCourtSubDistrict {
    private static final long serialVersionUID = -222864131214757024L;

    @DatabaseField(columnName = "id",generatedId = true)
    int id;


    @DatabaseField(columnName = "subDistrictId")
    String subDistrictId;

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

    public String getId() {
        return subDistrictId;
    }

    public void setId(String id) {
        this.subDistrictId = id;
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

    public TodayCaseCourtSubDistrict() {
    }

    public TodayCaseCourtSubDistrict(String id, String parent_id, String external_id, String name, String location_type, String pin) {
        this.subDistrictId = id;
        this.pin = pin;
        this.name = name;
        this.location_type = location_type;
        this.parent_id = parent_id;
        this.external_id = external_id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
