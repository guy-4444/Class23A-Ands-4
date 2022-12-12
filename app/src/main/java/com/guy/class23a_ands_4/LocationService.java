package com.guy.class23a_ands_4;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class LocationService extends Service {

    public static final String START_FOREGROUND_SERVICE = "START_FOREGROUND_SERVICE";
    public static final String STOP_FOREGROUND_SERVICE = "STOP_FOREGROUND_SERVICE";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent.getAction().equals(START_FOREGROUND_SERVICE)) {
            startRecording();

        } else if (intent.getAction().equals(STOP_FOREGROUND_SERVICE)) {
            stopRecording();
        }

        return START_STICKY;
    }


    private MCT5.CycleTicker cycleTicker = new MCT5.CycleTicker() {
        @Override
        public void secondly(int repeatsRemaining) {
            Log.d("pttt", Thread.currentThread().getName() + " - Hi " + System.currentTimeMillis());
        }

        @Override
        public void done() {}
    };

    private void startRecording() {
        Log.d("pttt", Thread.currentThread().getName() + " - startRecording()");

        MCT5.get().cycle(cycleTicker, MCT5.CONTINUOUSLY_REPEATS, 1000);
    }

    private void stopRecording() {
        MCT5.get().remove(cycleTicker);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
