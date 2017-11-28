package com.singular.barrister.Model.Today;

import com.google.gson.annotations.SerializedName;
import com.singular.barrister.Model.Cases.CasesData;
import com.singular.barrister.Model.Error;
import com.singular.barrister.Util.IModel;

/**
 * Created by rahul.kalamkar on 11/24/2017.
 */

public class TodayResponse implements IModel {
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
