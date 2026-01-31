package com.cathaybk.ddt.coindesk.base.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final String FORMATTER_YYYYMMDD = "yyyy/MM/dd";

    public static String transLocalDateTimeToString(LocalDateTime time, String formatStr){
        if (time == null || formatStr == null || formatStr.trim().isEmpty()) {
            return "";
        }
        try {
            return time.format(DateTimeFormatter.ofPattern(formatStr));
        } catch (Exception e) {
            return "";
        }
    }
}
