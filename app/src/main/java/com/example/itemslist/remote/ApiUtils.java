package com.example.itemslist.remote;

public class ApiUtils {

    public static final String BASE_URL = "http://192.168.56.1:9110";

    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
}
