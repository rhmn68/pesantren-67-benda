package com.madrasahdigital.walisantri.ppi67benda.view.fragment;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.payment.PaymentModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.adapter.RecyclerPayment;

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
public class PaymentFragment extends Fragment {

    private final String TAG = PaymentFragment.class.getSimpleName();
    private RecyclerPayment recyclerListChat;
    private RecyclerPayment.OnArtikelClickListener onArtikelClickListener;
    private List<PaymentModel> paymentModelList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPrefManager sharedPrefManager;

    public PaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(getResources().getString(R.string.title_pembayaran));

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mRecyclerView = v.findViewById(R.id.rv_numbers);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);
        sharedPrefManager = new SharedPrefManager(getContext());
        paymentModelList = new ArrayList<>();

        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.YELLOW, Color.GREEN);

        new GetPaymentData().execute();
        return v;
    }

    private void initializationOfListener() {
        onArtikelClickListener = new RecyclerPayment.OnArtikelClickListener() {
            @Override
            public void onClick(int posisi, PaymentModel paymentModel) {

            }
        };

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }

    private class GetPaymentData extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Constant.LINK_GET_PAYMENT_INFO)
                    .get()
                    .addHeader("Authorization", sharedPrefManager.getToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();

                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();

                Gson gson = new Gson();
                PaymentModel[] paymentModel =
                        gson.fromJson(bodyString, PaymentModel[].class);

                paymentModelList.addAll(Arrays.asList(paymentModel));

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            swipeRefreshLayout.setRefreshing(false);
            if (isSuccess) {
                initializationOfViewer();
            } else {
                UtilsManager.showToast(getContext(), getResources().getString(R.string.cekkoneksi));
            }
        }
    }

    private void initializationOfViewer() {
        final LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(PaymentFragment.this.getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        recyclerListChat = new RecyclerPayment(getContext(), paymentModelList);
        recyclerListChat.setOnArtikelClickListener(onArtikelClickListener);

        mRecyclerView.setAdapter(recyclerListChat);
    }
}
