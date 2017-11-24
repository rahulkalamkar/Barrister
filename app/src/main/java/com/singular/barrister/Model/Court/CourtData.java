package com.singular.barrister.Model.Court;

import com.google.gson.annotations.SerializedName;
import com.singular.barrister.Model.Cases.CaseDistrict;
import com.singular.barrister.Model.Cases.CaseState;
import com.singular.barrister.Model.Cases.CaseSubDistrict;
import com.singular.barrister.Model.District;
import com.singular.barrister.Model.State;
import com.singular.barrister.Model.SubDistrict;
import com.singular.barrister.Util.IModel;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CourtData implements IModel{
    @SerializedName("id")
    String id;
    @SerializedName("court_name")
    String court_name;
    @SerializedName("court_typw")
    String court_type;
    @SerializedName("court_number")
    String court_number;
    @SerializedName("state_id")
    String state_id;
    @SerializedName("district_id")
    String district_id;
    @SerializedName("sub_district_id")
    String sub_district_id;
    @SerializedName("state")
    CaseState state;
    @SerializedName("district")
    CaseDistrict district;
    @SerializedName("subdistrict")
    CaseSubDistrict subdistrict;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public CaseState getState() {
        return state;
    }

    public void setState(CaseState state) {
        this.state = state;
    }

    public CaseDistrict getDistrict() {
        return district;
    }

    public void setDistrict(CaseDistrict district) {
        this.district = district;
    }

    public CaseSubDistrict getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(CaseSubDistrict subdistrict) {
        this.subdistrict = subdistrict;
    }
}
