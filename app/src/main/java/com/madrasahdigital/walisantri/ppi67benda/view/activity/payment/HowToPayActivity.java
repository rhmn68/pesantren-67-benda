package com.madrasahdigital.walisantri.ppi67benda.view.activity.payment;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
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

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;

public class HowToPayActivity extends AppCompatActivity {

    private ActionBar aksibar;
    private ProgressBar progressBarweb;
    private WebView webView2;
    private LinearLayout layoutprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_pay);
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBarweb = findViewById(R.id.progressBarweb);
        webView2 = findViewById(R.id.webview2);
        layoutprogress = findViewById(R.id.layoutprogress);

        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(HowToPayActivity.this, R.color.colorPrimaryDark));
        }

        aksibar = HowToPayActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        initializeWebview();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_refresh) {
            initializeWebview();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeWebview() {
        if (Build.VERSION.SDK_INT >= 21) {
            webView2.getSettings().setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        }
        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.getSettings().setDomStorageEnabled(true);
        webView2.setWebChromeClient(new WebChromeClient() {
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
                    e.printStackTrace();
                }
            }
        });
        webView2.setWebViewClient(new WebViewClient());
        webView2.loadUrl(Constant.URL_HOW_TO_PAY);

    }
}
