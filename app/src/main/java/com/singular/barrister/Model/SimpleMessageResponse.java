package com.singular.barrister.Model;

import com.singular.barrister.Util.IModel;

/**
 * Created by rahulbabanaraokalamkar on 11/26/17.
 */

public class SimpleMessageResponse implements IModel {
    String message;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    int status_code;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
