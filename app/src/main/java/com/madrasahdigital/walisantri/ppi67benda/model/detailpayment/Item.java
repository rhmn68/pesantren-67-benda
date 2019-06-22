package com.madrasahdigital.walisantri.ppi67benda.model.detailpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alhudaghifari on 13:28 22/06/19
 */
public class Item {
    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("billing_id")
    @Expose
    private String billingId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getBillingId() {
        return billingId;
    }

    public void setBillingId(String billingId) {
        this.billingId = billingId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
