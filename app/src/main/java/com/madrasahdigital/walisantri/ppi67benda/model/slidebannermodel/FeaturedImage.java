package com.madrasahdigital.walisantri.ppi67benda.model.slidebannermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alhudaghifari on 21:45 01/09/19
 */
public class FeaturedImage {
    @SerializedName("original")
    @Expose
    private String original;
    @SerializedName("thumb_200x200")
    @Expose
    private String thumb200x200;
    @SerializedName("thumb_250x150")
    @Expose
    private String thumb250x150;
    @SerializedName("thumb_270x135")
    @Expose
    private String thumb270x135;
    @SerializedName("thumb_340x195")
    @Expose
    private String thumb340x195;
    @SerializedName("thumb_700x350")
    @Expose
    private String thumb700x350;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getThumb200x200() {
        return thumb200x200;
    }

    public void setThumb200x200(String thumb200x200) {
        this.thumb200x200 = thumb200x200;
    }

    public String getThumb250x150() {
        return thumb250x150;
    }

    public void setThumb250x150(String thumb250x150) {
        this.thumb250x150 = thumb250x150;
    }

    public String getThumb270x135() {
        return thumb270x135;
    }

    public void setThumb270x135(String thumb270x135) {
        this.thumb270x135 = thumb270x135;
    }

    public String getThumb340x195() {
        return thumb340x195;
    }

    public void setThumb340x195(String thumb340x195) {
        this.thumb340x195 = thumb340x195;
    }

    public String getThumb700x350() {
        return thumb700x350;
    }

    public void setThumb700x350(String thumb700x350) {
        this.thumb700x350 = thumb700x350;
    }
}
