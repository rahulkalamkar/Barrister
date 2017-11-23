package com.singular.barrister.Model;

import com.singular.barrister.Util.IModel;

/**
 * Created by rahul.kalamkar on 11/23/2017.
 */

public class RegisterResponse implements IModel {

    String token;
    RegisterData data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RegisterData getData() {
        return data;
    }

    public void setData(RegisterData data) {
        this.data = data;
    }
}
