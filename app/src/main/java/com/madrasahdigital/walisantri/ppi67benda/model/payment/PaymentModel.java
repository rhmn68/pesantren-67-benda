package com.madrasahdigital.walisantri.ppi67benda.model.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alhudaghifari on 14:25 13/05/19
 */
public class Payment {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("url")
    @Expose
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
