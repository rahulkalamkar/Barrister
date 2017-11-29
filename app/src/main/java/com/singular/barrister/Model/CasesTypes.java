package com.singular.barrister.Model;

import com.singular.barrister.Util.IModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahul.kalamkar on 11/29/2017.
 */

public class CasesTypes implements IModel, Serializable {
    ArrayList<CasesTypeData> casetype;
    ArrayList<String> courttype;

    public ArrayList<CasesTypeData> getCasetype() {
        return casetype;
    }

    public void setCasetype(ArrayList<CasesTypeData> casetype) {
        this.casetype = casetype;
    }

    public ArrayList<String> getCourttype() {
        return courttype;
    }

    public void setCourttype(ArrayList<String> courttype) {
        this.courttype = courttype;
    }
}
