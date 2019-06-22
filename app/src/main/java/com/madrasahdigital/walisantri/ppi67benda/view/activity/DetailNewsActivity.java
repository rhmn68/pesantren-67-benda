package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.DetailNewsModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DetailNewsActivity extends AppCompatActivity {

    private final String TAG = DetailNewsActivity.class.getSimpleName();
    private ActionBar aksibar;
    private ImageView ivNewsImage;
    private TextView tvTitleNews;
    private TextView tvDipostingPada;
    private TextView tvIsiBerita;
    private DetailNewsModel detailNewsModel;
    private SharedPrefManager sharedPrefManager;
    private ProgressBar progressBar;
    private ImageView ivRefreshPresenceToday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(DetailNewsActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = DetailNewsActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);
        ivNewsImage = findViewById(R.id.ivNewsImage);
        tvTitleNews = findViewById(R.id.tvTitleNews);
        tvDipostingPada = findViewById(R.id.tvDipostingPada);
        tvIsiBerita = findViewById(R.id.tvIsiBerita);
        progressBar = findViewById(R.id.progressBarToday);
        ivRefreshPresenceToday = findViewById(R.id.ivRefreshPresenceToday);
        sharedPrefManager = new SharedPrefManager(DetailNewsActivity.this);

        new GetDetailArticle().execute();
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


    private class GetDetailArticle extends AsyncTask<Void, Integer, Boolean> {

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
                detailNewsModel = gson.fromJson(bodyString, DetailNewsModel.class);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPreExecute() {
            ivNewsImage.setImageDrawable(getResources().getDrawable(R.drawable.round_silver));
            tvTitleNews.setText("");
            tvDipostingPada.setText("");
            tvIsiBerita.setText("");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            progressBar.setVisibility(View.GONE);
            if (isSuccess) {
                Glide
                        .with(DetailNewsActivity.this)
                        .load(detailNewsModel.getFeaturedImage())
                        .centerCrop()
                        .placeholder(R.drawable.round_silver)
                        .error(R.drawable.round_silver)
                        .into(ivNewsImage);
                tvTitleNews.setText(detailNewsModel.getTitle());
                if (detailNewsModel.getPublishedAt() != null)
                tvDipostingPada.setText("Diposting pada " + detailNewsModel.getPublishedAt());
                tvIsiBerita.setText(detailNewsModel.getContent());
            } else {
                ivRefreshPresenceToday.setVisibility(View.VISIBLE);
                UtilsManager.showToast(DetailNewsActivity.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
