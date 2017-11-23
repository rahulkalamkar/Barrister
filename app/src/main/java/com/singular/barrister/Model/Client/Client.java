package com.singular.barrister.Model.Client;

import com.singular.barrister.Util.IModel;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class Client implements IModel{
    String id;
    String created_at;
    String client_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public ClientDetail getClient() {
        return client;
    }

    public void setClient(ClientDetail client) {
        this.client = client;
    }

    ClientDetail client;
}
