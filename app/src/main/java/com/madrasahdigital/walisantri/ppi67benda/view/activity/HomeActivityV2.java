package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.BuildConfig;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.VersionCodeModel;
import com.madrasahdigital.walisantri.ppi67benda.model.allsantri.AllSantri;
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.NewsModel;
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.Post;
import com.madrasahdigital.walisantri.ppi67benda.model.notification.NotificationModel;
import com.madrasahdigital.walisantri.ppi67benda.model.presence.Presensi;
import com.madrasahdigital.walisantri.ppi67benda.model.slidebannermodel.Result;
import com.madrasahdigital.walisantri.ppi67benda.model.slidebannermodel.SlideBannerModel;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanallsantri.TagihanAllSantriModel;
import com.madrasahdigital.walisantri.ppi67benda.services.UpComingArticlesReceiver;
import com.madrasahdigital.walisantri.ppi67benda.utils.AlarmHelper;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.addsantri.AddSantriActivity;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.addsantri.WelcomeMsgAddSantri;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.payment.ChooseSantriPaymentActivity;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.presence.ChooseSantriPresenceActivity;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.presence.PresenceActivityV2;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerNewsHomeV2;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerPresenceHome;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.SlidingImageAdapter;
import com.madrasahdigital.walisantri.ppi67benda.view.dialog.LogoutDialog;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.madrasahdigital.walisantri.ppi67benda.utils.Constant.TIMEOUT;
import static com.madrasahdigital.walisantri.ppi67benda.utils.Constant.TIMEOUT_2;

public class HomeActivityV2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = HomeActivityV2.class.getSimpleName();
    private SharedPrefManager sharedPrefManager;
    private AllSantri allSantri;
    private List<Presensi> presenceList;
    private List<NotificationModel> notificationModelList;
    private NewsModel newsModel;
    private NewsModel newsModelWaliSantri;
    private ProgressBar progressBarToday;
    private ProgressBar progressBarNews;
    private ProgressBar progressBarNewsInfoWS;
    private ImageView ivRefreshPresenceToday;
    private RecyclerView rv_presence_today;
    private RecyclerView rv_news;
    private RecyclerView rv_news_info_ws;
    private RecyclerPresenceHome.OnArtikelClickListener onArtikelClickListener;
    private RecyclerNewsHomeV2.OnArtikelClickListener onArtikelClickListenerNewsHome;
    private RecyclerNewsHomeV2 recyclerNewsWaliSantriHome;
    private RecyclerNewsHomeV2.OnArtikelClickListener onWaliSantriClickListenerNewsHome;
    private TextView tvBelumAdaSantri;
    private TextView tvTotalTagihan;
    private TextView tvTextTagihanPembayaran;
    private Button btnTambahkanSantri;
    private Button btnLogin;
    private Button btnPerbarui;
    private RelativeLayout rellayTotalTagihan;
    private RelativeLayout rellayPerbarui;
    private RelativeLayout rellayBanner;
    private LinearLayout linlayWelcomeNotLogin;
    private LinearLayout linlayInfoSantri;
    private LinearLayout linlayTagihanPembayaran;
    private LinearLayout linlayInfoWaliSantri;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewPager mPager;
    private boolean isThreadPresenceWork = true;
    private boolean isThreadTotalTagihanWork = true;
    private boolean isThreadGetNewsWork = true;
    private boolean isThreadGetNewsInfoWSWork = true;
    private boolean isThreadGetImageBannerWork = true;

    private final int TYPE_LOAD_PRESENCE_TODAY = 0;
    private final int TYPE_DONE_LOAD_PRESENCE_TODAY = 1;
    private final int TYPE_FAIL_PRESENCE_TODAY = 2;
    private final int TYPE_NO_SANTRI_PRESENCE_TODAY = 3;
    private final int TYPE_NOT_LOGGED_IN = 4;
    private final int TYPE_LOGGED_IN = 5;
    private final int TYPE_SHOW_GET_LATEST_VERSION_APP = 6;
    private final int TYPE_HIDDEN_GET_LATEST_VERSION_APP = 7;

    private int currentPage = 0;
    private int NUM_PAGES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_v2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBarToday = findViewById(R.id.progressBarToday);
        rv_news = findViewById(R.id.rv_news);
        rv_news_info_ws = findViewById(R.id.rv_news_info_ws);
        mPager = findViewById(R.id.viewPagerBanner);
        linlayWelcomeNotLogin = findViewById(R.id.linlayWelcomeNotLogin);
        linlayInfoSantri = findViewById(R.id.linlayInfoSantri);
        linlayTagihanPembayaran = findViewById(R.id.linlayTagihanPembayaran);
        linlayInfoWaliSantri = findViewById(R.id.linlayInfoWaliSantri);
        rellayTotalTagihan = findViewById(R.id.rellayTotalTagihan);
        rellayPerbarui = findViewById(R.id.rellayPerbarui);
        rellayBanner = findViewById(R.id.rellayBanner);
        ivRefreshPresenceToday = findViewById(R.id.ivRefreshPresenceToday);
        rv_presence_today = findViewById(R.id.rv_presence_today);
        tvBelumAdaSantri = findViewById(R.id.tvBelumAdaSantri);
        btnTambahkanSantri = findViewById(R.id.btnTambahkanSantri);
        btnLogin = findViewById(R.id.btnLogin);
        btnPerbarui = findViewById(R.id.btnPerbarui);
        progressBarNews = findViewById(R.id.progressBarNews);
        progressBarNewsInfoWS = findViewById(R.id.progressBarNewsInfoWS);
        TextView tvTitleToday = findViewById(R.id.tvTitleToday);
        tvTotalTagihan = findViewById(R.id.tvTotalTagihan);
        tvTextTagihanPembayaran = findViewById(R.id.tvTextTagihanPembayaran);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        setSupportActionBar(toolbar);

        sharedPrefManager = new SharedPrefManager(HomeActivityV2.this);
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.MAGENTA);

        initializeListener();
        if (sharedPrefManager.isLoggedIn()) {
            setView(TYPE_LOGGED_IN);
            allSantri = sharedPrefManager.getAllSantri();
            String titleToday = getResources().getString(R.string.home_text1) + UtilsManager.getTodayDateStringMonthLinguistik(HomeActivityV2.this);
            tvTitleToday.setText(titleToday);

            new GetDataSantri().execute();
            new GetTagihanAlLSantri().execute();
            new GetInfoWaliSantri().execute();
        } else {
            setView(TYPE_NOT_LOGGED_IN);
        }

        if (sharedPrefManager.getStatusShowVersionApp()) {
            setView(TYPE_SHOW_GET_LATEST_VERSION_APP);
        } else {
            setView(TYPE_HIDDEN_GET_LATEST_VERSION_APP);
        }

        new GetLatestVersionCode().execute();
        new GetNews().execute();
        new GetImageBanner().execute();

        //Setup Firebase Messaging
        FirebaseMessaging.getInstance().subscribeToTopic("articles");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                   Log.d("coba", "token: "+task.getResult().getToken());
                });
    }

    private void initializeListener() {
        btnLogin.setOnClickListener(l -> {
            Intent intent = new Intent(HomeActivityV2.this, LoginActivity.class);
            startActivity(intent);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            if (sharedPrefManager.isLoggedIn()) {
                if (!isThreadPresenceWork)
                    new GetDataSantri().execute();
                if (!isThreadTotalTagihanWork)
                    new GetTagihanAlLSantri().execute();
                if (!isThreadGetNewsInfoWSWork)
                    new GetInfoWaliSantri().execute();
            }
            if (!isThreadGetNewsWork)
                new GetNews().execute();
            if (!isThreadGetImageBannerWork)
                new GetImageBanner().execute();
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

        onWaliSantriClickListenerNewsHome = (posisi, newsModel) -> {
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

        findViewById(R.id.drawer_button).setOnClickListener(v -> {
            // open right drawer
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.END);
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
            finish();
        });

        btnPerbarui.setOnClickListener(l -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.URL_APP_IN_PLAYSTORE)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.URL_APP_IN_PLAYSTORE)));
            }
        });

        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setCheckable(false);
            if (!sharedPrefManager.isLoggedIn() && (i == 5 || i == 6 || i == 7 || i == 8 || i == 10)) {
                // info wali santri, presensi, pembayaran, tambah santri, keluar
                navigationView.getMenu().getItem(i).setVisible(false);
            } else if (sharedPrefManager.isLoggedIn() && i == 9) {
                // login wali santri
                navigationView.getMenu().getItem(i).setVisible(false);
            }
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
        } else if (id == R.id.nav_info_wali_santri) {
            Intent intent = new Intent(HomeActivityV2.this, InfoWaliSantriActivity.class);
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
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(HomeActivityV2.this, LoginActivity.class);
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
        } else if (id == R.id.nav_info_pendaftaran) {
            Intent intent = new Intent(HomeActivityV2.this, InfoPendaftaranActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(HomeActivityV2.this, ProfilPesantrenActivity.class);
            startActivity(intent);
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

        RecyclerPresenceHome recyclerListChat = new RecyclerPresenceHome(HomeActivityV2.this, presenceList);
        recyclerListChat.setOnArtikelClickListener(onArtikelClickListener);

        rv_presence_today.setAdapter(recyclerListChat);
    }

    private void initializeSlideBanner(List<Result> articleList) {
        NUM_PAGES = articleList.size();
        final PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(NUM_PAGES); // specify total count of indicators
        mPager.setAdapter(new SlidingImageAdapter(HomeActivityV2.this, articleList.subList(0, NUM_PAGES)));
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            mPager.setCurrentItem(currentPage++, true);
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, 5000);

        // Pager listener over indicator
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });
    }

    private void initializationOfNewsWaliSantriViewer() {
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_news_info_ws.setLayoutManager(mLinearLayoutManager);
        rv_news_info_ws.setHasFixedSize(true);

        recyclerNewsWaliSantriHome = new RecyclerNewsHomeV2(HomeActivityV2.this, newsModelWaliSantri.getPosts(), Constant.TYPE_NEWS_HOME);
        recyclerNewsWaliSantriHome.setOnArtikelClickListener(onWaliSantriClickListenerNewsHome);

        rv_news_info_ws.setAdapter(recyclerNewsWaliSantriHome);
    }

    private void initializationOfNewsViewer() {
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_news.setLayoutManager(mLinearLayoutManager);
        rv_news.setHasFixedSize(true);

        RecyclerNewsHomeV2 recyclerNewsHome = new RecyclerNewsHomeV2(HomeActivityV2.this, newsModel.getPosts(), Constant.TYPE_NEWS_HOME);
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
            linlayWelcomeNotLogin.setVisibility(View.GONE);
            linlayTagihanPembayaran.setVisibility(View.GONE);
            linlayInfoWaliSantri.setVisibility(View.GONE);
        } else if (type == TYPE_LOGGED_IN) {
            linlayInfoSantri.setVisibility(View.VISIBLE);
            linlayWelcomeNotLogin.setVisibility(View.GONE);
            linlayTagihanPembayaran.setVisibility(View.VISIBLE);
            linlayInfoWaliSantri.setVisibility(View.VISIBLE);
        } else if (type == TYPE_SHOW_GET_LATEST_VERSION_APP) {
            rellayPerbarui.setVisibility(View.VISIBLE);
        } else if (type == TYPE_HIDDEN_GET_LATEST_VERSION_APP) {
            rellayPerbarui.setVisibility(View.GONE);
        }
    }

    public void gotoNews(View view) {
        Intent intent = new Intent(HomeActivityV2.this, NewsFromPesantrenActivity.class);
        startActivity(intent);
    }

    public void gotoInfoWaliSantri(View view) {
        Intent intent = new Intent(HomeActivityV2.this, InfoWaliSantriActivity.class);
        startActivity(intent);
    }

    private class GetLatestVersionCode extends AsyncTask<Void, Integer, Boolean> {

        private VersionCodeModel versionCodeModel;

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT_2, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_2, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_2, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_LATEST_VERSION)
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                int statusCode = response.code();

                if (statusCode == 200) {
                    Gson gson = new Gson();
                    versionCodeModel =
                            gson.fromJson(bodyString, VersionCodeModel.class);
                    return true;
                }
            } catch (Exception e) {
                Crashlytics.setString(TAG, "0-" + e.getMessage());
                Crashlytics.logException(e);
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if (isSuccess) {
                if (BuildConfig.VERSION_CODE < versionCodeModel.getValue()) {
                    sharedPrefManager.setShowGetLatestVersionApp(true);
                    setView(TYPE_SHOW_GET_LATEST_VERSION_APP);
                } else {
                    sharedPrefManager.setShowGetLatestVersionApp(false);
                    setView(TYPE_HIDDEN_GET_LATEST_VERSION_APP);
                }
            }
        }
    }

    private class GetTagihanAlLSantri extends AsyncTask<Void, Integer, Boolean> {

        private TagihanAllSantriModel tagihanAllSantriModel;
        private boolean status = false;

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

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
                Crashlytics.setString(TAG, "1-" + e.getMessage());
                Crashlytics.logException(e);
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

    private class GetImageBanner extends AsyncTask<Void, Integer, Boolean> {

        private SlideBannerModel slideBannerModel;

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_IMAGE_BANNER)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                slideBannerModel = gson.fromJson(bodyString, SlideBannerModel.class);

                return true;
            } catch (Exception e) {
                Crashlytics.setString(TAG, "2-" + e.getMessage());
                Crashlytics.logException(e);
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPreExecute() {
            isThreadGetImageBannerWork = true;
            rellayBanner.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            isThreadGetImageBannerWork = false;
            if (isSuccess) {
                rellayBanner.setVisibility(View.VISIBLE);
                initializeSlideBanner(slideBannerModel.getResults());
            } else {
                UtilsManager.showToast(HomeActivityV2.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }

    private class GetNews extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

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

                AlarmHelper.INSTANCE.startAlarmNewArticle(HomeActivityV2.this, bodyString);
//                if (!sharedPrefManager.isSetAlarm()){
//                    sharedPrefManager.setAlarm();
//                }

                return true;
            } catch (Exception e) {
                Crashlytics.setString(TAG, "2-" + e.getMessage());
                Crashlytics.logException(e);
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPreExecute() {
            isThreadGetNewsWork = true;
            progressBarNews.setVisibility(View.VISIBLE);
            rellayBanner.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            isThreadGetNewsWork = false;
            progressBarNews.setVisibility(View.GONE);
            if (isSuccess) {
                rellayBanner.setVisibility(View.VISIBLE);
                initializationOfNewsViewer();
            } else {
                UtilsManager.showToast(HomeActivityV2.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }

    private class GetInfoWaliSantri extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_INFO_WALI_SANTRI)
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                newsModelWaliSantri = gson.fromJson(bodyString, NewsModel.class);

                return true;
            } catch (Exception e) {
                Crashlytics.setString(TAG, "2-" + e.getMessage());
                Crashlytics.logException(e);
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPreExecute() {
            isThreadGetNewsInfoWSWork = true;
            progressBarNewsInfoWS.setVisibility(View.VISIBLE);
            rellayBanner.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            isThreadGetNewsInfoWSWork = false;
            progressBarNewsInfoWS.setVisibility(View.GONE);
            if (isSuccess) {
                rellayBanner.setVisibility(View.VISIBLE);
                initializationOfNewsWaliSantriViewer();
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
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_PRESENCE_TODAY_2.replace("$", id) + date)
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String bodyString = response.body().string();

                Gson gson = new Gson();
                presence = gson.fromJson(bodyString, Presensi.class);

                return true;
            } catch (Exception e) {
                Crashlytics.setString(TAG, "3-" + e.getMessage());
                Crashlytics.logException(e);
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
                if (presenceList.size() == allSantri.getTotal()) {
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
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

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
                Crashlytics.setString(TAG, "4-" + e.getMessage());
                Crashlytics.logException(e);
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