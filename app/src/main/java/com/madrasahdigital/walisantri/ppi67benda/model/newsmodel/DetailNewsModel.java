package com.madrasahdigital.walisantri.ppi67benda.model.newsmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alhudaghifari on 16:11 22/06/19
 */
public class DetailNewsModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("disqus_id")
    @Expose
    private Object disqusId;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("intro")
    @Expose
    private Object intro;
    @SerializedName("featured")
    @Expose
    private Object featured;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("total_seen")
    @Expose
    private String totalSeen;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("template")
    @Expose
    private String template;
    @SerializedName("featured_image")
    @Expose
    private String featuredImage;
    @SerializedName("published_at")
    @Expose
    private Object publishedAt;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("post_id")
    @Expose
    private Object postId;
    @SerializedName("event_status")
    @Expose
    private Object eventStatus;
    @SerializedName("event_kuota")
    @Expose
    private Object eventKuota;
    @SerializedName("event_tanggal")
    @Expose
    private Object eventTanggal;
    @SerializedName("event_tanggal_berakhir")
    @Expose
    private Object eventTanggalBerakhir;
    @SerializedName("event_waktu")
    @Expose
    private Object eventWaktu;
    @SerializedName("event_kota")
    @Expose
    private Object eventKota;
    @SerializedName("event_lokasi")
    @Expose
    private Object eventLokasi;
    @SerializedName("event_url_map")
    @Expose
    private Object eventUrlMap;
    @SerializedName("event_link")
    @Expose
    private Object eventLink;
    @SerializedName("event_poster")
    @Expose
    private Object eventPoster;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("embed_video")
    private String embedVideo;

    public String getEmbedVideo() {
        return embedVideo;
    }

    public void setEmbedVideo(String embedVideo) {
        this.embedVideo = embedVideo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getDisqusId() {
        return disqusId;
    }

    public void setDisqusId(Object disqusId) {
        this.disqusId = disqusId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getIntro() {
        return intro;
    }

    public void setIntro(Object intro) {
        this.intro = intro;
    }

    public Object getFeatured() {
        return featured;
    }

    public void setFeatured(Object featured) {
        this.featured = featured;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTotalSeen() {
        return totalSeen;
    }

    public void setTotalSeen(String totalSeen) {
        this.totalSeen = totalSeen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public Object getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Object publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getPostId() {
        return postId;
    }

    public void setPostId(Object postId) {
        this.postId = postId;
    }

    public Object getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Object eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Object getEventKuota() {
        return eventKuota;
    }

    public void setEventKuota(Object eventKuota) {
        this.eventKuota = eventKuota;
    }

    public Object getEventTanggal() {
        return eventTanggal;
    }

    public void setEventTanggal(Object eventTanggal) {
        this.eventTanggal = eventTanggal;
    }

    public Object getEventTanggalBerakhir() {
        return eventTanggalBerakhir;
    }

    public void setEventTanggalBerakhir(Object eventTanggalBerakhir) {
        this.eventTanggalBerakhir = eventTanggalBerakhir;
    }

    public Object getEventWaktu() {
        return eventWaktu;
    }

    public void setEventWaktu(Object eventWaktu) {
        this.eventWaktu = eventWaktu;
    }

    public Object getEventKota() {
        return eventKota;
    }

    public void setEventKota(Object eventKota) {
        this.eventKota = eventKota;
    }

    public Object getEventLokasi() {
        return eventLokasi;
    }

    public void setEventLokasi(Object eventLokasi) {
        this.eventLokasi = eventLokasi;
    }

    public Object getEventUrlMap() {
        return eventUrlMap;
    }

    public void setEventUrlMap(Object eventUrlMap) {
        this.eventUrlMap = eventUrlMap;
    }

    public Object getEventLink() {
        return eventLink;
    }

    public void setEventLink(Object eventLink) {
        this.eventLink = eventLink;
    }

    public Object getEventPoster() {
        return eventPoster;
    }

    public void setEventPoster(Object eventPoster) {
        this.eventPoster = eventPoster;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
