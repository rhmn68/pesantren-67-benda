package com.madrasahdigital.walisantri.ppi67benda.services

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.NewsModel
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.Post
import com.madrasahdigital.walisantri.ppi67benda.notification.NotificationArticleHelper
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant
import com.madrasahdigital.walisantri.ppi67benda.utils.DateHelper
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class NewArticleJobService : JobService() {

    companion object{
        private val TAG = NewArticleJobService::class.java.simpleName
    }

    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        sharedPrefManager = SharedPrefManager(this)

        GetNewsArticle(params, sharedPrefManager, this, this).execute()
        return true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_NOT_STICKY
    }

    class GetNewsArticle(params: JobParameters?, sharedPrefManager: SharedPrefManager, jobService: JobService, ctx: Context)
        : AsyncTask<Void, Void, Boolean>(){

        private val paramsWeakReference = WeakReference(params)
        private val sharedPrefManagerWeakReference = WeakReference(sharedPrefManager)
        private val jobServiceWeakReference = WeakReference(jobService)
        private val contextWeakReference = WeakReference(ctx)

        private lateinit var jobParams: JobParameters
        private lateinit var sharedPrefManager: SharedPrefManager
        private lateinit var jobService: JobService
        private lateinit var context: Context

        override fun onPreExecute() {
            super.onPreExecute()
            jobParams = paramsWeakReference.get()!!
            sharedPrefManager = sharedPrefManagerWeakReference.get()!!
            jobService = jobServiceWeakReference.get()!!
            context = contextWeakReference.get()!!
        }

        override fun doInBackground(vararg params: Void?): Boolean? {
            val client = OkHttpClient.Builder()
                    .connectTimeout(Constant.TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(Constant.TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .readTimeout(Constant.TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .build()

            val request = Request.Builder()
                    .url(Constant.LINK_GET_NEWS)
                    .get()
                    .addHeader(Constant.Authorization, sharedPrefManager.token)
                    .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body
                val bodyString = responseBody!!.string()
                val gson = Gson()
                val newsModel =gson.fromJson(bodyString, NewsModel::class.java)

                var lastAccessDate = sharedPrefManager.lastAccessDate

                if (lastAccessDate.isEmpty()){
                    sharedPrefManager.lastAccessDate = DateHelper.getCurrentDate()
                    lastAccessDate = sharedPrefManager.lastAccessDate
                }

                Log.d(TAG, "last access date: $lastAccessDate")

                if (newsModel != null){
                    val posts = newsModel.posts
                    showNotification(posts[0])
                    for (post in posts){
                        Log.d(TAG, "${post.title} : ${post.publishedAt}")
                        if (DateHelper.compareDateForShowArticles(post.publishedAt.toString(), lastAccessDate)){
                            Log.d(TAG, "[show notif]: ${post.title} : ${post.publishedAt}")
                        }
                    }
                }

                if (DateHelper.compareDateAfter(lastAccessDate)){
                    sharedPrefManager.lastAccessDate = DateHelper.getCurrentDate()
                }

                return true
            } catch (e: Exception) {
                Crashlytics.setString(TAG, "2-" + e.message)
                Crashlytics.logException(e)
                e.printStackTrace()

                return false
            }
        }

        override fun onCancelled() {
            super.onCancelled()
            Log.d(TAG, "onCancelled")
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if (result != null){
                if (result){
                    jobService.jobFinished(jobParams, true)
                }else{
                    jobService.jobFinished(jobParams, true)
                }
            }
        }

        private fun showNotification(post: Post) {
            val imageUrl = getBitmapFromURL(post.featuredImage)
            if (imageUrl != null){
                val notificationHelper = NotificationArticleHelper(context)
                val nb = notificationHelper.getChannelNotification(post, imageUrl)
                notificationHelper.getManager()?.notify(post.id.toInt(), nb?.build())
            }
        }

        private fun getBitmapFromURL(src: String): Bitmap? {
            return try {
                val url = URL(src)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                Crashlytics.setString(TAG, "2-" + e.message)
                Crashlytics.logException(e)
                null
            }
        }
    }
}
