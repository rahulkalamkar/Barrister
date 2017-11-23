package com.singular.barrister.RetrofitManager;

import com.singular.barrister.Model.Cases.CasesResponse;
import com.singular.barrister.Model.Client.ClientResponse;
import com.singular.barrister.Model.Court.CourtResponse;
import com.singular.barrister.Model.RegisterResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
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



    @GET("{GET_CASES_LIST}")
    Call<CasesResponse> getCasesList(@Path(value = "GET_CASES_LIST", encoded = true) String path, @HeaderMap Map<String, String> headers);


}
