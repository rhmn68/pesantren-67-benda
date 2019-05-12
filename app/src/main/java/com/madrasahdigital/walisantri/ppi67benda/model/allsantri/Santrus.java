package com.madrasahdigital.walisantri.ppi67benda.model.allsantri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alhudaghifari on 23:58 12/05/19
 */
public class Santrus {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("place_of_birth")
    @Expose
    private String placeOfBirth;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("family_role")
    @Expose
    private String familyRole;
    @SerializedName("family_order")
    @Expose
    private String familyOrder;
    @SerializedName("brother_total")
    @Expose
    private String brotherTotal;
    @SerializedName("stepbrother_total")
    @Expose
    private String stepbrotherTotal;
    @SerializedName("stepsibling_total")
    @Expose
    private String stepsiblingTotal;
    @SerializedName("school_origin")
    @Expose
    private String schoolOrigin;
    @SerializedName("school_origin_address")
    @Expose
    private String schoolOriginAddress;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("date_join")
    @Expose
    private String dateJoin;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("url")
    @Expose
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFamilyRole() {
        return familyRole;
    }

    public void setFamilyRole(String familyRole) {
        this.familyRole = familyRole;
    }

    public String getFamilyOrder() {
        return familyOrder;
    }

    public void setFamilyOrder(String familyOrder) {
        this.familyOrder = familyOrder;
    }

    public String getBrotherTotal() {
        return brotherTotal;
    }

    public void setBrotherTotal(String brotherTotal) {
        this.brotherTotal = brotherTotal;
    }

    public String getStepbrotherTotal() {
        return stepbrotherTotal;
    }

    public void setStepbrotherTotal(String stepbrotherTotal) {
        this.stepbrotherTotal = stepbrotherTotal;
    }

    public String getStepsiblingTotal() {
        return stepsiblingTotal;
    }

    public void setStepsiblingTotal(String stepsiblingTotal) {
        this.stepsiblingTotal = stepsiblingTotal;
    }

    public String getSchoolOrigin() {
        return schoolOrigin;
    }

    public void setSchoolOrigin(String schoolOrigin) {
        this.schoolOrigin = schoolOrigin;
    }

    public String getSchoolOriginAddress() {
        return schoolOriginAddress;
    }

    public void setSchoolOriginAddress(String schoolOriginAddress) {
        this.schoolOriginAddress = schoolOriginAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateJoin() {
        return dateJoin;
    }

    public void setDateJoin(String dateJoin) {
        this.dateJoin = dateJoin;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
