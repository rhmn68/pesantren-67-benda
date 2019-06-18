package com.madrasahdigital.walisantri.ppi67benda.model.datasantri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alhudaghifari on 23:39 18/06/19
 */
public class DataAssignSantri {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nis")
    @Expose
    private String nis;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("url_confirm")
    @Expose
    private String urlConfirm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUrlConfirm() {
        return urlConfirm;
    }

    public void setUrlConfirm(String urlConfirm) {
        this.urlConfirm = urlConfirm;
    }
}
