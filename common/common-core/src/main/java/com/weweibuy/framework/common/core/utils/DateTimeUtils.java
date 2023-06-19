package com.weweibuy.framework.common.core.utils;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
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
        DATE_TIME_FORMATTER_MAP.put(CommonConstant.DateConstant.STANDARD_TIME_FORMAT_STR, CommonConstant.DateConstant.STANDARD_TIME_FORMATTER);
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


    public static String toStringDate(LocalTime date) {
        return date.format(CommonConstant.DateConstant.STANDARD_TIME_FORMATTER);
    }

    public static String toStringDate(LocalTime date, String pattern) {
        return date.format(dateTimeFormatter(pattern));
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

    public static LocalTime stringToLocalTime(String str) {
        return LocalTime.parse(str, CommonConstant.DateConstant.STANDARD_TIME_FORMATTER);
    }


    public static LocalTime stringToLocalTime(String str, String pattern) {
        return LocalTime.parse(str, dateTimeFormatter(pattern));
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
     * 转为日期 String
     *
     * @param date
     * @param formatter 日期格式
     * @return
     */
    public static String toStringDate(LocalDate date, DateTimeFormatter formatter) {
        return date.format(formatter);
    }

    /**
     * 转为日期 String
     *
     * @param date
     * @param formatter 日期格式
     * @return
     */
    public static String toStringDate(ChronoLocalDate date, DateTimeFormatter formatter) {
        return date.format(formatter);
    }

    /**
     * 转为日期 String
     *
     * @param date
     * @return
     */
    public static String toStringDate(TemporalAccessor date, String pattern) {
        DateTimeFormatter dateTimeFormatter = dateTimeFormatter(pattern);
        return dateTimeFormatter.format(date);
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
        return date.toInstant().atZone(CommonConstant.DateConstant.ZONE_ID).toLocalDateTime();
    }

    /**
     * data 转localDate
     *
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(CommonConstant.DateConstant.ZONE_ID).toLocalDate();
    }


    /**
     * LocalDateTime 转 date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(CommonConstant.DateConstant.ZONE_ID).toInstant());
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


    public static Date strToDate(String strDate, String pattern) {
        return localDateTimeToDate(stringToLocalDateTime(strDate, pattern));
    }

    /**
     * localData 转 date
     *
     * @param localDate
     * @return
     */
    public static Date localDateTimeToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(CommonConstant.DateConstant.ZONE_ID).toInstant());
    }


    /**
     * 毫秒时间戳转 LocalDateTime
     *
     * @param timestampMilli
     * @return
     */
    public static LocalDateTime timestampMilliToLocalDateTime(long timestampMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMilli), CommonConstant.DateConstant.ZONE_ID);
    }


    /**
     * 毫秒时间戳转 LocalDate
     *
     * @param timestampMilli
     * @return
     */
    public static LocalDate timestampMilliToLocalDate(long timestampMilli) {
        return Instant.ofEpochMilli(timestampMilli).atZone(CommonConstant.DateConstant.ZONE_ID).toLocalDate();
    }


    /**
     * 秒时间转 LocalDateTime
     *
     * @param timestampSecond
     * @return
     */
    public static LocalDateTime timestampSecondToLocalDateTime(long timestampSecond) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestampSecond), CommonConstant.DateConstant.ZONE_ID);
    }

    /**
     * 秒时间转 LocalDate
     *
     * @param timestampSecond
     * @return
     */
    public static LocalDate timestampSecondToLocalDate(long timestampSecond) {
        return Instant.ofEpochSecond(timestampSecond).atZone(CommonConstant.DateConstant.ZONE_ID).toLocalDate();
    }


    /**
     * LocalDateTime 转毫秒时间
     *
     * @param localDateTime
     * @return
     */
    public static long localDateTimeToTimestampMilli(LocalDateTime localDateTime) {
        return localDateTime.toInstant(CommonConstant.DateConstant.ZONE_OFFSET).toEpochMilli();
    }


    /**
     * LocalDateTime 转毫秒时间
     *
     * @param localDate
     * @return
     */
    public static long localDateToTimestampMilli(LocalDate localDate) {
        return localDate.atStartOfDay(CommonConstant.DateConstant.ZONE_ID).toInstant().toEpochMilli();
    }

    /**
     * LocalDateTime 转秒时间
     *
     * @param localDateTime
     * @return
     */
    public static long localDateTimeToTimestampSecond(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(CommonConstant.DateConstant.ZONE_OFFSET);
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
     * @param interval      毫秒
     * @return
     */
    public static boolean isCurrentTimeOverInterval(LocalDateTime localDateTime, long interval) {
        return Math.abs(System.currentTimeMillis() - localDateTimeToTimestampMilli(localDateTime)) > interval;
    }

    /**
     * dateTime1 与 dateTime2 是否超过 interval
     *
     * @param dateTime1
     * @param dateTime2
     * @param interval  毫秒
     * @return
     */
    public static boolean isOverInterval(LocalDateTime dateTime1, LocalDateTime dateTime2, long interval) {
        return Math.abs(localDateTimeToTimestampMilli(dateTime1) - localDateTimeToTimestampMilli(dateTime2)) > interval;
    }

    /**
     * dateTime1 与 dateTime2 是否超过 duration
     *
     * @param dateTime1
     * @param dateTime2
     * @param duration  时间值
     * @param timeUnit  时间单位
     * @return
     */
    public static boolean isOverInterval(LocalDateTime dateTime1, LocalDateTime dateTime2, Long duration, TimeUnit timeUnit) {
        return Math.abs(localDateTimeToTimestampMilli(dateTime1) - localDateTimeToTimestampMilli(dateTime2)) > toMils(duration, timeUnit);
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
     * dateTime1 是否比 dateTime2 晚
     *
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    public static boolean isAfter(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.compareTo(dateTime2) > 0;
    }

    /**
     * dateTime1 是否比 dateTime2 早
     *
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    public static boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return !isAfter(dateTime1, dateTime2);
    }

    /**
     * 是否是相同的一天
     *
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    public static boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return isSameDay(dateTime1.toLocalDate(), dateTime2.toLocalDate());
    }

    /**
     * 是否是相同的一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(LocalDate date1, LocalDate date2) {
        return date1.compareTo(date2) == 0;
    }


    /**
     * 月份数字 是否相同
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMouthNumber(LocalDateTime date1, LocalDateTime date2) {
        return isSameMouthNumber(date1.toLocalDate(), date2.toLocalDate());
    }

    /**
     * 月份数字 是否相同
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMouthNumber(LocalDate date1, LocalDate date2) {
        return date1.getMonth().compareTo(date2.getMonth()) == 0;
    }

    /**
     * 是否同年 同月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMouth(LocalDate date1, LocalDate date2) {
        return isSameYear(date1, date2) && date1.getMonth().equals(date2.getMonth());
    }

    /**
     * 是否同年 同月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMouth(LocalDateTime date1, LocalDateTime date2) {
        return isSameYear(date1, date2)
                && date1.getMonth().equals(date2.getMonth());
    }

    /**
     * 是否同年
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameYear(LocalDateTime date1, LocalDateTime date2) {
        return Objects.equals(date1.getYear(), date2.getYear());
    }

    /**
     * 是否同年
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameYear(LocalDate date1, LocalDate date2) {
        return Objects.equals(date1.getYear(), date2.getYear());
    }

    /**
     * 是否同一小时
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameHour(LocalDateTime date1, LocalDateTime date2) {
        return isSameDay(date1, date2) && Objects.equals(date1.getHour(), date2.getHour());
    }

    /**
     * 是否同一分钟
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMinute(LocalDateTime date1, LocalDateTime date2) {
        return isSameHour(date1, date2) && Objects.equals(date1.getMinute(), date2.getMinute());
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

    /**
     * 当前时间秒值
     *
     * @return
     */
    public static Long currentTimeSeconds() {
        return LocalDateTime.now().toEpochSecond(CommonConstant.DateConstant.ZONE_OFFSET);
    }

}
