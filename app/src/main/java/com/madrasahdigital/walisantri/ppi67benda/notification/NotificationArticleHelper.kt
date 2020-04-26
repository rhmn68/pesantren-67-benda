package com.madrasahdigital.walisantri.ppi67benda.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.madrasahdigital.walisantri.ppi67benda.BuildConfig
import com.madrasahdigital.walisantri.ppi67benda.R
import com.madrasahdigital.walisantri.ppi67benda.view.activity.DetailNewsActivity
import com.madrasahdigital.walisantri.ppi67benda.view.activity.HomeActivityV2

class NotificationArticleHelper (context: Context?) : ContextWrapper(context){
    companion object{
        const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "${BuildConfig.APPLICATION_ID}.ACTION_UPDATE_ARTICLE"
        private const val CHANNEL_NAME = "NEW_ARTICLE_NOTIFICATION"
        private const val GROUP_KEY_NOTIFICATION_ARTICLES = "${BuildConfig.APPLICATION_ID}.GROUP_KEY_NOTIFICATION_ARTICLES"
    }
    private var mManager: NotificationManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        getManager()?.createNotificationChannel(channel)
    }

    fun getManager(): NotificationManager? {
        if (mManager == null) {
            mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return mManager
    }

    fun getChannelNotification(desc: String, image: Bitmap): NotificationCompat.Builder? {
//        val intent = Intent(this, DetailNewsActivity::class.java)
//        intent.putExtra("urlberita", newsModel.getUrl())
        val notificationIntent = Intent(this, HomeActivityV2::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(desc)
                .setSmallIcon(R.drawable.ic_stat_logo)
                .setLargeIcon(image)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(image))
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                .setGroup(GROUP_KEY_NOTIFICATION_ARTICLES)
    }
}