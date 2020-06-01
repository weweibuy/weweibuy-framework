package com.weweibuy.framework.common.core.utils;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 时间日期工具
 *
 * @author durenhao
 * @date 2019/7/29 18:14
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {


    /**
     * 转为标准时间日期 String
     *
     * @param date
     * @return
     */
    public static String toStringDate(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTime.format(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMATTER);
    }


    /**
     * 转为标准时间日期 String
     *
     * @param date
     * @return
     */
    public static String toStringDate(LocalDateTime date) {
        return date.format(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMATTER);
    }

    /**
     * 转为时间日期 String
     *
     * @param date
     * @param formatter
     * @return
     */
    public static String toStringDate(LocalDateTime date, DateTimeFormatter formatter) {
        return date.format(formatter);
    }

    /**
     * date 转 LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime 转 date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 毫秒时间戳转 LocalDateTime
     *
     * @param timestampMilli
     * @return
     */
    public static LocalDateTime timestampMilliToLocalDateTime(long timestampMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMilli), ZoneId.systemDefault());
    }

    /**
     * 秒时间转 LocalDateTime
     *
     * @param timestampSecond
     * @return
     */
    public static LocalDateTime timestampSecondToLocalDateTime(long timestampSecond) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestampSecond), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime 转毫秒时间
     *
     * @param localDateTime
     * @return
     */
    public static long localDateTimeToTimestampMilli(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of(CommonConstant.DateConstant.TIME_OFFSET_ID)).toEpochMilli();
    }

    /**
     * LocalDateTime 转秒时间
     *
     * @param localDateTime
     * @return
     */
    public static long localDateTimeToTimestampSecond(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(ZoneOffset.of(CommonConstant.DateConstant.TIME_OFFSET_ID));
    }


    public static long toMils(Long duration, TimeUnit timeUnit) {
        return timeUnit.toMillis(duration);
    }

    /**
     * 当前时间 是否晚于 给定时间 localDateTime   interval 值
     *
     * @param localDateTime
     * @param interval
     * @return
     */
    public static boolean isCurrentTimeOverInterval(LocalDateTime localDateTime, long interval) {
        return System.currentTimeMillis() - localDateTimeToTimestampMilli(localDateTime) > interval;
    }

    /**
     * 时间差
     *
     * @param start
     * @param end
     * @return
     */
    public static Duration between(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end);
    }

    /**
     * 时间差毫秒值
     *
     * @param start
     * @param end
     * @return
     */
    public static long betweenMilli(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMillis();
    }


}
