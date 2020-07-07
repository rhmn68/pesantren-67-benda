package com.madrasahdigital.walisantri.ppi67benda.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.Post
import com.madrasahdigital.walisantri.ppi67benda.notification.NotificationHelper


class ArticlesFirebaseMessagingServices : FirebaseMessagingService(){

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isNotEmpty()){
            val data = remoteMessage.data
            val dataString = data["post"]
            val post = Gson().fromJson(dataString, Post::class.java)

            NotificationHelper(this).showNotification(post)
        }

        if (remoteMessage.notification != null){
            val title = remoteMessage.notification?.title
            val desc = remoteMessage.notification?.body
            val image = remoteMessage.notification?.imageUrl
            val post = Post()
            post.id = remoteMessage.messageId
            post.title = title
            post.featuredImage = image.toString()
            post.slug = desc

            NotificationHelper(this).showNotification(post)
        }
    }

}