package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.madrasahdigital.walisantri.ppi67benda.utils.Constant.TIMEOUT;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = LoginActivity.class.getSimpleName();

    private EditText etEmail;
    private EditText etPassword;
    private LoadingDialog loadingDialog;
    private TextView tvErrorMessage;
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

        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        loadingDialog = new LoadingDialog(LoginActivity.this);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sharedPrefManager = new SharedPrefManager(LoginActivity.this);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideError();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideError();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult RESULT OK");
                int pagetype = data.getIntExtra("pagetype", Constant.TYPE_WELCOME_MSG);
                Intent intent;

                if (pagetype == Constant.TYPE_HOME)
                    intent = new Intent(LoginActivity.this, HomeActivityV2.class);
                else
                    intent = new Intent(LoginActivity.this, WelcomeMsgAddSantri.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "onActivityResult RESULT CANCELED");
            }
        }
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

    public void gotoRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 1);
    }

    public void gotoLupaSandi(View view) {
        Intent intent = new Intent(LoginActivity.this, LupaSandiActivity.class);
        startActivity(intent);
    }

    private void showError(String message) {
        tvErrorMessage.setText(message);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        tvErrorMessage.setVisibility(View.GONE);
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
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

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
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    showError(jsonObject.getString("message"));
                    Log.w(TAG, "message : " + jsonObject.getString("message"));
                }
            } catch (Exception e) {
                showError(getResources().getString(R.string.cekkoneksi));
                e.printStackTrace();
            }
        }
    }
}
