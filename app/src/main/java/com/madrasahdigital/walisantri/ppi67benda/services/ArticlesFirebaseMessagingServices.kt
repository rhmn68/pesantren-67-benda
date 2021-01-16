package com.madrasahdigital.walisantri.ppi67benda.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.Post
import com.madrasahdigital.walisantri.ppi67benda.notification.NotificationHelper
import com.madrasahdigital.walisantri.ppi67benda.notification.NotificationNewsVideoHelper


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

            when(remoteMessage.from){
                "/topics/articles-video"-> {
                    NotificationNewsVideoHelper(this).showNotification(post)
                }

                "/topics/articles-video-staging"-> {
                    NotificationNewsVideoHelper(this).showNotification(post)
                }

                "/topics/articles-staging"-> {
                    NotificationHelper(this).showNotification(post)
                }

                "/topics/articles"->{
                    NotificationHelper(this).showNotification(post)
                }
            }
        }
    }

}