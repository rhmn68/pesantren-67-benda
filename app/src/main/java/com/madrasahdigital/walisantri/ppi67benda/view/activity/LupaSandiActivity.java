package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
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

public class LupaSandiActivity extends AppCompatActivity {

    private final String TAG = LupaSandiActivity.class.getSimpleName();
    private ActionBar aksibar;
    private EditText etEmail;
    private Button btnLanjut;
    private LoadingDialog loadingDialog;
    private boolean isEmailTrue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_sandi);

        etEmail = findViewById(R.id.etEmail);
        btnLanjut = findViewById(R.id.btnLanjut);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(LupaSandiActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = LupaSandiActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        loadingDialog = new LoadingDialog(LupaSandiActivity.this);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etEmail.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String textEmail = etEmail.getText().toString().trim();

                if (!UtilsManager.validateEmail(textEmail)) {
                    etEmail.setError("Isi dengan email valid");
                    isEmailTrue = false;
                } else {
                    isEmailTrue = true;
                }
                changeButtonColor();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeButtonColor() {
        if (isEmailTrue) {
            btnLanjut.setBackground(getResources().getDrawable(R.drawable.btn_green));
        } else {
            btnLanjut.setBackground(getResources().getDrawable(R.drawable.btn_silver));
        }
    }

    public void callLupaSandiApi(View view) {
        if (isEmailTrue) {
            new RegisterToServer(etEmail.getText().toString().trim()).execute();
        }
    }

    private class RegisterToServer extends AsyncTask<Void, Integer, Boolean> {

        private String email;
        private String message;

        public RegisterToServer(String email) {
            this.email = email;
        }

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

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .build();
            Request request = new Request.Builder()
                    .url(Constant.LINK_FORGOT_PASSWORD)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                JSONObject jsonObject = new JSONObject(responseBody.string());

                int statusCode = response.code();
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
                    UtilsManager.showToast(LupaSandiActivity.this, message);
                    finish();
                } else {
                    UtilsManager.showToast(LupaSandiActivity.this, message);
                }
            } catch (Exception e) {
                Crashlytics.setString(TAG, "2-" + e.getMessage());
                Crashlytics.logException(e);
                UtilsManager.showToast(LupaSandiActivity.this, getResources().getString(R.string.cekkoneksi));
                e.printStackTrace();
            }
        }
    }

}
