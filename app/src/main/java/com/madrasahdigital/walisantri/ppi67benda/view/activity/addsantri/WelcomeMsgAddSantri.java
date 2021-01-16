package com.madrasahdigital.walisantri.ppi67benda.view.activity.addsantri;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.HomeActivityV2;

public class WelcomeMsgAddSantri extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_msg_add_santri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(WelcomeMsgAddSantri.this, R.color.colorPrimaryDark));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(WelcomeMsgAddSantri.this, HomeActivityV2.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(WelcomeMsgAddSantri.this, HomeActivityV2.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoAddSantriActivity(View view) {
        Intent intent = new Intent(WelcomeMsgAddSantri.this, AddSantriActivity.class);
        startActivity(intent);
        finish();
    }
}
