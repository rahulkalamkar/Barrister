package com.singular.barrister.Model.States;

import com.singular.barrister.Util.IModel;

import java.io.Serializable;

/**
 * Created by rahul.kalamkar on 11/29/2017.
 */

public class StateResponse implements IModel, Serializable {
    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
