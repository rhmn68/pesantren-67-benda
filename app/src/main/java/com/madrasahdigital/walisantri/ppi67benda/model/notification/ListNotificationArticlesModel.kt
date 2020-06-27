package com.madrasahdigital.walisantri.ppi67benda.model.notification

import com.google.gson.annotations.SerializedName

data class ListNotificationArticlesModel(
        @SerializedName("articles_notification")
        var listNotificationArticles: List<NotificationArticlesModel>? = null
)