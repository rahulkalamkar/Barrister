package com.singular.barrister.Model.Cases;

import com.google.gson.annotations.SerializedName;
import com.singular.barrister.Model.Error;
import com.singular.barrister.Util.IModel;

import java.io.Serializable;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CasesResponse implements IModel,Serializable {
    public CasesData getData() {
        return data;
    }

    public void setData(CasesData data) {
        this.data = data;
    }

    @SerializedName("data")
    CasesData data;


    @SerializedName("error")
    Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
