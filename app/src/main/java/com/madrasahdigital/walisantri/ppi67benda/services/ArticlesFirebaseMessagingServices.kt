package com.madrasahdigital.walisantri.ppi67benda.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.madrasahdigital.walisantri.ppi67benda.notification.NotificationHelper
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ArticlesFirebaseMessagingServices : FirebaseMessagingService(){

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("coba", "The token refreshed: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.notification != null){
            val title = remoteMessage.notification?.title
            val desc = remoteMessage.notification?.body
            val image = remoteMessage.notification?.imageUrl

            SendNotification(this).execute(1, desc, image)
        }
    }

    class SendNotification(private val context: Context): AsyncTask<Any, Void, Bitmap>(){
        private var title = ""
        private var notificationId = 0
        private lateinit var uri: Uri

        override fun doInBackground(vararg params: Any?): Bitmap? {
            notificationId = params[0] as Int
            title = params[1].toString()
            uri = params[2] as Uri
            if (params[2] != null){
                return getBitmapFromURL(uri.toString())
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            NotificationHelper(context).showNotification(title, result)
        }

        private fun getBitmapFromURL(src: String?): Bitmap? {
            return try {
                val url = URL(src)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                Log.e(Constant.TAG, "error: ${e.message}")
                null
            }
        }
    }
}