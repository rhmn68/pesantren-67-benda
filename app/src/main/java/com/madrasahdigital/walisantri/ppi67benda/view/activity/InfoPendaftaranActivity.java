package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;

public class InfoPendaftaranActivity extends AppCompatActivity {

    private final String TAG = InfoPendaftaranActivity.class.getSimpleName();
    private ActionBar aksibar;
    private WebView wvDescription;
    private ProgressBar progressBarweb;
    private LinearLayout layoutprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pendaftaran);
        wvDescription = findViewById(R.id.wvDescription);
        progressBarweb = findViewById(R.id.progressBarweb);
        layoutprogress = findViewById(R.id.layoutprogress);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(InfoPendaftaranActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = InfoPendaftaranActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        initializeWebview();
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

    @SuppressLint("SetJavaScriptEnabled")
    private void initializeWebview() {
        if (Build.VERSION.SDK_INT >= 21) {
            wvDescription.getSettings().setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        }
        wvDescription.getSettings().setJavaScriptEnabled(true);
        wvDescription.getSettings().setDomStorageEnabled(true);
        wvDescription.getSettings().setAllowFileAccess(true);
        wvDescription.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int proses)
            {
                try {
                    if (layoutprogress != null && progressBarweb != null) {
                        layoutprogress.setVisibility(View.VISIBLE);
                        progressBarweb.setProgress(proses);

                        if (proses == 100) {
                            layoutprogress.setVisibility(View.GONE);
                            progressBarweb.setProgress(1);
                        } else {
                            layoutprogress.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    FirebaseCrashlytics.getInstance().setCustomKey(TAG, "1-" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
//        wvDescription.loadUrl(Constant.LINK_INFO_PENDAFTARAN);
        wvDescription.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Constant.LINK_INFO_PENDAFTARAN != null && Constant.LINK_INFO_PENDAFTARAN .startsWith("myscheme://")){
                    String newUrl = Constant.LINK_INFO_PENDAFTARAN.replace("myscheme://", "file://android_asset/");
                    wvDescription.loadUrl(newUrl);
                    return true;
                }
                return false;
            }
        });

    }
}
