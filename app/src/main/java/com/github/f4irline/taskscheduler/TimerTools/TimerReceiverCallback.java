package com.github.f4irline.taskscheduler.TimerTools;

import android.content.Intent;

/**
 * Interface which is used to communicate between the activity and the BroadCastReceiver.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0406
 */
public interface TimerReceiverCallback {
    public void onTimeReceived(Intent intent);
}
