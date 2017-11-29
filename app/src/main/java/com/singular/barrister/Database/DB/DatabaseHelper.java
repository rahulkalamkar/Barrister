package com.singular.barrister.Database.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.singular.barrister.Database.Tables.StateTable;

import java.sql.SQLException;

/**
 * Created by rahulbabanaraokalamkar on 11/29/17.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{
    public static final String DB_NAME = "student_manager.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            // Create Table with given table name with columnName
            TableUtils.createTable(connectionSource, StateTable.class);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, StateTable.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
