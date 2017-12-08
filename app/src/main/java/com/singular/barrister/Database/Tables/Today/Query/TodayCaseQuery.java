package com.singular.barrister.Database.Tables.Today.Query;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.singular.barrister.Database.DB.DatabaseHelper;
import com.singular.barrister.Database.Tables.Today.TodayCaseClientTable;
import com.singular.barrister.Database.Tables.Today.TodayCaseCourtDistrict;
import com.singular.barrister.Database.Tables.Today.TodayCaseCourtState;
import com.singular.barrister.Database.Tables.Today.TodayCaseCourtSubDistrict;
import com.singular.barrister.Database.Tables.Today.TodayCaseCourtTable;
import com.singular.barrister.Database.Tables.Today.TodayCasePerson;
import com.singular.barrister.Database.Tables.Today.TodayCaseTable;
import com.singular.barrister.Database.Tables.Today.TodayCasesCaseType;
import com.singular.barrister.Database.Tables.Today.TodayCasesHearingTable;
import com.singular.barrister.Database.Tables.Today.TodayCasesSubCaseType;
import com.singular.barrister.Model.Cases.Case;
import com.singular.barrister.Model.Cases.CaseClient;
import com.singular.barrister.Model.Cases.CaseDistrict;
import com.singular.barrister.Model.Cases.CaseHearing;
import com.singular.barrister.Model.Cases.CasePersons;
import com.singular.barrister.Model.Cases.CaseState;
import com.singular.barrister.Model.Cases.CaseSubDistrict;
import com.singular.barrister.Model.Cases.CaseType;
import com.singular.barrister.Model.Cases.SubCaseType;
import com.singular.barrister.Model.Court.CourtData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul.kalamkar on 12/4/2017.
 */

public class TodayCaseQuery {
    DatabaseHelper databaseHelper = null;
    Context context;

    public TodayCaseQuery(Context context) {
        this.context = context;
    }

    private DatabaseHelper getHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void releaseHelper() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public void addList(ArrayList<Case> caseArrayList) {
        for (Case aCase : caseArrayList) {
            if (checkCase(aCase)) {
                updateCase(aCase);
            } else {
                addCase(aCase);
                insertPersonsForCase(aCase.getPersons());
            }
        }
        compareAndSyncDB(caseArrayList);
    }

    public boolean checkCase(Case aCase) {
        List<TodayCaseTable> list = null;
        Dao<TodayCaseTable, Integer> caseTableIntegerDao;
        try {
            caseTableIntegerDao = getHelper(context).getATodayCaseTableDao();
            list = caseTableIntegerDao.queryForEq("case_id", aCase.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Case table list", "" + e);
        }
        return (list != null && list.size() > 0);
    }

    public void updateCase(Case aCase) {
        TodayCaseClientTable caseClientTable = prePareCaseClient(aCase.getClient());
        TodayCasesCaseType casesCaseType = prePareCaseType(aCase.getCasetype());
        TodayCasesSubCaseType casesSubCaseType = prePareSubCaseType(aCase.getSubCasetype());
        TodayCaseCourtTable caseCourtTable = prePareCaseCourt(aCase.getCourt());
        TodayCasesHearingTable casesHearingTable = prePareCaseHearing(aCase.getHearing());
        TodayCaseTable caseTable = new TodayCaseTable(aCase.getId(), aCase.getClient_id(), aCase.getCase_cnr_number(), aCase.getCase_register_number(),
                aCase.getCase_register_date(), aCase.getCase_status(), aCase.getCase_type(),
                aCase.getCase_sub_type(), aCase.getClient_type(), aCase.getCourt_id(), caseClientTable, casesCaseType, casesSubCaseType, caseCourtTable, aCase.getDiff(), casesHearingTable);

        Dao<TodayCaseTable, Integer> caseTableIntegerDao;
        try {
            caseTableIntegerDao = getHelper(context).getATodayCaseTableDao();
            caseTableIntegerDao.update(caseTable);
            Log.e("Case table", "updated");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Case table", "" + e.getMessage());
        }
    }

    public void compareAndSyncDB(ArrayList<Case> caseArrayList) {
        if (caseArrayList == null)
            return;
        List<TodayCaseTable> list = getList();
        if (list == null)
            return;
        if (list != null && list.size() != caseArrayList.size()) {
            for (TodayCaseTable caseTable : list) {
                boolean delete = true;
                for (Case aCase : caseArrayList) {
                    if (aCase.getId().equalsIgnoreCase(caseTable.getCase_id())) {
                        delete = false;
                        break;
                    }
                }
                if (delete) {
                    deleteCase(caseTable);
                }
            }
        }
    }

    public void deleteCase(TodayCaseTable caseTable) {
        Dao<TodayCaseTable, Integer> caseTableIntegerDao;
        try {
            caseTableIntegerDao = getHelper(context).getATodayCaseTableDao();
            caseTableIntegerDao.delete(caseTable);
            Log.e("Case table", "deleted");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Case table", "" + e.getMessage());
        }
    }

    public void addCase(Case aCase) {
        TodayCaseClientTable caseClientTable = prePareCaseClient(aCase.getClient());
        TodayCasesCaseType casesCaseType = prePareCaseType(aCase.getCasetype());
        TodayCasesSubCaseType casesSubCaseType = prePareSubCaseType(aCase.getSubCasetype());
        TodayCaseCourtTable caseCourtTable = prePareCaseCourt(aCase.getCourt());
        TodayCasesHearingTable casesHearingTable = prePareCaseHearing(aCase.getHearing());
        TodayCaseTable caseTable = new TodayCaseTable(aCase.getId(), aCase.getClient_id(), aCase.getCase_cnr_number(), aCase.getCase_register_number(),
                aCase.getCase_register_date(), aCase.getCase_status(), aCase.getCase_type(),
                aCase.getCase_sub_type(), aCase.getClient_type(), aCase.getCourt_id(), caseClientTable, casesCaseType, casesSubCaseType, caseCourtTable, aCase.getDiff(), casesHearingTable);

        Dao<TodayCaseTable, Integer> caseTableIntegerDao;
        try {
            caseTableIntegerDao = getHelper(context).getATodayCaseTableDao();
            caseTableIntegerDao.create(caseTable);
            Log.e("Case table", "inserted");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Case table", "" + e.getMessage());
        }
    }

    public List<TodayCaseTable> getList() {
        List<TodayCaseTable> list = null;
        Dao<TodayCaseTable, Integer> caseTableIntegerDao;
        try {
            caseTableIntegerDao = getHelper(context).getATodayCaseTableDao();
            list = caseTableIntegerDao.queryForAll();
            Log.e("Case table list", "list" + list.size());
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Case table list", "" + e);
        }
        return list;
    }

    public List<Case> convertListToOnLineList(List<TodayCaseTable> list) {
        List<Case> onlineCaseList = new ArrayList<>();
        onlineCaseList.clear();
        for (TodayCaseTable caseTable : list) {
            CaseClient caseClient = new CaseClient(caseTable.getClient().getClient_id(), caseTable.getClient().getFirst_name(), caseTable.getClient().getLast_name(), caseTable.getClient().getCountry_code(),
                    caseTable.getClient().getMobile(), caseTable.getClient().getEmail(), caseTable.getClient().getAddress());

            CaseState caseState = new CaseState(caseTable.getCourt().getCourtState().getName(),
                    caseTable.getCourt().getCourtState().getId(),
                    caseTable.getCourt().getCourtState().getParent_id(),
                    caseTable.getCourt().getCourtState().getExternal_id(),
                    caseTable.getCourt().getCourtState().getLocation_type(),
                    caseTable.getCourt().getCourtState().getPin());

            CaseDistrict caseDistrict = null;
            if (caseTable.getCourt().getCourtDistrict() != null) {
                caseDistrict = new CaseDistrict(caseTable.getCourt().getCourtDistrict().getName(),
                        caseTable.getCourt().getCourtDistrict().getId(),
                        caseTable.getCourt().getCourtDistrict().getParent_id(),
                        caseTable.getCourt().getCourtDistrict().getExternal_id(),
                        caseTable.getCourt().getCourtDistrict().getLocation_type(),
                        caseTable.getCourt().getCourtDistrict().getPin());
            }

            CaseSubDistrict caseSubDistrict = null;
            if (caseTable.getCourt().getCourtSubDistrict() != null) {
                caseSubDistrict = new CaseSubDistrict(caseTable.getCourt().getCourtSubDistrict().getName(),
                        caseTable.getCourt().getCourtSubDistrict().getId(),
                        caseTable.getCourt().getCourtSubDistrict().getParent_id(),
                        caseTable.getCourt().getCourtSubDistrict().getExternal_id(),
                        caseTable.getCourt().getCourtSubDistrict().getLocation_type(),
                        caseTable.getCourt().getCourtSubDistrict().getPin());
            }

            CourtData courtData = new CourtData(caseTable.getCourt().getCourt_id(), caseTable.getCourt().getCourt_name(), caseTable.getCourt().getCourt_type(),
                    caseTable.getCourt().getCourt_number(), caseTable.getCourt().getState_id(), caseTable.getCourt().getDistrict_id(), caseTable.getCourt().getSub_district_id(),
                    caseState, caseDistrict, caseSubDistrict);

            CaseType caseType = null;
            if (caseTable.getCasesCaseType() != null)
                caseType = new CaseType(caseTable.getCasesCaseType().getCase_type_id(), caseTable.getCasesCaseType().getCase_type_name());

            SubCaseType subCaseType = null;
            if (caseTable.getCasesSubCaseType() != null)
                subCaseType = new SubCaseType(caseTable.getCasesSubCaseType().getSub_case_type_id(), caseTable.getCasesSubCaseType().getSub_case_type_name());

            List<TodayCasePerson> personList = getPersonList(caseTable.getCase_id());

            List<CasePersons> casePersonses = null;
            if (personList != null)
                casePersonses = getPersonsList(personList);

            CaseHearing caseHearing = null;
            if (caseTable.getHearingTable() != null)
                caseHearing = new CaseHearing(caseTable.getHearingTable().getHearing_id(), caseTable.getHearingTable().getCase_id(), caseTable.getHearingTable().getCase_hearing_date(),
                        caseTable.getHearingTable().getCase_decision(), caseTable.getHearingTable().getCase_disposed());

            Case aCase = new Case(caseTable.getCase_id(), caseTable.getClient_id(), caseTable.getClient_type(), caseTable.getCourt_id(), caseTable.getCase_cnr_number(),
                    caseTable.getCase_register_number(), caseTable.getCase_register_date(), caseTable.getCase_type(), caseTable.getCase_sub_type(),
                    caseTable.getCase_status(), caseClient, casePersonses, caseType, subCaseType, courtData, caseTable.getDiff(), caseHearing);
            onlineCaseList.add(aCase);
        }
        return onlineCaseList;
    }

    public TodayCaseClientTable prePareCaseClient(CaseClient Client) {
        TodayCaseClientTable caseClient = new TodayCaseClientTable(Client.getId(), Client.getFirst_name(), Client.getLast_name(),
                Client.getCountry_code(), Client.getMobile(), Client.getEmail(), Client.getAddress());
        return caseClient;
    }

    public TodayCasesHearingTable prePareCaseHearing(CaseHearing caseHearing) {
        TodayCasesHearingTable casesHearingTable = null;
        if (caseHearing != null)
            casesHearingTable = new TodayCasesHearingTable(caseHearing.getId(), caseHearing.getCase_id(), caseHearing.getCase_hearing_date(), caseHearing.getCase_decision(), caseHearing.getCase_disposed());
        return casesHearingTable;
    }

    public TodayCasesCaseType prePareCaseType(CaseType caseType) {
        if (caseType == null)
            return null;
        TodayCasesCaseType casesCaseType = new TodayCasesCaseType(caseType.getCase_type_id(), caseType.getCase_type_name());
        return casesCaseType;
    }

    public TodayCasesSubCaseType prePareSubCaseType(SubCaseType subCaseType) {
        if (subCaseType == null)
            return null;
        TodayCasesSubCaseType casesSubCaseType = new TodayCasesSubCaseType(subCaseType.getSubcase_type_id(), subCaseType.getSubcase_type_name());
        return casesSubCaseType;
    }


    public TodayCaseCourtTable prePareCaseCourt(CourtData courtData) {
        TodayCaseCourtTable caseCourtTable = null;
        TodayCaseCourtState caseCourtState = prePareCaseState(courtData.getState());
        TodayCaseCourtDistrict caseCourtDistrict = prePareCaseDistrict(courtData.getDistrict());
        TodayCaseCourtSubDistrict caseCourtSubDistrict = prePareCaseSubDistrict(courtData.getSubdistrict());

        caseCourtTable = new TodayCaseCourtTable(courtData.getId(), courtData.getCourt_name(), courtData.getCourt_number(), courtData.getCourt_type(),
                courtData.getState_id(), courtData.getDistrict_id(), courtData.getSub_district_id(), caseCourtState, caseCourtDistrict, caseCourtSubDistrict);
        return caseCourtTable;
    }

    public TodayCaseCourtState prePareCaseState(CaseState caseState) {
        if (caseState == null)
            return null;
        TodayCaseCourtState caseCourtState = new TodayCaseCourtState(caseState.getId(), caseState.getParent_id(), caseState.getExternal_id(), caseState.getName(), caseState.getLocation_type(), caseState.getPin());
        return caseCourtState;
    }

    public TodayCaseCourtDistrict prePareCaseDistrict(CaseDistrict caseDistrict) {
        if (caseDistrict == null)
            return null;
        TodayCaseCourtDistrict caseCourtDistrict = new TodayCaseCourtDistrict(caseDistrict.getId(), caseDistrict.getParent_id(), caseDistrict.getExternal_id(), caseDistrict.getName(), caseDistrict.getLocation_type(), caseDistrict.getPin());
        return caseCourtDistrict;
    }

    public TodayCaseCourtSubDistrict prePareCaseSubDistrict(CaseSubDistrict subDistrict) {
        if (subDistrict == null)
            return null;
        TodayCaseCourtSubDistrict caseCourtSubDistrict = new TodayCaseCourtSubDistrict(subDistrict.getId(), subDistrict.getParent_id(), subDistrict.getExternal_id(), subDistrict.getName(), subDistrict.getLocation_type(), subDistrict.getPin());
        return caseCourtSubDistrict;
    }

    // Insert persons of case
    public void insertPersonsForCase(List<CasePersons> personsList) {
        for (CasePersons casePersons : personsList) {
            addPersonList(casePersons);
        }
    }

    public void addPersonList(CasePersons persons) {
        Dao<TodayCasePerson, Integer> casePersonIntegerDao;
        TodayCasePerson casePerson = new TodayCasePerson(persons.getCase_id(), persons.getOpp_name(), persons.getMobile(), persons.getCountry_code(), persons.getType());
        try {
            casePersonIntegerDao = getHelper(context).getATodayCasePersonDao();
            casePersonIntegerDao.create(casePerson);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<TodayCasePerson> getPersonList(String aCaseid) {
        List<TodayCasePerson> casePersons = null;
        Dao<TodayCasePerson, Integer> casePersonIntegerDao;
        try {
            casePersonIntegerDao = getHelper(context).getATodayCasePersonDao();
            casePersons = casePersonIntegerDao.queryForEq("case_id", aCaseid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return casePersons;
    }

    public List<CasePersons> getPersonsList(List<TodayCasePerson> list) {
        List<CasePersons> casePersonses = new ArrayList<CasePersons>();
        casePersonses.clear();
        for (TodayCasePerson persons : list) {
            CasePersons casePersons = new CasePersons(persons.getCase_id(), persons.getOpp_name(), persons.getMobile(), persons.getCountry_code(), persons.getType());
            casePersonses.add(casePersons);
        }
        return casePersonses;
    }
}
