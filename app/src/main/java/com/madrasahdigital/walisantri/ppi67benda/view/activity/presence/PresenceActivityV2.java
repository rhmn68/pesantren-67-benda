package com.madrasahdigital.walisantri.ppi67benda.view.activity.presence;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.presence.PresenceModel;
import com.madrasahdigital.walisantri.ppi67benda.model.presence.Presensi;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.DetailDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PresenceActivityV2 extends AppCompatActivity {

    private final String TAG = PresenceActivityV2.class.getSimpleName();

    private ProgressBar progressBarToday;
    private ProgressBar progressBarYesterday;
    private ImageView ivRefreshToday;
    private ImageView ivRefreshYesterday;
    private TextView tvDateToday;
    private TextView tvDateYesterday;
    private TextView tvStatusPresenceToday;
    private TextView tvStatusPresenceYesterday;
    private TextView tvSantriName;
    private TextView tvFirstCharForImageProfil;
    private RelativeLayout rellayTglHariIni;
    private RelativeLayout rellayTglYesterday;
    private RelativeLayout rellayInfoPresenceToday;
    private RelativeLayout rellayInfoPresenceYesterday;
    private SharedPrefManager sharedPrefManager;
    private CalendarView calendarView;
    private ProgressBar progressBar;
    private PresenceModel presenceModel;
    private List<Calendar> calendarList;
//    private List<Presensi> presence;
    private ActionBar aksibar;
    private String idSantri;

    private final int TODAY = 0;
    private final int YESTERDAY = 1;
    private boolean isActivityActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence_v2);
        progressBar = findViewById(R.id.progressBar);
        progressBarToday = findViewById(R.id.progressBarToday);
        progressBarYesterday = findViewById(R.id.progressBarYesterday);
        ivRefreshToday = findViewById(R.id.ivRefreshToday);
        ivRefreshYesterday = findViewById(R.id.ivRefreshYesterday);
        tvDateToday = findViewById(R.id.tvDateToday);
        tvDateYesterday = findViewById(R.id.tvDateYesterday);
        tvStatusPresenceToday = findViewById(R.id.tvStatusPresenceToday);
        tvSantriName = findViewById(R.id.tvSantriName);
        tvStatusPresenceYesterday = findViewById(R.id.tvStatusPresenceYesterday);
        tvFirstCharForImageProfil = findViewById(R.id.tvFirstCharForImageProfil);
        rellayTglHariIni = findViewById(R.id.rellayTglHariIni);
        rellayTglYesterday = findViewById(R.id.rellayTglYesterday);
        calendarView = findViewById(R.id.calendarView);
        rellayInfoPresenceToday = findViewById(R.id.rellayInfoPresenceToday);
        rellayInfoPresenceYesterday = findViewById(R.id.rellayInfoPresenceYesterday);
        sharedPrefManager = new SharedPrefManager(PresenceActivityV2.this);
        isActivityActive = true;
        calendarList = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(PresenceActivityV2.this, R.color.colorPrimaryDark));
        }

        aksibar = PresenceActivityV2.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String namaSantri = intent.getStringExtra("namasantri");
        idSantri = intent.getStringExtra("idsantri");
        tvSantriName.setText(namaSantri);
        if (namaSantri.length() > 0) {
            tvFirstCharForImageProfil.setText(namaSantri.substring(0,1));
        }

        initializeListener();

        getPresenceStatusToday();

        new GetPresenceByYearAndMonth(idSantri, UtilsManager.getYear(), UtilsManager.getMonth()).execute();
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
        ivRefreshToday.setOnClickListener(l -> getPresenceStatusToday());

        ivRefreshYesterday.setOnClickListener(l -> getPresenceStatusYesterday());

        calendarView.setOnDayClickListener((EventDay eventDay) -> {
            Calendar clickedDayCalendar = eventDay.getCalendar();

            if (calendarList.contains(clickedDayCalendar)) {
                int i = 0;
                while (!calendarList.get(i).equals(clickedDayCalendar)) {
                    i++;
                }
                String status = presenceModel.getPresensi().get(i).getStatus();
                String deskrip = presenceModel.getPresensi().get(i).getDescription();
                if (deskrip == null) deskrip = "";
                String deskripsi = "Petugas : " + presenceModel.getPresensi().get(i).getPetugas() + "\n" +
                                    "Deskripsi : " + deskrip;
                DetailDialog detailDialog;
                if (status.equals("present")) {
                    detailDialog = new DetailDialog(PresenceActivityV2.this,
                            "Hadir",
                            presenceModel.getPresensi().get(i).getDate(),
                            deskripsi);
                } else if (status.equals("ill")) {
                    detailDialog = new DetailDialog(PresenceActivityV2.this,
                            "Sakit",
                            presenceModel.getPresensi().get(i).getDate(),
                            deskripsi);
                } else {
                    detailDialog = new DetailDialog(PresenceActivityV2.this,
                            "Izin",
                            presenceModel.getPresensi().get(i).getDate(),
                            deskripsi);
                }

                detailDialog.show();

            }
        });
    }

    private void setStatusPresenceInCalendar() {
        List<EventDay> events = new ArrayList<>();

        Calendar calendar;

        for (int i = 0; i < presenceModel.getPresensi().size(); i++) {
            calendar = Calendar.getInstance();
            if (presenceModel.getPresensi().get(i).getStatus().equals("present")) {
                calendar.set(UtilsManager.getYearFromString(presenceModel.getPresensi().get(i).getDate()),
                        UtilsManager.getMonthFromString(presenceModel.getPresensi().get(i).getDate()) - 1,
                        UtilsManager.getDayFromString(presenceModel.getPresensi().get(i).getDate()));
                events.add(new EventDay(calendar, R.drawable.ic_thumb_up_green_24dp));
            } else if (presenceModel.getPresensi().get(i).getStatus().equals("ill")) {
                calendar.set(UtilsManager.getYearFromString(presenceModel.getPresensi().get(i).getDate()),
                        UtilsManager.getMonthFromString(presenceModel.getPresensi().get(i).getDate()) - 1,
                        UtilsManager.getDayFromString(presenceModel.getPresensi().get(i).getDate()));
                events.add(new EventDay(calendar, UtilsManager.getRotateDrawable(getResources().getDrawable(R.drawable.ic_add_circle_red_24dp), 45)));
            } else if (presenceModel.getPresensi().get(i).getStatus().equals("permit")) {
                calendar.set(UtilsManager.getYearFromString(presenceModel.getPresensi().get(i).getDate()),
                        UtilsManager.getMonthFromString(presenceModel.getPresensi().get(i).getDate()) - 1,
                        UtilsManager.getDayFromString(presenceModel.getPresensi().get(i).getDate()));
                events.add(new EventDay(calendar, UtilsManager.getRotateDrawable(getResources().getDrawable(R.drawable.ic_add_circle_red_24dp), 45)));
            }
            calendarList.add(calendar);
        }

        calendarView.setEvents(events);
    }

    private void getPresenceStatusToday() {
        new GetPresenceStatusByDate(idSantri, UtilsManager.getTodayDateString(), TODAY).execute();
    }

    private void getPresenceStatusYesterday() {
        new GetPresenceStatusByDate(idSantri, UtilsManager.getYesterdayDateString(), YESTERDAY).execute();
    }

    private class GetPresenceByYearAndMonth extends AsyncTask<Void, Integer, Boolean> {

        private String id;
        private String year;
        private String month;

        public GetPresenceByYearAndMonth(String id, String year, String month) {
            this.id = id;
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
                    .url(Constant.LINK_GET_PRESENCE_SANTRI.replace("*", id).replace("$", year).replace("#", month))
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                presenceModel = gson.fromJson(bodyString, PresenceModel.class);

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
                setStatusPresenceInCalendar();
            }
        }
    }

    private class GetPresenceStatusByDate extends AsyncTask<Void, Integer, Boolean> {

        private String id;
        private String date;
        private Presensi presence;
        private int statusDay;

        public GetPresenceStatusByDate(String id, String date, int statusDay) {
            this.id = id;
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
                    .url(Constant.LINK_GET_PRESENCE_TODAY.replace("*", id) + date)
                    .get()
                    .addHeader("Authorization", sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                presence = gson.fromJson(bodyString, Presensi.class);

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
                        String dateToday = dayFormat.format(date) + "\n" + UtilsManager.getMonthFromNumber(PresenceActivityV2.this, monthFormat.format(date));
                        progressBarToday.setVisibility(View.GONE);
                        rellayTglHariIni.setVisibility(View.VISIBLE);
                        rellayInfoPresenceToday.setVisibility(View.VISIBLE);
                        ivRefreshToday.setVisibility(View.GONE);
                        tvDateToday.setText(dateToday);
                        tvStatusPresenceToday.setText(presence.getStatus());
                    } else {
                        String dateYesterday = dayFormat.format(UtilsManager.yesterday()) + "\n" + UtilsManager.getMonthFromNumber(PresenceActivityV2.this, monthFormat.format(date));
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
