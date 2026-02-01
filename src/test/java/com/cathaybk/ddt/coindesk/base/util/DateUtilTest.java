package com.cathaybk.ddt.coindesk.base.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateUtilTest {
    @Test
    void testFullPatternTransfer() {
        LocalDateTime time = LocalDateTime.of(2026, 2, 1, 23, 5, 7);

        String result = DateUtil.transLocalDateTimeToString(
                time,
                DateUtil.FORMATTER_YYYYMMDD_HHMMSS
        );

        assertEquals("2026/02/01 23:05:07", result);
    }

    @Test
    void testDatePatternTransfer() {
        LocalDateTime time = LocalDateTime.of(2026, 2, 1, 10, 30, 0);

        String result = DateUtil.transLocalDateTimeToString(
                time,
                DateUtil.FORMATTER_YYYYMMDD
        );

        assertEquals("2026/02/01", result);
    }

    @Test
    void testTimeNull() {
        String result = DateUtil.transLocalDateTimeToString(
                null,
                DateUtil.FORMATTER_YYYYMMDD
        );

        assertEquals("", result);
    }

    @Test
    void testFormatNull() {
        LocalDateTime time = LocalDateTime.now();

        String result = DateUtil.transLocalDateTimeToString(time, null);

        assertEquals("", result);
    }


    @Test
    void testFormatEmpty() {
        LocalDateTime time = LocalDateTime.now();

        String result = DateUtil.transLocalDateTimeToString(time, "");

        assertEquals("", result);
    }

    @Test
    void testWrongFormat() {
        LocalDateTime time = LocalDateTime.now();

        String result = DateUtil.transLocalDateTimeToString(time, "XXX");

        assertEquals("", result);
    }
}

