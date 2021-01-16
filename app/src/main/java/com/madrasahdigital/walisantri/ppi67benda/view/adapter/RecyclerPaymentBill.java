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
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanallsantri.Student;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Alhudaghifari on 22:50 15/06/19
 */
public class RecyclerPaymentBill extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<Student> santriList;
    private Context mContext;
    private OnArtikelClickListener mOnArtikelClickListener;


    public RecyclerPaymentBill(Context context, List<Student> santriList) {
        mContext = context;
        this.santriList = santriList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_payment_bill, parent, false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        ViewHolderCategory viewHolderCategory = (ViewHolderCategory) holder;
        final int position = i;
        String tot = "Rp " + UtilsManager.convertLongToCurrencyIDv2WithoutRp(santriList.get(position).getBills().getSubtotal());
        viewHolderCategory.tvSantriName.setText(santriList.get(position).getFullname());
        viewHolderCategory.tvTotalNominal.setText(tot);

        if (santriList.get(i).getFullname().length() > 1) {
            viewHolderCategory.tvFirstCharForImageProfil.setText(santriList.get(i).getFullname().charAt(0) + "");
        }

        viewHolderCategory.mViewContainer.setOnClickListener(view -> {
            if (mOnArtikelClickListener != null) {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (mOnArtikelClickListener != null)
                        mOnArtikelClickListener.onClick(position, santriList.get(position));
                }, 250);
            }
        });
    }

    @Override
    public int getItemCount() {
        return santriList.size();
    }


    private class ViewHolderCategory extends RecyclerView.ViewHolder {
        public View mViewContainer;

        public CircleImageView civProfPic;
        public TextView tvSantriName;
        public TextView tvTotalNominal;
        public TextView tvFirstCharForImageProfil;

        public ViewHolderCategory(View itemView) {
            super(itemView);
            mViewContainer = itemView;
            civProfPic = itemView.findViewById(R.id.civProfPic);
            tvFirstCharForImageProfil = itemView.findViewById(R.id.tvFirstCharForImageProfil);
            tvSantriName = itemView.findViewById(R.id.tvSantriName);
            tvTotalNominal = itemView.findViewById(R.id.tvTotalNominal);
        }
    }

    /**
     * interface ketika container di click
     */
    public interface OnArtikelClickListener {
        void onClick(int posisi, Student santri);
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
