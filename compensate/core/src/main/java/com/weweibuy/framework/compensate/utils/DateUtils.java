package com.weweibuy.framework.compensate.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author durenhao
 * @date 2020/2/15 19:29
 **/
public class DateUtils {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String toStringDate(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTime.format(dateTimeFormatter);
    }

    public static String toDateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String toDateFormat(LocalDateTime date) {
        return date.format(dateTimeFormatter);
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


    public static LocalDateTime systemTimestampToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static long localDateTimeToTimestamp(LocalDateTime localDateTime) {
        long timestamp = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return timestamp;
    }

    public static long toMils(Long duration, TimeUnit timeUnit) {
        return timeUnit.toMillis(duration);
    }

    public static boolean isCurrentTimeOverInterval(LocalDateTime localDateTime, long interval) {
        return System.currentTimeMillis() - localDateTimeToTimestamp(localDateTime) > interval;
    }


}
