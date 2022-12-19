package com.guy.class23a_ands_4;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class LocationService extends Service {

    public static final String START_FOREGROUND_SERVICE = "START_FOREGROUND_SERVICE";
    public static final String STOP_FOREGROUND_SERVICE = "STOP_FOREGROUND_SERVICE";


    public static int NOTIFICATION_ID = 154;
    private int lastShownNotificationId = -1;
    public static String CHANNEL_ID = "com.guy.class23a_ands_4.CHANNEL_ID_FOREGROUND";
    public static String MAIN_ACTION = "com.guy.class23a_ands_4.locationservice.action.main";
    private NotificationCompat.Builder notificationBuilder;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent.getAction().equals(START_FOREGROUND_SERVICE)) {
            notifyToUserForForegroundService();
            startRecording();

        } else if (intent.getAction().equals(STOP_FOREGROUND_SERVICE)) {
            stopRecording();
        }

        return START_STICKY;
    }


    int counter = 0;
    private MCT5.CycleTicker cycleTicker = new MCT5.CycleTicker() {
        @Override
        public void secondly(int repeatsRemaining) {
            Log.d("pttt", Thread.currentThread().getName() + " - Hi " + System.currentTimeMillis());
            counter += 100;

            String content = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.US).format(System.currentTimeMillis());
            updateNotification(counter + "m \n" + content);
        }

        @Override
        public void done() {}
    };

    private void startRecording() {
        Log.d("pttt", Thread.currentThread().getName() + " - startRecording()");

        MCT5.get().cycle(cycleTicker, MCT5.CONTINUOUSLY_REPEATS, 5000);
    }

    private void stopRecording() {
        MCT5.get().remove(cycleTicker);
    }

    private void updateNotification(String content) {
        notificationBuilder.setContentText(content);
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }







    // // // // // // // // // // // // // // // // Notification  // // // // // // // // // // // // // // //

    private void notifyToUserForForegroundService() {
        // On notification click
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        notificationBuilder = getNotificationBuilder(this,
                CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_LOW); //Low importance prevent visual appearance for this notification channel on top

        notificationBuilder
                .setContentIntent(pendingIntent) // Open activity
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_cycling)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setContentTitle("App in progress")
                .setContentText("Content")
        ;

        Notification notification = notificationBuilder.build();

        startForeground(NOTIFICATION_ID, notification);

        if (NOTIFICATION_ID != lastShownNotificationId) {
            // Cancel previous notification
            final NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
            notificationManager.cancel(lastShownNotificationId);
        }
        lastShownNotificationId = NOTIFICATION_ID;
    }

    public static NotificationCompat.Builder getNotificationBuilder(Context context, String channelId, int importance) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            prepareChannel(context, channelId, importance);
            builder = new NotificationCompat.Builder(context, channelId);
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        return builder;
    }

    @TargetApi(26)
    private static void prepareChannel(Context context, String id, int importance) {
        final String appName = context.getString(R.string.app_name);
        String notifications_channel_description = "Cycling map channel";
        String description = notifications_channel_description;
        final NotificationManager nm = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

        if(nm != null) {
            NotificationChannel nChannel = nm.getNotificationChannel(id);

            if (nChannel == null) {
                nChannel = new NotificationChannel(id, appName, importance);
                nChannel.setDescription(description);

                // from another answer
                nChannel.enableLights(true);
                nChannel.setLightColor(Color.BLUE);

                nm.createNotificationChannel(nChannel);
            }
        }
    }

}
