package com.madrasahdigital.walisantri.ppi67benda.model.notification

import com.google.gson.annotations.SerializedName
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.Post

data class NotificationArticlesModel(
        @SerializedName("is_read")
        var isRead: Boolean = false,

        @SerializedName("posts")
        var posts: Post? = null
)