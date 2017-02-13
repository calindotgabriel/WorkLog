package com.gabrielc.worklog;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by calin on 13.02.2017.
 */

public class WorkLogApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init().methodCount(1);
    }
}
