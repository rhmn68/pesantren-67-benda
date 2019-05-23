package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanspp.TagihanAllVarModel;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanspp.TagihanSppModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerMakePayment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MakePaymentActivity extends AppCompatActivity {

    private final String TAG = MakePaymentActivity.class.getSimpleName();
    private RecyclerMakePayment recyclerMakePayment;
    private RecyclerMakePayment.OnCheckBoxClickListener onCheckBoxClickListener;
    private List<TagihanSppModel> tagihanSppModelList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPrefManager sharedPrefManager;
    private TextView tvTotalNominal;
    private double totalPayment;
    private boolean isThreadWork = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        mRecyclerView = findViewById(R.id.rv_numbers);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        tvTotalNominal = findViewById(R.id.tvTotalNominal);
        sharedPrefManager = new SharedPrefManager(MakePaymentActivity.this);
        tagihanSppModelList = new ArrayList<>();

        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.MAGENTA);
        totalPayment = 0;

        initializationOfListener();
        new GetTagihanSpp().execute();
    }

    private void initializationOfListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isThreadWork)
                    new GetTagihanSpp().execute();
            }
        });

        onCheckBoxClickListener = new RecyclerMakePayment.OnCheckBoxClickListener() {
            @Override
            public void onClick(int posisi, boolean isCheckBoxChecked, TagihanAllVarModel tagihanAllVarModel) {
                Log.d(TAG, "posisi : " + posisi + " isCheckBoxChecked : " + isCheckBoxChecked +
                                " nama : " + tagihanAllVarModel.getFullname());
                if (isCheckBoxChecked)
                    totalPayment += Double.parseDouble(tagihanAllVarModel.getNominal());
                else
                    totalPayment -= Double.parseDouble(tagihanAllVarModel.getNominal());
                tvTotalNominal.setText(UtilsManager.convertLongToCurrencyIDv2WithoutRp(totalPayment));
            }
        };
    }

    private void initializationOfViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(MakePaymentActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        recyclerMakePayment = new RecyclerMakePayment(MakePaymentActivity.this, tagihanSppModelList);
        recyclerMakePayment.setCheckBoxClickListener(onCheckBoxClickListener);
        mRecyclerView.setAdapter(recyclerMakePayment);
    }


    private class GetTagihanSpp extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            isThreadWork = true;
            swipeRefreshLayout.setRefreshing(true);
            tagihanSppModelList = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_TAGIHAN_SPP)
                    .get()
                    .addHeader("Authorization", sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                TagihanSppModel[] tagihanSppModels =
                        gson.fromJson(bodyString, TagihanSppModel[].class);

                tagihanSppModelList.addAll(Arrays.asList(tagihanSppModels));

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
                UtilsManager.showToast(MakePaymentActivity.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
