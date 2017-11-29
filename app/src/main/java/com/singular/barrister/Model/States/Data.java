package com.singular.barrister.Model.States;

import com.singular.barrister.Model.District;
import com.singular.barrister.Model.State;
import com.singular.barrister.Model.SubDistrict;
import com.singular.barrister.Util.IModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahul.kalamkar on 11/29/2017.
 */

public class Data implements IModel, Serializable {
    ArrayList<State> state;

    public ArrayList<State> getState() {
        return state;
    }

    public void setState(ArrayList<State> state) {
        this.state = state;
    }

    ArrayList<District> district;

    public ArrayList<District> getDistrict() {
        return district;
    }

    public void setDistrict(ArrayList<District> district) {
        this.district = district;
    }

    ArrayList<SubDistrict> subdistrict;

    public ArrayList<SubDistrict> getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(ArrayList<SubDistrict> subdistrict) {
        this.subdistrict = subdistrict;
    }
}
