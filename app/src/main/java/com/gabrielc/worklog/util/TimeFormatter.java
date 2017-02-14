package com.gabrielc.worklog.util;

import java.util.concurrent.TimeUnit;

/**
 * Created by calin on 14.02.2017.
 */

public class TimeFormatter {
    public static String formatMillis(long millis) {
        final long hours = TimeUnit.MILLISECONDS.toHours(millis);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        if (hours == 0) {
            if (minutes == 0) {
                return String.format("%02d sec",
                        TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
            }
            return String.format("%02d min, %02d sec",
                    minutes % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));

        }
        return String.format("%02d h, %02d min, %02d sec",
                hours,
                minutes % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}
