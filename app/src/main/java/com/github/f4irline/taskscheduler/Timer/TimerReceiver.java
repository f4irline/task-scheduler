package com.github.f4irline.taskscheduler.Timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimerReceiver extends BroadcastReceiver {

    TimerReceiverCallback callback;

    public TimerReceiver(TimerReceiverCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        callback.onTimeReceived(intent);
    }
}
