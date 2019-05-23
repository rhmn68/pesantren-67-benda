package com.madrasahdigital.walisantri.ppi67benda.view.fragment;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanspp.TagihanSppModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerTagihanSpp;

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
public class InvoiceFragment extends Fragment {

    private final String TAG = InvoiceFragment.class.getSimpleName();
    private RecyclerTagihanSpp recyclerListChat;
    private List<TagihanSppModel> tagihanSppModelList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPrefManager sharedPrefManager;
    private boolean isThreadWork = false;
    private int positionSelected = 0;

    public InvoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_invoice, container, false);

        mRecyclerView = v.findViewById(R.id.rv_numbers);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);
        sharedPrefManager = new SharedPrefManager(getContext());
        tagihanSppModelList = new ArrayList<>();

        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.MAGENTA);

        initializationOfListener();
        new GetTagihanSpp().execute();

        return v;
    }

    private void initializationOfListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isThreadWork)
                    new GetTagihanSpp().execute();
            }
        });
    }

    private void initializationOfViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(InvoiceFragment.this.getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        recyclerListChat = new RecyclerTagihanSpp(getContext(), tagihanSppModelList);
        mRecyclerView.setAdapter(recyclerListChat);
    }

    private class GetTagihanSpp extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            isThreadWork = true;
            swipeRefreshLayout.setRefreshing(true);
            tagihanSppModelList = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_TAGIHAN_SPP)
                    .get()
                    .addHeader("Authorization", sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                TagihanSppModel[] tagihanSppModels =
                        gson.fromJson(bodyString, TagihanSppModel[].class);

                tagihanSppModelList.addAll(Arrays.asList(tagihanSppModels));

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
            if (isSuccess) {
                initializationOfViewer();
            } else {
                UtilsManager.showToast(getContext(), getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
