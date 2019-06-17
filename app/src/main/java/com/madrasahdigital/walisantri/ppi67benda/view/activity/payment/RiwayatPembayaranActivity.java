package com.madrasahdigital.walisantri.ppi67benda.view.activity.payment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.payment.PaymentModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerPayment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

;

public class RiwayatPembayaranActivity extends AppCompatActivity {

    private final String TAG = RiwayatPembayaranActivity.class.getSimpleName();
    private RecyclerPayment recyclerListChat;
    private RecyclerPayment.OnArtikelClickListener onArtikelClickListener;
    private List<PaymentModel> paymentModelList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPrefManager sharedPrefManager;
    private boolean isThreadWork = false;
    private ActionBar aksibar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pembayaran);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(RiwayatPembayaranActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = RiwayatPembayaranActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.rv_numbers);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        sharedPrefManager = new SharedPrefManager(RiwayatPembayaranActivity.this);
        paymentModelList = new ArrayList<>();
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.MAGENTA);

        initializationOfListener();

        new GetPaymentData().execute();
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


    private void initializationOfListener() {
        onArtikelClickListener = (posisi, paymentModel) -> {
            Intent intent = new Intent(RiwayatPembayaranActivity.this, DetailTagihanActivity.class);
            startActivity(intent);
        };

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (!isThreadWork)
                new GetPaymentData().execute();
        });
    }

    private void initializationOfViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(RiwayatPembayaranActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        recyclerListChat = new RecyclerPayment(RiwayatPembayaranActivity.this, paymentModelList);
        recyclerListChat.setOnArtikelClickListener(onArtikelClickListener);

        mRecyclerView.setAdapter(recyclerListChat);
    }


    private class GetPaymentData extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            isThreadWork = true;
            swipeRefreshLayout.setRefreshing(true);
            paymentModelList = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_PAYMENT_INFO)
                    .get()
                    .addHeader("Authorization", sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                PaymentModel[] paymentModel =
                        gson.fromJson(bodyString, PaymentModel[].class);

                paymentModelList.addAll(Arrays.asList(paymentModel));

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            swipeRefreshLayout.setRefreshing(false);
            isThreadWork = false;
            if (isSuccess) {
                initializationOfViewer();
            } else {
                UtilsManager.showToast(RiwayatPembayaranActivity.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
