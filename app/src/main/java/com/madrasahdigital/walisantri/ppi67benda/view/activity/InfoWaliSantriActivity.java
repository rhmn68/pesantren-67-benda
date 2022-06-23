package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.NewsModel;
import com.madrasahdigital.walisantri.ppi67benda.model.notification.NotificationModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerNewsHome;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.madrasahdigital.walisantri.ppi67benda.utils.Constant.TIMEOUT;

public class InfoWaliSantriActivity extends AppCompatActivity {
    
    private final String TAG = InfoWaliSantriActivity.class.getSimpleName();
    private ActionBar aksibar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerNewsHome recyclerNewsHome;
    private RecyclerNewsHome.OnArtikelClickListener onArtikelClickListenerNewsHome;
    private SharedPrefManager sharedPrefManager;
    private List<NotificationModel> notificationModelList;
    private ProgressBar progressBar;
    private NewsModel newsModel;
    private boolean isThreadWork = false;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_wali_santri);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        mRecyclerView = findViewById(R.id.rv_numbers);
        progressBar = findViewById(R.id.progressBar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(InfoWaliSantriActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = InfoWaliSantriActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        sharedPrefManager = new SharedPrefManager(InfoWaliSantriActivity.this);
        initializeListener();
        new GetNews().execute();
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

    private void initializeListener() {
        onArtikelClickListenerNewsHome = (posisi, notificationModel) -> {
            Intent intent = new Intent(InfoWaliSantriActivity.this, DetailNewsActivity.class);
            intent.putExtra("urlberita", notificationModel.getUrl());
            startActivity(intent);
        };

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    page++;
                    if (!isThreadWork)
                        new GetNews(page).execute();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (!isThreadWork)
                new GetNews().execute();
            else {
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initializationOfNewsViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        recyclerNewsHome = new RecyclerNewsHome(InfoWaliSantriActivity.this, newsModel.getPosts(), Constant.TYPE_NEWS_PAGE);
        recyclerNewsHome.setOnArtikelClickListener(onArtikelClickListenerNewsHome);

        mRecyclerView.setAdapter(recyclerNewsHome);
    }

    private void addNewData() {
        recyclerNewsHome.addNewData(newsModel.getPosts());
        recyclerNewsHome.notifyDataSetChanged();
        if (newsModel.getPosts().size() == 0)
            page--;
    }

    private class GetNews extends AsyncTask<Void, Integer, Boolean> {

        private int page;

        public GetNews(){
            page = 1;
        }

        public GetNews(int page) {
            this.page = page;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            String url = Constant.LINK_GET_INFO_WALI_SANTRI + "?page=" + page;

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
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
                FirebaseCrashlytics.getInstance().setCustomKey(TAG, "1-" + e.getMessage());
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onpre");
            isThreadWork = true;
            if (page == 1)
                swipeRefreshLayout.setRefreshing(true);
            else
                progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            isThreadWork = false;
            Log.d(TAG, "onpost");
            if (isSuccess) {
                if (page == 1)
                    initializationOfNewsViewer();
                else
                    addNewData();
            } else {
                UtilsManager.showToast(InfoWaliSantriActivity.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
