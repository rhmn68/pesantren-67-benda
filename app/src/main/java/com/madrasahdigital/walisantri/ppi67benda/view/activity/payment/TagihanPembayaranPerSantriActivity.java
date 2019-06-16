package com.madrasahdigital.walisantri.ppi67benda.view.activity.payment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.madrasahdigital.walisantri.ppi67benda.R;

public class TagihanPembayaranPerSantriActivity extends AppCompatActivity {

    private ActionBar aksibar;
    private LinearLayout linlayTagihan1;
    private LinearLayout linlayTagihan2;
    private LinearLayout linlayTagihan3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagihan_pembayaran_per_santri);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        linlayTagihan1 = findViewById(R.id.linlayTagihan1);
        linlayTagihan2 = findViewById(R.id.linlayTagihan2);
        linlayTagihan3 = findViewById(R.id.linlayTagihan3);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(TagihanPembayaranPerSantriActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = TagihanPembayaranPerSantriActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        linlayTagihan1.setOnClickListener(l -> {
            Intent intent = new Intent(TagihanPembayaranPerSantriActivity.this, DetailTagihanActivity.class);
            startActivity(intent);
        });

        linlayTagihan2.setOnClickListener(l -> {
            Intent intent = new Intent(TagihanPembayaranPerSantriActivity.this, DetailTagihanActivity.class);
            startActivity(intent);
        });

        linlayTagihan3.setOnClickListener(l -> {
            Intent intent = new Intent(TagihanPembayaranPerSantriActivity.this, DetailTagihanActivity.class);
            startActivity(intent);
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
}
