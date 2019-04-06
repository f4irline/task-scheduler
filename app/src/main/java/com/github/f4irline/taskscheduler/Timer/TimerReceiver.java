package com.github.f4irline.taskscheduler.Timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class TimerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        int seconds = 0;

        if (extras != null) {
            seconds = extras.getInt("time");
        }

        Log.d("TimerReceiver", "Time Tick, seconds: "+seconds);
    }
}
