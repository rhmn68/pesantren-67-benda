package com.madrasahdigital.walisantri.ppi67benda.view.activity.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerPaymentBill;

public class ChooseSantriPaymentActivity extends AppCompatActivity {

    private final String TAG = ChooseSantriPaymentActivity.class.getSimpleName();
    private ActionBar aksibar;
    private SharedPrefManager sharedPrefManager;
    private TagihanAllSantriModel tagihanAllSantriModel;
    private TextView tvNoBill;
    private TextView tvTotalText;
    private TextView tvTotalNominal;
    private LinearLayout linlayBottom;
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
        linlayBottom = findViewById(R.id.linlayBottom);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(ChooseSantriPaymentActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = ChooseSantriPaymentActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        sharedPrefManager = new SharedPrefManager(ChooseSantriPaymentActivity.this);
        tagihanAllSantriModel = sharedPrefManager.getTagihanAllSantri();

        if (tagihanAllSantriModel == null) {
            tvNoBill.setVisibility(View.VISIBLE);
            tvTotalText.setVisibility(View.GONE);
            tvTotalNominal.setVisibility(View.GONE);
            rv_numbers.setVisibility(View.GONE);
            linlayBottom.setVisibility(View.GONE);
        } else {
            if (tagihanAllSantriModel.getStudents().size() == 0) {
                tvNoBill.setVisibility(View.VISIBLE);
                tvTotalText.setVisibility(View.GONE);
                tvTotalNominal.setVisibility(View.GONE);
                rv_numbers.setVisibility(View.GONE);
            } else {
                tvNoBill.setVisibility(View.GONE);
                rv_numbers.setVisibility(View.VISIBLE);
            }
            String tot = "Rp " + UtilsManager.convertLongToCurrencyIDv2WithoutRp(Double.valueOf(sharedPrefManager.getTotalTagihan()));
            tvTotalNominal.setText(tot);

            initializationOfListener();
            initializationOfPresenceViewer();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "RESULT OK");
                Intent intent = new Intent(ChooseSantriPaymentActivity.this, RiwayatPembayaranActivity.class);
                startActivity(intent);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "RESULT CANCELED");
            }
        }
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
            intent.putExtra("posisi", posisi);
            startActivity(intent);
        };
    }

    private void initializationOfPresenceViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(ChooseSantriPaymentActivity.this);
        rv_numbers.setLayoutManager(mLinearLayoutManager);
        rv_numbers.setHasFixedSize(true);

        recyclerPaymentBill = new RecyclerPaymentBill(ChooseSantriPaymentActivity.this, tagihanAllSantriModel.getStudents());
        recyclerPaymentBill.setOnArtikelClickListener(onArtikelClickListener);

        rv_numbers.setAdapter(recyclerPaymentBill);
    }

    public void gotoRiwayatPembayaran(View view) {
        Intent intent = new Intent(ChooseSantriPaymentActivity.this, RiwayatPembayaranActivity.class);
        startActivity(intent);
    }

    public void gotoMakePayment(View view) {
        Intent intent = new Intent(ChooseSantriPaymentActivity.this, MakePaymentActivity.class);
        startActivityForResult(intent, 1);
    }
}
