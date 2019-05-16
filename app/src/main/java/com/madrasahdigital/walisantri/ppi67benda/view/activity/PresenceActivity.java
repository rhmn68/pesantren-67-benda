package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.presence.Presence;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PresenceActivity extends AppCompatActivity {

    private final String TAG = PresenceActivity.class.getSimpleName();

    private Spinner spinner;
    private ProgressBar progressBarToday;
    private ProgressBar progressBarYesterday;
    private ImageView ivRefreshToday;
    private ImageView ivRefreshYesterday;
    private TextView tvDateToday;
    private TextView tvDateYesterday;
    private TextView tvStatusPresenceToday;
    private TextView tvStatusPresenceYesterday;
    private RelativeLayout rellayTglHariIni;
    private RelativeLayout rellayTglYesterday;
    private RelativeLayout rellayInfoPresenceToday;
    private RelativeLayout rellayInfoPresenceYesterday;
    private SharedPrefManager sharedPrefManager;

    private final int TODAY = 0;
    private final int YESTERDAY = 1;
    private boolean isActivityActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence);

        spinner = findViewById(R.id.spinner);
        progressBarToday = findViewById(R.id.progressBarToday);
        progressBarYesterday = findViewById(R.id.progressBarYesterday);
        ivRefreshToday = findViewById(R.id.ivRefreshToday);
        ivRefreshYesterday = findViewById(R.id.ivRefreshYesterday);
        tvDateToday = findViewById(R.id.tvDateToday);
        tvDateYesterday = findViewById(R.id.tvDateYesterday);
        tvStatusPresenceToday = findViewById(R.id.tvStatusPresenceToday);
        tvStatusPresenceYesterday = findViewById(R.id.tvStatusPresenceYesterday);
        rellayTglHariIni = findViewById(R.id.rellayTglHariIni);
        rellayTglYesterday = findViewById(R.id.rellayTglYesterday);
        rellayInfoPresenceToday = findViewById(R.id.rellayInfoPresenceToday);
        rellayInfoPresenceYesterday = findViewById(R.id.rellayInfoPresenceYesterday);
        sharedPrefManager = new SharedPrefManager(PresenceActivity.this);
        isActivityActive = true;

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(getResources().getString(R.string.Presence));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(PresenceActivity.this, R.color.colorPrimaryDark));
        }

        getPresenceStatusToday();
        getPresenceStatusYesterday();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, " onResume");
        isActivityActive = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, " onPause");
        isActivityActive = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, " onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, " onDestroyView");
        isActivityActive = false;
    }

    private void getPresenceStatusToday() {
        new GetPresenceStatusByDate(UtilsManager.getTodayDateString(), TODAY).execute();
    }

    private void getPresenceStatusYesterday() {
        new GetPresenceStatusByDate(UtilsManager.getYesterdayDateString(), YESTERDAY).execute();
    }

    private class GetPresenceStatusByDate extends AsyncTask<Void, Integer, Boolean> {

        private String date;
        private Presence presence;
        private int statusDay;

        public GetPresenceStatusByDate(String date, int statusDay) {
            this.date = date;
            this.statusDay = statusDay;
        }

        @Override
        protected void onPreExecute() {
            if (isActivityActive) {
                if (statusDay == TODAY) {
                    progressBarToday.setVisibility(View.VISIBLE);
                    rellayTglHariIni.setVisibility(View.GONE);
                    rellayInfoPresenceToday.setVisibility(View.GONE);
                } else {
                    progressBarYesterday.setVisibility(View.VISIBLE);
                    rellayTglYesterday.setVisibility(View.GONE);
                    rellayInfoPresenceYesterday.setVisibility(View.GONE);
                }
            }
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
            if (isActivityActive) {
                if (isSuccess) {
                    DateFormat dayFormat = new SimpleDateFormat("dd");
                    DateFormat monthFormat = new SimpleDateFormat("MM");
                    Date date = new Date();

                    if (statusDay == TODAY) {
                        String dateToday = dayFormat.format(date) + "\n" + UtilsManager.getMonthFromNumber(PresenceActivity.this, monthFormat.format(date));
                        progressBarToday.setVisibility(View.GONE);
                        rellayTglHariIni.setVisibility(View.VISIBLE);
                        rellayInfoPresenceToday.setVisibility(View.VISIBLE);
                        ivRefreshToday.setVisibility(View.GONE);
                        tvDateToday.setText(dateToday);
                        tvStatusPresenceToday.setText(presence.getStatus());
                    } else {
                        String dateYesterday = dayFormat.format(UtilsManager.yesterday()) + "\n" + UtilsManager.getMonthFromNumber(PresenceActivity.this, monthFormat.format(date));
                        progressBarYesterday.setVisibility(View.GONE);
                        rellayTglYesterday.setVisibility(View.VISIBLE);
                        rellayInfoPresenceYesterday.setVisibility(View.VISIBLE);
                        ivRefreshYesterday.setVisibility(View.GONE);
                        tvDateYesterday.setText(dateYesterday);
                        tvStatusPresenceYesterday.setText(presence.getStatus());
                    }
                } else {
                    if (statusDay == TODAY) {
                        progressBarToday.setVisibility(View.GONE);
                        ivRefreshToday.setVisibility(View.VISIBLE);
                    } else {
                        progressBarYesterday.setVisibility(View.GONE);
                        ivRefreshYesterday.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
}
