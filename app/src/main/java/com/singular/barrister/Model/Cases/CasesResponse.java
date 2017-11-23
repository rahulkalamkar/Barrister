package com.singular.barrister.Model.Cases;

import com.singular.barrister.Util.IModel;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CasesResponse implements IModel {
    public CasesData getData() {
        return data;
    }

    public void setData(CasesData data) {
        this.data = data;
    }

    CasesData data;
}
