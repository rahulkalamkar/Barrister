package com.singular.barrister.RetrofitManager;

import com.singular.barrister.Model.CaseHearingResponse;
import com.singular.barrister.Model.Cases.CasesResponse;
import com.singular.barrister.Model.Client.ClientResponse;
import com.singular.barrister.Model.Court.CourtResponse;
import com.singular.barrister.Model.RegisterResponse;
import com.singular.barrister.Model.SimpleMessageResponse;
import com.singular.barrister.Model.Today.TodayResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by rahul.kalamkar on 11/23/2017.
 */

public interface API {
    @POST("{SIGN_UP}")
    Call<RegisterResponse> registerUser(@Path(value = "SIGN_UP", encoded = true) String path, @QueryMap Map<String, String> headers);

    @POST("{LOGIN}")
    Call<RegisterResponse> loginUser(@Path(value = "LOGIN", encoded = true) String path, @QueryMap Map<String, String> headers);

    @POST("{LOGOUT}")
    Call<RegisterResponse> logoutUser(@Path(value = "LOGOUT", encoded = true) String path, @HeaderMap Map<String, String> headers);

    @GET("{GET_COURT_LIST}")
    Call<CourtResponse> getCourtList(@Path(value = "GET_COURT_LIST", encoded = true) String path, @HeaderMap Map<String, String> headers);


    @GET("{GET_CLIENT_LIST}")
    Call<ClientResponse> getClientList(@Path(value = "GET_CLIENT_LIST", encoded = true) String path, @HeaderMap Map<String, String> headers);

    @GET("{GET_TODAY_CASES_LIST}")
    Call<TodayResponse> getTodayCasesList(@Path(value = "GET_TODAY_CASES_LIST", encoded = true) String path, @HeaderMap Map<String, String> headers);

    @GET("{GET_CASES_LIST}")
    Call<CasesResponse> getCasesList(@Path(value = "GET_CASES_LIST", encoded = true) String path, @HeaderMap Map<String, String> headers);

    @GET("{GET_USER_PROFILE}")
    Call<RegisterResponse> getProfile(@Path(value = "GET_USER_PROFILE", encoded = true) String path, @HeaderMap Map<String, String> headers);

    @POST("{GET_PASSWORD}")
    Call<SimpleMessageResponse> getPasswordChange(@Path(value = "GET_PASSWORD", encoded = true) String path, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query);

    @GET("{FORGOT_PASSWORD}")
    Call<SimpleMessageResponse> forgotPassword(@Path(value = "FORGOT_PASSWORD", encoded = true) String path, @QueryMap Map<String, String> query);

    @POST("{UPDATE_PROFILE}")
    Call<RegisterResponse> updateProfile(@Path(value = "UPDATE_PROFILE", encoded = true) String path, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query);

    @GET("{GET_HEARING_LIST}")
    Call<CaseHearingResponse> getHearingList(@Path(value = "GET_HEARING_LIST", encoded = true) String path, @HeaderMap Map<String, String> headers);

    @POST("{ADD_CLIENT}")
    Call<SimpleMessageResponse> addClient(@Path(value = "ADD_CLIENT", encoded = true) String path, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query);

    @DELETE("{DELETE_CLIENT}")
    Call<SimpleMessageResponse> deleteClient(@Path(value = "DELETE_CLIENT", encoded = true) String path, @HeaderMap Map<String, String> headers);
}
