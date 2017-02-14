package com.gabrielc.worklog;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielc.worklog.util.TimeFormatter;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    private CountdownService mService;
    private boolean isBound = false;

    @BindView(R.id.main_tv)
    TextView mMainTv;

    @BindView(R.id.main_input_et)
    EditText mMainEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final Intent serviceIntent = new Intent(MainActivity.this, CountdownService.class);
        bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mCountdownReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(mServiceConnection);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mCountdownReceiver, new IntentFilter(CountdownService.COUNTDOWN_BR));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Logger.d("onServiceConnected");
            CountdownService.CountdownBinder binder = (CountdownService.CountdownBinder) iBinder;
            mService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Logger.d("onServiceDisconnected");
            mService = null;
            isBound = false;
        }
    };

    private BroadcastReceiver mCountdownReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                final long millisUntilFinished = intent.getLongExtra(CountdownService.KEY_COUNTDOWN_INTENT, 0);
                mMainTv.setText(TimeFormatter.formatMillis(millisUntilFinished));
            }
        }
    };

    @OnClick(R.id.main_break_btn)
    void onBreakPressed() {
        mService.pauseCountdown();
    }

    @OnClick(R.id.main_resume_btn)
    void onResumePressed() {
        mService.resumeCountdown();
    }

    @OnClick(R.id.main_start_btn)
    void onStartPressed() {
        final String minutesAsString = mMainEt.getText().toString();
        if (!TextUtils.isEmpty(minutesAsString)) {
            final long minutes = Long.parseLong(minutesAsString);
            mService.startCountdown(minutes * 60);
        } else {
            Toast.makeText(this, "Enter input", Toast.LENGTH_SHORT).show();
        }
    }

}
