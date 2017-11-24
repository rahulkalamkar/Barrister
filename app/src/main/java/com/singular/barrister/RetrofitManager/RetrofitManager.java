package com.singular.barrister.RetrofitManager;

import com.singular.barrister.Model.Cases.CasesResponse;
import com.singular.barrister.Model.Client.ClientResponse;
import com.singular.barrister.Model.Court.CourtResponse;
import com.singular.barrister.Model.RegisterResponse;
import com.singular.barrister.Model.Today.TodayResponse;
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

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/signup");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
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

    public void setLogin(final IDataChangeListener<IModel> callbackListener, String number, String password, String token) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("mobile", number);
        hashMap.put("password", password);
        hashMap.put("deviceToken", token);
        hashMap.put("deviceType", "android");

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/login");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<RegisterResponse> call = api.loginUser(apiName, hashMap);
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

    public void setLogOut(final IDataChangeListener<IModel> callbackListener, String token) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Authorization", "Bearer " + token);
        hashMap.put("deviceType", "android");

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/logout");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<RegisterResponse> call = api.loginUser(apiName, hashMap);
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

    public void getCourtList(final IDataChangeListener<IModel> callbackListener, String token) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Authorization", "Bearer " + token);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/courts");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<CourtResponse> call = api.getCourtList(apiName, hashMap);
            call.enqueue(new Callback<CourtResponse>() {
                @Override
                public void onResponse(Call<CourtResponse> call, Response<CourtResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<CourtResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void getClientList(final IDataChangeListener<IModel> callbackListener, String token) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Authorization", "Bearer " + token);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/clients");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<ClientResponse> call = api.getClientList(apiName, hashMap);
            call.enqueue(new Callback<ClientResponse>() {
                @Override
                public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<ClientResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }


    public void getCasesList(final IDataChangeListener<IModel> callbackListener, String token) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Authorization", "Bearer " + token);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/case");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<CasesResponse> call = api.getCasesList(apiName, hashMap);
            call.enqueue(new Callback<CasesResponse>() {
                @Override
                public void onResponse(Call<CasesResponse> call, Response<CasesResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<CasesResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void getTodayCases(final IDataChangeListener<IModel> callbackListener, String token) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Authorization", "Bearer " + token);

        try {
            URL url = new URL("http://www.singularsacademy.com/lawyer/public/api/case/today");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<TodayResponse> call = api.getTodayCasesList(apiName, hashMap);
            call.enqueue(new Callback<TodayResponse>() {
                @Override
                public void onResponse(Call<TodayResponse> call, Response<TodayResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<TodayResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }


}
