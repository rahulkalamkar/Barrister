package com.singular.barrister.Model.Client;

import com.google.gson.annotations.SerializedName;
import com.singular.barrister.Model.Error;
import com.singular.barrister.Util.IModel;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class ClientResponse implements IModel{
    ClientData data;

    public ClientData getData() {
        return data;
    }

    public void setData(ClientData data) {
        this.data = data;
    }


    @SerializedName("error")
    Error error;


    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
