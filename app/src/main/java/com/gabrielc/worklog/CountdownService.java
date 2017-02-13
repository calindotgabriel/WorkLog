package com.gabrielc.worklog;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.orhanobut.logger.Logger;

/**
 * Created by GabrielC on 2/8/2017.
 */
public class CountdownService extends Service {

    public static final String COUNTDOWN_BR = "com.gabrielc.worklog.countdown_br";
    public static final String KEY_COUNTDOWN_INTENT = "COUNTDOWN";
    public static final int NOTIFICATION_ID = 1;

    private CountdownBinder mBinder;
    private Intent mBroadcastIntent;

    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;
    private CountDownTimerPauseable mCountDownTimer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("Service onStartCommand()");
        final long minutes = intent.getLongExtra(MainActivity.MINUTES_TO_COUNTDOWN, 0);
        final long seconds = minutes * 60;
        startCountdown(seconds);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new CountdownBinder();
        mBroadcastIntent = new Intent(COUNTDOWN_BR);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Logger.d("onCreate CountdownService");
    }

    public void startCountdown(final long seconds) {
        mNotificationBuilder = new NotificationCompat.Builder(this)
                .setContentText("X secs lefts")
                .setContentTitle("WorkLog")
                .setSmallIcon(R.drawable.pugnotification_ic_launcher)
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true);

        mCountDownTimer = new CountDownTimerPauseable(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                final long secsUntilFinished = millisUntilFinished / 1000;
                mBroadcastIntent.putExtra(KEY_COUNTDOWN_INTENT, secsUntilFinished);
                sendBroadcast(mBroadcastIntent);

                mNotificationBuilder.setContentText(secsUntilFinished + " secs left");
                mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
            }

            @Override
            public void onFinish() {
                Logger.d("Finished");
            }
        };

        mCountDownTimer.start();
    }

    public void pauseCountdown() {
        mCountDownTimer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy CountdownService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class CountdownBinder extends Binder {
        CountdownService getService() {
            return CountdownService.this;
        }
    }
}
