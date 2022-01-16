package com.example.itemslist.remote;

import com.example.itemslist.model.ResObj;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("/users/login")
    Call<ResObj> login(@Body ResObj data);
}
