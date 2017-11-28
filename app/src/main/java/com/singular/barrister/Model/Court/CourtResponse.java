package com.singular.barrister.Model.Court;

import com.google.gson.annotations.SerializedName;
import com.singular.barrister.Model.District;
import com.singular.barrister.Model.Error;
import com.singular.barrister.Model.State;
import com.singular.barrister.Model.SubDistrict;
import com.singular.barrister.Util.IModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CourtResponse implements IModel{
   CourtNode data;

    public CourtNode getData() {
        return data;
    }

    public void setData(CourtNode data) {
        this.data = data;
    }


    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @SerializedName("error")
    Error error;
}
