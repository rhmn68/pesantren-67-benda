package com.madrasahdigital.walisantri.ppi67benda.model.detailpayment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alhudaghifari on 13:28 22/06/19
 */
public class DetailPaymentModel implements Parcelable {

    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("subtotal")
    @Expose
    private Integer subtotal;
    @SerializedName("payment_fee")
    @Expose
    private Integer paymentFee;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("confirm_url")
    @Expose
    private String confirmUrl;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(Integer paymentFee) {
        this.paymentFee = paymentFee;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getConfirmUrl() {
        return confirmUrl;
    }

    public void setConfirmUrl(String confirmUrl) {
        this.confirmUrl = confirmUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.items);
        dest.writeValue(this.subtotal);
        dest.writeValue(this.paymentFee);
        dest.writeValue(this.total);
        dest.writeString(this.confirmUrl);
    }

    public DetailPaymentModel() {
    }

    protected DetailPaymentModel(Parcel in) {
        this.items = new ArrayList<Item>();
        in.readList(this.items, Item.class.getClassLoader());
        this.subtotal = (Integer) in.readValue(Integer.class.getClassLoader());
        this.paymentFee = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total = (Integer) in.readValue(Integer.class.getClassLoader());
        this.confirmUrl = in.readString();
    }

    public static final Parcelable.Creator<DetailPaymentModel> CREATOR = new Parcelable.Creator<DetailPaymentModel>() {
        @Override
        public DetailPaymentModel createFromParcel(Parcel source) {
            return new DetailPaymentModel(source);
        }

        @Override
        public DetailPaymentModel[] newArray(int size) {
            return new DetailPaymentModel[size];
        }
    };
}
