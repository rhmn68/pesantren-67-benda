package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.DetailNewsModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.madrasahdigital.walisantri.ppi67benda.utils.Constant.TIMEOUT;

public class DetailNewsActivity extends AppCompatActivity {

    private final String TAG = DetailNewsActivity.class.getSimpleName();
    private ActionBar aksibar;
    private ImageView ivNewsImage;
    private TextView tvTitleNews;
    private TextView tvDipostingPada;
    private DetailNewsModel detailNewsModel;
    private SharedPrefManager sharedPrefManager;
    private ProgressBar progressBar;
    private ImageView ivRefreshPresenceToday;
    private String urlBerita;
    private WebView wvDescription;

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;

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
        wvDescription = findViewById(R.id.wvDescription);
        tvTitleNews = findViewById(R.id.tvTitleNews);
        tvDipostingPada = findViewById(R.id.tvDipostingPada);
        progressBar = findViewById(R.id.progressBarToday);
        ivRefreshPresenceToday = findViewById(R.id.ivRefreshPresenceToday);
        sharedPrefManager = new SharedPrefManager(DetailNewsActivity.this);

        wvDescription.getSettings().setSupportZoom(true);
        wvDescription.getSettings().setBuiltInZoomControls(true);
        wvDescription.getSettings().setDisplayZoomControls(true);
        wvDescription.getSettings().setAllowFileAccess(true);
        WebSettings webSettings = wvDescription.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Intent intent = getIntent();
        urlBerita = intent.getStringExtra("urlberita");

        if (urlBerita != null) {
            if (!urlBerita.isEmpty()) {
                new GetDetailArticle().execute();
            } else {
                tvDipostingPada.setText("");
                tvTitleNews.setText("");
            }
        } else {
            tvDipostingPada.setText("");
            tvTitleNews.setText("");
        }
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
    public void onBackPressed() {
        if (wvDescription.canGoBack()) {
            wvDescription.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void setDetailArticle(String detail) {
        wvDescription.loadData(detail, "text/html", "utf-8");
    }

    public void setListenersWebview() {

        wvDescription.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                wvDescription.loadUrl("about:blank");
                setDetailArticle(detailNewsModel.getContent());
                view.clearHistory();
            }
        });

        wvDescription.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

            }

            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

            }
        });

        setDetailArticle(detailNewsModel.getContent());

        final MyJavaScriptInterface myJavaScriptInterface
                = new MyJavaScriptInterface(this);
        wvDescription.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");
    }

    public class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
            // webView.loadUrl("javascript:document.getElementById(\"Button3\").innerHTML = \"bye\";");
        }

        @JavascriptInterface
        public void openAndroidDialog() {
            AlertDialog.Builder myDialog
                    = new AlertDialog.Builder(DetailNewsActivity.this);
            myDialog.setTitle("Pertanyaan");
            myDialog.setMessage("Apakah anda ingin mengaktifkan?");
            myDialog.setPositiveButton("ON", null);
            myDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    private class GetDetailArticle extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(urlBerita)
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
                Crashlytics.setString(TAG, "1-" + e.getMessage());
                Crashlytics.logException(e);
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPreExecute() {
            ivNewsImage.setImageDrawable(getResources().getDrawable(R.drawable.bg_silver));
            tvTitleNews.setText("");
            tvDipostingPada.setText("");
            setDetailArticle("");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            progressBar.setVisibility(View.GONE);
            if (isSuccess) {
                if (detailNewsModel.getFeaturedImage() != null) {
                    try {
                        Glide
                                .with(DetailNewsActivity.this)
                                .load(detailNewsModel.getFeaturedImage())
                                .centerCrop()
                                .placeholder(R.drawable.bg_silver)
                                .error(R.drawable.bg_silver)
                                .into(ivNewsImage);
                    } catch (Exception e) {
                        Crashlytics.setString(TAG, "2-" + e.getMessage());
                        Crashlytics.logException(e);
                    }
                }
                tvTitleNews.setText(detailNewsModel.getTitle());
                if (detailNewsModel.getPublishedAt() != null)
                    tvDipostingPada.setText("Diposting pada " + detailNewsModel.getPublishedAt());
//                setDetailArticle(detailNewsModel.getContent());
                setListenersWebview();
            } else {
                ivRefreshPresenceToday.setVisibility(View.VISIBLE);
                UtilsManager.showToast(DetailNewsActivity.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
