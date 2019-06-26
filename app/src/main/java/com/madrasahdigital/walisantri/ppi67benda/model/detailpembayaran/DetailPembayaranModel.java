package com.madrasahdigital.walisantri.ppi67benda.model.detailpembayaran;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alhudaghifari on 20:07 26/06/19
 */
public class DetailPembayaranModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_code")
    @Expose
    private String orderCode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("gross_amount")
    @Expose
    private String grossAmount;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("instruction")
    @Expose
    private Instruction instruction;
    @SerializedName("settlement_url")
    @Expose
    private String settlementUrl;
    @SerializedName("remove_url")
    @Expose
    private String removeUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public String getSettlementUrl() {
        return settlementUrl;
    }

    public void setSettlementUrl(String settlementUrl) {
        this.settlementUrl = settlementUrl;
    }

    public String getRemoveUrl() {
        return removeUrl;
    }

    public void setRemoveUrl(String removeUrl) {
        this.removeUrl = removeUrl;
    }
}
