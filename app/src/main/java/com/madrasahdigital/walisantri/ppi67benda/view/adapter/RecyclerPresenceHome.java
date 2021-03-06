package com.madrasahdigital.walisantri.ppi67benda.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.presence.Presensi;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Alhudaghifari on 20:29 14/06/19
 */
public class RecyclerPresenceHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = RecyclerPresenceHome.class.getSimpleName();
    private List<Presensi> presenceList;
    private Context mContext;
    private OnArtikelClickListener mOnArtikelClickListener;

    public RecyclerPresenceHome(Context context, List<Presensi> presenceList) {
        mContext = context;
        this.presenceList = presenceList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_presence, parent, false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        ViewHolderCategory viewHolderCategory = (ViewHolderCategory) holder;
        final int position = i;

        viewHolderCategory.tvSantriName.setText(presenceList.get(position).getSantriName());
        viewHolderCategory.tvKeteranganAbsen.setText(presenceList.get(position).getDescription());

        if (presenceList.get(i).getUrlPhoto() != null) {
            viewHolderCategory.tvFirstCharForImageProfil.setText("");
            Glide
                    .with(mContext)
                    .load(presenceList.get(i).getUrlPhoto())
                    .centerCrop()
                    .placeholder(R.drawable.bg_silver)
                    .error(R.drawable.bg_silver)
                    .into(viewHolderCategory.civPhotoProfile);
        } else {
            if (presenceList.get(i).getSantriName().length() > 1) {
                viewHolderCategory.tvFirstCharForImageProfil.setText(presenceList.get(i).getSantriName().charAt(0) + "");
            }
        }

        Log.d(TAG, "oke : " + presenceList.get(i).getStatus());

        if (presenceList.get(i).getStatus().toLowerCase().equals("present")) {
            viewHolderCategory.tvStatusPresence.setText("Hadir");
            viewHolderCategory.tvStatusPresence.setBackground(mContext.getResources().getDrawable(R.drawable.btn_green));
            viewHolderCategory.tvStatusPresence.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolderCategory.tvKeteranganAbsen.setTextColor(mContext.getResources().getColor(R.color.greytextcolor2));
        } else if (presenceList.get(i).getStatus().toLowerCase().equals("ill")) {
            viewHolderCategory.tvStatusPresence.setText("Sakit");
            viewHolderCategory.tvStatusPresence.setBackground(mContext.getResources().getDrawable(R.drawable.btn_red));
            viewHolderCategory.tvStatusPresence.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolderCategory.tvKeteranganAbsen.setTextColor(mContext.getResources().getColor(R.color.colorIll));
        } else if (presenceList.get(i).getStatus().toLowerCase().equals("permit")) {
            viewHolderCategory.tvStatusPresence.setText("Izin");
            viewHolderCategory.tvStatusPresence.setBackground(mContext.getResources().getDrawable(R.drawable.btn_orange));
            viewHolderCategory.tvStatusPresence.setTextColor(mContext.getResources().getColor(R.color.greytextcolor3));
            viewHolderCategory.tvKeteranganAbsen.setTextColor(mContext.getResources().getColor(R.color.colorPermit));
        } else if (presenceList.get(i).getStatus().toLowerCase().equals("failed")) {
            viewHolderCategory.tvStatusPresence.setText("Belum ada data");
            viewHolderCategory.tvStatusPresence.setBackground(mContext.getResources().getDrawable(R.drawable.btn_silver));
            viewHolderCategory.tvStatusPresence.setTextColor(mContext.getResources().getColor(R.color.greytextcolor3));
            viewHolderCategory.tvKeteranganAbsen.setText("");
        } else {
            viewHolderCategory.tvStatusPresence.setText("Absen");
            viewHolderCategory.tvStatusPresence.setBackground(mContext.getResources().getDrawable(R.drawable.btn_red));
            viewHolderCategory.tvStatusPresence.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolderCategory.tvKeteranganAbsen.setText("Tanpa keterangan");
            viewHolderCategory.tvKeteranganAbsen.setTextColor(mContext.getResources().getColor(R.color.colorIll));
        }

        viewHolderCategory.mViewContainer.setOnClickListener(view -> {
            if (mOnArtikelClickListener != null) {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (mOnArtikelClickListener != null)
                        mOnArtikelClickListener.onClick(position, presenceList.get(position));
                }, 250);
            }
        });
    }

    @Override
    public int getItemCount() {
        return presenceList.size();
    }

    private class ViewHolderCategory extends RecyclerView.ViewHolder {
        public View mViewContainer;

        public CircleImageView civPhotoProfile;
        public TextView tvSantriName;
        public TextView tvStatusPresence;
        public TextView tvKeteranganAbsen;
        public TextView tvFirstCharForImageProfil;

        public ViewHolderCategory(View itemView) {
            super(itemView);
            mViewContainer = itemView;
            civPhotoProfile = itemView.findViewById(R.id.civProfPic);
            tvFirstCharForImageProfil = itemView.findViewById(R.id.tvFirstCharForImageProfil);
            tvSantriName = itemView.findViewById(R.id.tvSantriName);
            tvStatusPresence = itemView.findViewById(R.id.tvStatusPresence);
            tvKeteranganAbsen = itemView.findViewById(R.id.tvKeteranganAbsen);
        }
    }

    /**
     * interface ketika container di click
     */
    public interface OnArtikelClickListener {
        void onClick(int posisi, Presensi presence);
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
