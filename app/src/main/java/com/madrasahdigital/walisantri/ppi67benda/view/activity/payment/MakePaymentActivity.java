package com.madrasahdigital.walisantri.ppi67benda.view.activity.payment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.PaymentSelectedModel;
import com.madrasahdigital.walisantri.ppi67benda.model.detailpayment.DetailPaymentModel;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanallsantri.TagihanAllSantriModel;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanspp.TagihanSppModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerMakePayment;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MakePaymentActivity extends AppCompatActivity {

    private final String TAG = MakePaymentActivity.class.getSimpleName();
    private RecyclerMakePayment recyclerMakePayment;
    private RecyclerMakePayment.OnCheckBoxClickListener onCheckBoxClickListener;
    private List<TagihanSppModel> tagihanSppModelList;
    private List<PaymentSelectedModel> paymentSelectedModelList;
    private RecyclerView mRecyclerView;
    private SharedPrefManager sharedPrefManager;
    private TextView tvTotalNominal;
    private double totalPayment;
    private boolean isThreadWork = false;
    private ActionBar aksibar;
    private TagihanAllSantriModel tagihanAllSantriModel;
    private LoadingDialog loadingDialog;
    private String bodyItemPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        mRecyclerView = findViewById(R.id.rv_numbers);
        tvTotalNominal = findViewById(R.id.tvTotalNominal);
        sharedPrefManager = new SharedPrefManager(MakePaymentActivity.this);
        tagihanSppModelList = new ArrayList<>();

        totalPayment = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(MakePaymentActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = MakePaymentActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        loadingDialog = new LoadingDialog(MakePaymentActivity.this);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tagihanAllSantriModel = sharedPrefManager.getTagihanAllSantri();
        paymentSelectedModelList = new ArrayList<>();

        initializationOfListener();
        initializationOfViewer();
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
            if(resultCode == Activity.RESULT_OK){
                Intent returnIntent = getIntent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "tombol kembali");
        Intent returnIntent = getIntent();
        returnIntent.putExtra("result", 1);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void initializationOfListener() {
        onCheckBoxClickListener = (posisi, isCheckBoxChecked, tagihanAllVarModel) -> {
            PaymentSelectedModel paymentSelectedModel =
                    new PaymentSelectedModel(tagihanAllVarModel.getSantriId(), tagihanAllVarModel.getIdBill());
            if (isCheckBoxChecked) {
                totalPayment += Double.parseDouble(tagihanAllVarModel.getNominal());
                paymentSelectedModelList.add(paymentSelectedModel);
            } else {
                totalPayment -= Double.parseDouble(tagihanAllVarModel.getNominal());
                paymentSelectedModelList.remove(paymentSelectedModel);
                int i = 0;
                while (i < paymentSelectedModelList.size()-1 &&
                        !paymentSelectedModelList.get(i).getBillingId().equals(tagihanAllVarModel.getIdBill())) {
                    i++;
                }
                paymentSelectedModelList.remove(i);
            }
            tvTotalNominal.setText(UtilsManager.convertLongToCurrencyIDv2WithoutRp(totalPayment));
        };
    }

    private void initializationOfViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(MakePaymentActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        recyclerMakePayment = new RecyclerMakePayment(MakePaymentActivity.this, tagihanAllSantriModel);
        recyclerMakePayment.setCheckBoxClickListener(onCheckBoxClickListener);
        mRecyclerView.setAdapter(recyclerMakePayment);
    }

    public void bayarSekarang(View view) {
        if (paymentSelectedModelList.size() > 0) {
            bodyItemPayment = "[";
            for (int i = 0; i < paymentSelectedModelList.size(); i++) {
                if (i != 0) bodyItemPayment += ",";
                bodyItemPayment += "{";
                bodyItemPayment += "\"student_id\":" + paymentSelectedModelList.get(i).getSantriId();
                bodyItemPayment += ",\"billing_id\":" + paymentSelectedModelList.get(i).getBillingId();
                bodyItemPayment += "}";
            }
            bodyItemPayment += "]";
            new PostRequestPembayaran().execute();
        } else {
            UtilsManager.showToast(MakePaymentActivity.this, "Tidak ada tagihan yang dipilih");
        }
    }

    private class PostRequestPembayaran extends AsyncTask<Void, Integer, Integer> {

        private int statusCode;
        private int statusRespon;
        private DetailPaymentModel detailPaymentModel;

        @Override
        protected void onPreExecute() {
            loadingDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            statusRespon = 0;
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("items", bodyItemPayment)
                    .build();
            Request request = new Request.Builder()
                    .url(Constant.LINK_POST_TAGIHAN_PAYMENT)
                    .post(body)
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                String bodyString = responseBody.string();

                Gson gson = new Gson();
                statusRespon = 2;
                detailPaymentModel =
                        gson.fromJson(bodyString, DetailPaymentModel.class);

                statusCode = response.code();

                statusRespon = 1;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return statusRespon;
        }

        @Override
        protected void onPostExecute(Integer statusSuccess) {
            loadingDialog.dismiss();

            if (statusSuccess == 0) {
                UtilsManager.showToast(MakePaymentActivity.this, getResources().getString(R.string.cekkoneksi));
            } else if (statusSuccess == 1) {
                Intent intent = new Intent(MakePaymentActivity.this, DetailMakePaymentActivity.class);
                intent.putExtra("detailpayment", detailPaymentModel);
                startActivityForResult(intent, 1);
            } else {
                UtilsManager.showToast(MakePaymentActivity.this, "Terjadi kesalahan " + statusCode);
            }
        }
    }

    private class GetTagihanSpp extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            isThreadWork = true;
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
            isThreadWork = false;
            if (isSuccess) {
                initializationOfViewer();
            } else {
                UtilsManager.showToast(MakePaymentActivity.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
