package com.madrasahdigital.walisantri.ppi67benda.view.fragment;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.riwayatspp.RiwayatSpp;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerRiwayatSpp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiwayatSppFragment extends Fragment {

    private final String TAG = RiwayatSppFragment.class.getSimpleName();
    private RecyclerRiwayatSpp recyclerListChat;
    private RecyclerRiwayatSpp.OnArtikelClickListener onArtikelClickListener;
    private List<RiwayatSpp> riwayatSppList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPrefManager sharedPrefManager;
    private boolean isThreadWork = false;
    private boolean isActivityActive;

    public RiwayatSppFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_riwayat_spp, container, false);

        mRecyclerView = v.findViewById(R.id.rv_numbers);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);
        sharedPrefManager = new SharedPrefManager(getContext());
        riwayatSppList = new ArrayList<>();
        isActivityActive = true;

        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.MAGENTA);

        initializationOfListener();
        new GetRiwayatSpp().execute();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.w(TAG, " onStart - 543212345");
        isActivityActive = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(TAG, " onResume - 543212345");
        isActivityActive = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w(TAG, " onPause - 543212345");
        isActivityActive = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w(TAG, " onStop - 543212345");
        isActivityActive = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.w(TAG, " onDestroyView - 543212345");
        isActivityActive = false;
    }


    private void initializationOfListener() {
        onArtikelClickListener = new RecyclerRiwayatSpp.OnArtikelClickListener() {
            @Override
            public void onClick(int posisi, RiwayatSpp riwayatSpp) {

            }
        };

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isThreadWork)
                    new GetRiwayatSpp().execute();
            }
        });
    }

    private void initializationOfViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(RiwayatSppFragment.this.getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        recyclerListChat = new RecyclerRiwayatSpp(getContext(), riwayatSppList);
        recyclerListChat.setOnArtikelClickListener(onArtikelClickListener);

        mRecyclerView.setAdapter(recyclerListChat);
    }

    private class GetRiwayatSpp extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            isThreadWork = true;
            swipeRefreshLayout.setRefreshing(true);
            riwayatSppList = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_RIWAYAT_SPP)
                    .get()
                    .addHeader("Authorization", sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                RiwayatSpp[] paymentModel =
                        gson.fromJson(bodyString, RiwayatSpp[].class);

                riwayatSppList.addAll(Arrays.asList(paymentModel));

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            swipeRefreshLayout.setRefreshing(false);
            isThreadWork = false;
            if (isSuccess && isActivityActive) {
                initializationOfViewer();
            } else {
                UtilsManager.showToast(getContext(), getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
