package com.madrasahdigital.walisantri.ppi67benda.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.madrasahdigital.walisantri.ppi67benda.notification.NotificationArticleHelper.Companion.NOTIFICATION_ID
import com.madrasahdigital.walisantri.ppi67benda.services.UpComingArticlesReceiver
import java.util.*

object AlarmHelper{

    fun startAlarmNewArticle(context: Context, newsModel: String) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 13)
            set(Calendar.MINUTE, 13)
            set(Calendar.SECOND, 0)
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Intent(context, UpComingArticlesReceiver::class.java)

        if (calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE, 1)
        }

        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime() + 1000 * 10,
                AlarmManager.INTERVAL_DAY,
                getPendingIntent(context, newsModel)
        )

        val alarmIntent = Intent(context, UpComingArticlesReceiver::class.java)
        alarmIntent.putExtra(UpComingArticlesReceiver.EXTRA_DATA_ARTICLES, newsModel)

        val alarmManagerr = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val pendingIntent = PendingIntent.getService(context, NOTIFICATION_ID, alarmIntent, PendingIntent.FLAG_NO_CREATE)

        val isRunning = alarmManagerr != null && pendingIntent != null
        Log.d("coba", "alarm is running: $isRunning")
    }

    private fun getPendingIntent(context: Context, newsModel: String): PendingIntent {
        val alarmIntent = Intent(context, UpComingArticlesReceiver::class.java)
        alarmIntent.putExtra(UpComingArticlesReceiver.EXTRA_DATA_ARTICLES, newsModel)

        return PendingIntent.getBroadcast(
                context,
                NOTIFICATION_ID,
                alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        )
    }
}