package com.singular.barrister.Model.Cases;

import com.google.gson.annotations.SerializedName;
import com.singular.barrister.Util.IModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rahulbabanaraokalamkar on 11/23/17.
 */

public class CasesData implements IModel,Serializable {
    public List<Case> getCaseList() {
        return caseList;
    }

    public void setCaseList(List<Case> caseList) {
        this.caseList = caseList;
    }

    @SerializedName("case")
    List<Case> caseList;
}
