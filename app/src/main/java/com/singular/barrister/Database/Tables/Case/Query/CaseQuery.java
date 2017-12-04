package com.singular.barrister.Database.Tables.Case.Query;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.singular.barrister.Database.DB.DatabaseHelper;
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
import com.singular.barrister.Model.SubDistrict;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul.kalamkar on 12/4/2017.
 */

public class CaseQuery {
    DatabaseHelper databaseHelper = null;
    Context context;

    public CaseQuery(Context context) {
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
            addCase(aCase);
            insertPersonsForCase(aCase.getPersons());
        }
    }

    public void addCase(Case aCase) {
        CaseClientTable caseClientTable = prePareCaseClient(aCase.getClient());
        CasesCaseType casesCaseType = prePareCaseType(aCase.getCasetype());
        CasesSubCaseType casesSubCaseType = prePareSubCaseType(aCase.getSubCasetype());
        CaseCourtTable caseCourtTable = prePareCaseCourt(aCase.getCourt());
        CasesHearingTable casesHearingTable = prePareCaseHearing(aCase.getHearing());
        CaseTable caseTable = new CaseTable(aCase.getId(), aCase.getClient_id(), aCase.getCase_cnr_number(), aCase.getCase_register_number(),
                aCase.getCase_register_date(), aCase.getCase_status(), aCase.getCase_type(),
                aCase.getCase_sub_type(), aCase.getClient_type(), aCase.getCourt_id(), caseClientTable, casesCaseType, casesSubCaseType, caseCourtTable, aCase.getDiff(), casesHearingTable);

        Dao<CaseTable, Integer> caseTableIntegerDao;
        try {
            caseTableIntegerDao = getHelper(context).getACaseTableDao();
            caseTableIntegerDao.create(caseTable);
            Log.e("Case table", "inserted");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Case table", "" + e.getMessage());
        }
    }

    public List<CaseTable> getList() {
        List<CaseTable> list = null;
        Dao<CaseTable, Integer> caseTableIntegerDao;
        try {
            caseTableIntegerDao = getHelper(context).getACaseTableDao();
            list = caseTableIntegerDao.queryForAll();
            Log.e("Case table list", "list" + list.size());
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Case table list", "" + e);
        }
        return list;
    }

    public List<Case> convertListToOnLineList(List<CaseTable> list) {
        List<Case> onlineCaseList = new ArrayList<>();
        onlineCaseList.clear();
        for (CaseTable caseTable : list) {
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

            List<CasePerson> personList = getPersonList(caseTable.getCase_id());

            List<CasePersons> casePersonses = null;
            if (personList != null)
                casePersonses = getPersonsList(personList);

            CaseHearing caseHearing=null;
            if(caseTable.getHearingTable()!=null)
            caseHearing = new CaseHearing(caseTable.getHearingTable().getHearing_id(), caseTable.getHearingTable().getCase_id(), caseTable.getHearingTable().getCase_hearing_date(),
                    caseTable.getHearingTable().getCase_decision(), caseTable.getHearingTable().getCase_disposed());

            Case aCase = new Case(caseTable.getCase_id(), caseTable.getClient_id(), caseTable.getClient_type(), caseTable.getCourt_id(), caseTable.getCase_cnr_number(),
                    caseTable.getCase_register_number(), caseTable.getCase_register_date(), caseTable.getCase_type(), caseTable.getCase_sub_type(),
                    caseTable.getCase_status(), caseClient, casePersonses, caseType, subCaseType, courtData, caseTable.getDiff(), caseHearing);
            onlineCaseList.add(aCase);
        }
        return onlineCaseList;
    }

    public CaseClientTable prePareCaseClient(CaseClient Client) {
        CaseClientTable caseClient = new CaseClientTable(Client.getId(), Client.getFirst_name(), Client.getLast_name(),
                Client.getCountry_code(), Client.getMobile(), Client.getEmail(), Client.getAddress());
        return caseClient;
    }

    public CasesHearingTable prePareCaseHearing(CaseHearing caseHearing) {
        CasesHearingTable casesHearingTable = new CasesHearingTable(caseHearing.getId(), caseHearing.getCase_id(), caseHearing.getCase_hearing_date(), caseHearing.getCase_decision(), caseHearing.getCase_disposed());
        return casesHearingTable;
    }

    public CasesCaseType prePareCaseType(CaseType caseType) {
        if (caseType == null)
            return null;
        CasesCaseType casesCaseType = new CasesCaseType(caseType.getCase_type_id(), caseType.getCase_type_name());
        return casesCaseType;
    }

    public CasesSubCaseType prePareSubCaseType(SubCaseType subCaseType) {
        if (subCaseType == null)
            return null;
        CasesSubCaseType casesSubCaseType = new CasesSubCaseType(subCaseType.getSubcase_type_id(), subCaseType.getSubcase_type_name());
        return casesSubCaseType;
    }


    public CaseCourtTable prePareCaseCourt(CourtData courtData) {
        CaseCourtTable caseCourtTable = null;
        CaseCourtState caseCourtState = prePareCaseState(courtData.getState());
        CaseCourtDistrict caseCourtDistrict = prePareCaseDistrict(courtData.getDistrict());
        CaseCourtSubDistrict caseCourtSubDistrict = prePareCaseSubDistrict(courtData.getSubdistrict());

        caseCourtTable = new CaseCourtTable(courtData.getId(), courtData.getCourt_name(), courtData.getCourt_number(), courtData.getCourt_type(),
                courtData.getState_id(), courtData.getDistrict_id(), courtData.getSub_district_id(), caseCourtState, caseCourtDistrict, caseCourtSubDistrict);
        return caseCourtTable;
    }

    public CaseCourtState prePareCaseState(CaseState caseState) {
        if (caseState == null)
            return null;
        CaseCourtState caseCourtState = new CaseCourtState(caseState.getId(), caseState.getParent_id(), caseState.getExternal_id(), caseState.getName(), caseState.getLocation_type(), caseState.getPin());
        return caseCourtState;
    }

    public CaseCourtDistrict prePareCaseDistrict(CaseDistrict caseDistrict) {
        if (caseDistrict == null)
            return null;
        CaseCourtDistrict caseCourtDistrict = new CaseCourtDistrict(caseDistrict.getId(), caseDistrict.getParent_id(), caseDistrict.getExternal_id(), caseDistrict.getName(), caseDistrict.getLocation_type(), caseDistrict.getPin());
        return caseCourtDistrict;
    }

    public CaseCourtSubDistrict prePareCaseSubDistrict(CaseSubDistrict subDistrict) {
        if (subDistrict == null)
            return null;
        CaseCourtSubDistrict caseCourtSubDistrict = new CaseCourtSubDistrict(subDistrict.getId(), subDistrict.getParent_id(), subDistrict.getExternal_id(), subDistrict.getName(), subDistrict.getLocation_type(), subDistrict.getPin());
        return caseCourtSubDistrict;
    }

    // Insert persons of case
    public void insertPersonsForCase(List<CasePersons> personsList) {
        for (CasePersons casePersons : personsList) {
            addPersonList(casePersons);
        }
    }

    public void addPersonList(CasePersons persons) {
        Dao<CasePerson, Integer> casePersonIntegerDao;
        CasePerson casePerson = new CasePerson(persons.getCase_id(), persons.getOpp_name(), persons.getMobile(), persons.getCountry_code(), persons.getType());
        try {
            casePersonIntegerDao = getHelper(context).getACasePersonDao();
            casePersonIntegerDao.create(casePerson);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<CasePerson> getPersonList(String aCaseid) {
        List<CasePerson> casePersons = null;
        Dao<CasePerson, Integer> casePersonIntegerDao;
        try {
            casePersonIntegerDao = getHelper(context).getACasePersonDao();
            casePersons = casePersonIntegerDao.queryForEq("case_id", aCaseid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return casePersons;
    }

    public List<CasePersons> getPersonsList(List<CasePerson> list) {
        List<CasePersons> casePersonses = new ArrayList<CasePersons>();
        casePersonses.clear();
        for (CasePerson persons : list) {
            CasePersons casePersons = new CasePersons(persons.getCase_id(), persons.getOpp_name(), persons.getMobile(), persons.getCountry_code(), persons.getType());
            casePersonses.add(casePersons);
        }
        return casePersonses;
    }
}