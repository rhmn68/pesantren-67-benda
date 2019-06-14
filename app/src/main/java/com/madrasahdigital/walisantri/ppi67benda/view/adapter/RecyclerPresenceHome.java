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
import com.madrasahdigital.walisantri.ppi67benda.model.presence.Presence;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Alhudaghifari on 20:29 14/06/19
 */
public class RecyclerPresenceHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = RecyclerPresenceHome.class.getSimpleName();
    private List<Presence> presenceList;
    private Context mContext;
    private OnArtikelClickListener mOnArtikelClickListener;

    public RecyclerPresenceHome(Context context, List<Presence> presenceList) {
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
        viewHolderCategory.tvStatusPresence.setText(presenceList.get(position).getStatus());
        viewHolderCategory.tvKeteranganAbsen.setText(presenceList.get(position).getDescription());

        if (presenceList.get(i).getSantriName().length() > 1) {
            viewHolderCategory.tvFirstCharForImageProfil.setText(presenceList.get(i).getSantriName().charAt(0) + "");
        }

        // TODO Delete i == 0
        if (presenceList.get(i).getStatus().equals("present") && i == 0) {
            viewHolderCategory.tvStatusPresence.setBackground(mContext.getResources().getDrawable(R.drawable.btn_green));
            viewHolderCategory.tvKeteranganAbsen.setTextColor(mContext.getResources().getColor(R.color.greytextcolor2));
        } else {
            // TODO also delete this else
            viewHolderCategory.tvStatusPresence.setText("Absen");
            viewHolderCategory.tvKeteranganAbsen.setText("Tanpa keterangan");
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
        void onClick(int posisi, Presence presence);
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
