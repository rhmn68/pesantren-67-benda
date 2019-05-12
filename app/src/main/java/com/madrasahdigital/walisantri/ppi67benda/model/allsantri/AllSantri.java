package com.madrasahdigital.walisantri.ppi67benda.model.allsantri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alhudaghifari on 23:59 12/05/19
 */
public class AllSantri {

    @SerializedName("santri")
    @Expose
    private List<Santrus> santri = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<Santrus> getSantri() {
        return santri;
    }

    public void setSantri(List<Santrus> santri) {
        this.santri = santri;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
