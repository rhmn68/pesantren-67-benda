package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applikeysolutions.cosmocalendar.listeners.OnMonthChangeListener;
import com.applikeysolutions.cosmocalendar.model.Month;
import com.applikeysolutions.cosmocalendar.settings.lists.connected_days.ConnectedDays;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.presence.Presence;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PresenceActivity extends AppCompatActivity {

    private final String TAG = PresenceActivity.class.getSimpleName();

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
    private CalendarView cosmoCalendar;
    private ProgressBar progressBar;

    private final int TODAY = 0;
    private final int YESTERDAY = 1;
    private boolean isActivityActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence);

        progressBar = findViewById(R.id.progressBar);
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
        cosmoCalendar = findViewById(R.id.cosmoCalendar);
        rellayInfoPresenceToday = findViewById(R.id.rellayInfoPresenceToday);
        rellayInfoPresenceYesterday = findViewById(R.id.rellayInfoPresenceYesterday);
        sharedPrefManager = new SharedPrefManager(PresenceActivity.this);
        isActivityActive = true;
        cosmoCalendar.setCalendarOrientation(OrientationHelper.HORIZONTAL);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(getResources().getString(R.string.Presence));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(PresenceActivity.this, R.color.colorPrimaryDark));
        }

        initializeListener();

        getPresenceStatusToday();

        new GetPresenceByYearAndMonth(UtilsManager.getYear(), UtilsManager.getMonth()).execute();
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

    private void initializeListener() {
        ivRefreshToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenceStatusToday();
            }
        });

        ivRefreshYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenceStatusYesterday();
            }
        });

        cosmoCalendar.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChanged(Month month) {
                Log.d(TAG, "month : " + month.getMonthName());
                String[] monthYear = month.getMonthName().split("\\s+"); // Ex : May 2019
                if (!UtilsManager.getMonthNumberFromMonthNameString(monthYear[0]).equals("-")) {
                    new GetPresenceByYearAndMonth(monthYear[1], monthYear[0]).execute();
                } else {
                    Log.e(TAG, "month not found!!!");
                }

            }
        });
    }

    private void setStatusPresenceInCalendar(List<Presence> presence) {

        Calendar calendar = new GregorianCalendar();
        Set<Long> daysPresent = new TreeSet<>();
        Set<Long> daysIll = new TreeSet<>();
        Set<Long> daysPermit = new TreeSet<>();
        ConnectedDays connectedDaysPresent;
        ConnectedDays connectedIll;
        ConnectedDays connectedPermit;

        for (int i=0;i<presence.size();i++) {
            Log.d(TAG, "year : " + UtilsManager.getYearFromString(presence.get(i).getDate()) +
                            " month : " + UtilsManager.getMonthFromString(presence.get(i).getDate()) +
                            " day : " + UtilsManager.getDayFromString(presence.get(i).getDate()) +
                            " status : " + presence.get(i).getStatus());
            if (presence.get(i).getStatus().equals("present")) {
                calendar.set(UtilsManager.getYearFromString(presence.get(i).getDate()),
                        UtilsManager.getMonthFromString(presence.get(i).getDate()) - 1,
                        UtilsManager.getDayFromString(presence.get(i).getDate()));
                daysPresent.add(calendar.getTimeInMillis());
            } else if (presence.get(i).getStatus().equals("ill")) {
                calendar.set(UtilsManager.getYearFromString(presence.get(i).getDate()),
                        UtilsManager.getMonthFromString(presence.get(i).getDate()) - 1,
                        UtilsManager.getDayFromString(presence.get(i).getDate()));
                daysIll.add(calendar.getTimeInMillis());
            } else if (presence.get(i).getStatus().equals("permit")) {
                calendar.set(UtilsManager.getYearFromString(presence.get(i).getDate()),
                        UtilsManager.getMonthFromString(presence.get(i).getDate()) - 1,
                        UtilsManager.getDayFromString(presence.get(i).getDate()));
                daysPermit.add(calendar.getTimeInMillis());
            }
            Log.d(TAG, "tanggalan : " + calendar.getTimeInMillis());
        }

        //Define Present
        int textColor = Color.parseColor("#449425");
        int selectedTextColor = Color.parseColor("#449425");
        int disabledTextColor = Color.parseColor("#ff8000");
        connectedDaysPresent = new ConnectedDays(daysPresent, textColor, selectedTextColor, disabledTextColor);

        // define ill
        textColor = Color.parseColor("#D32F2F");
        selectedTextColor = Color.parseColor("#D32F2F");
        disabledTextColor = Color.parseColor("#ff8000");
        connectedIll = new ConnectedDays(daysIll, textColor, selectedTextColor, disabledTextColor);

        // define permit
        textColor = Color.parseColor("#f9ca24");
        selectedTextColor = Color.parseColor("#f9ca24");
        disabledTextColor = Color.parseColor("#ff8000");
        connectedPermit = new ConnectedDays(daysPermit, textColor, selectedTextColor, disabledTextColor);

        //Connect days to calendar
//        cosmoCalendar.addConnectedDays(connectedDaysPresent);
        cosmoCalendar.addConnectedDays(connectedIll);
        cosmoCalendar.addConnectedDays(connectedPermit);
        cosmoCalendar.setConnectedDayIconRes(R.drawable.ic_add_circle_red_24dp);
    }

    private void getPresenceStatusToday() {
        new GetPresenceStatusByDate(UtilsManager.getTodayDateString(), TODAY).execute();
    }

    private void getPresenceStatusYesterday() {
        new GetPresenceStatusByDate(UtilsManager.getYesterdayDateString(), YESTERDAY).execute();
    }

    private class GetPresenceByYearAndMonth extends AsyncTask<Void, Integer, Boolean> {

        private String year;
        private String month;
        private List<Presence> presences;

        public GetPresenceByYearAndMonth(String year, String month) {
            this.year = year;
            this.month = month;
        }

        @Override
        protected void onPreExecute() {
            if (isActivityActive) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_PRESENCE_SANTRI.replace("$", year).replace("#", month))
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                JSONObject jsonObject = new JSONObject(bodyString);
                Gson gson = new Gson();
                presences = new ArrayList<>();
                int i = 1;
                while (jsonObject.has(String.valueOf(i))) {
                    Log.d(TAG, "date : " + jsonObject.getJSONObject(String.valueOf(i)).get("date"));
                    presences.add(gson.fromJson(jsonObject.getString(String.valueOf(i)), Presence.class) );
                    i++;
                }

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
                setStatusPresenceInCalendar(presences);
//                setSomething();
            }
        }
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
