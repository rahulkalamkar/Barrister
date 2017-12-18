package com.singular.barrister.Model.Client;

import com.singular.barrister.Database.Tables.Client.ClientTable;
import com.singular.barrister.Util.IModel;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class Client implements IModel, Serializable {
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

    public Client(String id, String created_at, String client_id, ClientDetail clientTable) {
        this.id = id;
        this.created_at = created_at;
        this.client_id = client_id;
        this.client = clientTable;
    }

    public static Comparator<ClientDetail> ClientNameComparator = new Comparator<ClientDetail>() {

        public int compare(ClientDetail s1, ClientDetail s2) {
            String StudentName1 = s1.getFirst_name().toUpperCase();
            String StudentName2 = s2.getFirst_name().toUpperCase();

            return StudentName1.compareTo(StudentName2);

        }
    };

    public static Comparator<Client> ClientComparator = new Comparator<Client>() {

        public int compare(Client s1, Client s2) {
            String Name1 = s1.getClient().getFirst_name().toUpperCase();
            String Name2 = s2.getClient().getFirst_name().toUpperCase();

            return Name1.compareTo(Name2);

        }
    };
}
