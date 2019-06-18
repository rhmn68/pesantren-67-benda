package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.allsantri.AllSantri;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.addsantri.WelcomeMsgAddSantri;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.LoadingDialog;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = LoginActivity.class.getSimpleName();

    private EditText etEmail;
    private EditText etPassword;
    private LoadingDialog loadingDialog;
    private String email;
    private String password;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(LoginActivity.this, R.color.colorPrimaryDark));
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        loadingDialog = new LoadingDialog(LoginActivity.this);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sharedPrefManager = new SharedPrefManager(LoginActivity.this);
    }

    public void gotoHomePage(View view) {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            if (!email.equals("admin@admin.com"))
                password = UtilsManager.md5(password);
            new LoginToServer().execute();
        } else {
            UtilsManager.showToast(LoginActivity.this, getResources().getString(R.string.lengkapiform));
        }
    }

    public void gotoRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private class LoginToServer extends AsyncTask<Void, Integer, Boolean> {

        private JSONObject jsonObject;
        private int statusSuccess;

        @Override
        protected void onPreExecute() {
            loadingDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            boolean status = false;

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .addFormDataPart("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(Constant.LINK_LOGIN)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                jsonObject = new JSONObject(responseBody.string());

                int statusCode = response.code();

                if (statusCode == 200) {
                    status = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (status) {
                try {
                    request = new Request.Builder()
                            .url(Constant.LINK_GET_ALL_SANTRI)
                            .get()
                            .addHeader("Authorization", jsonObject.getString("token"))
                            .build();

                    Response response = client.newCall(request).execute();
                    ResponseBody responseBody = response.body();
                    String bodyString = responseBody.string();

                    Gson gson = new Gson();
                    AllSantri allSantri =
                            gson.fromJson(bodyString, AllSantri.class);

                    sharedPrefManager.saveAllSantri(allSantri);

                    if (allSantri.getTotal() == 0) {
                        statusSuccess = 0;
                    } else {
                        sharedPrefManager.setIdActiveSantriInHomePage(allSantri.getSantri().get(0).getId());
                        statusSuccess = 1;
                    }

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            loadingDialog.dismiss();
            try {
                if (isSuccess) {
                    sharedPrefManager.setLogin(true);
                    sharedPrefManager.setToken(jsonObject.getString("token"));
                    Intent intent;
                    if (statusSuccess == 1)
                        intent = new Intent(LoginActivity.this, HomeActivityV2.class);
                    else
                        intent = new Intent(LoginActivity.this, WelcomeMsgAddSantri.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Log.w(TAG, "message : " + jsonObject.getString("message"));
                    UtilsManager.showToast(LoginActivity.this, jsonObject.getString("message"));
                }
            } catch (Exception e) {
                UtilsManager.showToast(LoginActivity.this, getResources().getString(R.string.cekkoneksi));
                e.printStackTrace();
            }
        }
    }
}
