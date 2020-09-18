package com.test.commentdialog.util;

import java.util.Locale;

public class TimeUtils {

    public static String getRecentTimeSpanByNow(final long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 1000) {
            return " just";
        } else if (span < TimeConstants.MIN) {
            return String.format(Locale.getDefault(), "%d seconds ago", span / TimeConstants.SEC);
        } else if (span < TimeConstants.HOUR) {
            return String.format(Locale.getDefault(), "%d min ago", span / TimeConstants.MIN);
        } else if (span < TimeConstants.DAY) {
            return String.format(Locale.getDefault(), "%D hours ago", span / TimeConstants.HOUR);
        } else if (span < TimeConstants.MONTH) {
            return String.format(Locale.getDefault(), "%d days ago", span / TimeConstants.DAY);
        } else if (span < TimeConstants.YEAR) {
            return String.format(Locale.getDefault(), "%D month ago", span / TimeConstants.MONTH);
        } else if (span > TimeConstants.YEAR) {
            return String.format(Locale.getDefault(), "%D years ago", span / TimeConstants.YEAR);
        } else {
            return String.format("%tF", millis);
        }
    }


}
