package com.weiweibuy.framework.common.core.utils;

import com.weiweibuy.framework.common.core.model.constant.CommonConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author durenhao
 * @date 2019/7/29 18:14
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {


    public static String toStringDate(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTime.format(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMATTER);
    }


    public static String toDateFormat(LocalDateTime date) {
        return date.format(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMATTER);
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


    public static long localDateTimeToTimestampMilli(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of(CommonConstant.DateConstant.TIME_OFFSET_ID)).toEpochMilli();
    }

    public static long localDateTimeToTimestampSecond(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(ZoneOffset.of(CommonConstant.DateConstant.TIME_OFFSET_ID));
    }


    public static long toMils(Long duration, TimeUnit timeUnit) {
        return timeUnit.toMillis(duration);
    }

    public static boolean isCurrentTimeOverInterval(LocalDateTime localDateTime, long interval) {
        return System.currentTimeMillis() - localDateTimeToTimestampMilli(localDateTime) > interval;
    }

    public static Duration between(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end);
    }

}
