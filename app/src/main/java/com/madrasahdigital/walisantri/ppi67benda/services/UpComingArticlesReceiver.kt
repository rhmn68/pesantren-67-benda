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
import com.madrasahdigital.walisantri.ppi67benda.notification.NotificationHelper
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant
import com.madrasahdigital.walisantri.ppi67benda.utils.DateHelper
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class UpComingArticlesReceiver : BroadcastReceiver() {
    private lateinit var sharedPrefManager: SharedPrefManager
    override fun onReceive(context: Context, intent: Intent?) {
        sharedPrefManager = SharedPrefManager(context)
        if (intent != null){
            val dataString = intent.getStringExtra(EXTRA_DATA_ARTICLES)
            val gson = Gson()
            val newsModel = gson.fromJson(dataString, NewsModel::class.java)
            val posts = newsModel.posts
            var lastAccessDate = sharedPrefManager.lastAccessDate

            if (lastAccessDate.isEmpty()){
                sharedPrefManager.lastAccessDate = DateHelper.getCurrentDate()
                lastAccessDate = sharedPrefManager.lastAccessDate
            }

            Log.d("coba", "last access date: $lastAccessDate")

            for (i in 0 until posts.size){
                Log.d("coba", "published at : ${posts[i].publishedAt}")
                if (DateHelper.compareDateForShowArticles(posts[i].publishedAt.toString(), lastAccessDate)){
                    SendNotification(context).execute(i, posts[i], posts[i].featuredImage)
                }
            }

            if (DateHelper.compareDateAfter(lastAccessDate)){
                sharedPrefManager.lastAccessDate = DateHelper.getCurrentDate()
            }
        }

        context.startService(intent)
    }

    companion object {
        const val EXTRA_DATA_ARTICLES = "extra_data_news"
    }

    class SendNotification(private val context: Context): AsyncTask<Any, Void, Bitmap>(){
        private lateinit var post: Post
        private var notificationId = 0

        override fun doInBackground(vararg params: Any?): Bitmap? {
            notificationId = params[0] as Int
            post = params[1] as Post
            val image = params[2] as String
            if (params[2] != null){
                return getBitmapFromURL(image)
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            val notificationHelper = NotificationArticleHelper(context)
            val nb = notificationHelper.getChannelNotification(post, result!!)
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