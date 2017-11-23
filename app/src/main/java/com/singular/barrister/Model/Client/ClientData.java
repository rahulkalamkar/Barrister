package com.singular.barrister.Model.Client;

import com.singular.barrister.Util.IModel;

import java.util.List;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class ClientData implements IModel{
    List<Client> client;

    public List<Client> getClient() {
        return client;
    }

    public void setClient(List<Client> client) {
        this.client = client;
    }
}
