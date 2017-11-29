package com.singular.barrister.Database.Query;

import android.content.Context;

import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.StateTable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by rahulbabanaraokalamkar on 11/29/17.
 */

public class StateHelper{
    public void addState(Context context,String id,String externalId,String pin,
                         String name,String locationType,String parentId)
    {
        DatabaseHelper databaseHelper=new DatabaseHelper(context);
        StateTable stateTable=new StateTable();
        stateTable.setId(id);
        stateTable.setExternal_id(externalId);
        stateTable.setLocation_type(locationType);
        stateTable.setName(name);
        stateTable.setParent_id(parentId);
        stateTable.setPin(pin);
    }

}
