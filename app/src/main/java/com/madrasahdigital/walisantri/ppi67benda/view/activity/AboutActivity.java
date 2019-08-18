package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.madrasahdigital.walisantri.ppi67benda.BuildConfig;
import com.madrasahdigital.walisantri.ppi67benda.R;

public class AboutActivity extends AppCompatActivity {

    private ActionBar aksibar;
    private TextView tvKodeVersi;
    private TextView tvVersiAplikasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(AboutActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = AboutActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        tvKodeVersi = findViewById(R.id.tvKodeVersi);
        tvVersiAplikasi = findViewById(R.id.tvVersiAplikasi);

        String versionCode = "Versi Kode : " + BuildConfig.VERSION_CODE;
        String versionName = "Versi Aplikasi : " + BuildConfig.VERSION_NAME;

        tvKodeVersi.setText(versionCode);
        tvVersiAplikasi.setText(versionName);

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
