package com.library.app.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * The type Date utils.
 */
public final class DateUtils {

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private DateUtils() {
    }

    /**
     * Gets as date time. Format into instant date and time.
     *
     * @param dateTime the date time
     * @return the as date time
     */
    public static Date getAsDateTime(final String dateTime) {
        try {
            return new SimpleDateFormat(FORMAT).parse(dateTime);
        } catch (final ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Format date time string as instance date and time.
     *
     * @param date the date
     * @return the string
     */
    public static String formatDateTime(final Date date) {
        return new SimpleDateFormat(FORMAT).format(date);
    }


    /**
     * Current date plus days date. This method is used get current date and add a set of days to it.
     *
     * @param days the days
     * @return the date
     */
    public static Date currentDatePlusDays(final int days) {
        final LocalDateTime localDateTime = LocalDateTime.now();
        return Date.from(localDateTime.plusDays(days).atZone(ZoneId.systemDefault()).toInstant());
    }
}