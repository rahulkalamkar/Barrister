package com.singular.barrister.Preferance;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rahul.kalamkar on 11/23/2017.
 */

public class UserPreferance {

    private String FIRST_NAME = "first_name";
    private String LAST_NAME = "last_name";
    private String MOBILE = "mobile";
    private String EMAIL = "email";
    private String ADDRESS = "address";
    private String SUBSCRIPTION = "subscription";
    private String REFERRAL_CODE = "referral_code";
    private String START_DATE = "start_date";
    private String END_DATE = "end_date";
    private String USER_TYPE = "user_type";
    private String TOKEN = "token";
    private String IS_LOGGED_IN = "log_in";

    SharedPreferences UserPreference;
    private String PREFERENCE = "UserInfo";
    private String DEFAULT = "";

    public UserPreferance(Context context, String first_name, String last_name, String mobile, String email, String address, String subscription,
                          String referral_code, String start_date, String end_date, String user_type, String token, boolean is_loggedIn) {
        this.context = context;
        UserPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = UserPreference.edit();
        editor.putString(FIRST_NAME, first_name);
        editor.putString(LAST_NAME, last_name);
        editor.putString(MOBILE, mobile);
        editor.putString(EMAIL, email);
        editor.putString(ADDRESS, address);
        editor.putString(SUBSCRIPTION, subscription);
        editor.putString(REFERRAL_CODE, referral_code);
        editor.putString(START_DATE, start_date);
        editor.putString(END_DATE, end_date);
        editor.putString(USER_TYPE, user_type);
        editor.putString(TOKEN, token);
        editor.putBoolean(IS_LOGGED_IN, is_loggedIn);
        editor.commit();
    }

    Context context;

    public UserPreferance(Context context) {
        this.context = context;
        UserPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }

    public void logOut() {
        SharedPreferences.Editor editor = UserPreference.edit();
        editor.clear();
        editor.commit();
    }

    public boolean isUserLoggedIn() {
        return UserPreference.getBoolean(IS_LOGGED_IN, false);
    }

    public String getFirst_name() {
        return UserPreference.getString(FIRST_NAME, DEFAULT);
    }

    public String getLast_name() {
        return UserPreference.getString(LAST_NAME, DEFAULT);
    }

    public String getMobile() {
        return UserPreference.getString(MOBILE, DEFAULT);
    }

    public String getEmail() {
        return UserPreference.getString(EMAIL, DEFAULT);
    }

    public String getAddress() {
        return UserPreference.getString(ADDRESS, DEFAULT);
    }

    public String getSubscription() {
        return UserPreference.getString(SUBSCRIPTION, DEFAULT);
    }

    public String getReferral_code() {
        return UserPreference.getString(REFERRAL_CODE, DEFAULT);
    }

    public String getStart_date() {
        return UserPreference.getString(START_DATE, DEFAULT);
    }

    public String getEnd_date() {
        return UserPreference.getString(END_DATE, DEFAULT);
    }

    public String getUser_type() {
        return UserPreference.getString(USER_TYPE, DEFAULT);
    }

    public String getToken() {
        return UserPreference.getString(TOKEN, DEFAULT);
    }
}
