package com.github.f4irline.taskscheduler.TimerTools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * The receiver implementation which receives the time ticks from the timer service.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0406
 */
public class TimerReceiver extends BroadcastReceiver {

    TimerReceiverCallback callback;

    public TimerReceiver() {
    }

    /**
     * Constructor which is used to initialize the callback implementation to the activity which
     * uses the receiver.
     *
     * @param callback the callback implementation which holds the onTimeReceived() method.
     */
    public TimerReceiver(TimerReceiverCallback callback) {
        this.callback = callback;
    }

    /**
     * Called when the BroadcastReceiver receives a broadcast from the timer service.
     *
     * @param context the context in which the receiver is running.
     * @param intent the intent which is being received, holds the current time running the service.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        callback.onTimeReceived(intent);
    }
}
