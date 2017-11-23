package com.singular.barrister.Model.Client;

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
}
