package com.singular.barrister.Model;

import com.singular.barrister.Util.IModel;

import java.io.Serializable;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class SubDistrict implements IModel, Serializable {
    String id;
    String parent_id;
    String external_id;
    String name;
    String location_type;
    String pin;

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

    public SubDistrict(String name, String id, String parent_id, String external_id, String location_type, String pin) {
        this.id = id;
        this.name = name;
        this.parent_id = parent_id;
        this.external_id = external_id;
        this.location_type = location_type;
        this.pin = pin;
    }
}
