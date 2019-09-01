package com.madrasahdigital.walisantri.ppi67benda.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.model.slidebannermodel.Result;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.DetailNewsActivity;

import java.util.List;

/**
 * Created by Alhudaghifari on 13:07 01/09/19
 */
public class SlidingImageAdapter extends PagerAdapter {

    private List<Result> articleList;
    private LayoutInflater inflater;
    private Context context;

    public SlidingImageAdapter(Context context, List<Result> articleList) {
        this.context = context;
        this.articleList = articleList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_image_banner, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.ivImageBanner);

        Glide
                .with(context)
                .load(articleList.get(position).getFeaturedImage())
                .into(imageView);

        view.addView(imageLayout, 0);

        imageLayout.setOnClickListener(l -> {
            Intent intent = new Intent(context, DetailNewsActivity.class);
            intent.putExtra("urlberita", articleList.get(position).getUrl());
            context.startActivity(intent);
        });

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
