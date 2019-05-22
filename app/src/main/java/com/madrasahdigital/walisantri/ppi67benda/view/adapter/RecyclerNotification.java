package com.madrasahdigital.walisantri.ppi67benda.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.notification.NotificationModel;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import java.util.List;

/**
 * Created by Alhudaghifari on 13:57 22/05/19
 */
public class RecyclerNotification extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = RecyclerNotification.class.getSimpleName();
    private List<NotificationModel> notificationModelList;
    private Context mContext;
    private OnArtikelClickListener mOnArtikelClickListener;

    public RecyclerNotification(Context context, List<NotificationModel> notificationModels) {
        mContext = context;
        notificationModelList = notificationModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_notif, viewGroup, false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderCategory viewHolderCategory = (ViewHolderCategory) viewHolder;
        final int position = i;
        final NotificationModel notificationModel = notificationModelList.get(position);

        viewHolderCategory.tvSubject.setText(notificationModel.getSubject());
        viewHolderCategory.tvDate.setText(UtilsManager.getDateAnotherFormatFromString(notificationModel.getDate()));

        viewHolderCategory.mViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnArtikelClickListener != null) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mOnArtikelClickListener != null)
                                mOnArtikelClickListener.onClick(position, notificationModel);
                        }
                    }, 250);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    private class ViewHolderCategory extends RecyclerView.ViewHolder {
        public View mViewContainer;

        public TextView tvSubject;
        public TextView tvDate;

        public ViewHolderCategory(View itemView) {
            super(itemView);
            mViewContainer = itemView;
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }


    /**
     * interface ketika container di click
     */
    public interface OnArtikelClickListener {
        void onClick(int posisi, NotificationModel notificationModel);
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
