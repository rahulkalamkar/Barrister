package com.singular.barrister.Model;

import com.singular.barrister.Util.IModel;

/**
 * Created by rahul.kalamkar on 11/23/2017.
 */

public class RegisterResponse implements IModel{
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;
}
