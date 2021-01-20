
package com.madrasahdigital.walisantri.ppi67benda.model.tagihanallsantri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Student_ {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nis")
    @Expose
    private String nis;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("class_id")
    @Expose
    private String classId;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("level_id")
    @Expose
    private String levelId;
    @SerializedName("level_name")
    @Expose
    private String levelName;
    @SerializedName("url")
    @Expose
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
