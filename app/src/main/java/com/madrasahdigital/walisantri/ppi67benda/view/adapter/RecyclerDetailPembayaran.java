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
import com.madrasahdigital.walisantri.ppi67benda.model.detailpembayaran.Item;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Alhudaghifari on 17:07 26/06/19
 */
public class RecyclerDetailPembayaran extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = RecyclerDetailPembayaran.class.getSimpleName();
    private List<Item> itemPembayaranList;
    private Context mContext;
    private OnArtikelClickListener mOnArtikelClickListener;

    public RecyclerDetailPembayaran(Context context, List<Item> itemPembayaranList) {
        mContext = context;
        this.itemPembayaranList = itemPembayaranList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_pembayaran, parent, false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        ViewHolderCategory viewHolderCategory = (ViewHolderCategory) holder;
        final int position = i;
        String qty = "qty : " + itemPembayaranList.get(i).getQty();
        viewHolderCategory.tvProductName.setText(itemPembayaranList.get(i).getProductName());
        viewHolderCategory.tvQty.setText(qty);
        viewHolderCategory.tvNominal.setText(UtilsManager.convertLongToCurrencyIDv2WithoutRp(Double.valueOf(itemPembayaranList.get(i).getSubtotal())));

        viewHolderCategory.mViewContainer.setOnClickListener(view -> {
            if (mOnArtikelClickListener != null) {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (mOnArtikelClickListener != null)
                        mOnArtikelClickListener.onClick(position, itemPembayaranList.get(i));
                }, 250);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemPembayaranList.size();
    }

    private class ViewHolderCategory extends RecyclerView.ViewHolder {
        public View mViewContainer;

        public CircleImageView civProfPic;
        public TextView tvProductName;
        public TextView tvQty;
        public TextView tvNominal;

        public ViewHolderCategory(View itemView) {
            super(itemView);
            mViewContainer = itemView;
            civProfPic = itemView.findViewById(R.id.civProfPic);
            tvNominal = itemView.findViewById(R.id.tvNominal);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQty = itemView.findViewById(R.id.tvQty);
        }
    }

    /**
     * interface ketika container di click
     */
    public interface OnArtikelClickListener {
        void onClick(int posisi, Item itemPembayaran);
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
