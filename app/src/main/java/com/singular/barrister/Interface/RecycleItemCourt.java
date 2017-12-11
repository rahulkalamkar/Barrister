package com.singular.barrister.Interface;

import com.singular.barrister.Model.Client.Client;
import com.singular.barrister.Model.Court.CourtData;

/**
 * Created by rahul.kalamkar on 12/11/2017.
 */

public interface RecycleItemCourt {

    void onItemClick(CourtData court);

    void onItemLongClick(CourtData court);
}
