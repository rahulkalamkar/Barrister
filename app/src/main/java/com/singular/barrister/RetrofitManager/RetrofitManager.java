package com.singular.barrister.RetrofitManager;

import com.singular.barrister.Model.CaseHearingResponse;
import com.singular.barrister.Model.Cases.CasesResponse;
import com.singular.barrister.Model.CasesTypeResponse;
import com.singular.barrister.Model.Client.ClientResponse;
import com.singular.barrister.Model.Court.CourtResponse;
import com.singular.barrister.Model.RegisterResponse;
import com.singular.barrister.Model.SimpleMessageResponse;
import com.singular.barrister.Model.States.StateResponse;
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

    public void setLogOut(/*final IDataChangeListener<IModel> callbackListener, */String token) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Authorization", "Bearer " + token);
        //  hashMap.put("deviceType", "android");

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/logout");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<RegisterResponse> call = api.logoutUser(apiName, hashMap);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    //     callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    //     callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            //   callbackListener.onDataReceived(null);
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
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/case/today");
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

    public void getProfile(final IDataChangeListener<IModel> callbackListener, String token) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Authorization", "Bearer " + token);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/profile");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<RegisterResponse> call = api.getProfile(apiName, hashMap);
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

    public void updateProfile(final IDataChangeListener<IModel> callbackListener, String token, String first_name, String last_name, String address) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Authorization", "Bearer " + token);

        HashMap<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("first_name", first_name);
        queryMap.put("last_name", last_name);
        queryMap.put("address", address);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/profile");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<RegisterResponse> call = api.updateProfile(apiName, hashMap, queryMap);
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


    public void changePassword(final IDataChangeListener<IModel> callbackListener, String token, String currentPassword,
                               String newPassword, String confirmNewPassword) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Authorization", "Bearer " + token);

        HashMap<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("nowpassword", currentPassword);
        queryMap.put("password", newPassword);
        queryMap.put("password_confirmation", confirmNewPassword);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/changepassword");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<SimpleMessageResponse> call = api.getPasswordChange(apiName, hashMap, queryMap);
            call.enqueue(new Callback<SimpleMessageResponse>() {
                @Override
                public void onResponse(Call<SimpleMessageResponse> call, Response<SimpleMessageResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<SimpleMessageResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }


    public void forgotPassword(final IDataChangeListener<IModel> callbackListener, String emailId) {
        HashMap<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("email", emailId);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/recovery");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<SimpleMessageResponse> call = api.forgotPassword(apiName, queryMap);
            call.enqueue(new Callback<SimpleMessageResponse>() {
                @Override
                public void onResponse(Call<SimpleMessageResponse> call, Response<SimpleMessageResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<SimpleMessageResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void getCaseHearingList(final IDataChangeListener<IModel> callbackListener, String token, String caseId) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/hearing/" + caseId);
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<CaseHearingResponse> call = api.getHearingList(apiName, headerMap);
            call.enqueue(new Callback<CaseHearingResponse>() {
                @Override
                public void onResponse(Call<CaseHearingResponse> call, Response<CaseHearingResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<CaseHearingResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void addHearingDate(final IDataChangeListener<IModel> callbackListener, String token, String caseId, String caseDate, String caseNotes) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);

        HashMap<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("case_id", caseId);
        queryMap.put("case_hearing_date", caseDate);
        queryMap.put("case_decision", caseNotes);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/hearing");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<SimpleMessageResponse> call = api.addHearingDate(apiName, headerMap, queryMap);
            call.enqueue(new Callback<SimpleMessageResponse>() {
                @Override
                public void onResponse(Call<SimpleMessageResponse> call, Response<SimpleMessageResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<SimpleMessageResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void addClient(final IDataChangeListener<IModel> callbackListener, String token, String first_name, String last_name, String mobile,
                          String country_code, String email, String password) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);


        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("first_name", first_name);
        hashMap.put("last_name", last_name);
        hashMap.put("country_code", country_code);
        hashMap.put("mobile", mobile);
        hashMap.put("email", email);
        hashMap.put("password", password);


        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/clients");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<SimpleMessageResponse> call = api.addClient(apiName, headerMap, hashMap);
            call.enqueue(new Callback<SimpleMessageResponse>() {
                @Override
                public void onResponse(Call<SimpleMessageResponse> call, Response<SimpleMessageResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<SimpleMessageResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }

    }

    public void deleteClient(/*final IDataChangeListener<IModel> callbackListener,*/ String token, String clientId) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/clients/" + clientId);
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<SimpleMessageResponse> call = api.deleteClient(apiName, headerMap);
            call.enqueue(new Callback<SimpleMessageResponse>() {
                @Override
                public void onResponse(Call<SimpleMessageResponse> call, Response<SimpleMessageResponse> response) {
                    //  callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<SimpleMessageResponse> call, Throwable t) {
                    //   callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            //  callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void deleteCase(/*final IDataChangeListener<IModel> callbackListener,*/ String token, String caseId) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/case/" + caseId);
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<SimpleMessageResponse> call = api.deleteCase(apiName, headerMap);
            call.enqueue(new Callback<SimpleMessageResponse>() {
                @Override
                public void onResponse(Call<SimpleMessageResponse> call, Response<SimpleMessageResponse> response) {
                    //  callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<SimpleMessageResponse> call, Throwable t) {
                    //   callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            //  callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void deleteCourt(/*final IDataChangeListener<IModel> callbackListener,*/ String token, String courtId) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/courts/" + courtId);
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<SimpleMessageResponse> call = api.deleteCourt(apiName, headerMap);
            call.enqueue(new Callback<SimpleMessageResponse>() {
                @Override
                public void onResponse(Call<SimpleMessageResponse> call, Response<SimpleMessageResponse> response) {
                    //  callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<SimpleMessageResponse> call, Throwable t) {
                    //   callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            //  callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void updateClient(final IDataChangeListener<IModel> callbackListener, String token, String clientId, String first_name, String last_name) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);

        HashMap<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("first_name", first_name);
        queryMap.put("last_name", last_name);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/clients/" + clientId);
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<SimpleMessageResponse> call = api.editClient(apiName, headerMap, queryMap);
            call.enqueue(new Callback<SimpleMessageResponse>() {
                @Override
                public void onResponse(Call<SimpleMessageResponse> call, Response<SimpleMessageResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<SimpleMessageResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void getState(final IDataChangeListener<IModel> callbackListener, String token) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);
        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/state");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<StateResponse> call = api.getStateList(apiName, headerMap);
            call.enqueue(new Callback<StateResponse>() {
                @Override
                public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<StateResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void getDistrictList(final IDataChangeListener<IModel> callbackListener, String token, String stateId) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);
        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/district");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<StateResponse> call = api.getDistrictList(apiName, headerMap, stateId);
            call.enqueue(new Callback<StateResponse>() {
                @Override
                public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<StateResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void getSubDistrictList(final IDataChangeListener<IModel> callbackListener, String token, String districtId) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);
        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/subdistrict");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<StateResponse> call = api.getSubDistrictList(apiName, headerMap, districtId);
            call.enqueue(new Callback<StateResponse>() {
                @Override
                public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<StateResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void addCourt(final IDataChangeListener<IModel> callbackListener, String token, String courtName,
                         String courtNumber, String courtType, String stateName, String districtName, String subDistrictName) {

        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);

        HashMap<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("court_name", courtName);
        queryMap.put("state_id", stateName);
        queryMap.put("district_id", districtName);
        queryMap.put("sub_district_id", subDistrictName);
        queryMap.put("court_number", courtNumber);
        queryMap.put("court_type", courtType);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/courts");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<SimpleMessageResponse> call = api.addCourt(apiName, headerMap, queryMap);
            call.enqueue(new Callback<SimpleMessageResponse>() {
                @Override
                public void onResponse(Call<SimpleMessageResponse> call, Response<SimpleMessageResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<SimpleMessageResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void editCourt(final IDataChangeListener<IModel> callbackListener, String token, String courtName,
                          String courtNumber, String courtType, String stateName, String districtName, String subDistrictName, String courtId) {

        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);

        HashMap<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("court_name", courtName);
        queryMap.put("state_id", stateName);
        queryMap.put("district_id", districtName);
        queryMap.put("sub_district_id", subDistrictName);
        queryMap.put("court_number", courtNumber);
        queryMap.put("court_type", courtType);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/courts/" + courtId);
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<SimpleMessageResponse> call = api.editCourt(apiName, headerMap, queryMap);
            call.enqueue(new Callback<SimpleMessageResponse>() {
                @Override
                public void onResponse(Call<SimpleMessageResponse> call, Response<SimpleMessageResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<SimpleMessageResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void getCourtType(final IDataChangeListener<IModel> callbackListener, String token) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);
        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/casetype");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<CasesTypeResponse> call = api.getCaseType(apiName, headerMap);
            call.enqueue(new Callback<CasesTypeResponse>() {
                @Override
                public void onResponse(Call<CasesTypeResponse> call, Response<CasesTypeResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<CasesTypeResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void changeCaseStatus(final IDataChangeListener<IModel> callbackListener, String token, String caseId, String caseStatus) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);

        HashMap<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("case_status", caseStatus);
        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/case/" + caseId);
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<SimpleMessageResponse> call = api.changeCaseStatus(apiName, headerMap, queryMap);
            call.enqueue(new Callback<SimpleMessageResponse>() {
                @Override
                public void onResponse(Call<SimpleMessageResponse> call, Response<SimpleMessageResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<SimpleMessageResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }
    }

    public void addCase(final IDataChangeListener<IModel> callbackListener, String token, String client_id, String client_type, String court_id, String case_cnr_number,
                        String case_register_number, String case_register_date, String case_type, String case_sub_type, String case_status, String opp_lawyer,
                        String opp_client, String opp_third_person,
                        String hearing) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", "Bearer " + token);

        HashMap<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("client_id", client_id);
        queryMap.put("client_type", client_type);
        queryMap.put("court_id", court_id);
        queryMap.put("case_cnr_number", case_cnr_number);
        queryMap.put("case_register_number", case_register_number);
        queryMap.put("case_register_date", case_register_date);
        queryMap.put("case_type", case_type);
        queryMap.put("case_sub_type", case_sub_type);
        queryMap.put("case_status", case_status);
        queryMap.put("opp_lawyer", opp_lawyer);
        queryMap.put("opp_client", opp_client);
        queryMap.put("opp_third_person", opp_third_person);
        queryMap.put("hearing", hearing);

        try {
            URL url = new URL("http://singularsacademy.com/lawyer/public/api/case");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<SimpleMessageResponse> call = api.addCase(apiName, headerMap, queryMap);
            call.enqueue(new Callback<SimpleMessageResponse>() {
                @Override
                public void onResponse(Call<SimpleMessageResponse> call, Response<SimpleMessageResponse> response) {
                    callbackListener.onDataReceived(response.body());
                }

                @Override
                public void onFailure(Call<SimpleMessageResponse> call, Throwable t) {
                    callbackListener.onDataReceived(null);
                }
            });
        } catch (MalformedURLException e) {
            callbackListener.onDataReceived(null);
            e.printStackTrace();
        }

    }
}
