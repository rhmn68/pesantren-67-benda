package com.madrasahdigital.walisantri.ppi67benda.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanspp.TagihanAllVarModel;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanspp.TagihanSppModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alhudaghifari on 0:19 14/05/19
 */
public class RecyclerTagihanSpp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = RecyclerTagihanSpp.class.getSimpleName();
    private List<TagihanAllVarModel> tagihanAllVarModelList;
    private Context mContext;

    public RecyclerTagihanSpp(Context context, List<TagihanSppModel> riwayatSpps) {
        mContext = context;
        tagihanAllVarModelList = new ArrayList<>();
        TagihanAllVarModel tagihanAllVarModel;
        for (int i=0;i<riwayatSpps.size();i++) {
            tagihanAllVarModel = new TagihanAllVarModel();
            for (int j=0;j<riwayatSpps.get(i).getUnpaid().size();j++) {
                tagihanAllVarModel.setSantriId(riwayatSpps.get(i).getSantriId());
                tagihanAllVarModel.setFullname(riwayatSpps.get(i).getFullname());
                tagihanAllVarModel.setKelas(riwayatSpps.get(i).getKelas());
                tagihanAllVarModel.setId(riwayatSpps.get(i).getUnpaid().get(j).getId());
                tagihanAllVarModel.setStatus(riwayatSpps.get(i).getUnpaid().get(j).getStatus());
                tagihanAllVarModel.setNominal(riwayatSpps.get(i).getUnpaid().get(j).getNominal());
                tagihanAllVarModel.setPeriode(riwayatSpps.get(i).getUnpaid().get(j).getPeriode());
                tagihanAllVarModel.setBulan(riwayatSpps.get(i).getUnpaid().get(j).getBulan());
                tagihanAllVarModel.setUrl(riwayatSpps.get(i).getUnpaid().get(j).getUrl());
                tagihanAllVarModelList.add(tagihanAllVarModel);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_tagihan_spp, viewGroup, false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        ViewHolderCategory viewHolderCategory = (ViewHolderCategory) holder;
        final TagihanAllVarModel tagihanSppModel = tagihanAllVarModelList.get(i);

        String date = UtilsManager.getMonthFromNumber(mContext, tagihanSppModel.getBulan())
                + " " + tagihanSppModel.getPeriode();
        String status;
        if (tagihanSppModel.getStatus()) {
            status = mContext.getResources().getString(R.string.lunas);
        } else {
            status = mContext.getResources().getString(R.string.belumdibayar);
        }

        viewHolderCategory.tvKelas.setText("Kelas " + tagihanSppModel.getKelas());
        viewHolderCategory.tvName.setText(tagihanSppModel.getFullname());
        viewHolderCategory.tvPeriode.setText(date);
        viewHolderCategory.tvStatus.setText(status);
        viewHolderCategory.tvNominal.setText(UtilsManager.convertLongToCurrencyIDv2WithoutRp(Double.parseDouble(tagihanSppModel.getNominal())));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "size tagihan spp : " + tagihanAllVarModelList.size());
        return tagihanAllVarModelList.size();
    }

    private class ViewHolderCategory extends RecyclerView.ViewHolder {
        public View mViewContainer;

        public CardView cardView;
        public TextView tvPeriode;
        public TextView tvStatus;
        public TextView tvNominal;
        public TextView tvName;
        public TextView tvKelas;

        public ViewHolderCategory(View itemView) {
            super(itemView);
            mViewContainer = itemView;
            cardView = itemView.findViewById(R.id.cardView);
            tvPeriode = itemView.findViewById(R.id.tvPeriode);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvNominal = itemView.findViewById(R.id.tvNominal);
            tvName = itemView.findViewById(R.id.tvName);
            tvKelas = itemView.findViewById(R.id.tvKelas);
        }
    }
}
