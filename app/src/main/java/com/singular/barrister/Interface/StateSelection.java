package com.singular.barrister.Interface;

import com.singular.barrister.Model.District;
import com.singular.barrister.Model.State;
import com.singular.barrister.Model.SubDistrict;

/**
 * Created by rahul.kalamkar on 11/29/2017.
 */

public interface StateSelection {
    void getSelectedState(State state);

    void getSelectedDisstrict(District district);

    void getSelectedSubDistrict(SubDistrict subDistrict);
}
