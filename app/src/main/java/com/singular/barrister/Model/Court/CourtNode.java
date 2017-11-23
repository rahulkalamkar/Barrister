package com.singular.barrister.Model.Court;

import com.singular.barrister.Util.IModel;

import java.util.List;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CourtNode implements IModel{
    List<CourtData> court;

    public List<CourtData> getCourt() {
        return court;
    }

    public void setCourt(List<CourtData> court) {
        this.court = court;
    }
}
