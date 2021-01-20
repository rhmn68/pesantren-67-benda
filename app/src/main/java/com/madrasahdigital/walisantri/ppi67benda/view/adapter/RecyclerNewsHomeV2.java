package com.madrasahdigital.walisantri.ppi67benda.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.Post;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager;

import java.util.List;

/**
 * Created by Alhudaghifari on 7:06 12/10/19
 */
public class RecyclerNewsHomeV2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = RecyclerNewsHomeV2.class.getSimpleName();
    private List<Post> postList;
    private Context mContext;
    private OnArtikelClickListener mOnArtikelClickListener;
    private int type;
    private int sizeList;
    private final int TAG_NORMAL_LIST = 1;
    private final int TAG_END_LIST = 2;

    public RecyclerNewsHomeV2(Context context, List<Post> postList, int type) {
        mContext = context;
        this.postList = postList;
        this.type = type;
        if (type == Constant.TYPE_NEWS_HOME)
            sizeList = postList.size();
        else
            sizeList = postList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(mContext).inflate(R.layout.item_news_v2, parent, false);
        return new ViewHolderCategory(view);
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        return TAG_NORMAL_LIST;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        int viewCode = getItemViewType(i);
        if (viewCode == TAG_NORMAL_LIST) {
            ViewHolderCategory viewHolderCategory = (ViewHolderCategory) holder;
            final int position = i;
            final Post post = postList.get(position);

            String title = post.getTitle();
            if (type == Constant.TYPE_NEWS_PAGE) {
                if (title.length() > 90) {
                    title = title.substring(0, 90);
                    title += "...";
                }
            } else {
                if (title.length() > 50) {
                    title = title.substring(0, 50);
                    title += "...";
                }
            }

            viewHolderCategory.tvTitleNews.setText(title);
            if (post.getPublishedAt() != null)
                viewHolderCategory.tvTanggal.setText(UtilsManager.getDateAnotherFormatFromString2(String.valueOf(post.getPublishedAt())));
            else
                viewHolderCategory.tvTanggal.setText("");

            Glide
                    .with(mContext)
                    .load(post.getFeaturedImage())
                    .centerCrop()
                    .placeholder(R.drawable.bg_silver)
                    .error(R.drawable.bg_silver)
                    .into(viewHolderCategory.ivThumbnailNews);

            viewHolderCategory.mViewContainer.setOnClickListener(view -> {
                if (mOnArtikelClickListener != null) {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        if (mOnArtikelClickListener != null)
                            mOnArtikelClickListener.onClick(position, post);
                    }, 250);
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

        public ImageView ivThumbnailNews;
        public TextView tvTitleNews;
        public TextView tvTanggal;

        public ViewHolderCategory(View itemView) {
            super(itemView);
            mViewContainer = itemView;
            ivThumbnailNews = itemView.findViewById(R.id.ivThumbnailNews);
            tvTitleNews = itemView.findViewById(R.id.tvTitleNews);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
        }
    }

    /**
     * interface ketika container di click
     */
    public interface OnArtikelClickListener {
        void onClick(int posisi, Post post);
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
