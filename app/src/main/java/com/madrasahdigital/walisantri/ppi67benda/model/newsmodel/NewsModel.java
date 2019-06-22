package com.madrasahdigital.walisantri.ppi67benda.model.newsmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alhudaghifari on 15:06 22/06/19
 */
public class NewsModel {
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;
    @SerializedName("pagination")
    @Expose
    private String pagination;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getPagination() {
        return pagination;
    }

    public void setPagination(String pagination) {
        this.pagination = pagination;
    }
}
