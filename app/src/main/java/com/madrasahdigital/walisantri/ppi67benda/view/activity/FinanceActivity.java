package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.view.fragment.PaymentFragment;
import com.madrasahdigital.walisantri.ppi67benda.view.fragment.SppFragment;

public class FinanceActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_spp:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container,
                                    new SppFragment(),
                                    SppFragment.class.getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_payment:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container,
                                    new PaymentFragment(),
                                    PaymentFragment.class.getSimpleName())
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(FinanceActivity.this, R.color.colorPrimaryDark));
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container,
                        new SppFragment(),
                        SppFragment.class.getSimpleName())
                .commit();
    }

}
