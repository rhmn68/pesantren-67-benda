package com.madrasahdigital.walisantri.ppi67benda.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.NewsModel
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.Post
import com.madrasahdigital.walisantri.ppi67benda.notification.NotificationArticleHelper
import com.madrasahdigital.walisantri.ppi67benda.notification.NotificationArticleHelper.Companion.NOTIFICATION_ID
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant
import com.madrasahdigital.walisantri.ppi67benda.utils.DateHelper
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class UpComingArticlesReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null){
            val dataString = intent.getStringExtra(EXTRA_DATA_ARTICLES)
            val gson = Gson()
            val newsModel = gson.fromJson(dataString, NewsModel::class.java)
            val posts = newsModel.posts

            for (i in 0 until posts.size){
                if (DateHelper.compareDateForShowArticles(posts[i].createdAt)){
                    SendNotification(context).execute(i.toString(), posts[i].title, posts[i].featuredImage)
                }
            }
        }
    }

    companion object {
        const val EXTRA_DATA_ARTICLES = "extra_data_news"
    }

    class SendNotification(private val context: Context): AsyncTask<String, Void, Bitmap>(){
        private var title = ""
        private var notificationId = 0

        override fun doInBackground(vararg params: String?): Bitmap? {
            notificationId = params[0]?.toInt()!!
            title = params[1].toString()
            if (params[2] != null){
                return getBitmapFromURL(params[2])!!
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            val notificationHelper = NotificationArticleHelper(context)
            val nb = notificationHelper.getChannelNotification(title, result!!)
            notificationHelper.getManager()?.notify(notificationId, nb?.build())
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