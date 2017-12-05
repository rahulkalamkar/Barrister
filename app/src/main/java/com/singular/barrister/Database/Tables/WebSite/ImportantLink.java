package com.singular.barrister.Database.Tables.WebSite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by rahul.kalamkar on 12/5/2017.
 */
@DatabaseTable(tableName = "ImportantLink")
public class ImportantLink {
    @DatabaseField(columnName = "id", generatedId = true)
    int id;

    @DatabaseField(columnName = "web_name")
    String web_name;

    @DatabaseField(columnName = "web_site")
    String web_site;

    public String getWeb_name() {
        return web_name;
    }

    public void setWeb_name(String web_name) {
        this.web_name = web_name;
    }

    public String getWeb_site() {
        return web_site;
    }

    public void setWeb_site(String web_site) {
        this.web_site = web_site;
    }

    public ImportantLink() {
    }

    public ImportantLink(String web_name, String web_site) {
        this.web_name = web_name;
        this.web_site = web_site;
    }
}
