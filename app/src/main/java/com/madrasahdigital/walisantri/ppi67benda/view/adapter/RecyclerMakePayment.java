package com.madrasahdigital.walisantri.ppi67benda.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanspp.TagihanAllVarModel;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanspp.TagihanSppModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alhudaghifari on 5:41 22/05/19
 */
public class RecyclerMakePayment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = RecyclerMakePayment.class.getSimpleName();
    private List<TagihanAllVarModel> tagihanAllVarModelList;
    private Context mContext;
    private int sizeList;
    private final int TAG_NORMAL_LIST = 1;
    private final int TAG_END_LIST = 2;
    private OnCheckBoxClickListener onCheckBoxClickListener;

    public RecyclerMakePayment(Context context, List<TagihanSppModel> riwayatSpps) {
        mContext = context;
        tagihanAllVarModelList = new ArrayList<>();
        TagihanAllVarModel tagihanAllVarModel;
        for (int i = 0; i < riwayatSpps.size(); i++) {
            tagihanAllVarModel = new TagihanAllVarModel();
            for (int j = 0; j < riwayatSpps.get(i).getUnpaid().size(); j++) {
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
        sizeList = tagihanAllVarModelList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (position < sizeList - 1)
            return TAG_NORMAL_LIST;
        else return TAG_END_LIST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case TAG_NORMAL_LIST:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_make_payment, viewGroup, false);
                return new ViewHolderCategory(view);
            case TAG_END_LIST:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_blank, viewGroup, false);
                return new ViewHolderCategory(view);
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_make_payment, viewGroup, false);
                return new ViewHolderCategory(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int i = position;
        int viewCode = getItemViewType(i);
        if (viewCode == TAG_NORMAL_LIST) {
            final ViewHolderCategory viewHolderCategory = (ViewHolderCategory) holder;
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
            viewHolderCategory.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onCheckBoxClickListener != null) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (onCheckBoxClickListener != null)
                                    onCheckBoxClickListener.onClick(i, viewHolderCategory.checkBox.isChecked(), tagihanSppModel);
                            }
                        }, 250);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return sizeList;
    }

    private class ViewHolderCategory extends RecyclerView.ViewHolder {
        public View mViewContainer;

        public CardView cardView;
        public TextView tvPeriode;
        public TextView tvStatus;
        public TextView tvNominal;
        public TextView tvName;
        public TextView tvKelas;
        public CheckBox checkBox;

        public ViewHolderCategory(View itemView) {
            super(itemView);
            mViewContainer = itemView;
            cardView = itemView.findViewById(R.id.cardView);
            tvPeriode = itemView.findViewById(R.id.tvPeriode);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvNominal = itemView.findViewById(R.id.tvNominal);
            tvName = itemView.findViewById(R.id.tvName);
            tvKelas = itemView.findViewById(R.id.tvKelas);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    /**
     * interface ketika container di click
     */
    public interface OnCheckBoxClickListener {
        void onClick(int posisi, boolean isCheckBoxChecked, TagihanAllVarModel tagihanAllVarModel);
    }

    public void setCheckBoxClickListener(OnCheckBoxClickListener onCheckBoxClickListener) {
        this.onCheckBoxClickListener = onCheckBoxClickListener;
    }
}
