package com.madrasahdigital.walisantri.ppi67benda.model.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Alhudaghifari on 11:16 22/05/19
 */
data class NotificationModel(
    @SerializedName("id")
    @Expose
    var id: Int = 0,

    @SerializedName("subject")
    @Expose
    var subject: String? = null,

    @SerializedName("date")
    @Expose
    var date: String? = null,

    @SerializedName("read")
    @Expose
    var isRead: Boolean = false,

    @SerializedName("url")
    @Expose
    var url: String? = null
)