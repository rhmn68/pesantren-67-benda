package com.madrasahdigital.walisantri.ppi67benda.model;

/**
 * Created by Alhudaghifari on 21:43 21/06/19
 */
public class PaymentSelectedModel {
    private String santriId;
    private String billingId;

    public PaymentSelectedModel() {
    }

    public PaymentSelectedModel(String santriId, String billingId) {
        this.santriId = santriId;
        this.billingId = billingId;
    }

    public String getSantriId() {
        return santriId;
    }

    public void setSantriId(String santriId) {
        this.santriId = santriId;
    }

    public String getBillingId() {
        return billingId;
    }

    public void setBillingId(String billingId) {
        this.billingId = billingId;
    }
}
