package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.madrasahdigital.walisantri.ppi67benda.BuildConfig;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.madrasahdigital.walisantri.ppi67benda.utils.Constant.TIMEOUT;

public class AboutActivity extends AppCompatActivity {

    private ActionBar aksibar;
    private TextView tvKodeVersi;
    private TextView tvVersiAplikasi;
    private WebView wvDescription;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(AboutActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = AboutActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        tvKodeVersi = findViewById(R.id.tvKodeVersi);
        tvVersiAplikasi = findViewById(R.id.tvVersiAplikasi);
        wvDescription = findViewById(R.id.wvDescription);
        progressBar = findViewById(R.id.progressBar);

        String versionCode = "Tipe : " + BuildConfig.VERSION_CODE;
        String versionName = "Versi Aplikasi : " + BuildConfig.VERSION_NAME;

        tvKodeVersi.setText(versionCode);
        tvVersiAplikasi.setText(versionName);

        new GetAboutDetail().execute();
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

    private void setDetailArticle(String detail) {
        wvDescription.loadData(detail, "text/html", "utf-8");
    }


    private class GetAboutDetail extends AsyncTask<Void, Integer, Boolean> {

        private String aboutText = "";

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_ABOUT_DETAIL)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();
                int statusCode = response.code();

                if (statusCode == 200) {
                    JSONObject jsonObject = new JSONObject(bodyString.replace("\\n", ""));
                    aboutText = jsonObject.getString("value");
                    return true;
                }
            } catch (Exception e) {
                Crashlytics.setString(Constant.TAG, "1-" + e.getMessage());
                Crashlytics.logException(e);
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            progressBar.setVisibility(View.GONE);
            if (isSuccess) {
                setDetailArticle(aboutText);
            } else {
                UtilsManager.showToast(AboutActivity.this, "Terjadi kesalahan");
            }
        }
    }
}
