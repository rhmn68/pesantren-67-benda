package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.service.AppLink;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.LoadingDialog;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = LoginActivity.class.getSimpleName();

    private EditText etEmail;
    private EditText etPassword;
    private LoadingDialog loadingDialog;
    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        loadingDialog = new LoadingDialog(LoginActivity.this);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void gotoHomePage(View view) {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
           new LoginToServer().execute();
        } else {
            UtilsManager.showToast(LoginActivity.this, getResources().getString(R.string.lengkapiform));
        }
    }

    private class LoginToServer extends AsyncTask<Void, Integer, Boolean> {

        private JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            loadingDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .addFormDataPart("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(AppLink.LINK_LOGIN)
                    .post(body)
                    .build();

            try {
                okhttp3.Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                jsonObject = new JSONObject(responseBody.string());

                int statusCode = response.code();

                if (statusCode == 200) {
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            loadingDialog.dismiss();
            try {
                if (isSuccess) {
                    SharedPrefManager sharedPrefManager = new SharedPrefManager(LoginActivity.this);
                    sharedPrefManager.setLogin(true);
                    sharedPrefManager.setToken(jsonObject.getString("token"));
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Log.w(TAG, "message : " + jsonObject.getString("message"));
                    UtilsManager.showToast(LoginActivity.this, jsonObject.getString("message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
