package com.singular.barrister.Database.Tables.Client;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by rahulbabanaraokalamkar on 12/2/17.
 */
@DatabaseTable(tableName = "BaseClientTable")
public class BaseClientTable {

    @DatabaseField(columnName = "id", generatedId = true,unique = true)
    int id;

    @DatabaseField(columnName = "base_id")
    String base_id;

    @DatabaseField(columnName = "created_at")
    String created_at;

    @DatabaseField(columnName = "client_id")
    String client_id;

    @DatabaseField(foreign = true, columnName = "client_details", canBeNull = true,
            foreignAutoCreate = true, foreignAutoRefresh = true)
    ClientTable clientTable;

    public String getBase_id() {
        return base_id;
    }

    public void setBase_id(String base_id) {
        this.base_id = base_id;
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

    public ClientTable getClientTable() {
        return clientTable;
    }

    public void setClientTable(ClientTable clientTable) {
        this.clientTable = clientTable;
    }

    public BaseClientTable(String id, String created_at, String client_id, ClientTable clientTable) {
        this.base_id = id;
        this.created_at = created_at;
        this.client_id = client_id;
        this.clientTable = clientTable;
    }

    public BaseClientTable() {
    }

}
