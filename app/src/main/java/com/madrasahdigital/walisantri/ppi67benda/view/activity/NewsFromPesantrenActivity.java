package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.NewsModel;
import com.madrasahdigital.walisantri.ppi67benda.model.notification.NotificationModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerNewsHome;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NewsFromPesantrenActivity extends AppCompatActivity {

    private ActionBar aksibar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerNewsHome recyclerNewsHome;
    private RecyclerNewsHome.OnArtikelClickListener onArtikelClickListenerNewsHome;
    private SharedPrefManager sharedPrefManager;
    private List<NotificationModel> notificationModelList;
    private NewsModel newsModel;
    private boolean isThreadWork = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_from_pesantren);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        mRecyclerView = findViewById(R.id.rv_numbers);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(NewsFromPesantrenActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = NewsFromPesantrenActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        sharedPrefManager = new SharedPrefManager(NewsFromPesantrenActivity.this);
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
            Intent intent = new Intent(NewsFromPesantrenActivity.this, DetailNewsActivity.class);
            startActivity(intent);
        };

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (!isThreadWork)
                new GetNews().execute();
        });
    }

    private void initializationOfNewsViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        recyclerNewsHome = new RecyclerNewsHome(NewsFromPesantrenActivity.this, newsModel.getPosts(), Constant.TYPE_NEWS_PAGE);
        recyclerNewsHome.setOnArtikelClickListener(onArtikelClickListenerNewsHome);

        mRecyclerView.setAdapter(recyclerNewsHome);
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
            isThreadWork = true;
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            swipeRefreshLayout.setRefreshing(false);
            isThreadWork = true;
            if (isSuccess) {
                initializationOfNewsViewer();
            } else {
                UtilsManager.showToast(NewsFromPesantrenActivity.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
