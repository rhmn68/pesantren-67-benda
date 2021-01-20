
package com.madrasahdigital.walisantri.ppi67benda.model.tagihanallsantri;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bills {

    @SerializedName("student")
    @Expose
    private Student_ student;
    @SerializedName("bill_items")
    @Expose
    private List<BillItem> billItems = null;
    @SerializedName("subtotal")
    @Expose
    private Integer subtotal;

    public Student_ getStudent() {
        return student;
    }

    public void setStudent(Student_ student) {
        this.student = student;
    }

    public List<BillItem> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<BillItem> billItems) {
        this.billItems = billItems;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

}
