package com.madrasahdigital.walisantri.ppi67benda.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.madrasahdigital.walisantri.ppi67benda.BuildConfig
import com.madrasahdigital.walisantri.ppi67benda.R
import com.madrasahdigital.walisantri.ppi67benda.view.activity.HomeActivityV2

class NotificationHelper(context: Context): ContextWrapper(context) {
    companion object{
        private const val CHANNEL_ID = BuildConfig.APPLICATION_ID + "FCM_NOTIFICATION"
        private const val CHANNEL_NAME = "FCM_NOTIFICATION"
        private const val CHANNEL_DESC = "Fcm Notification Articles"
    }

    private var  mNotificationManager = NotificationManagerCompat.from(this)

    private fun createNotification(desc: String?, image: Bitmap?): Notification{
        val intent = Intent(this, HomeActivityV2::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 100, intent,  PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH)
            notificationChannel.description = CHANNEL_DESC
            notificationChannel.enableLights(true)
            mNotificationManager.createNotificationChannel(notificationChannel)
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_stat_logo_persis)
            .setColor(ContextCompat.getColor(this, R.color.logo_persis))
            .setContentTitle(getString(R.string.app_name))
            .setContentText(desc)
            .setLargeIcon(image)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(image))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()
    }

    fun showNotification(desc: String?, image: Bitmap?){
        mNotificationManager.notify(1, createNotification(desc, image))
    }
}