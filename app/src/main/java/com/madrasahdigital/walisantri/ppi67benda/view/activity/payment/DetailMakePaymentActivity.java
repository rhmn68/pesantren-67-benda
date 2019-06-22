package com.madrasahdigital.walisantri.ppi67benda.view.activity.payment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.detailpayment.DetailPaymentModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerDetailMakePayment;

public class DetailMakePaymentActivity extends AppCompatActivity {

    private ActionBar aksibar;
    private TextView tvSubTotal;
    private TextView tvFee;
    private TextView tvTotalNominal;
    private DetailPaymentModel detailPaymentModel;
    private RecyclerView rv_numbers;
    private RecyclerDetailMakePayment recyclerPaymentBill;
    private RecyclerDetailMakePayment.OnArtikelClickListener onArtikelClickListener;

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

        detailPaymentModel = getIntent().getParcelableExtra("detailpayment");
        initializationOfListener();
        initializationOfRecyclerViewer();

        tvSubTotal.setText(UtilsManager.getRupiahMoney(String.valueOf(detailPaymentModel.getSubtotal())));
        tvFee.setText(UtilsManager.getRupiahMoney(String.valueOf(detailPaymentModel.getPaymentFee())));
        tvTotalNominal.setText(UtilsManager.getRupiahMoney(String.valueOf(detailPaymentModel.getTotal())));
    }

    private void initializationOfListener() {
        onArtikelClickListener = (posisi, santri) -> {
            Intent intent = new Intent(DetailMakePaymentActivity.this, DetailTagihanActivity.class);
            startActivity(intent);
        };
    }

    private void initializationOfRecyclerViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(DetailMakePaymentActivity.this);
        rv_numbers.setLayoutManager(mLinearLayoutManager);
        rv_numbers.setHasFixedSize(true);

        recyclerPaymentBill = new RecyclerDetailMakePayment(DetailMakePaymentActivity.this,
                detailPaymentModel.getItems());
        recyclerPaymentBill.setOnArtikelClickListener(onArtikelClickListener);

        rv_numbers.setAdapter(recyclerPaymentBill);
    }

    public void bayarSekarang(View view) {
    }
}
