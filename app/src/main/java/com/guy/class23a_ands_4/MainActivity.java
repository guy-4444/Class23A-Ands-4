package com.guy.class23a_ands_4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.start);
        start.setOnClickListener(v -> startService());

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (getIntent().getAction().equals(LocationService.MAIN_ACTION)) {
            // came from notification
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (getIntent().getAction().equals(LocationService.MAIN_ACTION)) {

        }

    }

    private void startService() {
        Intent intent = new Intent(this, LocationService.class);
        intent.setAction(LocationService.START_FOREGROUND_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
            // or
            //ContextCompat.startForegroundService(this, startIntent);
        } else {
            startService(intent);
        }
    }

    private void stopService() {

    }
}