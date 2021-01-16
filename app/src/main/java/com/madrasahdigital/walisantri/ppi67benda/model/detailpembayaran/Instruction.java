package com.madrasahdigital.walisantri.ppi67benda.model.detailpembayaran;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alhudaghifari on 20:07 26/06/19
 */
public class Instruction {
    @SerializedName("bank")
    @Expose
    private String bank;
    @SerializedName("norek")
    @Expose
    private String norek;
    @SerializedName("an")
    @Expose
    private String an;
    @SerializedName("url_wa")
    @Expose
    private String urlWa;

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNorek() {
        return norek;
    }

    public void setNorek(String norek) {
        this.norek = norek;
    }

    public String getAn() {
        return an;
    }

    public void setAn(String an) {
        this.an = an;
    }

    public String getUrlWa() {
        return urlWa;
    }

    public void setUrlWa(String urlWa) {
        this.urlWa = urlWa;
    }

}
