package com.madrasahdigital.walisantri.ppi67benda.view.activity.payment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.detailpayment.DetailPaymentModel;
import com.madrasahdigital.walisantri.ppi67benda.model.detailpayment.KonfirmasiCheckoutModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerDetailMakePayment;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.LoadingDialog;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.madrasahdigital.walisantri.ppi67benda.utils.Constant.TIMEOUT;

public class DetailMakePaymentActivity extends AppCompatActivity {

    private final String TAG = DetailMakePaymentActivity.class.getSimpleName();
    private ActionBar aksibar;
    private TextView tvSubTotal;
    private TextView tvFee;
    private TextView tvTotalNominal;
    private DetailPaymentModel detailPaymentModel;
    private LoadingDialog loadingDialog;
    private SharedPrefManager sharedPrefManager;
    private RecyclerView rv_numbers;
    private RecyclerDetailMakePayment recyclerPaymentBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_make_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(DetailMakePaymentActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = DetailMakePaymentActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        tvSubTotal = findViewById(R.id.tvSubTotal);
        tvFee = findViewById(R.id.tvFee);
        tvTotalNominal = findViewById(R.id.tvTotalNominal);
        rv_numbers = findViewById(R.id.rv_numbers);

        loadingDialog = new LoadingDialog(DetailMakePaymentActivity.this);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sharedPrefManager = new SharedPrefManager(DetailMakePaymentActivity.this);
        detailPaymentModel = getIntent().getParcelableExtra("detailpayment");
        initializationOfRecyclerViewer();

        tvSubTotal.setText(UtilsManager.getRupiahMoney(String.valueOf(detailPaymentModel.getSubtotal())));
        tvFee.setText(UtilsManager.getRupiahMoney(String.valueOf(detailPaymentModel.getPaymentFee())));
        tvTotalNominal.setText(UtilsManager.getRupiahMoney(String.valueOf(detailPaymentModel.getTotal())));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            Intent returnIntent = getIntent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    private void initializationOfRecyclerViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(DetailMakePaymentActivity.this);
        rv_numbers.setLayoutManager(mLinearLayoutManager);
        rv_numbers.setHasFixedSize(true);

        recyclerPaymentBill = new RecyclerDetailMakePayment(DetailMakePaymentActivity.this,
                detailPaymentModel.getItems());

        rv_numbers.setAdapter(recyclerPaymentBill);
    }

    public void bayarSekarang(View view) {
        new KonfirmasiCheckoutPembayaran().execute();
    }


    private class KonfirmasiCheckoutPembayaran extends AsyncTask<Void, Integer, Boolean> {

        private KonfirmasiCheckoutModel konfirmasiCheckoutModel;

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

            Request request = new Request.Builder()
                    .url(detailPaymentModel.getConfirmUrl())
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                konfirmasiCheckoutModel =
                        gson.fromJson(bodyString, KonfirmasiCheckoutModel.class);

                return true;
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
            if (isSuccess) {
                Intent intent = new Intent(DetailMakePaymentActivity.this, DetailPembayaran.class);
                intent.putExtra("urldetailpembayaran", konfirmasiCheckoutModel.getUrl());
                startActivityForResult(intent, 1);
            } else {
                UtilsManager.showToast(DetailMakePaymentActivity.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
