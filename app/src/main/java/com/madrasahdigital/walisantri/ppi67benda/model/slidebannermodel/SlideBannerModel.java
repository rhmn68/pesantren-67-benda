package com.madrasahdigital.walisantri.ppi67benda.model.slidebannermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alhudaghifari on 14:22 01/09/19
 */
public class SlideBannerModel {

    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("pagination")
    @Expose
    private String pagination;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getPagination() {
        return pagination;
    }

    public void setPagination(String pagination) {
        this.pagination = pagination;
    }
}
