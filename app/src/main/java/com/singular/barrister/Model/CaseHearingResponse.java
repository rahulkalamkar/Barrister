package com.singular.barrister.Model;

import com.singular.barrister.Util.IModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahul.kalamkar on 11/28/2017.
 */

public class CaseHearingResponse implements IModel, Serializable {

    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
