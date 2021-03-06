package com.madrasahdigital.walisantri.ppi67benda.view.adapter;

import android.content.Context;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.allsantri.AllSantri;
import com.madrasahdigital.walisantri.ppi67benda.model.riwayatspp.RiwayatSpp;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import java.util.List;

/**
 * Created by Alhudaghifari on 22:21 13/05/19
 */
public class RecyclerRiwayatSpp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = RecyclerRiwayatSpp.class.getSimpleName();
    private List<RiwayatSpp> riwayatSppList;
    private Context mContext;
    private OnArtikelClickListener mOnArtikelClickListener;
    private SharedPrefManager sharedPrefManager;
    private AllSantri allSantri;
    private String santriName;

    public RecyclerRiwayatSpp(Context context, List<RiwayatSpp> riwayatSpps) {
        mContext = context;
        riwayatSppList = riwayatSpps;
        sharedPrefManager = new SharedPrefManager(context);
        allSantri = sharedPrefManager.getAllSantri();
        for (int i=0;i<allSantri.getTotal();i++) {
            if (allSantri.getSantri().get(i).getId().equals(sharedPrefManager.getIdActiveSantriInHomePage())) {
                if (allSantri.getSantri().get(i).getFullname().length() < 30)
                    santriName = allSantri.getSantri().get(i).getFullname();
                else
                    santriName = allSantri.getSantri().get(i).getFullname().substring(0,30);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_riwayat_spp, viewGroup, false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        ViewHolderCategory viewHolderCategory = (ViewHolderCategory) holder;
        final int position = i;
        final RiwayatSpp riwayatSpp = riwayatSppList.get(i);

        String date = UtilsManager.getMonthFromNumber(mContext, riwayatSpp.getBulan())
                        + " " + riwayatSpp.getPeriode();
        String status;
        if (riwayatSpp.getStatus()) {
            status = mContext.getResources().getString(R.string.lunas);
            viewHolderCategory.ivCheck.setImageResource(R.drawable.ic_check_circle_green_24dp);
        } else {
            status = mContext.getResources().getString(R.string.belumdibayar);
            viewHolderCategory.ivCheck.setImageResource(R.drawable.ic_add_circle_red_24dp);
            viewHolderCategory.ivCheck.setRotation(45);
        }

        viewHolderCategory.tvPeriode.setText(date);
        viewHolderCategory.tvStatus.setText(status);
        viewHolderCategory.tvSantriName.setText(santriName);
        viewHolderCategory.tvNominal.setText(UtilsManager.convertLongToCurrencyIDv2WithoutRp(Double.parseDouble(riwayatSpp.getNominal())));

        viewHolderCategory.mViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnArtikelClickListener != null) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mOnArtikelClickListener != null)
                                mOnArtikelClickListener.onClick(position, riwayatSpp);
                        }
                    }, 250);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return riwayatSppList.size();
    }

    private class ViewHolderCategory extends RecyclerView.ViewHolder {
        public View mViewContainer;

        public CardView cardView;
        public TextView tvPeriode;
        public TextView tvStatus;
        public TextView tvNominal;
        public TextView tvSantriName;
        public ImageView ivCheck;

        public ViewHolderCategory(View itemView) {
            super(itemView);
            mViewContainer = itemView;
            cardView = itemView.findViewById(R.id.cardView);
            tvPeriode = itemView.findViewById(R.id.tvPeriode);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvNominal = itemView.findViewById(R.id.tvNominal);
            tvSantriName = itemView.findViewById(R.id.tvSantriName);
            ivCheck = itemView.findViewById(R.id.ivCheck);
        }
    }

    /**
     * interface ketika container di click
     */
    public interface OnArtikelClickListener {
        void onClick(int posisi, RiwayatSpp riwayatSpp);
    }

    /**
     * listener artikel di klik
     *
     * @param onArtikelClickListener onartikelclicklistener
     */
    public void setOnArtikelClickListener(OnArtikelClickListener onArtikelClickListener) {
        mOnArtikelClickListener = onArtikelClickListener;
    }
}
