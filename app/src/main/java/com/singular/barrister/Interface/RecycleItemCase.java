package com.singular.barrister.Interface;

import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Client.Client;

/**
 * Created by rahul.kalamkar on 12/8/2017.
 */

public interface RecycleItemCase {
    void onItemClick(Case aCase);

    void onItemLongClick(Case aCase);
}
