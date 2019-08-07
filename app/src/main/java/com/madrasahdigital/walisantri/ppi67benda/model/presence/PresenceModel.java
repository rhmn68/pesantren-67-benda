package com.madrasahdigital.walisantri.ppi67benda.model.presence;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alhudaghifari on 21:10 07/08/19
 */
public class PresenceModel {
    @SerializedName("presensi")
    @Expose
    private List<Presensi> presensi = null;

    public List<Presensi> getPresensi() {
        return presensi;
    }

    public void setPresensi(List<Presensi> presensi) {
        this.presensi = presensi;
    }
}
