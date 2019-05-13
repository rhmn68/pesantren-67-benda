package com.madrasahdigital.walisantri.ppi67benda.model.riwayatspp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alhudaghifari on 14:19 13/05/19
 */
public class RiwayatSpp {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("nominal")
    @Expose
    private String nominal;
    @SerializedName("periode")
    @Expose
    private String periode;
    @SerializedName("bulan")
    @Expose
    private String bulan;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("payment_id")
    @Expose
    private Integer paymentId;
    @SerializedName("payment_url")
    @Expose
    private String paymentUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
