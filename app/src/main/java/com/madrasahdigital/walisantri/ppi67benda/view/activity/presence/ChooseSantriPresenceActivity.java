package com.madrasahdigital.walisantri.ppi67benda.view.activity.presence;

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
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerChooseSantri;

public class ChooseSantriPresenceActivity extends AppCompatActivity {

    private static String TAG = ChooseSantriPresenceActivity.class.getSimpleName();
    private ActionBar aksibar;
    private SharedPrefManager sharedPrefManager;
    private AllSantri allSantri;
    private TextView tvKeteranganTidakAdaSantri;
    private RecyclerView rv_numbers;
    private RecyclerChooseSantri recyclerChooseSantri;
    private RecyclerChooseSantri.OnArtikelClickListener onArtikelClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_santri_presence);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        tvKeteranganTidakAdaSantri = findViewById(R.id.tvKeteranganTidakAdaSantri);
        rv_numbers = findViewById(R.id.rv_numbers);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(ChooseSantriPresenceActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = ChooseSantriPresenceActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        sharedPrefManager = new SharedPrefManager(ChooseSantriPresenceActivity.this);
        allSantri = sharedPrefManager.getAllSantri();

        if (allSantri.getTotal() == 0) {
            tvKeteranganTidakAdaSantri.setVisibility(View.VISIBLE);
            rv_numbers.setVisibility(View.GONE);
        } else {
            tvKeteranganTidakAdaSantri.setVisibility(View.GONE);
            rv_numbers.setVisibility(View.VISIBLE);
        }

        initializationOfListener();
        initializationOfPresenceViewer();
    }

    private void initializationOfListener() {
        onArtikelClickListener = (posisi, santri) -> {
            Intent intent = new Intent(ChooseSantriPresenceActivity.this, PresenceActivityV2.class);
            intent.putExtra("namasantri", santri.getFullname());
            startActivity(intent);
        };
    }

    private void initializationOfPresenceViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(ChooseSantriPresenceActivity.this);
        rv_numbers.setLayoutManager(mLinearLayoutManager);
        rv_numbers.setHasFixedSize(true);

        recyclerChooseSantri = new RecyclerChooseSantri(ChooseSantriPresenceActivity.this, allSantri.getSantri());
        recyclerChooseSantri.setOnArtikelClickListener(onArtikelClickListener);

        rv_numbers.setAdapter(recyclerChooseSantri);
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
