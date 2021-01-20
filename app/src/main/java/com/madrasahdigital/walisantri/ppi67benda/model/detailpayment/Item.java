package com.madrasahdigital.walisantri.ppi67benda.model.detailpayment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alhudaghifari on 13:28 22/06/19
 */
public class Item implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.studentId);
        dest.writeString(this.billingId);
        dest.writeString(this.productName);
        dest.writeString(this.amount);
    }

    public Item() {
    }

    protected Item(Parcel in) {
        this.studentId = in.readString();
        this.billingId = in.readString();
        this.productName = in.readString();
        this.amount = in.readString();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
