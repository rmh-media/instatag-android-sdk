package com.bi_instatag.itandroidsdk;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Body;

class CreateScreenRequest {
    @SerializedName("name")
    String name;

    public CreateScreenRequest(String name) {
        this.name = name;
    }
}

public interface ApiService {

    @GET("mobile-config/{mobileKey}")
    Call<MobileConfigResponse> loadMobileConfig(@Path("mobileKey") String mobileKey);

    @POST("mobile-config/{mobileKey}/screen")
    Call<CreateScreenResponse> createScreen(@Path("mobileKey") String mobileKey, @Body CreateScreenRequest screen);

}
