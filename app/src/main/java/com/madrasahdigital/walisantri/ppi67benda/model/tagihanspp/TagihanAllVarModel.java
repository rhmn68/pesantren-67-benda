package com.madrasahdigital.walisantri.ppi67benda.model.tagihanspp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alhudaghifari on 1:39 14/05/19
 */
public class TagihanAllVarModel {
    @SerializedName("santri_id")
    @Expose
    private String santriId;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("kelas")
    @Expose
    private String kelas;
    @SerializedName("id")
    @Expose
    private String idBill;
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
    private String dueDate;

    public TagihanAllVarModel() {
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getSantriId() {
        return santriId;
    }

    public void setSantriId(String santriId) {
        this.santriId = santriId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String id) {
        this.idBill = id;
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
}
