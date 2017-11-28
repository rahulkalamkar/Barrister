package com.singular.barrister.Model;

import com.singular.barrister.Model.Cases.CaseHearing;
import com.singular.barrister.Util.IModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahul.kalamkar on 11/28/2017.
 */

public class Data implements IModel, Serializable {
    ArrayList<CaseHearing> hearing;

    public ArrayList<CaseHearing> getHearing() {
        return hearing;
    }

    public void setHearing(ArrayList<CaseHearing> hearing) {
        this.hearing = hearing;
    }
}
