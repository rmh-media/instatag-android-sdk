package com.bi_instatag.itandroidsdk;


import android.util.Log;

import com.bi_instatag.itandroidsdk.entities.MobileConfig;
import com.bi_instatag.itandroidsdk.entities.Screen;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.MissingResourceException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

interface InstatagAPIHandler {
    void handleLoadMobileConfig(MobileConfig config);
    void handleLoadMobileConfigFailed(String reason);

    void handleCreateScreen(Screen config);
    void handleCreateScreenFailed(String reason);
}

class InstatagAPI {

    private static final String TAG = "Instatag";

    static final String BASE_URL = "https://mapi.bi-instatag.com/api/v1/";
    // openssl s_client -connect mapi.bi-instatag.com:443 -tls1
    private InstatagAPIHandler consumer;

    public InstatagAPI(InstatagAPIHandler consumer) {
        this.consumer = consumer;
    }

    public void loadMobileConfig(String mobileKey) throws IOException, MissingResourceException {
        if (consumer == null) {
            Log.i(TAG, "To use loadMobileConfig the API instance needs a consumer attached");
            return;
        }

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService api = retrofit.create(ApiService.class);

        Call<MobileConfigResponse> call = api.loadMobileConfig(mobileKey);
        call.enqueue(new Callback<MobileConfigResponse>() {
            @Override
            public void onResponse(Call<MobileConfigResponse> call, Response<MobileConfigResponse> response) {
                if(response.isSuccessful()) {
                    MobileConfigResponse resp = response.body();
                    consumer.handleLoadMobileConfig(resp.config);
                } else {
                    Log.e(TAG, "Request failed: " + response.errorBody());
                    consumer.handleLoadMobileConfigFailed(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<MobileConfigResponse> call, Throwable t) {
                Log.e(TAG, "Request failed: " + call.request().toString() + " -> " + t.getMessage());
                t.printStackTrace();
                consumer.handleLoadMobileConfigFailed("Request failed: " + call.request().toString() + " -> " + t.getMessage());
            }
        });
    }

    public void createScreen(String mobileKey, String screenName) throws IOException {
        Log.d(TAG, "#createScreen ");
        if (consumer == null) {
            Log.i(TAG, "To use createScreen the API instance needs a consumer attached");
            return;
        }

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService api = retrofit.create(ApiService.class);

        Call<CreateScreenResponse> call = api.createScreen(mobileKey, new CreateScreenRequest(screenName));
        call.enqueue(new Callback<CreateScreenResponse>() {
            @Override
            public void onResponse(Call<CreateScreenResponse> call, Response<CreateScreenResponse> response) {
                if(response.isSuccessful()) {
                    CreateScreenResponse resp = response.body();
                    consumer.handleCreateScreen(resp.screen);
                } else {
                    Log.e(TAG, "Request failed: " + response.errorBody());
                    consumer.handleCreateScreenFailed(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<CreateScreenResponse> call, Throwable t) {
                Log.e(TAG, "Request failed: " + call.request().toString() + " -> " + t.getMessage());
                t.printStackTrace();
                consumer.handleCreateScreenFailed("Request failed: " + call.request().toString() + " -> " + t.getMessage());
            }
        });
    }

}

class MobileConfigResponse {
    @SerializedName("mobile_config")
    MobileConfig config;
}

class CreateScreenResponse {
    @SerializedName("screen")
    Screen screen;
}

