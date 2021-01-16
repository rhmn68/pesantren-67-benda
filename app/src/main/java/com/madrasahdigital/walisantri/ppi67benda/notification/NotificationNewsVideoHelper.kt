package com.madrasahdigital.walisantri.ppi67benda.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.madrasahdigital.walisantri.ppi67benda.BuildConfig
import com.madrasahdigital.walisantri.ppi67benda.R
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.Post
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant
import com.madrasahdigital.walisantri.ppi67benda.view.activity.DetailNewsActivity
import com.madrasahdigital.walisantri.ppi67benda.view.activity.DetailNewsVideoActivity
import me.leolin.shortcutbadger.ShortcutBadger
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class NotificationNewsVideoHelper(context: Context): ContextWrapper(context) {
    companion object{
        private const val CHANNEL_ID = BuildConfig.APPLICATION_ID + ".FCM_NOTIFICATION"
        private const val CHANNEL_NAME = "FCM_NOTIFICATION"
        private const val CHANNEL_DESC = "Fcm Notification Articles"
        private const val GROUP_KEY_NOTIFICATION_ARTICLES = "${BuildConfig.APPLICATION_ID}.GROUP_KEY_FCM_NOTIFICATION_ARTICLES"
        private const val NOTIFICATION_ID = 100
    }

    private var  mNotificationManager = NotificationManagerCompat.from(this)

    private fun createNotification(post: Post?, image: Bitmap?): Notification{

        val intent = Intent(this, DetailNewsVideoActivity::class.java)
        intent.putExtra(DetailNewsVideoActivity.EXTRA_POST, post)
        val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent,  PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH).apply {
                setShowBadge(true)
                description = CHANNEL_DESC
                enableLights(true)
            }
            mNotificationManager.createNotificationChannel(notificationChannel)
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_stat_logo_persis)
                .setColor(ContextCompat.getColor(this, R.color.logo_persis))
                .setContentTitle(post?.title)
                .setContentText(post?.slug)
                .setLargeIcon(image)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(image))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setGroup(GROUP_KEY_NOTIFICATION_ARTICLES)
                .setNumber(1)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .build()
    }

    fun showNotification(post: Post?){
        SendNotification(this, post).execute(post?.featuredImage)
    }

    fun buildNotification(post: Post?, image: Bitmap){
        val badgeCount = 4
        ShortcutBadger.applyNotification(this, createNotification(post, image), badgeCount)
        mNotificationManager.notify(post?.id?.toInt()!!, createNotification(post, image))
    }

    class SendNotification(context: Context?, post: Post?) : AsyncTask<String, Void, Bitmap>(){

        private val weakPost = WeakReference(post)
        private val weakContext = WeakReference(context)

        private val post = weakPost.get()
        private val context = weakContext.get()

        override fun doInBackground(vararg params: String?): Bitmap? {
            val image = params[0]
            if (image != null){
                return getBitmapFromURL(image)
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            if (result != null && context != null){
                NotificationNewsVideoHelper(context).buildNotification(post, result)
            }
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