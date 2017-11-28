package com.singular.barrister.Model;

import com.singular.barrister.Util.IModel;

import java.io.Serializable;

/**
 * Created by rahulbabanaraokalamkar on 11/28/17.
 */

public class Error implements IModel,Serializable{
String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    int status_code;
}
