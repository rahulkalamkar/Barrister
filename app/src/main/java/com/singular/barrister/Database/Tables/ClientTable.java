package com.singular.barrister.Database.Tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by rahulbabanaraokalamkar on 11/29/17.
 */

@DatabaseTable(tableName = "ClientTable")
public class ClientTable implements Serializable{
    @DatabaseField(columnName = "id",canBeNull = true)
    String id;

    @DatabaseField(columnName = "first_name",canBeNull = true)
    String first_name;

    @DatabaseField(columnName = "last_name",canBeNull = true)
    String last_name;

    @DatabaseField(columnName = "country_code",canBeNull = true)
    String country_code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }

    public String getParent_user_id() {
        return parent_user_id;
    }

    public void setParent_user_id(String parent_user_id) {
        this.parent_user_id = parent_user_id;
    }

    public String getUsed_referral_code() {
        return used_referral_code;
    }

    public void setUsed_referral_code(String used_referral_code) {
        this.used_referral_code = used_referral_code;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @DatabaseField(columnName = "mobile",canBeNull = true)
    String mobile;

    @DatabaseField(columnName = "email",canBeNull = true)
    String email;

    @DatabaseField(columnName = "address",canBeNull = true)
    String address;

    @DatabaseField(columnName = "user_type",canBeNull = true)
    String user_type;

    @DatabaseField(columnName = "referral_code",canBeNull = true)
    String referral_code;

    @DatabaseField(columnName = "parent_user_id",canBeNull = true)
    String parent_user_id;


    @DatabaseField(columnName = "user_referral_code",canBeNull = true)
    String used_referral_code;

    @DatabaseField(columnName = "device_type",canBeNull = true)
    String device_type;

    @DatabaseField(columnName = "device_token",canBeNull = true)
    String device_token;

    @DatabaseField(columnName = "subscription",canBeNull = true)
    String subscription;


    @DatabaseField(columnName = "created_at",canBeNull = true)
    String created_at;


    @DatabaseField(columnName = "updated_at",canBeNull = true)
    String updated_at;

}
