package com.github.f4irline.taskscheduler.TimerTools;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * The service which is used to update goal's done time every second when user starts the timer.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0406
 */
public class TimerService extends Service implements Runnable {
    public static boolean isRunning;
    private Thread thread;
    private int seconds;
    private int minutes;
    private int hours;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    /**
     * Return's the communication channel to the service. Returns null, since we're not binding
     * to the service.
     *
     * @param arg0 the intent which was used to bind to this service.
     * @return null, since we're not binding to the service.
     */
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * Called when the service is destroyed (stopped). Updates the seconds, minutes and hours
     * to the sharedPreferences back to 0, since we don't need them anymore.
     */
    @Override
    public void onDestroy() {
        editor = sharedPref.edit();
        editor.putInt("seconds", 0);
        editor.putInt("minutes", 0);
        editor.putInt("hours", 0);
        editor.apply();
        isRunning = false;
    }

    /**
     * Called when a client starts the service.
     *
     * @param intent the intent supplied.
     * @param flags data about the start request.
     * @param startId unique integer representing the start request.
     * @return START_STICKY which tells the system to recreate the service if the system kills
     * the service when running out of memory.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            thread.start();
        }
        return START_STICKY;
    }

    /**
     * Called when the service is created the first time.
     *
     * <p>
     * Initializes the shared preference which is used to temporarily save the seconds,
     * minutes and hours this service has been running.
     * </p>
     *
     * <p>
     * Also initializes the new Thread which is running when the service starts.
     * </p>
     */
    @Override
    public void onCreate() {
        isRunning = false;
        sharedPref = getSharedPreferences("Time", Context.MODE_PRIVATE);
        seconds = seconds + sharedPref.getInt("seconds", 0);
        minutes = minutes + sharedPref.getInt("minutes", 0);
        hours = hours + sharedPref.getInt("hours", 0);
        thread = new Thread(this);
    }

    /**
     * Starts the thread.
     *
     * <p>
     * Runs as long as the service is kept running. Sends a local broadcast every second, which
     * is then received by a receiver implementation.
     * </p>
     */
    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            try {
                Intent intent = new Intent("android.intent.action.TIME_TICK");
                intent.putExtra("seconds", seconds);
                intent.putExtra("minutes", minutes);
                intent.putExtra("hours", hours);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                calculateTime();
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Calculates time to the stored local variables and adds them also to the sharedPreferences
     * which can later be used.
     */
    private void calculateTime() {
        seconds++;
        if (seconds % 60 == 0) {
            minutes++;
            seconds = 0;
            if (minutes % 60 == 0) {
                hours++;
                minutes = 0;
            }
        }

        editor = sharedPref.edit();
        editor.putInt("seconds", seconds);
        editor.putInt("minutes", minutes);
        editor.putInt("hours", hours);
        editor.apply();
    }
}
