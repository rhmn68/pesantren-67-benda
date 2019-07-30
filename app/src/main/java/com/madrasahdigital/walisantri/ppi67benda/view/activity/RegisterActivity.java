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
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.LoadingDialog;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RegisterActivity extends AppCompatActivity {

    private ActionBar aksibar;
    private Button btnLanjut;
    private EditText etInputNama;
    private EditText etEmail;
    private EditText etHp;
    private EditText etPassword;
    private EditText etKonfirmasiPassword;
    private TextView tvErrorMessage;
    private boolean isEmailTrue = false;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(RegisterActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = RegisterActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        btnLanjut = findViewById(R.id.btnLanjut);
        etInputNama = findViewById(R.id.etInputNama);
        etEmail = findViewById(R.id.etEmail);
        etHp = findViewById(R.id.etHp);
        etPassword = findViewById(R.id.etPassword);
        etKonfirmasiPassword = findViewById(R.id.etPasswordKonfirmasi);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);

        loadingDialog = new LoadingDialog(RegisterActivity.this);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        etInputNama.addTextChangedListener(new TextWatcher() {
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

        etHp.addTextChangedListener(new TextWatcher() {
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

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideError();
                etEmail.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String textEmail = etEmail.getText().toString().trim();

                if (!UtilsManager.validateEmail(textEmail)) {
                    etEmail.setError("Mohon isi dengan email valid");
                    isEmailTrue = false;
                } else {
                    isEmailTrue = true;
                }
                changeButtonColor();
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideError();
                etPassword.setError(null);
                etKonfirmasiPassword.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etKonfirmasiPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideError();
                etKonfirmasiPassword.setError(null);
                etPassword.setError(null);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void registerData(View view) {
        String nama = etInputNama.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String noHp = etHp.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String konfirmasiPassword = etKonfirmasiPassword.getText().toString().trim();

        if (!nama.isEmpty() && !email.isEmpty() && !noHp.isEmpty() && !password.isEmpty() && !konfirmasiPassword.isEmpty() ) {
            if (password.equals(konfirmasiPassword)) {
                if (isEmailTrue) {
                    new RegisterToServer(nama, email, noHp, password, password).execute();
                }
            } else {
                etKonfirmasiPassword.setError("Samakan password");
                etPassword.setError("Samakan password");
            }
        } else {
            UtilsManager.showToast(RegisterActivity.this, "Mohon lengkapi formulir");
        }
    }

    private void changeButtonColor() {
        if (isEmailTrue) {
            btnLanjut.setBackground(getResources().getDrawable(R.drawable.btn_green));
        } else {
            btnLanjut.setBackground(getResources().getDrawable(R.drawable.btn_silver));
        }
    }

    private void showError(String message) {
        tvErrorMessage.setText(message);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        tvErrorMessage.setVisibility(View.GONE);
    }

    private class RegisterToServer extends AsyncTask<Void, Integer, Boolean> {

        private String nama;
        private String email;
        private String noHp;
        private String password;
        private String konfirmasiPassword;
        private String message = "";

        public RegisterToServer(String nama, String email, String noHp, String password, String konfirmasiPassword) {
            this.nama = nama;
            this.email = email;
            this.noHp = noHp;
            this.password = password;
            this.konfirmasiPassword = konfirmasiPassword;
        }

        @Override
        protected void onPreExecute() {
            loadingDialog.show();
            hideError();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .addFormDataPart("nomorhp", noHp)
                    .addFormDataPart("password", password)
                    .addFormDataPart("confirm_password", konfirmasiPassword)
                    .addFormDataPart("name", nama)
                    .build();
            Request request = new Request.Builder()
                    .url(Constant.LINK_REGISTER)
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
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            loadingDialog.dismiss();
            try {
                if (isSuccess) {
                    UtilsManager.showToast(RegisterActivity.this, message);
                    finish();
                } else {
                    showError(message);
                }
            } catch (Exception e) {
                showError(getResources().getString(R.string.cekkoneksi));
                e.printStackTrace();
            }
        }
    }
}
