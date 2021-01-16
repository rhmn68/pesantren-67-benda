package com.madrasahdigital.walisantri.ppi67benda.model.tagihanspp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alhudaghifari on 23:00 13/05/19
 */
public class TagihanSppModel {

    @SerializedName("santri_id")
    @Expose
    private Integer santriId;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("kelas")
    @Expose
    private String kelas;
    @SerializedName("unpaid")
    @Expose
    private List<Unpaid> unpaid = null;

    public Integer getSantriId() {
        return santriId;
    }

    public void setSantriId(Integer santriId) {
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

    public List<Unpaid> getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(List<Unpaid> unpaid) {
        this.unpaid = unpaid;
    }
}
