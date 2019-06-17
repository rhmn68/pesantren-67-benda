package com.madrasahdigital.walisantri.ppi67benda.view.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.payment.PaymentModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.payment.MakePaymentActivity;
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
    private RelativeLayout rellayPembayaran;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPrefManager sharedPrefManager;
    private boolean isThreadWork = false;

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
        rellayPembayaran = v.findViewById(R.id.rellayPembayaran);
        sharedPrefManager = new SharedPrefManager(getContext());
        paymentModelList = new ArrayList<>();
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.MAGENTA);

        initializationOfListener();

        new GetPaymentData().execute();
        return v;
    }

    private void initializationOfListener() {
        onArtikelClickListener = (posisi, paymentModel) -> {

        };

        rellayPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MakePaymentActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isThreadWork)
                    new GetPaymentData().execute();
            }
        });
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

    private class GetPaymentData extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            isThreadWork = true;
            swipeRefreshLayout.setRefreshing(true);
            paymentModelList = new ArrayList<>();
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
            isThreadWork = false;
            if (isSuccess) {
                initializationOfViewer();
            } else {
                UtilsManager.showToast(getContext(), getResources().getString(R.string.cekkoneksi));
            }
        }
    }
}
