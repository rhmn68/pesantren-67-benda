package com.madrasahdigital.walisantri.ppi67benda.model.newsmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alhudaghifari on 15:06 22/06/19
 */
public class Post implements Serializable {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("featured_image")
    @Expose
    private String featuredImage;
    @SerializedName("intro")
    @Expose
    private String intro;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("featured")
    @Expose
    private Object featured;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("published_at")
    @Expose
    private Object publishedAt;
    @SerializedName("category")
    @Expose
    private Object category;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("embed_video")
    private String embedVideo;

    public String getEmbedVideo() {
        return embedVideo;
    }

    public void setEmbedVideo(String embedVideo) {
        this.embedVideo = embedVideo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Object getFeatured() {
        return featured;
    }

    public void setFeatured(Object featured) {
        this.featured = featured;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Object publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
