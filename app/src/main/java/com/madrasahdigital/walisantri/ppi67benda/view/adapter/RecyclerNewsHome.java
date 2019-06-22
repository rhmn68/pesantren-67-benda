package com.madrasahdigital.walisantri.ppi67benda.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.Post;
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant;

import java.util.List;

/**
 * Created by Alhudaghifari on 10:33 15/06/19
 */
public class RecyclerNewsHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = RecyclerNewsHome.class.getSimpleName();
    private List<Post> postList;
    private Context mContext;
    private OnArtikelClickListener mOnArtikelClickListener;
    private int type;

    public RecyclerNewsHome(Context context, List<Post> postList, int type) {
        mContext = context;
        this.postList = postList;
        this.type = type;
    }
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (type == Constant.TYPE_NEWS_HOME)
            view = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false);
        else
            view = LayoutInflater.from(mContext).inflate(R.layout.item_news_large, parent, false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        ViewHolderCategory viewHolderCategory = (ViewHolderCategory) holder;
        final int position = i;
        final Post post = postList.get(position);

        viewHolderCategory.tvTitleNews.setText(post.getTitle());
        if (post.getIntro() != null)
        viewHolderCategory.tvDescriptionNews.setText(post.getIntro());
        else
            viewHolderCategory.tvDescriptionNews.setText("");

        Glide
                .with(mContext)
                .load(post.getFeaturedImage())
                .centerCrop()
                .placeholder(R.drawable.round_silver)
                .error(R.drawable.round_silver)
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

        viewHolderCategory.btnMore.setOnClickListener(view -> {
            if (mOnArtikelClickListener != null) {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (mOnArtikelClickListener != null)
                        mOnArtikelClickListener.onClick(position, post);
                }, 250);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    private class ViewHolderCategory extends RecyclerView.ViewHolder {
        public View mViewContainer;

        public ImageView ivThumbnailNews;
        public TextView tvTitleNews;
        public TextView tvDescriptionNews;
        public Button btnMore;

        public ViewHolderCategory(View itemView) {
            super(itemView);
            mViewContainer = itemView;
            ivThumbnailNews = itemView.findViewById(R.id.ivThumbnailNews);
            btnMore = itemView.findViewById(R.id.btnMore);
            tvTitleNews = itemView.findViewById(R.id.tvTitleNews);
            tvDescriptionNews = itemView.findViewById(R.id.tvDescriptionNews);
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
