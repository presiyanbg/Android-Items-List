package com.example.itemslist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itemslist.model.ResObj;
import com.example.itemslist.remote.ApiUtils;
import com.example.itemslist.remote.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        userService = ApiUtils.getUserService();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                if (validateLogin(username,password)) {
                    doLogin(username, password);
                }
            }
        });
    }

    /**
     * Validate data for login.
     *
     * @param username String
     * @param password String
     * @return boolean
     */
    private boolean validateLogin(String username, String password) {
        if (username == null || username.trim().length() == 0) {
            Toast.makeText(this, "Username is required!", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (password == null || password.trim().length() == 0) {
            Toast.makeText(this, "Password is required!", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    /**
     * Login user to API.
     *
     * @param username String
     * @param password String
     */
    private void doLogin(String username, String password) {
        retrofit2.Call<ResObj> call = userService.login(username, password);

        call.enqueue(new Callback<ResObj>() {
            @Override
            public void onResponse(Call<ResObj> call, Response<ResObj> response) {
                if (response.isSuccessful()) {
                    ResObj resObj = response.body();

                    Integer id = resObj.getId();

                    if (resObj != null && (response.code() + "").equals("200")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        intent.putExtra("username", username);

                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error ? :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResObj> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
