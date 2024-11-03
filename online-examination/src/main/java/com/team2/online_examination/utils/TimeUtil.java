package com.team2.online_examination.utils;

public class TimeUtil {

    private TimeUtil() {
        // Private constructor to prevent instantiation
    }

    public static long daysToMilliseconds(int days) {
        return days * 24L * 60 * 60 * 1000;
    }
}
