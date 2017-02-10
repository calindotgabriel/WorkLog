package com.gabrielc.worklog;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    public static final String MINUTES_TO_COUNTDOWN = "MINUTES_TO_COUNTDOWN";

    @BindView(R.id.main_tv)
    TextView mMainTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Logger.d("onCreate");

        registerReceiver(mCountdownReceiver, new IntentFilter(CountdownService.COUNTDOWN_BR));
        final boolean isMyServiceRunning = isMyServiceRunning(CountdownService.class);
        Logger.d(isMyServiceRunning);
        if (!isMyServiceRunning) {
            Logger.d("Starting service..");
            final Intent serviceIntent = new Intent(MainActivity.this, CountdownService.class);
            serviceIntent.putExtra(MINUTES_TO_COUNTDOWN, 1L);
            startService(serviceIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d("onPause");

//        stopService(new Intent(MainActivity.this, CountdownService.class));
        unregisterReceiver(mCountdownReceiver);
    }

    private BroadcastReceiver mCountdownReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                final long secsUntilFinished = intent.getLongExtra(CountdownService.KEY_COUNTDOWN_INTENT, 0);
                Logger.d("Activity received %d", secsUntilFinished);
                final String text = secsUntilFinished + " secounds left";
                mMainTv.setText(text);
            }
        }
    };

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
