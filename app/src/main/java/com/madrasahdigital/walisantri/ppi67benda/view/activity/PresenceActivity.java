package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.madrasahdigital.walisantri.ppi67benda.R;

public class PresenceActivity extends AppCompatActivity {

    private final String TAG = PresenceActivity.class.getSimpleName();

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence);

        spinner = findViewById(R.id.spinner);
    }
}
