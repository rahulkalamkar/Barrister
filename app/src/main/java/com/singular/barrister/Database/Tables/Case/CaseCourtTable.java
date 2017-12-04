package com.singular.barrister.Database.Tables.Case;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.singular.barrister.Database.Tables.CourtDistrict;
import com.singular.barrister.Database.Tables.CourtState;
import com.singular.barrister.Database.Tables.CourtSubDistrict;

/**
 * Created by rahulbabanaraokalamkar on 12/2/17.
 */

@DatabaseTable(tableName = "CaseCourtTable")
public class CaseCourtTable {

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
    CaseCourtState courtState;

    @DatabaseField(foreign = true, columnName = "district", canBeNull = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    CaseCourtDistrict courtDistrict;


    @DatabaseField(foreign = true, columnName = "sub_district", canBeNull = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    CaseCourtSubDistrict courtSubDistrict;

    public CaseCourtTable() {

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

    public CaseCourtState getCourtState() {
        return courtState;
    }

    public void setCourtState(CaseCourtState courtState) {
        this.courtState = courtState;
    }

    public CaseCourtDistrict getCourtDistrict() {
        return courtDistrict;
    }

    public void setCourtDistrict(CaseCourtDistrict courtDistrict) {
        this.courtDistrict = courtDistrict;
    }

    public CaseCourtSubDistrict getCourtSubDistrict() {
        return courtSubDistrict;
    }

    public void setCourtSubDistrict(CaseCourtSubDistrict courtSubDistrict) {
        this.courtSubDistrict = courtSubDistrict;
    }

    public CaseCourtTable(String id, String court_name, String court_number, String court_type, String state_id, String district_id,
                          String sub_district_id, CaseCourtState courtState, CaseCourtDistrict courtDistrict, CaseCourtSubDistrict courtSubDistrict) {
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
