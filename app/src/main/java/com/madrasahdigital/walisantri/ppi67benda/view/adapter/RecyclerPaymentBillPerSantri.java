package com.madrasahdigital.walisantri.ppi67benda.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanallsantri.BillItem;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import java.util.List;

/**
 * Created by Alhudaghifari on 17:19 21/06/19
 */
public class RecyclerPaymentBillPerSantri  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BillItem> billItems;
    private Context mContext;
    private OnArtikelClickListener mOnArtikelClickListener;


    public RecyclerPaymentBillPerSantri(Context context, List<BillItem> billItems) {
        mContext = context;
        this.billItems = billItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_payment_bill_2, parent, false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        ViewHolderCategory viewHolderCategory = (ViewHolderCategory) holder;
        final int position = i;
        String tot = "Rp " + UtilsManager.convertLongToCurrencyIDv2WithoutRp(Double.valueOf(billItems.get(position).getAmount()));
        viewHolderCategory.tvKeteranganBayar.setText(billItems.get(position).getTitle());
        viewHolderCategory.tvTotalNominal.setText(tot);

        viewHolderCategory.mViewContainer.setOnClickListener(view -> {
            if (mOnArtikelClickListener != null) {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (mOnArtikelClickListener != null)
                        mOnArtikelClickListener.onClick(position, billItems.get(position));
                }, 250);
            }
        });
    }

    @Override
    public int getItemCount() {
        return billItems.size();
    }


    private class ViewHolderCategory extends RecyclerView.ViewHolder {
        public View mViewContainer;

        public TextView tvKeteranganBayar;
        public TextView tvTotalNominal;

        public ViewHolderCategory(View itemView) {
            super(itemView);
            mViewContainer = itemView;
            tvKeteranganBayar = itemView.findViewById(R.id.tvKeteranganBayar);
            tvTotalNominal = itemView.findViewById(R.id.tvTotalNominal);
        }
    }

    /**
     * interface ketika container di click
     */
    public interface OnArtikelClickListener {
        void onClick(int posisi, BillItem billItem);
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
