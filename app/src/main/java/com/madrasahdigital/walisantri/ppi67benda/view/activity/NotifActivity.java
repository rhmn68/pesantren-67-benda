package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.notification.NotificationModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerNotification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NotifActivity extends AppCompatActivity {

    private final String TAG = NotifActivity.class.getSimpleName();
    private RecyclerNotification recyclerNotification;
    private RecyclerNotification.OnArtikelClickListener onArtikelClickListener;
    private List<NotificationModel> notificationModelList ;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPrefManager sharedPrefManager;
    private boolean isThreadWork = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);


        mRecyclerView = findViewById(R.id.rv_numbers);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        sharedPrefManager = new SharedPrefManager(NotifActivity.this);
        notificationModelList = new ArrayList<>();

        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.MAGENTA);

        notificationModelList = sharedPrefManager.getNotificationList();
        if (notificationModelList != null)
            initializationOfViewer();
        initializationOfListener();
        new GetNotification().execute();
    }


    private void initializationOfListener() {
        onArtikelClickListener = new RecyclerNotification.OnArtikelClickListener() {
            @Override
            public void onClick(int posisi, NotificationModel notificationModel) {

            }
        };

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isThreadWork)
                    new GetNotification().execute();
            }
        });
    }

    private void initializationOfViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(NotifActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        recyclerNotification = new RecyclerNotification(NotifActivity.this, notificationModelList);
        recyclerNotification.setOnArtikelClickListener(onArtikelClickListener);

        mRecyclerView.setAdapter(recyclerNotification);
    }

    private class GetNotification extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_NOTIFICATION)
                    .get()
                    .addHeader("Authorization", sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                NotificationModel[] paymentModel =
                        gson.fromJson(bodyString, NotificationModel[].class);

                notificationModelList = new ArrayList<>();
                notificationModelList.addAll(Arrays.asList(paymentModel));

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
            isThreadWork = false;
            if (isSuccess) {
                initializationOfViewer();
            } else {
                UtilsManager.showToast(NotifActivity.this, getResources().getString(R.string.cekkoneksi));
            }
        }
    }

}
