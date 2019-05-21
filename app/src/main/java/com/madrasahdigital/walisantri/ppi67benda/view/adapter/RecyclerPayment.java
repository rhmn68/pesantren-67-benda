package com.madrasahdigital.walisantri.ppi67benda.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.payment.PaymentModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import java.util.List;

/**
 * Created by Alhudaghifari on 14:50 13/05/19
 */
public class RecyclerPayment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = RecyclerPayment.class.getSimpleName();
    private List<PaymentModel> paymentModelList;
    private Context mContext;
    private OnArtikelClickListener mOnArtikelClickListener;

    public RecyclerPayment(Context context, List<PaymentModel> paymentModels) {
        mContext = context;
        paymentModelList = paymentModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_payment, viewGroup, false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        ViewHolderCategory viewHolderCategory = (ViewHolderCategory) holder;
        final int position = i;
        final PaymentModel paymentModel = paymentModelList.get(i);

        viewHolderCategory.tvDate.setText(paymentModel.getDate());
        viewHolderCategory.tvStatus.setText(paymentModel.getStatus());

        if (paymentModel.getStatus().equals("pending")) {
            viewHolderCategory.ivCheck.setImageResource(R.drawable.ic_add_circle_red_24dp);
            viewHolderCategory.ivCheck.setRotation(45);
            viewHolderCategory.tvNominal.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        viewHolderCategory.tvNominal.setText(UtilsManager.convertLongToCurrencyIDv2WithoutRp(Double.parseDouble(paymentModel.getTotal())));

        viewHolderCategory.mViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnArtikelClickListener != null) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mOnArtikelClickListener != null)
                                mOnArtikelClickListener.onClick(position, paymentModel);
                        }
                    }, 250);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentModelList.size();
    }

    private class ViewHolderCategory extends RecyclerView.ViewHolder {
        public View mViewContainer;

        public CardView cardView;
        public TextView tvDate;
        public TextView tvStatus;
        public TextView tvNominal;
        public ImageView ivCheck;

        public ViewHolderCategory(View itemView) {
            super(itemView);
            mViewContainer = itemView;
            cardView = itemView.findViewById(R.id.cardView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvNominal = itemView.findViewById(R.id.tvNominal);
            ivCheck = itemView.findViewById(R.id.ivCheck);
        }
    }

    /**
     * interface ketika container di click
     */
    public interface OnArtikelClickListener {
        void onClick(int posisi, PaymentModel paymentModel);
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
