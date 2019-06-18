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
import com.madrasahdigital.walisantri.ppi67benda.model.allsantri.AllSantri;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerPaymentBill;

public class ChooseSantriPaymentActivity extends AppCompatActivity {

    private ActionBar aksibar;
    private SharedPrefManager sharedPrefManager;
    private AllSantri allSantri;
    private TextView tvNoBill;
    private TextView tvTotalText;
    private TextView tvTotalNominal;
    private RecyclerView rv_numbers;
    private RecyclerPaymentBill recyclerPaymentBill;
    private RecyclerPaymentBill.OnArtikelClickListener onArtikelClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_santri_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        tvNoBill = findViewById(R.id.tvNoBill);
        rv_numbers = findViewById(R.id.rv_numbers);
        tvTotalNominal = findViewById(R.id.tvTotalNominal);
        tvTotalText = findViewById(R.id.tvTotalText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(ChooseSantriPaymentActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = ChooseSantriPaymentActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        sharedPrefManager = new SharedPrefManager(ChooseSantriPaymentActivity.this);
        allSantri = sharedPrefManager.getAllSantri();

        if (allSantri.getTotal() == 0) {
            tvNoBill.setVisibility(View.VISIBLE);
            tvTotalText.setVisibility(View.GONE);
            tvTotalNominal.setVisibility(View.GONE);
            rv_numbers.setVisibility(View.GONE);
        } else {
            tvNoBill.setVisibility(View.GONE);
            rv_numbers.setVisibility(View.VISIBLE);
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
            Intent intent = new Intent(ChooseSantriPaymentActivity.this, TagihanPembayaranPerSantriActivity.class);
            intent.putExtra("santriname", santri.getFullname());
            startActivity(intent);
        };
    }

    private void initializationOfPresenceViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(ChooseSantriPaymentActivity.this);
        rv_numbers.setLayoutManager(mLinearLayoutManager);
        rv_numbers.setHasFixedSize(true);

        recyclerPaymentBill = new RecyclerPaymentBill(ChooseSantriPaymentActivity.this, allSantri.getSantri());
        recyclerPaymentBill.setOnArtikelClickListener(onArtikelClickListener);

        rv_numbers.setAdapter(recyclerPaymentBill);
    }

    public void gotoRiwayatPembayaran(View view) {
        Intent intent = new Intent(ChooseSantriPaymentActivity.this, RiwayatPembayaranActivity.class);
        startActivity(intent);
    }

    public void gotoMakePayment(View view) {
        Intent intent = new Intent(ChooseSantriPaymentActivity.this, MakePaymentActivity.class);
        startActivity(intent);
    }
}