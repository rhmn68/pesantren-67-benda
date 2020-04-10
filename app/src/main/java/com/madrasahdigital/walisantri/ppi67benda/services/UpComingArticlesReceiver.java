package com.madrasahdigital.walisantri.ppi67benda.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.view.activity.DetailNewsActivity;

public class UpComingArticlesReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 0;
    private static final String NOTIFICATION_ARTICLES_CHANNEL_ID = "notification_articles_channel_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void showNotificationArticles(Context context, String urlImage){
        Bitmap imageBitmap = BitmapFactory.decodeFile(urlImage);
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, DetailNewsActivity.class);
        PendingIntent pendingIntentNotification = PendingIntent.getBroadcast(context, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_ARTICLES_CHANNEL_ID)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntentNotification)
                .setSmallIcon(R.drawable.icon_green)
                .setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(imageBitmap));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_ARTICLES_CHANNEL_ID,
                    "Articles Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setDescription("Articles Notification");

            assert notifyManager != null;
            notifyManager.createNotificationChannel(notificationChannel);
        }

        assert notifyManager != null;
        notifyManager.notify(NOTIFICATION_ID, builder.build());
    }
}
