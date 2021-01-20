package com.madrasahdigital.walisantri.ppi67benda.model.detailpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alhudaghifari on 13:15 26/06/19
 */
public class KonfirmasiCheckoutModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_code")
    @Expose
    private String orderCode;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("url")
    @Expose
    private String url;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
