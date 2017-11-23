package com.singular.barrister.Model.Court;

import com.singular.barrister.Model.District;
import com.singular.barrister.Model.State;
import com.singular.barrister.Model.SubDistrict;
import com.singular.barrister.Util.IModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CourtResponse implements IModel{
   CourtNode data;

    public CourtNode getData() {
        return data;
    }

    public void setData(CourtNode data) {
        this.data = data;
    }
}
