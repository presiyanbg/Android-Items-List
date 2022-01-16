package com.example.itemslist.model;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResObj {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("password")
    public String password;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("message")
    public String message;

    public ResObj(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ResObj(String username, String password, Integer id) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCode() {
        return code;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }

}