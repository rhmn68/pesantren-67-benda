package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.allsantri.AllSantri;
import com.madrasahdigital.walisantri.ppi67benda.model.presence.Presence;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.LogoutDialog;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.SantriChooserDialog;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.SettingDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HomeActivity extends AppCompatActivity {

    private final String TAG = HomeActivity.class.getSimpleName();

    private SharedPrefManager sharedPrefManager;
    private AllSantri allSantri;
    private LinearLayout linlayName;
    private RelativeLayout rellayTglHariIni;
    private RelativeLayout rellayTodayDetails;
    private SantriChooserDialog santriChooserDialog;
    private TextView tvSantriActive;
    private TextView tvFirstCharForImageProfil;
    private TextView tvDatePresenceToday;
    private TextView tvStatusPresenceToday;
    private ProgressBar progressBar;
    private ImageView ivRefresh;
    private ImageView ivSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        linlayName = findViewById(R.id.linlayName);
        rellayTodayDetails = findViewById(R.id.rellayTodayDetails);
        rellayTglHariIni = findViewById(R.id.rellayTglHariIni);
        tvSantriActive = findViewById(R.id.tvSantriActive);
        progressBar = findViewById(R.id.progressBar);
        ivRefresh = findViewById(R.id.ivRefresh);
        ivSetting = findViewById(R.id.ivSetting);
        tvStatusPresenceToday = findViewById(R.id.tvStatusPresenceToday);
        tvDatePresenceToday = findViewById(R.id.tvDatePresenceToday);
        tvFirstCharForImageProfil = findViewById(R.id.tvFirstCharForImageProfil);
        sharedPrefManager = new SharedPrefManager(HomeActivity.this);
        allSantri = sharedPrefManager.getAllSantri();
        santriChooserDialog = new SantriChooserDialog(HomeActivity.this, allSantri.getSantri());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(HomeActivity.this, R.color.colorPrimaryDark));
        }

        getPresenceStatusToday();
        setNameAndImageProfil(sharedPrefManager.getIdActiveSantriInHomePage());

        linlayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                santriChooserDialog.show();
            }
        });

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenceStatusToday();
            }
        });

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SettingDialog settingDialog = new SettingDialog(HomeActivity.this);
                settingDialog.show();
                settingDialog.setDialogResult(new SettingDialog.OnMyDialogResult() {
                    @Override
                    public void finish(String buttonClicked) {
                        if (buttonClicked.equals("logout")) {
                            settingDialog.dismiss();
                            LogoutDialog logoutDialog = new LogoutDialog(HomeActivity.this);
                            logoutDialog.show();
                            logoutDialog.setDialogResult(new LogoutDialog.OnMyDialogResult() {
                                @Override
                                public void finish(boolean result) {
                                    if (result) {
                                        sharedPrefManager.resetData();
                                        Intent intent = new Intent(HomeActivity.this, SplashScreen.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        HomeActivity.this.finish();
                                    }
                                }
                            });
                        }
                    }
                });
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

    private void getPresenceStatusToday() {
        new GetPresenceToday(UtilsManager.getTodayDateString()).execute();
    }

    public void gotoPresence(View view) {
        Intent intent = new Intent(HomeActivity.this, PresenceActivity.class);
        startActivity(intent);
    }

    public void gotoFinance(View view) {
        Intent intent = new Intent(HomeActivity.this, FinanceActivity.class);
        startActivity(intent);
    }

    public void gotoAnnounce(View view) {
        Intent intent = new Intent(HomeActivity.this, NotifActivity.class);
        startActivity(intent);
    }

    private class GetPresenceToday extends AsyncTask<Void, Integer, Boolean> {

        private String date;
        private Presence presence;

        public GetPresenceToday(String date) {
            this.date = date;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            ivRefresh.setVisibility(View.GONE);
            rellayTglHariIni.setVisibility(View.GONE);
            rellayTodayDetails.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_PRESENCE_TODAY + date)
                    .get()
                    .addHeader("Authorization", sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                presence = gson.fromJson(bodyString, Presence.class);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            progressBar.setVisibility(View.GONE);

            if (isSuccess) {
                rellayTglHariIni.setVisibility(View.VISIBLE);
                rellayTodayDetails.setVisibility(View.VISIBLE);
                ivRefresh.setVisibility(View.GONE);
                DateFormat dayFormat = new SimpleDateFormat("dd");
                DateFormat monthFormat = new SimpleDateFormat("MM");
                Date date = new Date();
                String dateToday = dayFormat.format(date) + "\n" + UtilsManager.getMonthFromNumber(HomeActivity.this, monthFormat.format(date));
                tvDatePresenceToday.setText(dateToday);
                tvStatusPresenceToday.setText(presence.getStatus());
            } else {
                ivRefresh.setVisibility(View.VISIBLE);
            }
        }
    }
}
