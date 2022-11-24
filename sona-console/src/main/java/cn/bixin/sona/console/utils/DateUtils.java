package cn.bixin.sona.console.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.math.DoubleMath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by zhichao.guo on 8/24/21 4:14 PM
 */
@Slf4j
public class DateUtils {
    public static final String DATE_FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_MM_DD = "MM-dd";
    public static final String DATE_FORMAT_DATE_TIME_MSEC = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String DATE_FORMAT_DATE = "yyyy-MM-dd";
    public static final String DATE_FORMAT_MONTH_DAY = "MM.dd";
    public static final String DATE_FORMAT_TIME = "HH:mm:ss";
    public static final String DATE_FORMAT_DATE_MINITE = "dd-HH-mm";
    public static final String DATE_FORMAT_MINUTE_TIME = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_MINUTE_ES = "yyyy_MM_dd";
    public static final String DATE_FORMAT_DATE_TIME_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_DATE_TIME_CN = "yyyy年MM月dd日 HH:mm";

    public static final String DATE_FORMAT_HOUR = "yyyy-MM-dd-HH";

    public static final int ONE_SECOND_MESCS = 1000;
    public static final int ONE_MINUTE_SECONDS = 60;
    public static final int ONE_HOUR_MINUTES = 60;
    public static final int ONE_DAY_HOURS = 24;
    public static final int ID_CARD_LENGTH = 18;
    public static final int ONE_HOUR_SECONDS = ONE_HOUR_MINUTES * ONE_MINUTE_SECONDS;
    public static final int ONE_DAY_SECONDS = ONE_DAY_HOURS * ONE_HOUR_MINUTES * ONE_MINUTE_SECONDS;

    /**
     * 把日期转换为指定格式化的字符串
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String formatStr(Date date, String formatStr) {
        if (date == null || StringUtils.isBlank(formatStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    /**
     * 获取当前日期时间毫秒的常用格式化字符串
     *
     * @return
     */
    public static String getDateFormatDateTimeMsecStr() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATE_TIME_MSEC);
        return sdf.format(date);
    }

    /**
     * 获取指定日期和时间的常用格式化字符串
     *
     * @param date
     * @return
     */
    public static String getDateFormatDateTimeStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATE_TIME);
        return sdf.format(date);
    }

    public static String formatToMD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_MM_DD);
        return sdf.format(date);
    }

    /**
     * 获取当前日期和时间的常用格式化字符串
     *
     * @return
     */
    public static String getDateFormatDateTimeStr() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATE_TIME);
        return sdf.format(date);
    }

    /**
     * 获取当前日期的常用格式化字符串
     *
     * @return
     */
    public static String getDateFormatDateStr() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATE);
        return sdf.format(date);
    }

    /**
     * 获取指定日期的常用格式化字符串
     *
     * @return
     */
    public static String getDateFormatDateStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATE);
        return sdf.format(date);
    }

    /**
     * 当前系统时间毫秒
     **/
    public static Long time() {
        return System.currentTimeMillis();
    }

    public static Long time(Date date) {
        return date.getTime();
    }

    /**
     * 获取当前月
     *
     * @return
     */
    public static String getMonthOfYear() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        return year + "-" + month;
    }

    /**
     * 获取当前月
     *
     * @return
     */
    public static String getMonthOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        return year + "-" + month;
    }

    /**
     * 获取当前周，周一为每周第一天
     *
     * @return
     */
    public static String getWeekOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        int year = c.get(Calendar.YEAR);
        int week = c.get(Calendar.WEEK_OF_YEAR);
        return year + "-" + week;
    }

    public static int getHourOfDate(Date currentDate) {
        if (currentDate == null) {
            return 0;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        return hour;
    }

    /**
     * 给定 format 将 string 转换成 Date
     **/
    public static Date formatToDate(String dateStr, String formatStr) {
        try {
            return new SimpleDateFormat(formatStr).parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 给定 format 将 string 转换成 Date
     **/
    public static Date formatToDate(String dateStr) {
        try {
            return new SimpleDateFormat(DATE_FORMAT_DATE_TIME).parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 给定 format 将 string 转换成 Date
     **/
    public static Date formatYMDHMToDate(String dateStr) {
        try {
            return new SimpleDateFormat(DATE_FORMAT_MINUTE_TIME).parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 时间转换 分钟 小时 天
     * 计算时间差
     *
     * @param date
     * @return
     */
    public static String timeHint(Date date) {
        String zeroStr = "0";
        if (null == date) {
            return zeroStr;
        }
        Date now = new Date();
        Long seconds = DoubleMath.roundToLong((date.getTime() - now.getTime()) / ONE_SECOND_MESCS, RoundingMode.FLOOR);
        if (seconds <= 0) {
            return zeroStr;
        }
        if (seconds < ONE_MINUTE_SECONDS) {
            return "1分钟";
        }
        // 分钟
        if (seconds < ONE_HOUR_SECONDS) {
            return DoubleMath.roundToLong(seconds / ONE_MINUTE_SECONDS, RoundingMode.FLOOR) + "分钟";
        }
        // 小时
        if (seconds < ONE_DAY_SECONDS) {
            return DoubleMath.roundToLong(seconds / ONE_HOUR_SECONDS, RoundingMode.FLOOR) + "小时";
        }
        // 天
        int nowMonthMaxDays = getNowMonthMaxDays();
        if (seconds < nowMonthMaxDays * ONE_DAY_SECONDS) {
            return DoubleMath.roundToLong(seconds / ONE_DAY_SECONDS, RoundingMode.FLOOR) + "天";
        }

        return DoubleMath.roundToLong(seconds / ONE_DAY_SECONDS, RoundingMode.FLOOR) + "天";
    }

    public static long diffSeconds(Date date1, Date date2) {
        if (null == date1 || null == date2) {
            return 0;
        }

        return DoubleMath.roundToLong((date1.getTime() - date2.getTime()) / ONE_SECOND_MESCS, RoundingMode.FLOOR);
    }

    /**
     * 时间转换 天+小时+分钟 格式
     * 计算时间差
     *
     * @param date
     * @return
     */
    public static String timeHintDMS(Date date) {
        String zeroStr = "0";
        String result = "";
        if (null == date) {
            return zeroStr;
        }
        Date now = new Date();
        Long seconds = DoubleMath.roundToLong((date.getTime() - now.getTime()) / ONE_SECOND_MESCS, RoundingMode.FLOOR);
        if (seconds <= 0) {
            return zeroStr;
        }
        Long remainSeconds;
        // 天
        if (seconds >= ONE_DAY_SECONDS) {
            remainSeconds = seconds % ONE_DAY_SECONDS;
            result = seconds / ONE_DAY_SECONDS + "天";
            seconds = remainSeconds;
        }
        // 小时
        if (seconds < ONE_DAY_SECONDS && seconds >= ONE_HOUR_SECONDS) {
            remainSeconds = seconds % ONE_HOUR_SECONDS;
            result += seconds / ONE_HOUR_SECONDS + "小时";
            seconds = remainSeconds;
        }
        // 分钟
        if (seconds < ONE_HOUR_SECONDS) {
            result += DoubleMath.roundToLong(seconds / ONE_MINUTE_SECONDS, RoundingMode.FLOOR) + "分钟";
        }
        return result;
    }

    /**
     * 获取当前月一共有多少天
     *
     * @return
     */
    public static int getNowMonthMaxDays() {
        Calendar now = Calendar.getInstance();
        // 把日期设置为当月第一天
        now.set(Calendar.DATE, 1);
        // 日期回滚一天，也就是最后一天
        now.roll(Calendar.DATE, -1);
        int nowMonthTotalDays = now.get(Calendar.DATE);
        return nowMonthTotalDays;
    }

    /**
     * 在给定的 Date 时间加 days 天
     **/
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 在给定的 Date 时间加 minutes 分钟
     **/
    public static Date addMinutes(Date date, int minutes) {
        if (minutes == 0) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    /**
     * 在给定的 Date 时间加 seconds 分钟
     **/
    public static Date addSeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * 获取距今一周前的时间点
     *
     * @return
     */
    public static Date getBeforeWeekDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, -6);
        return cal.getTime();
    }

    /**
     * 获取距今一周前的0点
     *
     * @return
     */
    public static Date getBeforeWeekZeroTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, -6);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取距今2周前的时间点
     *
     * @return
     */
    public static Date getBeforeTwoWeekDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, -13);
        return cal.getTime();
    }

    /**
     * 返回当前时间
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获取凌晨时间
     *
     * @return
     */
    public static String getZeroTime(Date date, int days) {
        Date zero = getZeroTimeToDate(date, days);
        return getDateFormatDateTimeStr(zero);
    }

    /**
     * 获取凌晨时间返回Date
     *
     * @return
     */
    public static Date getZeroTimeToDate(Date date, int days) {
        return getZeroTimeToDate(addDays(date, days));
    }

    /**
     * 获取凌晨时间返回Date
     *
     * @return
     */
    public static Date getZeroTimeToDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }

    /**
     * 获取一天最晚的时间点
     *
     * @return
     */
    public static Date getEndOfToday() {
        return getEndOfToday(new Date());
    }

    /**
     * 获取一天最晚的时间点
     *
     * @return
     */
    public static Date getEndOfToday(Date date, int days) {
        return getEndOfToday(addDays(date, days));
    }

    public static Date getEndOfToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static int getAgeByIdCard(String idCard) {
        int iAge = 0;
        if (StringUtils.isEmpty(idCard) || idCard.length() != ID_CARD_LENGTH) {
            return iAge;
        }
        Calendar cal = Calendar.getInstance();
        String year = idCard.substring(6, 10);
        int iCurrYear = cal.get(Calendar.YEAR);
        iAge = iCurrYear - Integer.valueOf(year);
        return iAge;
    }

    public static boolean isOverMinute(Date date, int minute) {
        return System.currentTimeMillis() - date.getTime() >= minute * 60 * 1000;
    }

    public static Integer diffSeconds(Long date, Integer expireTime) {
        Long diff = expireTime * 60 - (System.currentTimeMillis() - date) / 1000;
        if (diff < 0) {
            return 0;
        } else {
            return diff.intValue();
        }
    }

    /**
     * 获取当周星期一
     *
     * @param
     * @return
     */
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getNextWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, 7);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date geLastWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }

    /**
     * 获取当月第一天
     *
     * @param
     * @return
     */
    public static Date getFirstOfThisMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 0);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 获取下月第一天
     *
     * @param
     * @return
     */
    public static Date getFirstOfNextMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static boolean isBefore(Date date1, Date date2) {
        return date1.before(date2);
    }

    /**
     * 获取生日
     *
     * @param date
     * @return
     */
    public static String getBirthdayString(Date date) {
        return getDateFormatDateStr(date);
    }

    /**
     * 计算年龄
     *
     * @param birthday
     * @return
     */
    public static int getAgeByBirthday(Date birthday) {
        if (birthday == null) {
            return 0;
        }

        Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) {
            log.error("生日错误!Birthday:{}", JSON.toJSONString(birthday));
            return 0;
        }
        int yearNow = cal.get(Calendar.YEAR);
        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        return yearNow - yearBirth;
    }

    /**
     * 当前系统时间秒
     **/
    public static Long second() {
        return Instant.now().getEpochSecond();
    }

    /**
     * 获取分钟
     *
     * @param
     * @return
     */
    public static int getCurrentMinute() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.MINUTE);
    }

    public static int getTimeDelta(Date startTime, Date endTime) {
        //单位是秒
        long timeDelta = (endTime.getTime() - startTime.getTime()) / 1000;
        return (int)timeDelta;
    }

    /**
     * LocalDate转Date
     *
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate) {
        if (null == localDate) {
            return null;
        }

        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date addYears(Date date, int addAmount) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, addAmount);
        return calendar.getTime();
    }

    public static String formatYMD() {
        return DateUtils.formatStr(new Date(), "yyyyMMdd");
    }

    public static String formatYMD(Date date) {
        return DateUtils.formatStr(date, "yyyyMMdd");
    }

    // 获得某天最大时间 2017-10-15 23:59:59
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()),
            ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()),
            ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date addYears(int addAmount) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, addAmount);
        return calendar.getTime();
    }

    public static String getCountDownDHM(Date endDate) {

        long nd = ONE_SECOND_MESCS * ONE_DAY_SECONDS;
        long nh = ONE_SECOND_MESCS * ONE_HOUR_SECONDS;
        long nm = ONE_SECOND_MESCS * ONE_MINUTE_SECONDS;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - System.currentTimeMillis();
        // 计算差多少天
        long day = diff / nd;
        String dayStr = day < 10 ? "0" .concat(String.valueOf(day)) : String.valueOf(day);
        // 计算差多少小时
        long hour = diff % nd / nh;
        String hourStr = hour < 10 ? "0" .concat(String.valueOf(hour)) : String.valueOf(hour);
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        String minStr = min < 10 ? "0" .concat(String.valueOf(min)) : String.valueOf(min);

        return dayStr + "-" + hourStr + "-" + minStr;
    }

    /**
     * 获取两个日期差多少天 具体计算后 + 1 当天算在内
     *
     * @param endDate
     * @param nowDate
     * @return
     */
    public static long differDay(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;

        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd + 1;

        return day;
    }

    /**
     * 本周剩余多少长时间 单位 second
     *
     * @return
     */
    public static long diffTimeWeek() {
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);// 此处可换为具体某一时间
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        //当日剩余时间
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long time = calendar.getTimeInMillis() - System.currentTimeMillis();
        if (1 == weekDay) {
            //周日
            return time / 1000;
        }
        long timeDifferWeek = time / 1000 + (7 - weekDay + 1) * 24 * 60 * 60;
        return timeDifferWeek;

    }

    /**
     * 本月剩余多少长时间 单位 second
     *
     * @return
     */
    public static long diffTimeMonth() {
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);// 此处可换为具体某一时间
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //当日剩余时间
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long time = calendar.getTimeInMillis() - System.currentTimeMillis();
        return time + (actualMaximum - dayOfMonth) * 24 * 60 * 60 * 1000;

    }

    /**
     * 当日剩下多长时间秒 单位 second
     *
     * @return
     */
    public static long diffTimeDay() {
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);// 此处可换为具体某一时间
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        //当日剩余时间
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long time = calendar.getTimeInMillis() - System.currentTimeMillis();

        return time / 1000;

    }

    public static Boolean isSameDay(Date date1, Date date2) {
        String s1 = DateUtils.formatYMD(date1);
        String s2 = DateUtils.formatYMD(date2);
        if (s1.equalsIgnoreCase(s2)) {
            return true;
        }
        return false;
    }

    /**
     * 获取季度格式化时间
     *
     * @param date
     * @return
     */
    public static int getQuarterInt(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if (month < 3) {
            return NumberUtils.toInt(String.valueOf(year).concat("01"));
        }
        if (month < 6) {
            return NumberUtils.toInt(String.valueOf(year).concat("02"));
        }
        if (month < 9) {
            return NumberUtils.toInt(String.valueOf(year).concat("03"));
        }
        if (month < 12) {
            return NumberUtils.toInt(String.valueOf(year).concat("04"));
        }
        return 0;
    }

    public static String formatTimeDesc(int minutes) {
        int hour = minutes / 60;
        if (hour < 1) {
            return minutes + "分钟";
        }
        if (hour < 24) {
            return hour + "小时";
        }
        int day = minutes / (60 * 24);
        return day + "天";
    }

    public static String formatTimeDescBySeconds(Integer seconds) {
        if (seconds == null) {
            return "未知时间";
        }
        int hour = seconds / 3600;
        if (hour < 1) {
            return seconds + "分钟";
        }
        if (hour < 24) {
            return hour + "小时";
        }
        int day = seconds / (60 * 60 * 24);
        return day + "天";
    }

    /**
     * HH:mm:ss
     *
     * @param timetemp
     * @return
     */
    public static String getHMS(long timetemp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timetemp);
        SimpleDateFormat fmat = new SimpleDateFormat("HH:mm:ss");
        return fmat.format(calendar.getTime());
    }

    /**
     * 在给定的 Date 时间加 days 天
     **/
    public static Date addHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }

    /**
     * 分割时间，以小时为界限
     *
     * @param
     * @return
     */
    public static Map<String, String> slicedHourTime(String startTime, String endTime) {
        Map<String, String> map = Maps.newHashMap();
        Date startDate = formatToDate(startTime);
        Date endDate = formatToDate(endTime);

        if (startDate == null || endDate == null) {
            return null;
        }

        Date tempDate = addHours(startDate, 1);
        while (startDate.before(endDate)) {
            if (tempDate.after(endDate)) {
                tempDate = endDate;
            }
            map.put(getDateFormatDateTimeStr(startDate), getDateFormatDateTimeStr(tempDate));

            startDate = addHours(startDate, 1);
            tempDate = addHours(tempDate, 1);
        }
        return map;
    }

    public static String secondToTime(long second) {
        second = second / 1000;            //剩余秒数
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second / 60;            //转换分钟
        second = second % 60;                //剩余秒数
        return hours + ":" + minutes + ":" + second;
    }

    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(calendar.HOUR_OF_DAY);
    }

    /**
     * 时间转换 分钟 小时 天
     * 计算时间差
     *
     * @param date
     * @return
     */
    public static String expireTimeHint(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        Date now = new Date();
        Long seconds = DoubleMath.roundToLong((date.getTime() - now.getTime()) / ONE_SECOND_MESCS, RoundingMode.FLOOR);
        if (seconds <= 0) {
            return StringUtils.EMPTY;
        }
        // 分钟
        if (seconds < ONE_HOUR_SECONDS) {
            return DoubleMath.roundToLong(seconds / ONE_MINUTE_SECONDS, RoundingMode.FLOOR) + "分钟";
        }
        // 48小时
        if (seconds < 2 * ONE_DAY_SECONDS) {
            return DoubleMath.roundToLong(seconds / (ONE_HOUR_SECONDS), RoundingMode.FLOOR) + "小时";
        }
        // 30天
        if (seconds <= 180 * ONE_DAY_SECONDS) {
            return DoubleMath.roundToLong(seconds / ONE_DAY_SECONDS, RoundingMode.FLOOR) + "天";
        }
        return StringUtils.EMPTY;
    }

    public static String getWeekStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        return formatStr(date, DATE_FORMAT_DATE_TIME);
    }

    public static String getFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return formatStr(cal.getTime(), DATE_FORMAT_DATE_TIME);
    }

    public static Date getFirstDateOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static String getLastWeekStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DATE, -7);
        Date date = cal.getTime();
        return formatStr(date, DATE_FORMAT_DATE_TIME);
    }

    public static String getFirstDayOfLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.MONTH, -1);
        return formatStr(cal.getTime(), DATE_FORMAT_DATE_TIME);
    }

    public static String formatYMDH() {
        return DateUtils.formatStr(new Date(), DATE_FORMAT_HOUR);
    }

    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    public static int yearToNow(Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);
        Calendar to = Calendar.getInstance();
        to.setTime(toDate);

        int fromYear = from.get(Calendar.YEAR);

        int toYear = to.get(Calendar.YEAR);
        int year = toYear - fromYear;
        return year;
    }

    /**
     * 今天还剩多少秒
     */
    public static int remainSecondToday() {
        long nowTimeMillis = System.currentTimeMillis();
        Calendar tomorrowZero = Calendar.getInstance();
        tomorrowZero.setTime(new Date(nowTimeMillis + 24 * 60 * 60 * 1000L));
        tomorrowZero.set(Calendar.HOUR_OF_DAY, 0);
        tomorrowZero.set(Calendar.SECOND, 0);
        tomorrowZero.set(Calendar.MINUTE, 0);
        tomorrowZero.set(Calendar.MILLISECOND, 0);
        return (int)(tomorrowZero.getTime().getTime() - nowTimeMillis) / 1000;
    }

    /**
     * 判断当前时间是否在指定时间范围内
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isCurrentTimeBetween(String startTime, String endTime) {
        DateFormat hhmmssFormat = new SimpleDateFormat(DATE_FORMAT_MINUTE_TIME);
        Date now = new Date();
        try {
            Date startDate = hhmmssFormat.parse(startTime);
            Date endDate = hhmmssFormat.parse(endTime);
            return startDate.before(now) && endDate.after(now);

        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 获取当前时间往前推的整点时间
     *
     * @param executeTime
     * @return
     */
    public static Date getCurrentTheHour(Date executeTime) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(executeTime.getTime()),
            ZoneId.systemDefault());
        // 2021-07-02 10:01:15 -> 2021-07-02 10:00:00
        LocalDateTime formatTime = localDateTime.minusMinutes(localDateTime.getMinute()).minusSeconds(
            localDateTime.getSecond()).atZone(ZoneId.systemDefault()).toLocalDateTime();

        return Date.from(formatTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}