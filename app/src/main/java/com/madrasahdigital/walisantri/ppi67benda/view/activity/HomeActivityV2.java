package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.allsantri.AllSantri;
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.NewsModel;
import com.madrasahdigital.walisantri.ppi67benda.model.notification.NotificationModel;
import com.madrasahdigital.walisantri.ppi67benda.model.presence.Presensi;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanallsantri.TagihanAllSantriModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.addsantri.AddSantriActivity;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.addsantri.WelcomeMsgAddSantri;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.payment.ChooseSantriPaymentActivity;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.presence.ChooseSantriPresenceActivity;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.presence.PresenceActivityV2;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerNewsHome;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerPresenceHome;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.LogoutDialog;

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
    private List<Presensi> presenceList;
    private List<NotificationModel> notificationModelList;
    private NewsModel newsModel;
    private ProgressBar progressBarToday;
    private ProgressBar progressBarNews;
    private ImageView ivRefreshPresenceToday;
    private RecyclerView rv_presence_today;
    private RecyclerView rv_news;
    private RecyclerPresenceHome recyclerListChat;
    private RecyclerPresenceHome.OnArtikelClickListener onArtikelClickListener;
    private RecyclerNewsHome recyclerNewsHome;
    private RecyclerNewsHome.OnArtikelClickListener onArtikelClickListenerNewsHome;
    private TextView tvBelumAdaSantri;
    private TextView tvTitleToday;
    private TextView tvTotalTagihan;
    private TextView tvTextTagihanPembayaran;
    private Button btnTambahkanSantri;
    private Button btnLogin;
    private NavigationView navigationView;
    private RelativeLayout rellayTotalTagihan;
    private LinearLayout linlayWelcomeNotLogin;
    private LinearLayout linlayInfoSantri;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isThreadPresenceWork = true;
    private boolean isThreadTotalTagihanWork = true;

    private final int TYPE_LOAD_PRESENCE_TODAY = 0;
    private final int TYPE_DONE_LOAD_PRESENCE_TODAY = 1;
    private final int TYPE_FAIL_PRESENCE_TODAY = 2;
    private final int TYPE_NO_SANTRI_PRESENCE_TODAY = 3;
    private final int TYPE_NOT_LOGGED_IN = 4;
    private final int TYPE_LOGGED_IN = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_v2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBarToday = findViewById(R.id.progressBarToday);
        rv_news = findViewById(R.id.rv_news);
        linlayWelcomeNotLogin = findViewById(R.id.linlayWelcomeNotLogin);
        linlayInfoSantri = findViewById(R.id.linlayInfoSantri);
        rellayTotalTagihan = findViewById(R.id.rellayTotalTagihan);
        ivRefreshPresenceToday = findViewById(R.id.ivRefreshPresenceToday);
        rv_presence_today = findViewById(R.id.rv_presence_today);
        tvBelumAdaSantri = findViewById(R.id.tvBelumAdaSantri);
        btnTambahkanSantri = findViewById(R.id.btnTambahkanSantri);
        btnLogin = findViewById(R.id.btnLogin);
        progressBarNews = findViewById(R.id.progressBarNews);
        tvTitleToday = findViewById(R.id.tvTitleToday);
        tvTotalTagihan = findViewById(R.id.tvTotalTagihan);
        tvTextTagihanPembayaran = findViewById(R.id.tvTextTagihanPembayaran);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        setSupportActionBar(toolbar);

        sharedPrefManager = new SharedPrefManager(HomeActivityV2.this);

        initializeListener();
        if (sharedPrefManager.isLoggedIn()) {
            setView(TYPE_LOGGED_IN);
            allSantri = sharedPrefManager.getAllSantri();
            String titleToday = getResources().getString(R.string.home_text1) + UtilsManager.getTodayDateStringMonthLinguistik(HomeActivityV2.this);
            tvTitleToday.setText(titleToday);
            swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.MAGENTA);

            new GetDataSantri().execute();
            new GetTagihanAlLSantri().execute();
        } else {
            setView(TYPE_NOT_LOGGED_IN);
        }

        new GetNews().execute();
    }

    private void initializeListener() {

        btnLogin.setOnClickListener(l -> {

        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            if (!isThreadPresenceWork) {
                new GetDataSantri().execute();
            }
            if (!isThreadTotalTagihanWork)
                new GetTagihanAlLSantri().execute();
        });

        ivRefreshPresenceToday.setOnClickListener(l -> new GetDataSantri().execute());

        rellayTotalTagihan.setOnClickListener(l -> {
            Intent intent = new Intent(HomeActivityV2.this, ChooseSantriPaymentActivity.class);
            startActivity(intent);
        });

        onArtikelClickListenerNewsHome = (posisi, newsModel) -> {
            Intent intent = new Intent(HomeActivityV2.this, DetailNewsActivity.class);
            intent.putExtra("urlberita", newsModel.getUrl());
            startActivity(intent);
        };

        onArtikelClickListener = (posisi, presence) -> {
            Intent intent = new Intent(HomeActivityV2.this, PresenceActivityV2.class);
            intent.putExtra("namasantri", presence.getSantriName());
            intent.putExtra("idsantri", presence.getId());
            startActivity(intent);
        };

        findViewById(R.id.drawer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open right drawer
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.END);
            }
        });

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnTambahkanSantri.setOnClickListener(l -> {
            Intent intent;
            if (allSantri.getTotal() == 0) {
                intent = new Intent(HomeActivityV2.this, WelcomeMsgAddSantri.class);
            } else {
                intent = new Intent(HomeActivityV2.this, AddSantriActivity.class);
            }
            startActivity(intent);
            finish();
        });

        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setCheckable(false);
        }
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

        if (id == R.id.nav_presensi) {
            Intent intent = new Intent(HomeActivityV2.this, ChooseSantriPresenceActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_payment) {
            Intent intent = new Intent(HomeActivityV2.this, ChooseSantriPaymentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_news) {
            Intent intent = new Intent(HomeActivityV2.this, NewsFromPesantrenActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_add_santri) {
            Intent intent;
            if (allSantri.getTotal() == 0) {
                intent = new Intent(HomeActivityV2.this, WelcomeMsgAddSantri.class);
            } else {
                intent = new Intent(HomeActivityV2.this, AddSantriActivity.class);
            }
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(HomeActivityV2.this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            LogoutDialog logoutDialog = new LogoutDialog(HomeActivityV2.this);
            logoutDialog.show();
            logoutDialog.setDialogResult(result -> {
                if (result) {
                    sharedPrefManager.resetData();
                    Intent intent = new Intent(HomeActivityV2.this, SplashScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    HomeActivityV2.this.finish();
                }
            });
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

    private void initializationOfNewsViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_news.setLayoutManager(mLinearLayoutManager);
        rv_news.setHasFixedSize(true);

        recyclerNewsHome = new RecyclerNewsHome(HomeActivityV2.this, newsModel.getPosts(), Constant.TYPE_NEWS_HOME);
        recyclerNewsHome.setOnArtikelClickListener(onArtikelClickListenerNewsHome);

        rv_news.setAdapter(recyclerNewsHome);
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
        } else if (type == TYPE_NOT_LOGGED_IN) {
            linlayInfoSantri.setVisibility(View.GONE);
            linlayWelcomeNotLogin.setVisibility(View.VISIBLE);
        } else if (type == TYPE_LOGGED_IN) {
            linlayInfoSantri.setVisibility(View.VISIBLE);
            linlayWelcomeNotLogin.setVisibility(View.GONE);
        }
    }

    private class GetTagihanAlLSantri extends AsyncTask<Void, Integer, Boolean> {

        private TagihanAllSantriModel tagihanAllSantriModel;
        private boolean status = false;

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_TAGIHAN_ALL_SANTRI)
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                int statusCode = response.code();

                if (statusCode == 200) {
                    status = true;
                    Gson gson = new Gson();
                    tagihanAllSantriModel =
                            gson.fromJson(bodyString, TagihanAllSantriModel.class);
                }

                return status;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return status;
        }

        @Override
        protected void onPreExecute() {
            isThreadTotalTagihanWork = true;
            if (sharedPrefManager.getTotalTagihan() != null) {
                rellayTotalTagihan.setVisibility(View.VISIBLE);
                tvTextTagihanPembayaran.setVisibility(View.VISIBLE);
                Log.d(TAG, "total tagihan : " + sharedPrefManager.getTotalTagihan());
                String tot = "Rp " + UtilsManager.convertLongToCurrencyIDv2WithoutRp(Double.valueOf(sharedPrefManager.getTotalTagihan()));
                tvTotalTagihan.setText(tot);
            } else {
                rellayTotalTagihan.setVisibility(View.GONE);
                tvTextTagihanPembayaran.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            isThreadTotalTagihanWork = false;
            if (isSuccess) {
                rellayTotalTagihan.setVisibility(View.VISIBLE);
                tvTextTagihanPembayaran.setVisibility(View.VISIBLE);
                sharedPrefManager.saveTagihanAllSantri(tagihanAllSantriModel);
                sharedPrefManager.setTotalTagihan(String.valueOf(tagihanAllSantriModel.getTotal()));
                String tot = "Rp " + UtilsManager.convertLongToCurrencyIDv2WithoutRp(tagihanAllSantriModel.getTotal());
                tvTotalTagihan.setText(tot);
            } else {
                sharedPrefManager.saveTagihanAllSantri(null);
                rellayTotalTagihan.setVisibility(View.GONE);
                tvTextTagihanPembayaran.setVisibility(View.GONE);
            }
        }
    }

    private class GetNews extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_NEWS)
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                newsModel = gson.fromJson(bodyString, NewsModel.class);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPreExecute() {
            progressBarNews.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            progressBarNews.setVisibility(View.GONE);
            if (isSuccess) {
                initializationOfNewsViewer();
            } else {
                UtilsManager.showToast(HomeActivityV2.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }

    private class GetPresenceToday extends AsyncTask<Void, Integer, Boolean> {

        private String date;
        private Presensi presence;
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
                presence = gson.fromJson(bodyString, Presensi.class);

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
                presence.setId(id);
                presence.setSantriName(allSantri.getSantri().get(i).getFullname());
                if (allSantri.getSantri().get(i).getPhoto() != null)
                    presence.setUrlPhoto(allSantri.getSantri().get(i).getPhoto().toString());
                presenceList.add(presence);
                Log.d(TAG, "call GetPresenceToday santri : " + presence.getSantriName());
                if (presenceList.size() == allSantri.getTotal()) {
                    Log.d(TAG, "call GetPresenceToday total");
                    isThreadPresenceWork = false;
                    setView(TYPE_DONE_LOAD_PRESENCE_TODAY);
                    initializationOfPresenceViewer();
                }
            } else {
                isThreadPresenceWork = false;
                setView(TYPE_FAIL_PRESENCE_TODAY);
            }
        }
    }

    private class GetDataSantri extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            presenceList = new ArrayList<>();
            isThreadPresenceWork = true;
            setView(TYPE_LOAD_PRESENCE_TODAY);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_ALL_SANTRI)
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                allSantri =
                        gson.fromJson(bodyString, AllSantri.class);

                sharedPrefManager.saveAllSantri(allSantri);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if (isSuccess) {
                if (allSantri.getTotal() == 0) {
                    isThreadPresenceWork = false;
                    Log.d(TAG, "call GetDatasantri : no santri");
                    setView(TYPE_NO_SANTRI_PRESENCE_TODAY);
                } else {
                    for (int i = 0; i < allSantri.getTotal(); i++) {
                        Log.d(TAG, "call GetDataSantri ke " + i);
                        new GetPresenceToday(UtilsManager.getTodayDateString(), allSantri.getSantri().get(i).getId()).execute();
                    }
                }
            } else {
                isThreadPresenceWork = false;
                UtilsManager.showToast(HomeActivityV2.this, "Periksa koneksi anda");
            }
        }
    }
}