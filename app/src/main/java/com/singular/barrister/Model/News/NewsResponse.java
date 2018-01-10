package com.singular.barrister.Model.News;

import com.singular.barrister.Util.IModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahul.kalamkar on 1/9/2018.
 */

public class NewsResponse implements Serializable, IModel {
    String status;
    ArrayList<News> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<News> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<News> articles) {
        this.articles = articles;
    }
}
