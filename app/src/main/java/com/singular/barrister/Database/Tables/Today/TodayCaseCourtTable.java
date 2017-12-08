package com.singular.barrister.Database.Tables.Today;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by rahulbabanaraokalamkar on 12/2/17.
 */

@DatabaseTable(tableName = "TodayCaseCourtTable")
public class TodayCaseCourtTable {

    @DatabaseField(columnName = "id", generatedId = true)
    int id;


    @DatabaseField(columnName = "court_id", canBeNull = true)
    String court_id;

    @DatabaseField(columnName = "court_name", canBeNull = true)
    String court_name;

    @DatabaseField(columnName = "court_type", canBeNull = true)
    String court_type;

    @DatabaseField(columnName = "court_number", canBeNull = true)
    String court_number;

    @DatabaseField(columnName = "state_id", canBeNull = true)
    String state_id;


    @DatabaseField(columnName = "district_id", canBeNull = true)
    String district_id;


    @DatabaseField(columnName = "sub_district_id", canBeNull = true)
    String sub_district_id;


    @DatabaseField(foreign = true, columnName = "state", canBeNull = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    TodayCaseCourtState courtState;

    @DatabaseField(foreign = true, columnName = "district", canBeNull = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    TodayCaseCourtDistrict courtDistrict;


    @DatabaseField(foreign = true, columnName = "sub_district", canBeNull = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    TodayCaseCourtSubDistrict courtSubDistrict;

    public TodayCaseCourtTable() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourt_id() {
        return court_id;
    }

    public void setCourt_id(String court_id) {
        this.court_id = court_id;
    }

    public String getCourt_name() {
        return court_name;
    }

    public void setCourt_name(String court_name) {
        this.court_name = court_name;
    }

    public String getCourt_type() {
        return court_type;
    }

    public void setCourt_type(String court_type) {
        this.court_type = court_type;
    }

    public String getCourt_number() {
        return court_number;
    }

    public void setCourt_number(String court_number) {
        this.court_number = court_number;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getSub_district_id() {
        return sub_district_id;
    }

    public void setSub_district_id(String sub_district_id) {
        this.sub_district_id = sub_district_id;
    }

    public TodayCaseCourtState getCourtState() {
        return courtState;
    }

    public void setCourtState(TodayCaseCourtState courtState) {
        this.courtState = courtState;
    }

    public TodayCaseCourtDistrict getCourtDistrict() {
        return courtDistrict;
    }

    public void setCourtDistrict(TodayCaseCourtDistrict courtDistrict) {
        this.courtDistrict = courtDistrict;
    }

    public TodayCaseCourtSubDistrict getCourtSubDistrict() {
        return courtSubDistrict;
    }

    public void setCourtSubDistrict(TodayCaseCourtSubDistrict courtSubDistrict) {
        this.courtSubDistrict = courtSubDistrict;
    }

    public TodayCaseCourtTable(String id, String court_name, String court_number, String court_type, String state_id, String district_id,
                          String sub_district_id, TodayCaseCourtState courtState, TodayCaseCourtDistrict courtDistrict, TodayCaseCourtSubDistrict courtSubDistrict) {
        this.court_id = id;
        this.court_name = court_name;

        this.court_number = court_number;
        this.court_type = court_type;
        this.courtState = courtState;
        this.courtDistrict = courtDistrict;
        this.courtSubDistrict = courtSubDistrict;
        this.state_id = state_id;
        this.district_id = district_id;
        this.sub_district_id = sub_district_id;
    }

}
