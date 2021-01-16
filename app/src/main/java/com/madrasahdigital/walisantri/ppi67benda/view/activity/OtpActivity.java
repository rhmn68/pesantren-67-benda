package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.allsantri.AllSantri;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.LoadingDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.madrasahdigital.walisantri.ppi67benda.utils.Constant.TIMEOUT;

public class OtpActivity extends AppCompatActivity {

    private final String TAG = OtpActivity.class.getSimpleName();

    private TextView tvTimer;
    private TextView tvResendOtp;
    private TextView tvOtpBelumTepat;
    private EditText etKodeOtp;
    private Button btnKirim;
    private CountDownTimer countDownTimer;
    private LoadingDialog loadingDialog;

    private boolean isTimerDone = false;
    private String email;
    private String password;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(OtpActivity.this, R.color.colorPrimaryDark));
        }

        tvTimer = findViewById(R.id.tvTimer);
        tvResendOtp = findViewById(R.id.tvResendOtp);
        etKodeOtp = findViewById(R.id.etKodeOtp);
        btnKirim = findViewById(R.id.btnKirim);
        tvOtpBelumTepat = findViewById(R.id.tvOtpBelumTepat);

        loadingDialog = new LoadingDialog(OtpActivity.this);
        loadingDialog.setCancelable(false);
        try {
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            Crashlytics.setString(TAG, "loadial-" + e.getMessage());
            Crashlytics.logException(e);
            e.printStackTrace();
        }


        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("pass");
        idUser = intent.getStringExtra("id");

        initializeListener();
        startTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, " onDestroyView");
        countDownTimer.cancel();
    }

    private void initializeListener() {
        tvResendOtp.setOnClickListener(l -> {
            if (isTimerDone) {
                new GetResendOtp().execute();
            }
        });

        btnKirim.setOnClickListener(l -> {
            String kodeOtp = etKodeOtp.getText().toString().trim();

            if (!kodeOtp.isEmpty()) {
                new RegisterToServer(kodeOtp).execute();
            }
        });

        etKodeOtp.addTextChangedListener(new TextWatcher() {
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

    private void startTimer() {
        isTimerDone = false;
        tvResendOtp.setTextColor(getResources().getColor(R.color.silverstrong));
        countDownTimer = new CountDownTimer(90000, 1000) {
            @Override
            public void onTick(long l) {
                if (l > 60000) {
                    if ((l-60000)/1000 >= 10)
                        tvTimer.setText(String.format(Locale.getDefault(), "%d:%d", 1, (l - 60000)/1000));
                    else
                        tvTimer.setText(String.format(Locale.getDefault(), "%d:0%d", 1, (l - 60000)/1000));
                } else {
                    if (l/1000 >= 10)
                        tvTimer.setText(String.format(Locale.getDefault(), "%d:%d", 0, l/1000));
                    else
                        tvTimer.setText(String.format(Locale.getDefault(), "%d:0%d", 0, l/1000));
                }
            }

            @Override
            public void onFinish() {
                isTimerDone = true;
                tvResendOtp.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }.start();
    }

    private void showError(String msg) {
        tvOtpBelumTepat.setText(msg);
        tvOtpBelumTepat.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        tvOtpBelumTepat.setVisibility(View.GONE);
    }

    private class GetResendOtp extends AsyncTask<Void, Integer, Boolean> {

        private String message;

        @Override
        protected void onPreExecute() {
            loadingDialog.show();
            hideError();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(Constant.LINK_GET_OTP.replace("$", idUser))
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                int statusCode = response.code();

                JSONObject jsonObject = new JSONObject(responseBody.string());
                message = jsonObject.getString("message");

                if (statusCode == 200) {
                    return true;
                }
            } catch (Exception e) {
                Crashlytics.setString(TAG, "1-" + e.getMessage());
                Crashlytics.logException(e);
                e.printStackTrace();
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean isSuccess) {
            loadingDialog.dismiss();
            try {
                if (isSuccess) {
                    UtilsManager.showToast(OtpActivity.this, message);
                    startTimer();
                } else {
                    if (message.isEmpty()) message = "Terjadi kesalahan - 700";
                    showError(message);
                }
            } catch (Exception e) {
                Crashlytics.setString(TAG, "2-" + e.getMessage());
                Crashlytics.logException(e);
                showError(getResources().getString(R.string.cekkoneksi));
                e.printStackTrace();
            }
        }

    }

    private class RegisterToServer extends AsyncTask<Void, Integer, Boolean> {

        private String kodeOtp;
        private String message;
        private int statusSuccess;
        private JSONObject jsonObject;
        private SharedPrefManager sharedPrefManager;

        public RegisterToServer(String kodeOtp) {
            this.kodeOtp = kodeOtp;
        }

        @Override
        protected void onPreExecute() {
            sharedPrefManager = new SharedPrefManager(OtpActivity.this);
            loadingDialog.show();
            hideError();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

            RequestBody bodyOtp = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("token", kodeOtp)
                    .build();

            Request requestOtp = new Request.Builder()
                    .url(Constant.LINK_POST_OTP)
                    .post(bodyOtp)
                    .build();

            try {
                Response responseOtp = client.newCall(requestOtp).execute();
                ResponseBody responseBodyOtp = responseOtp.body();
                int statusCodeOtp = responseOtp.code();

                JSONObject jsonObjectOtp = new JSONObject(responseBodyOtp.string().replace("\\n",""));
                message = jsonObjectOtp.getString("message");

                if (statusCodeOtp == 200) {
                    /** call login **/

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
                        } else {
                            message = jsonObject.getString("message");
                        }

                    } catch (Exception e) {
                        Crashlytics.setString(TAG, "3-" + e.getMessage());
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }

                    if (status) {
                        /** call data santri **/
                        try {
                            request = new Request.Builder()
                                    .url(Constant.LINK_GET_ALL_SANTRI)
                                    .get()
                                    .addHeader("Authorization", jsonObject.getString("token"))
                                    .build();

                            Response response = client.newCall(request).execute();
                            ResponseBody responseBody = response.body();
                            String bodyString = responseBody.string();

                            int statusCode = response.code();

                            if (statusCode == 200) {

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
                            } else {
                                jsonObject = new JSONObject(responseBody.string());
                                message = jsonObject.getString("message");
                            }
                        } catch (Exception e) {
                            Crashlytics.setString(TAG, "4-" + e.getMessage());
                            Crashlytics.logException(e);
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                Crashlytics.setString(TAG, "5-" + e.getMessage());
                Crashlytics.logException(e);
                e.printStackTrace();
            } catch (Exception e) {
                Crashlytics.setString(TAG, "6-" + e.getMessage());
                Crashlytics.logException(e);
                e.printStackTrace();
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
                    Intent returnIntent = getIntent();
                    if (statusSuccess == 1) {
                        returnIntent.putExtra("pagetype", Constant.TYPE_HOME);
                    } else {
                        returnIntent.putExtra("pagetype", Constant.TYPE_WELCOME_MSG);
                    }
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    if (message.isEmpty()) message = "Terjadi kesalahan - 900";
                    showError(message);
                }
            } catch (Exception e) {
                Crashlytics.setString(TAG, "7-" + e.getMessage());
                Crashlytics.logException(e);
                showError(getResources().getString(R.string.cekkoneksi));
                e.printStackTrace();
            }
        }
    }
}
