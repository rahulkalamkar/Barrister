package com.singular.barrister.Model.News;

import com.singular.barrister.Util.IModel;

import java.io.Serializable;

/**
 * Created by rahul.kalamkar on 1/9/2018.
 */

public class source implements Serializable,IModel {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
}
