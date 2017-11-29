package com.singular.barrister.Model;

import com.singular.barrister.Util.IModel;

import java.io.Serializable;

/**
 * Created by rahul.kalamkar on 11/29/2017.
 */

public class CasesTypeResponse implements IModel, Serializable {
    CasesTypes data;

    public CasesTypes getData() {
        return data;
    }

    public void setData(CasesTypes data) {
        this.data = data;
    }
}
