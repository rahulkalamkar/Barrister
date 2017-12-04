package com.singular.barrister.Database.DB;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.singular.barrister.Database.Tables.Case.CaseClientTable;
import com.singular.barrister.Database.Tables.Case.CaseCourtDistrict;
import com.singular.barrister.Database.Tables.Case.CaseCourtState;
import com.singular.barrister.Database.Tables.Case.CaseCourtSubDistrict;
import com.singular.barrister.Database.Tables.Case.CaseCourtTable;
import com.singular.barrister.Database.Tables.Case.CasePerson;
import com.singular.barrister.Database.Tables.Case.CaseTable;
import com.singular.barrister.Database.Tables.Case.CasesCaseType;
import com.singular.barrister.Database.Tables.Case.CasesHearingTable;
import com.singular.barrister.Database.Tables.Case.CasesSubCaseType;
import com.singular.barrister.Database.Tables.CaseTypeTable;
import com.singular.barrister.Database.Tables.Client.BaseClientTable;
import com.singular.barrister.Database.Tables.Client.ClientTable;
import com.singular.barrister.Database.Tables.CourtDistrict;
import com.singular.barrister.Database.Tables.CourtState;
import com.singular.barrister.Database.Tables.CourtSubDistrict;
import com.singular.barrister.Database.Tables.CourtTable;
import com.singular.barrister.Database.Tables.CourtTypeTable;
import com.singular.barrister.Database.Tables.StateTable;
import com.singular.barrister.Database.Tables.SubCaseTypeTable;
import com.singular.barrister.Model.Cases.SubCaseType;

import android.util.Log;

import java.sql.SQLException;

/**
 * Created by rahulbabanaraokalamkar on 11/29/17.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "barrister.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<StateTable, Integer> stateTableDao;
    private Dao<CourtTypeTable, Integer> courtTypeTableDao;
    private Dao<CourtTable, Integer> courtTableDao;
    private Dao<CourtState, Integer> courtStateDao;
    private Dao<CourtDistrict, Integer> courtDistrictDao;
    private Dao<CourtSubDistrict, Integer> courtSubDistrictDao;
    private Dao<ClientTable, Integer> clientTableDao;
    private Dao<BaseClientTable, Integer> baseClientTableDao;
    private Dao<CaseTypeTable, Integer> caseTypeTableDao;
    private Dao<SubCaseTypeTable, Integer> subCaseTypeTableDao;

    private Dao<CaseClientTable, Integer> aCaseClientTableDao;
    private Dao<CaseCourtDistrict, Integer> aCaseCourtDistrictDao;
    private Dao<CaseCourtState, Integer> aCaseCourtStateDao;
    private Dao<CaseCourtSubDistrict, Integer> aCaseCourtSubDistrictDao;
    private Dao<CaseCourtTable, Integer> aCaseCourtTableDao;
    private Dao<CasePerson, Integer> aCasePersonDao;
    private Dao<CasesCaseType, Integer> aCasesCaseTypeDao;
    private Dao<CasesSubCaseType, Integer> aCasesSubCaseTypeDao;
    private Dao<CaseTable, Integer> aCaseTableDao;
    private Dao<CasesHearingTable, Integer> aCaseHearingTableDao;

    public SQLiteDatabase sqLiteDatabase;
    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        try {
            sqLiteDatabase = getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource, StateTable.class);
            TableUtils.createTable(connectionSource, CourtTypeTable.class);
            TableUtils.createTable(connectionSource, CourtTable.class);
            TableUtils.createTable(connectionSource, CourtState.class);
            TableUtils.createTable(connectionSource, CourtDistrict.class);
            TableUtils.createTable(connectionSource, CourtSubDistrict.class);
            TableUtils.createTable(connectionSource, BaseClientTable.class);
            TableUtils.createTable(connectionSource, ClientTable.class);
            TableUtils.createTable(connectionSource, CaseTypeTable.class);
            TableUtils.createTable(connectionSource, SubCaseTypeTable.class);

            // Cases related table
            TableUtils.createTable(connectionSource, CaseClientTable.class);
            TableUtils.createTable(connectionSource, CaseCourtDistrict.class);
            TableUtils.createTable(connectionSource, CaseCourtSubDistrict.class);
            TableUtils.createTable(connectionSource, CaseCourtState.class);
            TableUtils.createTable(connectionSource, CaseCourtTable.class);
            TableUtils.createTable(connectionSource, CasePerson.class);
            TableUtils.createTable(connectionSource, CasesCaseType.class);
            TableUtils.createTable(connectionSource, CasesSubCaseType.class);
            TableUtils.createTable(connectionSource, CaseTable.class);
            TableUtils.createTable(connectionSource, CasesHearingTable.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, StateTable.class, true);
            TableUtils.dropTable(connectionSource, CourtTypeTable.class, true);
            TableUtils.createTable(connectionSource, CourtTable.class);
            TableUtils.createTable(connectionSource, CourtState.class);
            TableUtils.createTable(connectionSource, CourtDistrict.class);
            TableUtils.createTable(connectionSource, CourtSubDistrict.class);
            TableUtils.createTable(connectionSource, BaseClientTable.class);
            TableUtils.createTable(connectionSource, ClientTable.class);
            TableUtils.createTable(connectionSource, CaseTypeTable.class);
            TableUtils.createTable(connectionSource, SubCaseTypeTable.class);

            TableUtils.createTable(connectionSource, CaseClientTable.class);
            TableUtils.createTable(connectionSource, CaseCourtDistrict.class);
            TableUtils.createTable(connectionSource, CaseCourtSubDistrict.class);
            TableUtils.createTable(connectionSource, CaseCourtState.class);
            TableUtils.createTable(connectionSource, CaseCourtTable.class);
            TableUtils.createTable(connectionSource, CasePerson.class);
            TableUtils.createTable(connectionSource, CasesCaseType.class);
            TableUtils.createTable(connectionSource, CasesSubCaseType.class);
            TableUtils.createTable(connectionSource, CaseTable.class);
            TableUtils.createTable(connectionSource, CasesHearingTable.class);

            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    public Dao<StateTable, Integer> getStateTableDao() throws SQLException {
        if (stateTableDao == null) {
            stateTableDao = getDao(StateTable.class);
        }
        return stateTableDao;
    }

    public Dao<CourtTypeTable, Integer> getCourtTypeTableDao() throws SQLException {
        if (courtTypeTableDao == null) {
            courtTypeTableDao = getDao(CourtTypeTable.class);
        }
        return courtTypeTableDao;
    }

    public Dao<CourtTable, Integer> getCourtTableDao() throws SQLException {
        if (courtTableDao == null) {
            courtTableDao = getDao(CourtTable.class);
        }
        return courtTableDao;
    }

    public Dao<CourtState, Integer> getCourtStateDao() throws SQLException {
        if (courtStateDao == null) {
            courtStateDao = getDao(CourtState.class);
        }
        return courtStateDao;
    }

    public Dao<CourtDistrict, Integer> getCourtDistrictDao() throws SQLException {
        if (courtDistrictDao == null) {
            courtDistrictDao = getDao(CourtDistrict.class);
        }
        return courtDistrictDao;
    }

    public Dao<CourtSubDistrict, Integer> getCourtSubDistrictDao() throws SQLException {
        if (courtSubDistrictDao == null) {
            courtSubDistrictDao = getDao(CourtSubDistrict.class);
        }
        return courtSubDistrictDao;
    }

    public Dao<BaseClientTable, Integer> getBaseClientTableDao() throws SQLException {
        if (baseClientTableDao == null) {
            baseClientTableDao = getDao(BaseClientTable.class);
        }
        return baseClientTableDao;
    }

    public Dao<ClientTable, Integer> getClientTableDao() throws SQLException {
        if (clientTableDao == null) {
            clientTableDao = getDao(ClientTable.class);
        }
        return clientTableDao;
    }

    public Dao<CaseTypeTable, Integer> getCaseTypeTableDao() throws SQLException {
        if (caseTypeTableDao == null) {
            caseTypeTableDao = getDao(CaseTypeTable.class);
        }
        return caseTypeTableDao;
    }

    public Dao<SubCaseTypeTable, Integer> getSubCaseTypeTableDao() throws SQLException {
        if (subCaseTypeTableDao == null) {
            subCaseTypeTableDao = getDao(SubCaseTypeTable.class);
        }
        return subCaseTypeTableDao;
    }

    public Dao<CaseClientTable, Integer> getACaseClientTableDao() throws SQLException {
        if (aCaseClientTableDao == null) {
            aCaseClientTableDao = getDao(CaseClientTable.class);
        }
        return aCaseClientTableDao;
    }


    public Dao<CaseCourtDistrict, Integer> getACaseCourtDistrictDao() throws SQLException {
        if (aCaseCourtDistrictDao == null) {
            aCaseCourtDistrictDao = getDao(CaseCourtDistrict.class);
        }
        return aCaseCourtDistrictDao;
    }


    public Dao<CaseCourtState, Integer> getACaseCourtStateDao() throws SQLException {
        if (aCaseCourtStateDao == null) {
            aCaseCourtStateDao = getDao(CaseCourtState.class);
        }
        return aCaseCourtStateDao;
    }


    public Dao<CaseCourtSubDistrict, Integer> getACaseCourtSubDistrictDao() throws SQLException {
        if (aCaseCourtSubDistrictDao == null) {
            aCaseCourtSubDistrictDao = getDao(CaseCourtSubDistrict.class);
        }
        return aCaseCourtSubDistrictDao;
    }


    public Dao<CaseCourtTable, Integer> getACaseCourtTableDao() throws SQLException {
        if (aCaseCourtTableDao == null) {
            aCaseCourtTableDao = getDao(CaseCourtTable.class);
        }
        return aCaseCourtTableDao;
    }


    public Dao<CasePerson, Integer> getACasePersonDao() throws SQLException {
        if (aCasePersonDao == null) {
            aCasePersonDao = getDao(CasePerson.class);
        }
        return aCasePersonDao;
    }


    public Dao<CasesCaseType, Integer> getACasesCaseTypeDao() throws SQLException {
        if (aCasesCaseTypeDao == null) {
            aCasesCaseTypeDao = getDao(CasesCaseType.class);
        }
        return aCasesCaseTypeDao;
    }


    public Dao<CasesSubCaseType, Integer> getACasesSubCaseTypeDao() throws SQLException {
        if (aCasesSubCaseTypeDao == null) {
            aCasesSubCaseTypeDao = getDao(CasesSubCaseType.class);
        }
        return aCasesSubCaseTypeDao;
    }


    public Dao<CaseTable, Integer> getACaseTableDao() throws SQLException {
        if (aCaseTableDao == null) {
            aCaseTableDao = getDao(CaseTable.class);
        }
        return aCaseTableDao;
    }

    public Dao<CasesHearingTable, Integer> getACaseHearingTableDao() throws SQLException {
        if (aCaseHearingTableDao == null) {
            aCaseHearingTableDao = getDao(CasesHearingTable.class);
        }
        return aCaseHearingTableDao;
    }
}
