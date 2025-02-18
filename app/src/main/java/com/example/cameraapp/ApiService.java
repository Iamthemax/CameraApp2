package com.example.cameraapp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("37653536-f4b6-4068-b4ab-b4c5450afa3e") // Change to your API endpoint
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file, @Part("description") RequestBody description);
}
