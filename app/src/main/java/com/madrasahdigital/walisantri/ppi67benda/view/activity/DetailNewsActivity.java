package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.DetailNewsModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private String mCurrentPhotoPath = "";
    private boolean isFromAndroid5 = false;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;
    private static final int REQUEST_GET_THE_THUMBNAIL = 4000;
    private static final long ANIMATION_DURATION = 200;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
    private final static int PERMISSION_REQUEST_CODE = 232;

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
        //set ChromeClient
        wvDescription.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                wvDescription.loadData(detail, "text/html", "utf-8");

                view.clearHistory();
            }
        });
        wvDescription.setWebChromeClient(getChromeClient());
        wvDescription.loadData(detail, "text/html", "utf-8");

        final MyJavaScriptInterface myJavaScriptInterface
                = new MyJavaScriptInterface(this);
        wvDescription.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");
    }

    private WebChromeClient getChromeClient() {
        return new WebChromeClient() {

            //3.0++
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                openFileChooserImpl();
            }

            //3.0--
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                openFileChooserImpl();
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                openFileChooserImpl();
            }

            // For Android > 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
                mUploadMessageForAndroid5 = uploadMsg;
                openFileChooserImplForAndroid5();
                return true;
            }

        };
    }

    private void openFileChooserImplForAndroid5() {
        isFromAndroid5 = true;
        AlertDialog.Builder myDialog
                = new AlertDialog.Builder(DetailNewsActivity.this);
        myDialog.setTitle("Ambil file gambar");
        myDialog.setMessage("Apakah anda ingin mengambil file gambar dari penyimpanan?");

        myDialog.setPositiveButton("Ya", (dialogInterface, i) -> {
            if (havePermissionGranted(DetailNewsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
            } else {
                // request permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        });
        myDialog.setNegativeButton("Tidak", (dialogInterface, i) -> {
            Log.v(TAG, TAG + " # onCancel");
            //important to return new Uri[]{}, when nothing to do. This can slove input file wrok for once.
            //InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            mUploadMessageForAndroid5 = null;
        });
        myDialog.show();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            File file = new File(createImageFile());
            Uri imageUri = null;
            try {
                imageUri = Uri.fromFile(createImageFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //temp sd card file
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(takePictureIntent, REQUEST_GET_THE_THUMBNAIL);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/don_test/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.v(TAG, TAG + " # onActivityResult # requestCode=" + requestCode + " # resultCode=" + resultCode);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result;

            if (intent == null || resultCode != Activity.RESULT_OK) {
                result = null;
            } else {
                result = intent.getData();
            }

            if (result != null) {
                Log.v(TAG, TAG + " # result.getPath()=" + result.getPath());
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        } else if (requestCode == REQUEST_GET_THE_THUMBNAIL) {
            if (resultCode == Activity.RESULT_OK) {
                File file = new File(mCurrentPhotoPath);
                Uri localUri = Uri.fromFile(file);
                Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                sendBroadcast(localIntent);

                Uri result = Uri.fromFile(file);
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
                mUploadMessageForAndroid5 = null;
            } else {

                File file = new File(mCurrentPhotoPath);
                Log.v(TAG, TAG + " # file=" + file.exists());
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
                if (isFromAndroid5) {
                    openFileChooserImplForAndroid5();
                } else {
                    openFileChooserImpl();
                }
            } else {
                if (isFromAndroid5) {
                    mUploadMessageForAndroid5 = null;
                } else {
                    mUploadMessage = null;
                }
                UtilsManager.showToast(this, "Anda harus memberikan izin. Silahkan reload halaman.");
            }
        }
    }

    public boolean havePermissionGranted(Context context, String permission) {
        if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{permission}, PERMISSION_REQUEST_CODE);

            return false;
        }
    }

    private void openFileChooserImpl() {
        isFromAndroid5 = false;
        AlertDialog.Builder myDialog
                = new AlertDialog.Builder(DetailNewsActivity.this);
        myDialog.setTitle("Ambil file gambar");
        myDialog.setMessage("Apakah anda ingin mengambil file gambar dari penyimpanan?");
        myDialog.setPositiveButton("Ya", (dialogInterface, i) -> {
            if (havePermissionGranted(DetailNewsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Chooser"), FILECHOOSER_RESULTCODE);
            } else {
                // request permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        });
        myDialog.setNegativeButton("Tidak", (dialogInterface, i) -> {
            Log.v(TAG, TAG + " # onCancel");
            mUploadMessage = null;
        });
        myDialog.show();
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
           if (isFromAndroid5) {
               openFileChooserImplForAndroid5();
           } else {
               openFileChooserImpl();
           }
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
                setDetailArticle(detailNewsModel.getContent());
            } else {
                ivRefreshPresenceToday.setVisibility(View.VISIBLE);
                UtilsManager.showToast(DetailNewsActivity.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
