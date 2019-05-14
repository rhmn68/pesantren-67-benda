package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.allsantri.AllSantri;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.SantriChooserDialog;

public class HomeActivity extends AppCompatActivity {

    private final String TAG = HomeActivity.class.getSimpleName();

    private SharedPrefManager sharedPrefManager;
    private AllSantri allSantri;
    private LinearLayout linlayName;
    private SantriChooserDialog santriChooserDialog;
    private TextView tvSantriActive;
    private TextView tvFirstCharForImageProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        linlayName = findViewById(R.id.linlayName);
        tvSantriActive = findViewById(R.id.tvSantriActive);
        tvFirstCharForImageProfil = findViewById(R.id.tvFirstCharForImageProfil);
        sharedPrefManager = new SharedPrefManager(HomeActivity.this);
        allSantri = sharedPrefManager.getAllSantri();
        santriChooserDialog = new SantriChooserDialog(HomeActivity.this, allSantri.getSantri());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(HomeActivity.this, R.color.colorPrimaryDark));
        }

        setNameAndImageProfil(sharedPrefManager.getIdActiveSantriInHomePage());

        linlayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                santriChooserDialog.show();
            }
        });

        santriChooserDialog.setDialogResult(new SantriChooserDialog.OnMyDialogResult() {
            @Override
            public void finish(String id) {
                sharedPrefManager.setIdActiveSantriInHomePage(id);
                setNameAndImageProfil(id);
            }
        });
    }

    private void setNameAndImageProfil(String id) {
        for (int i=0;i<allSantri.getTotal();i++) {
            if (allSantri.getSantri().get(i).getId().equals(id)) {
                if (allSantri.getSantri().get(i).getFullname().length() < 30)
                    tvSantriActive.setText(allSantri.getSantri().get(i).getFullname());
                else
                    tvSantriActive.setText(allSantri.getSantri().get(i).getFullname().substring(0,30));

                if (allSantri.getSantri().get(i).getFullname().length() > 1) {
                    String firstCharName = allSantri.getSantri().get(i).getFullname().charAt(0) + "";
                    tvFirstCharForImageProfil.setText(firstCharName);
                }
            }
        }
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
