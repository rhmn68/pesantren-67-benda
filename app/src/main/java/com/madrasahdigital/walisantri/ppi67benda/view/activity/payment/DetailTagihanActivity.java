package com.madrasahdigital.walisantri.ppi67benda.view.activity.payment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanallsantri.DetailTagihanModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.madrasahdigital.walisantri.ppi67benda.utils.Constant.TIMEOUT;

public class DetailTagihanActivity extends AppCompatActivity {

    private final String TAG = DetailTagihanActivity.class.getSimpleName();
    private ActionBar aksibar;
    private TextView tvTotalText;
    private TextView tvTotalNominal;
    private TextView tvTitlePayment;
    private TextView tvBatasPembayaran;
    private TextView tvDeskripsi;
    private ProgressBar progressBarToday;
    private ImageView ivRefreshPresenceToday;
    private LinearLayout linlayContent;
    private String url;
    private SharedPrefManager sharedPrefManager;
    private boolean isThreadCalled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tagihan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(DetailTagihanActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = DetailTagihanActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);
        tvTotalNominal = findViewById(R.id.tvTotalNominal);
        tvTotalText = findViewById(R.id.tvTotalText);
        tvBatasPembayaran = findViewById(R.id.tvBatasPembayaran);
        tvTitlePayment = findViewById(R.id.tvTitlePayment);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
        progressBarToday = findViewById(R.id.progressBarToday);
        ivRefreshPresenceToday = findViewById(R.id.ivRefreshPresenceToday);
        linlayContent = findViewById(R.id.linlayContent);
        Intent intent = getIntent();
        url = intent.getStringExtra("urldetail");
        sharedPrefManager = new SharedPrefManager(DetailTagihanActivity.this);

        new GetDetailTagihan().execute();

        ivRefreshPresenceToday.setOnClickListener(l -> {
            new GetDetailTagihan().execute();
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


    private class GetDetailTagihan extends AsyncTask<Void, Integer, Boolean> {

        private DetailTagihanModel detailTagihanModel;

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                detailTagihanModel =
                        gson.fromJson(bodyString, DetailTagihanModel.class);


                return true;
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey(TAG, "1-" + e.getMessage());
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPreExecute() {
            linlayContent.setVisibility(View.GONE);
            progressBarToday.setVisibility(View.VISIBLE);
            ivRefreshPresenceToday.setVisibility(View.GONE);
            isThreadCalled = true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            isThreadCalled = false;
            linlayContent.setVisibility(View.VISIBLE);
            progressBarToday.setVisibility(View.GONE);
            if (isSuccess) {
                ivRefreshPresenceToday.setVisibility(View.GONE);
                String batasPembayaran = "Batas Pembayaran " + UtilsManager.getDateAnotherFormatFromString2(detailTagihanModel.getDueDate());
                tvTitlePayment.setText(detailTagihanModel.getTitle());
                tvBatasPembayaran.setText(batasPembayaran);
                tvDeskripsi.setText(detailTagihanModel.getDescription());
                tvTotalNominal.setText(UtilsManager.getRupiahMoney(detailTagihanModel.getAmount()));
            } else {
                ivRefreshPresenceToday.setVisibility(View.VISIBLE);
                UtilsManager.showToast(DetailTagihanActivity.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
