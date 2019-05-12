package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.madrasahdigital.walisantri.ppi67benda.R;

public class HomeActivity extends AppCompatActivity {

    private final String TAG = HomeActivity.class.getSimpleName();

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        spinner = findViewById(R.id.spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.name_example, R.layout.spinner_text);
        spinner.setAdapter(adapter);
    }

    public void gotoPresence(View view) {
        Intent intent = new Intent(HomeActivity.this, PresenceActivity.class);
        startActivity(intent);
    }

    public void gotoFinance(View view) {
        Intent intent = new Intent(HomeActivity.this, FinanceActivity.class);
        startActivity(intent);
    }
}
