package com.singular.barrister.Database.Tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by rahulbabanaraokalamkar on 11/29/17.
 */

@DatabaseTable(tableName = "ClientTable")
public class ClientTable implements Serializable {

    public ClientTable() {

    }

    public ClientTable(String id, String first_name, String last_name, String country_code, String mobile, String email, String address, String user_type, String referral_code,
                       String parent_user_id, String used_referral_code, String device_type, String device_token, String subscription, String created_at, String updated_at) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.country_code = country_code;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.user_type = user_type;
        this.referral_code = referral_code;
        this.parent_user_id = parent_user_id;
        this.used_referral_code = used_referral_code;
        this.device_type = device_type;
        this.device_token = device_token;
        this.subscription = subscription;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @DatabaseField(columnName = "id", canBeNull = true)
    String id;

    @DatabaseField(columnName = "first_name", canBeNull = true)
    String first_name;

    @DatabaseField(columnName = "last_name", canBeNull = true)
    String last_name;

    @DatabaseField(columnName = "country_code", canBeNull = true)
    String country_code;

    @DatabaseField(columnName = "mobile", canBeNull = true)
    String mobile;

    @DatabaseField(columnName = "email", canBeNull = true)
    String email;

    @DatabaseField(columnName = "address", canBeNull = true)
    String address;

    @DatabaseField(columnName = "user_type", canBeNull = true)
    String user_type;

    @DatabaseField(columnName = "referral_code", canBeNull = true)
    String referral_code;

    @DatabaseField(columnName = "parent_user_id", canBeNull = true)
    String parent_user_id;


    @DatabaseField(columnName = "user_referral_code", canBeNull = true)
    String used_referral_code;

    @DatabaseField(columnName = "device_type", canBeNull = true)
    String device_type;

    @DatabaseField(columnName = "device_token", canBeNull = true)
    String device_token;

    @DatabaseField(columnName = "subscription", canBeNull = true)
    String subscription;


    @DatabaseField(columnName = "created_at", canBeNull = true)
    String created_at;


    @DatabaseField(columnName = "updated_at", canBeNull = true)
    String updated_at;

}
