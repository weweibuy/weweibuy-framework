package com.weweibuy.framework.common.core.utils;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 时间日期工具
 *
 * @author durenhao
 * @date 2019/7/29 18:14
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {

    private static final Map<String, DateTimeFormatter> DATE_TIME_FORMATTER_MAP
            = new ConcurrentHashMap<>();

    static {
        DATE_TIME_FORMATTER_MAP.put(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMAT_STR, CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMATTER);
        DATE_TIME_FORMATTER_MAP.put(CommonConstant.DateConstant.STANDARD_DATE_FORMAT_STR, CommonConstant.DateConstant.STANDARD_DATE_FORMATTER);
    }

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
     * 根据格式转时间日期
     *
     * @param date
     * @param pattern 时间格式
     * @return
     */
    public static String toStringDate(Date date, String pattern) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        return localDateTime.format(dateTimeFormatter(pattern));
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
     * 根据格式时间
     *
     * @param date
     * @param pattern 时间格式
     * @return
     */
    public static String toStringDate(LocalDateTime date, String pattern) {
        return date.format(dateTimeFormatter(pattern));
    }

    /**
     * 标准时间转 LocalDateTime
     *
     * @param str yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static LocalDateTime stringToLocalDateTime(String str) {
        return LocalDateTime.parse(str, CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMATTER);
    }

    /**
     * 时间转 LocalDateTime
     *
     * @param str
     * @param pattern 时间格式
     * @return
     */
    public static LocalDateTime stringToLocalDateTime(String str, String pattern) {
        return LocalDateTime.parse(str, dateTimeFormatter(pattern));
    }


    /**
     * 标准时间转 LocalDate
     *
     * @param str yyyy-MM-dd
     * @return
     */
    public static LocalDate stringToLocalDate(String str) {
        return LocalDate.parse(str, CommonConstant.DateConstant.STANDARD_DATE_FORMATTER);
    }

    /**
     * 时间转 LocalDate
     *
     * @param str
     * @param pattern 时间格式
     * @return
     */
    public static LocalDate stringToLocalDate(String str, String pattern) {
        return LocalDate.parse(str, dateTimeFormatter(pattern));
    }

    /**
     * 转为标准日期 String
     *
     * @param date
     * @return
     */
    public static String toStringDate(LocalDate date) {
        return date.format(CommonConstant.DateConstant.STANDARD_DATE_FORMATTER);
    }

    /**
     * 转为日期 String
     *
     * @param date
     * @param pattern 日期格式
     * @return
     */
    public static String toStringDate(LocalDate date, String pattern) {
        return date.format(dateTimeFormatter(pattern));
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
     * data 转localDate
     *
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
     * string 转 date
     *
     * @param strDate yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date strToDate(String strDate) {
        return localDateTimeToDate(stringToLocalDateTime(strDate));
    }

    /**
     * localData 转 date
     *
     * @param localDate
     * @return
     */
    public static Date localDateTimeToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
     * LocalDateTime 转毫秒时间
     *
     * @param localDate
     * @return
     */
    public static long localDateToTimestampMilli(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
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

    /**
     * 时间单位转化
     *
     * @param duration
     * @param timeUnit
     * @return
     */
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

    /**
     * 标准时间字符 yyyy-MM-dd HH:mm:ss 转毫秒
     *
     * @param date
     * @return
     */
    public static long strDateTimeToMilli(String date) {
        return localDateTimeToTimestampMilli(stringToLocalDateTime(date));
    }

    /**
     * 标准时间字符 yyyy-MM-dd 转毫秒
     *
     * @param date
     * @return
     */
    public static long strDateToMilli(String date) {
        return localDateToTimestampMilli(stringToLocalDate(date));
    }

    /**
     * 时间字符  转毫秒
     *
     * @param date
     * @param pattern 时间格式
     * @return
     */
    public static long strDateToMilli(String date, String pattern) {
        return localDateToTimestampMilli(stringToLocalDate(date, pattern));
    }


    /**
     * 获取 DateTimeFormatter
     *
     * @param str
     * @return
     */
    public static DateTimeFormatter dateTimeFormatter(String str) {
        return DATE_TIME_FORMATTER_MAP.computeIfAbsent(str, key ->
                DateTimeFormatter.ofPattern(key));
    }


}
