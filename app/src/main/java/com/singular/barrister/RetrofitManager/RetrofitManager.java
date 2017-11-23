package com.singular.barrister.RetrofitManager;

import com.singular.barrister.Model.RegisterResponse;
import com.singular.barrister.Util.IDataChangeListener;
import com.singular.barrister.Util.IModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rahul.kalamkar on 11/23/2017.
 */

public class RetrofitManager {
    public void setRegistration(final IDataChangeListener<IModel> callbackListener, String first_name, String last_name, String mobile,
                                String country_code, String email, String password, String referral_code, String address, String deviceToken) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("first_name", first_name);
        hashMap.put("last_name", last_name);
        hashMap.put("country_code", country_code);
        hashMap.put("mobile", mobile);
        hashMap.put("email", email);
        hashMap.put("password", password);
        hashMap.put("referral_code", referral_code);
        hashMap.put("address", address);
        hashMap.put("deviceToken", deviceToken);
        hashMap.put("deviceType", "android");

        API api = APIClient.getClient("www.singularsacademy.com").create(API.class);

        try {
            URL url = new URL("www.singularsacademy.com/lawyer/public/api/signup");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            Call<RegisterResponse> call = api.registerUser(apiName, hashMap);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void setLogin(final IDataChangeListener<IModel> callbackListener) {

    }
}
