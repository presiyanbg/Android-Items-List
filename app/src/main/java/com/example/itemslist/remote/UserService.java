package com.example.itemslist.remote;

import com.example.itemslist.model.ResObj;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {
    @FormUrlEncoded
    @POST("/users/login")
    Call<ResObj> login(@Field("username") String username, @Field("password") String password );

    @GET("/users")
    Call<ResObj> user();
}
