package com.gabrielc.worklog;

import com.gabrielc.worklog.util.TimeUtils;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by calin on 14.02.2017.
 */

public class TimeFormatterTest {
    @Test
    public void formatMillis() {
        long millis = 50 * 1000;
        final String hhmmss = TimeUtils.formatMillis(millis);
        Assert.assertEquals("50 sec", hhmmss);
    }
}
