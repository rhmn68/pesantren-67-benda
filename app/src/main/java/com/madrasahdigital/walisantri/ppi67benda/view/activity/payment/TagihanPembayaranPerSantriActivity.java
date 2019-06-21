package com.madrasahdigital.walisantri.ppi67benda.view.activity.payment;

import android.content.Intent;
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

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanallsantri.TagihanAllSantriModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerPaymentBillPerSantri;

public class TagihanPembayaranPerSantriActivity extends AppCompatActivity {

    private ActionBar aksibar;
    private SharedPrefManager sharedPrefManager;
    private TagihanAllSantriModel tagihanAllSantriModel;
    private int posisi = 0;
    private TextView tvNoBill;
    private TextView tvSantriName;
    private TextView tvTotalText;
    private TextView tvTotalNominal;
    private TextView tvFirstCharForImageProfil;
    private RecyclerView rv_numbers;
    private RecyclerPaymentBillPerSantri recyclerPaymentBill;
    private RecyclerPaymentBillPerSantri.OnArtikelClickListener onArtikelClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagihan_pembayaran_per_santri);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        tvSantriName = findViewById(R.id.tvSantriName);
        tvNoBill = findViewById(R.id.tvNoBill);
        rv_numbers = findViewById(R.id.rv_numbers);
        tvTotalNominal = findViewById(R.id.tvTotalNominal);
        tvTotalText = findViewById(R.id.tvTotalText);
        tvFirstCharForImageProfil = findViewById(R.id.tvFirstCharForImageProfil);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(TagihanPembayaranPerSantriActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = TagihanPembayaranPerSantriActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        sharedPrefManager = new SharedPrefManager(TagihanPembayaranPerSantriActivity.this);
        tagihanAllSantriModel = sharedPrefManager.getTagihanAllSantri();
        Intent intent = getIntent();
        String santriname = intent.getStringExtra("santriname");
        posisi = intent.getIntExtra("posisi", -1);
        tvSantriName.setText(santriname);

        if (santriname.length() > 1) {
            tvFirstCharForImageProfil.setText(santriname.charAt(0) + "");
        }

        if (tagihanAllSantriModel.getStudents().size() == 0) {
            tvNoBill.setVisibility(View.VISIBLE);
            tvTotalText.setVisibility(View.GONE);
            tvTotalNominal.setVisibility(View.GONE);
            rv_numbers.setVisibility(View.GONE);
        } else {
            tvNoBill.setVisibility(View.GONE);
            rv_numbers.setVisibility(View.VISIBLE);
            String tot = "Rp " + UtilsManager.convertLongToCurrencyIDv2WithoutRp(tagihanAllSantriModel.getStudents().get(posisi).getBills().getSubtotal());
            tvTotalNominal.setText(tot);
        }
        initializationOfListener();
        initializationOfPresenceViewer();
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
        onArtikelClickListener = (posisi, santri) -> {
            Intent intent = new Intent(TagihanPembayaranPerSantriActivity.this, DetailTagihanActivity.class);
            intent.putExtra("urldetail", santri.getUrl());
            startActivity(intent);
        };
    }

    private void initializationOfPresenceViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(TagihanPembayaranPerSantriActivity.this);
        rv_numbers.setLayoutManager(mLinearLayoutManager);
        rv_numbers.setHasFixedSize(true);

        recyclerPaymentBill = new RecyclerPaymentBillPerSantri(TagihanPembayaranPerSantriActivity.this,
                tagihanAllSantriModel.getStudents().get(posisi).getBills().getBillItems());
        recyclerPaymentBill.setOnArtikelClickListener(onArtikelClickListener);

        rv_numbers.setAdapter(recyclerPaymentBill);
    }
}
