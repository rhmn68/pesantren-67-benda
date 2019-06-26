package com.madrasahdigital.walisantri.ppi67benda.view.activity.payment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.detailpembayaran.DetailPembayaranModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerDetailPembayaran;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DetailPembayaran extends AppCompatActivity {

    private ActionBar aksibar;
    private TextView tvOrderCode;
    private TextView tvStatusPembayaran;
    private TextView tvTotalTagihan;
    private TextView tvAtasNama;
    private TextView tvBankName;
    private TextView tvNoRekening;
    private TextView tvCopyNorek;
    private TextView tvTextTagihan;
    private ImageView ivRefresh;
    private TextView tvMuatUlang;
    private Button btnKonfirmasiWhatsapp;
    private LinearLayout linlayInfoCaraBayar;
    private SharedPrefManager sharedPrefManager;
    private DetailPembayaranModel detailPembayaranModel;
    private RecyclerView rv_numbers;
    private RecyclerDetailPembayaran recyclerListIni;
    private String urlGetDetailPembayaran;
    private String urlWhatsapp = "";
    private String norekpembayaran = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembayaran);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(DetailPembayaran.this, R.color.colorPrimaryDark));
        }

        aksibar = DetailPembayaran.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        ivRefresh = findViewById(R.id.ivRefresh);
        tvMuatUlang = findViewById(R.id.tvMuatUlang);
        tvTextTagihan = findViewById(R.id.tvTextTagihan);
        tvAtasNama = findViewById(R.id.tvAtasNama);
        tvBankName = findViewById(R.id.tvBankName);
        tvNoRekening = findViewById(R.id.tvNoRekening);
        tvCopyNorek = findViewById(R.id.tvCopyNorek);
        btnKonfirmasiWhatsapp = findViewById(R.id.btnKonfirmasiWhatsapp);
        linlayInfoCaraBayar = findViewById(R.id.linlayInfoCaraBayar);
        tvOrderCode = findViewById(R.id.tvOrderCode);
        tvStatusPembayaran = findViewById(R.id.tvStatusPembayaran);
        tvTotalTagihan = findViewById(R.id.tvTotalTagihan);
        rv_numbers = findViewById(R.id.rv_numbers);
        sharedPrefManager = new SharedPrefManager(DetailPembayaran.this);

        initializerOfListener();

        Intent intent = getIntent();
        urlGetDetailPembayaran = intent.getStringExtra("urldetailpembayaran");

        new GetDetailPembayaran().execute();
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

    private void initializeViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(DetailPembayaran.this);
        rv_numbers.setLayoutManager(mLinearLayoutManager);
        rv_numbers.setHasFixedSize(true);

        recyclerListIni = new RecyclerDetailPembayaran(DetailPembayaran.this, detailPembayaranModel.getItems());

        rv_numbers.setAdapter(recyclerListIni);
    }

    private void initializerOfListener() {

        ivRefresh.setOnClickListener(l -> {
            ivRefresh.setVisibility(View.GONE);
            tvMuatUlang.setVisibility(View.GONE);
            new GetDetailPembayaran().execute();
        });

        tvCopyNorek.setOnClickListener(l -> {
            try {
                final android.content.ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("norekpemb", norekpembayaran);
                clipboardManager.setPrimaryClip(clipData);
                UtilsManager.showToast(DetailPembayaran.this, "No rekening tersalin");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btnKonfirmasiWhatsapp.setOnClickListener(l -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(urlWhatsapp));
            startActivity(i);
        });
    }

    private class GetDetailPembayaran extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            tvOrderCode.setText("");
            tvStatusPembayaran.setText("");
            tvTotalTagihan.setText("0");
            linlayInfoCaraBayar.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(urlGetDetailPembayaran)
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                detailPembayaranModel =
                        gson.fromJson(bodyString, DetailPembayaranModel.class);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if (isSuccess) {
                if (detailPembayaranModel.getStatus().equals("pending")) {
                    linlayInfoCaraBayar.setVisibility(View.VISIBLE);
                    String bankname = "Bank : " + detailPembayaranModel.getInstruction().getBank();
                    String namapenerima = "Penerima : " + detailPembayaranModel.getInstruction().getAn();
                    String norek = "No. Rekening :\n" + detailPembayaranModel.getInstruction().getNorek();
                    String textPastikanBayar = "Silahkan transfer dengan nominal tepat Rp$ ke rekening berikut:";
                    textPastikanBayar = textPastikanBayar.replace("$", UtilsManager.getRupiahMoney(detailPembayaranModel.getTotal()));
                    tvTextTagihan.setText(textPastikanBayar);
                    norekpembayaran = detailPembayaranModel.getInstruction().getNorek();
                    urlWhatsapp = detailPembayaranModel.getInstruction().getUrlWa();
                    tvBankName.setText(bankname);
                    tvAtasNama.setText(namapenerima);
                    tvNoRekening.setText(norek);
                }
                tvOrderCode.setText(detailPembayaranModel.getOrderCode());
                tvStatusPembayaran.setText(detailPembayaranModel.getStatus());
                tvTotalTagihan.setText(UtilsManager.getRupiahMoney(detailPembayaranModel.getTotal()));
                initializeViewer();
            } else {
                ivRefresh.setVisibility(View.VISIBLE);
                tvMuatUlang.setVisibility(View.VISIBLE);
                UtilsManager.showToast(DetailPembayaran.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
