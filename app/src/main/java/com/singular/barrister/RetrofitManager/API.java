package com.singular.barrister.RetrofitManager;

import com.singular.barrister.Model.RegisterResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by rahul.kalamkar on 11/23/2017.
 */

public interface API {
    @GET("{SIGN_UP}")
    Call<RegisterResponse> registerUser(@Path(value = "SIGN_UP", encoded = true) String path, @HeaderMap Map<String, String> headers);

}
