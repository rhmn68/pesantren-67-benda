package com.madrasahdigital.walisantri.ppi67benda.model.datasantri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alhudaghifari on 23:39 18/06/19
 */
public class AssignSantriModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private DataAssignSantri data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataAssignSantri getData() {
        return data;
    }

    public void setData(DataAssignSantri data) {
        this.data = data;
    }

}