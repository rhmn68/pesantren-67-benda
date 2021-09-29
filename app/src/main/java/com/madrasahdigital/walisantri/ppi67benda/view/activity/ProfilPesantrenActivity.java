package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
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

public class ProfilPesantrenActivity extends AppCompatActivity {

    private final String TAG = ProfilPesantrenActivity.class.getSimpleName();
    private ActionBar aksibar;
    private WebView wvDescription;
    private ProgressBar progressBarweb;
    private LinearLayout layoutprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_pesantren);
        wvDescription = findViewById(R.id.wvDescription);
        progressBarweb = findViewById(R.id.progressBarweb);
        layoutprogress = findViewById(R.id.layoutprogress);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(ProfilPesantrenActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = ProfilPesantrenActivity.this.getSupportActionBar();
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


    private void initializeWebview() {
        if (Build.VERSION.SDK_INT >= 21) {
            wvDescription.getSettings().setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        }
        wvDescription.getSettings().setJavaScriptEnabled(true);
        wvDescription.getSettings().setDomStorageEnabled(true);
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
        wvDescription.setWebViewClient(new WebViewClient());
        wvDescription.loadUrl(Constant.LINK_PROFIL_PESANTREN);

    }
}
