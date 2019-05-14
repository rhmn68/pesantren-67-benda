package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;

import com.madrasahdigital.walisantri.ppi67benda.R;

public class PresenceActivity extends AppCompatActivity {

    private final String TAG = PresenceActivity.class.getSimpleName();

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(getResources().getString(R.string.Presence));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(PresenceActivity.this, R.color.colorPrimaryDark));
        }

//        spinner = findViewById(R.id.spinner);
    }
}
