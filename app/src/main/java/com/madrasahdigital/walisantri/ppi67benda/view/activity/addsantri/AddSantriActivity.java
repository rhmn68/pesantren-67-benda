package com.madrasahdigital.walisantri.ppi67benda.view.activity.addsantri;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.datasantri.AssignSantriModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.HomeActivityV2;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.InfoDialog;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.KonfirmasiSantriDialog;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.LoadingDialog;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AddSantriActivity extends AppCompatActivity {

    private final String TAG = AddSantriActivity.class.getSimpleName();
    private ActionBar aksibar;
    private EditText etNomorIndukSantri;
    private LoadingDialog loadingDialog;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_santri);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        toolbar.setTitle(getResources().getString(R.string.tambahkan_santri));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(AddSantriActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = AddSantriActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        loadingDialog = new LoadingDialog(AddSantriActivity.this);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        etNomorIndukSantri = findViewById(R.id.etNomorIndukSantri);
        sharedPrefManager = new SharedPrefManager(AddSantriActivity.this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(AddSantriActivity.this, HomeActivityV2.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void AddSantri(View view) {
        String nis = etNomorIndukSantri.getText().toString().trim();
        if (!nis.isEmpty()) {
            new AddSantriToServer(nis).execute();
        } else {
            UtilsManager.showToast(AddSantriActivity.this, "Mohon isi Nomor Induk Santri");
        }
    }

    private void openConfirmDialog(AssignSantriModel assignSantriModel) {
        KonfirmasiSantriDialog konfirmasiSantriDialog =
                new KonfirmasiSantriDialog(AddSantriActivity.this, assignSantriModel.getData().getNis(),
                        assignSantriModel.getData().getFullname(), assignSantriModel.getData().getDateOfBirth());
        konfirmasiSantriDialog.setDialogResult(isConfirmClicked -> {
            if (isConfirmClicked) {
                new KonfirmasiAssignSantri(assignSantriModel.getData().getUrlConfirm()).execute();
            }
        });
        konfirmasiSantriDialog.show();
    }


    private class KonfirmasiAssignSantri extends AsyncTask<Void, Integer, Boolean> {

        private String urlConfirm;
        private String message = "";

        public KonfirmasiAssignSantri(String urlConfirm) {
            this.urlConfirm = urlConfirm;
        }

        @Override
        protected void onPreExecute() {
            loadingDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(urlConfirm)
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                JSONObject jsonObject = new JSONObject(responseBody.string());
                message = jsonObject.getString("message");

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
                    Log.d(TAG, "sukses konfirm");
                    InfoDialog infoDialog = new InfoDialog(AddSantriActivity.this, message);
                    infoDialog.setCancelable(false);
                    infoDialog.setDialogResult(buttonClicked -> {
                        if (buttonClicked) {
                            Intent intent = new Intent(AddSantriActivity.this, HomeActivityV2.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    infoDialog.show();
                } else {
                    UtilsManager.showToast(AddSantriActivity.this, message);
                }
            } catch (Exception e) {
                UtilsManager.showToast(AddSantriActivity.this, getResources().getString(R.string.cekkoneksi));
                e.printStackTrace();
            }
        }
    }

    private class AddSantriToServer extends AsyncTask<Void, Integer, Integer> {

        private String nis;
        private String message = "";
        private AssignSantriModel assignSantriModel;

        public AddSantriToServer(String nis) {
            this.nis = nis;
        }

        @Override
        protected void onPreExecute() {
            loadingDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("nis", nis)
                    .build();
            Request request = new Request.Builder()
                    .url(Constant.LINK_ASSIGN_SANTRI)
                    .post(body)
                    .addHeader("Authorization", sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                String bodyString = responseBody.string();

                Gson gson = new Gson();
                assignSantriModel =
                        gson.fromJson(bodyString, AssignSantriModel.class);

                int statusCode = response.code();

                if (statusCode == 200) {
                    return 0;
                } else {
                    JSONObject jsonObject = new JSONObject(bodyString);
                    message = jsonObject.getString("message");
                    return 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return 2;
        }

        @Override
        protected void onPostExecute(Integer status) {
            loadingDialog.dismiss();
            if (status == 0) {
                openConfirmDialog(assignSantriModel);
            } else if (status == 1) {
                UtilsManager.showToast(AddSantriActivity.this, message);
            } else {
                UtilsManager.showToast(AddSantriActivity.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
