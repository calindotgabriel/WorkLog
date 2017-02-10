package com.gabrielc.worklog;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

/**
 * Created by GabrielC on 2/8/2017.
 */
public class CountdownService extends Service {

    public static final String COUNTDOWN_BR = "com.gabrielc.worklog.countdown_br";
    public static final String KEY_COUNTDOWN_INTENT = "COUNTDOWN";

    private CountdownBinder mBinder;
    private Intent mBroadcastIntent;

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
        Logger.d("onCreate CountdownService");
    }

    private void startCountdown(final long seconds) {
        CountDownTimerPauseable countDownTimer = new CountDownTimerPauseable(seconds * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                final long secsUntilFinished = millisUntilFinished / 1000;
//                Logger.d("onTick %d", secsUntilFinished);
                mBroadcastIntent.putExtra(KEY_COUNTDOWN_INTENT, secsUntilFinished);
                sendBroadcast(mBroadcastIntent);
            }

            @Override
            public void onFinish() {
                Logger.d("Finished");
            }
        };

        countDownTimer.start();
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

    }
}
