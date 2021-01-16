
package com.madrasahdigital.walisantri.ppi67benda.model.tagihanallsantri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TagihanAllSantriModel {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("students")
    @Expose
    private List<Student> students = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

}
