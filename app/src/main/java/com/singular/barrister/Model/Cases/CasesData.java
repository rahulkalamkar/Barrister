package com.singular.barrister.Model.Cases;

import com.google.gson.annotations.SerializedName;
import com.singular.barrister.Util.IModel;

import java.util.List;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CasesData implements IModel{
    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }

    @SerializedName("case")
    List<Case> cases;
}
