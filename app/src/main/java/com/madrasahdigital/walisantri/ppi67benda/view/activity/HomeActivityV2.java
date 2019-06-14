package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.allsantri.AllSantri;
import com.madrasahdigital.walisantri.ppi67benda.model.presence.Presence;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.addsantri.AddSantriActivity;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.addsantri.WelcomeMsgAddSantri;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerPresenceHome;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HomeActivityV2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = HomeActivityV2.class.getSimpleName();
    private SharedPrefManager sharedPrefManager;
    private AllSantri allSantri;
    private List<Presence> presenceList;
    private ProgressBar progressBarToday;
    private ImageView ivRefreshPresenceToday;
    private RecyclerView rv_presence_today;
    private RecyclerPresenceHome recyclerListChat;
    private RecyclerPresenceHome.OnArtikelClickListener onArtikelClickListener;
    private TextView tvBelumAdaSantri;
    private Button btnTambahkanSantri;

    private final int TYPE_LOAD_PRESENCE_TODAY = 0;
    private final int TYPE_DONE_LOAD_PRESENCE_TODAY = 1;
    private final int TYPE_FAIL_PRESENCE_TODAY = 2;
    private final int TYPE_NO_SANTRI_PRESENCE_TODAY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_v2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBarToday = findViewById(R.id.progressBarToday);
        ivRefreshPresenceToday = findViewById(R.id.ivRefreshPresenceToday);
        rv_presence_today = findViewById(R.id.rv_presence_today);
        tvBelumAdaSantri = findViewById(R.id.tvBelumAdaSantri);
        btnTambahkanSantri = findViewById(R.id.btnTambahkanSantri);
        setSupportActionBar(toolbar);

        sharedPrefManager = new SharedPrefManager(HomeActivityV2.this);
        allSantri = sharedPrefManager.getAllSantri();
        presenceList = new ArrayList<>();

        initializeListener();

        if (allSantri.getTotal() == 0) {
           setView(TYPE_NO_SANTRI_PRESENCE_TODAY);
        } else {
            for (int i=0;i<allSantri.getTotal();i++) {
                new GetPresenceToday(UtilsManager.getTodayDateString(), allSantri.getSantri().get(i).getId()).execute();
            }
        }


    }

    private void initializeListener() {
        onArtikelClickListener = (posisi, presence) -> {

        };

        findViewById(R.id.drawer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open right drawer
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.END);
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnTambahkanSantri.setOnClickListener(l -> {
            Intent intent;
            if (allSantri.getTotal() == 0) {
                intent = new Intent(HomeActivityV2.this, WelcomeMsgAddSantri.class);
            } else {
                intent = new Intent(HomeActivityV2.this, AddSantriActivity.class);
            }
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_activity_v2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_presensi) {

        } else if (id == R.id.nav_payment) {

        } else if (id == R.id.nav_news) {

        } else if (id == R.id.nav_add_santri) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    private void initializationOfPresenceViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(HomeActivityV2.this);
        rv_presence_today.setLayoutManager(mLinearLayoutManager);
        rv_presence_today.setHasFixedSize(true);

        recyclerListChat = new RecyclerPresenceHome(HomeActivityV2.this, presenceList);
        recyclerListChat.setOnArtikelClickListener(onArtikelClickListener);

        rv_presence_today.setAdapter(recyclerListChat);
    }

    private void setView(int type) {
        if (type == TYPE_LOAD_PRESENCE_TODAY) {
            btnTambahkanSantri.setVisibility(View.GONE);
            tvBelumAdaSantri.setVisibility(View.GONE);
            rv_presence_today.setVisibility(View.GONE);
            progressBarToday.setVisibility(View.VISIBLE);
            ivRefreshPresenceToday.setVisibility(View.GONE);
        } else if (type == TYPE_NO_SANTRI_PRESENCE_TODAY) {
            btnTambahkanSantri.setVisibility(View.VISIBLE);
            tvBelumAdaSantri.setVisibility(View.VISIBLE);
            rv_presence_today.setVisibility(View.GONE);
            progressBarToday.setVisibility(View.GONE);
            ivRefreshPresenceToday.setVisibility(View.GONE);
        } else if (type == TYPE_DONE_LOAD_PRESENCE_TODAY) {
            btnTambahkanSantri.setVisibility(View.GONE);
            tvBelumAdaSantri.setVisibility(View.GONE);
            rv_presence_today.setVisibility(View.VISIBLE);
            progressBarToday.setVisibility(View.GONE);
            ivRefreshPresenceToday.setVisibility(View.GONE);
        } else if (type == TYPE_FAIL_PRESENCE_TODAY) {
            btnTambahkanSantri.setVisibility(View.GONE);
            tvBelumAdaSantri.setVisibility(View.GONE);
            rv_presence_today.setVisibility(View.GONE);
            progressBarToday.setVisibility(View.GONE);
            ivRefreshPresenceToday.setVisibility(View.VISIBLE);
        }
    }


    private class GetPresenceToday extends AsyncTask<Void, Integer, Boolean> {

        private String date;
        private Presence presence;
        private String id;

        public GetPresenceToday(String date, String id) {
            this.date = date;
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            setView(TYPE_LOAD_PRESENCE_TODAY);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_PRESENCE_TODAY_2.replace("$", id) + date)
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
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
            if (isSuccess) {
                int i = 0;
                while (i < allSantri.getTotal() && !allSantri.getSantri().get(i).getId().equals(id)) {
                    i++;
                }
                presence.setSantriName(allSantri.getSantri().get(i).getFullname());
                presenceList.add(presence);
                if (presenceList.size() == allSantri.getTotal()) {
                    setView(TYPE_DONE_LOAD_PRESENCE_TODAY);
                    initializationOfPresenceViewer();
                }
            } else {
                setView(TYPE_FAIL_PRESENCE_TODAY);
            }
        }
    }
}
