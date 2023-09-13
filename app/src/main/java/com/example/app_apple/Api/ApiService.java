package com.example.app_apple.Api;

import com.example.app_apple.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/users")
    Call<Void> registerUser(@Body User user);
}
