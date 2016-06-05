package com.ppma.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

final public class DateUtils {

    public static int getAmPm() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.AM_PM); // 0 : am, 1 : pm
    }

    public static int getHour() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR);
    }

    public static int getMinute() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MINUTE);
    }

    public static int getSecond() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.SECOND);
    }

    public static String getTime(String pattern) {
        return getHour() + pattern + getMinute() + pattern + getSecond();
    }

    public static int getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Calendar cal = Calendar.getInstance();
        return (cal.get(Calendar.MONTH) + 1);
    }

    public static int getDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateToday() {
        String curDate = getDateToday("yyyyMMdd");
        return curDate;
    }

    public static String getDateToday(String format) {
        if (format == null) {
            format = "yyyyMMdd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Calendar cal = Calendar.getInstance();
        String curDate = sdf.format(cal.getTime());

        return curDate;
    }

    public static String getDateOfFirst(String date, String format) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String parseDate = date.replace("-", "").replace(":", "").replace(" ", "");
        Date tempDate = sdf.parse(parseDate);

        Calendar cal = Calendar.getInstance();

        cal.setTime(tempDate);
        cal.add(Calendar.DAY_OF_MONTH, -(cal.get(Calendar.DAY_OF_MONTH) - 1));

        if (format == null) {
            format = "yyyyMMdd";
        }

        return new SimpleDateFormat(format).format(cal.getTime());
    }

    public static String getYearOfDate() {
        return getDateToday().substring(0, 4);
    }

    public static String getMonthOfDate() {
        return getDateToday().substring(4, 6);
    }

    public static String getTodayOfDate() {
        return getDateToday().substring(6, 8);
    }

    public static String getYearOfDate(String date) {
        String parseDate = date.replace("-", "").replace(":", "").replace(" ", "");
        return parseDate.substring(0, 4);
    }

    public static String getMonthOfDate(String date) {
        String parseDate = date.replace("-", "").replace(":", "").replace(" ", "");
        return parseDate.substring(4, 6);
    }

    public static String getTodayOfDate(String date) {
        String parseDate = date.replace("-", "").replace(":", "").replace(" ", "");
        return parseDate.substring(6, 8);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateWithFormat(String date, String format) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date tempDate = sdf.parse(date);

        Calendar cal = Calendar.getInstance();

        cal.setTime(tempDate);

        sdf = new SimpleDateFormat(format);

        return sdf.format(cal.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDifferenceDay(String date, int diff) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String parseDate = date.replace("-", "").replace(":", "").replace(" ", "");
        Date tempDate = sdf.parse(parseDate);

        Calendar cal = Calendar.getInstance();

        cal.setTime(tempDate);
        cal.add(Calendar.DAY_OF_MONTH, diff);


        return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDifferenceMonth(String date, int diff, String format) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String parseDate = date.replace("-", "").replace(":", "").replace(" ", "");
        Date tempDate = sdf.parse(parseDate);

        Calendar cal = Calendar.getInstance();

        cal.setTime(tempDate);
        cal.add(Calendar.MONTH, diff);

        if (format == null) {
            format = "yyyyMMdd";
        }

        return new SimpleDateFormat(format).format(cal.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTodayOfStrDay(String date) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String parseDate = date.replace("-", "").replace(":", "").replace(" ", "");
        Date tempDate = sdf.parse(parseDate);

        Calendar cal = Calendar.getInstance();

        cal.setTime(tempDate);

        String[] week = {"일", "월", "화", "수", "목", "금", "토"};

        return week[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static String getStringDate(String date) throws ParseException {
        String strDate = "";

        strDate = getMonthOfDate(date) + "월 " + getTodayOfDate(date) + "일 (" + getTodayOfStrDay(date) + ")";

        return strDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTimeStampAmPm(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("a");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String today = sdf.format(cal.getTime());

        return today;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTimeStamp(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timeStamp = new Timestamp(time);
        String today = sdf.format(timeStamp);

        return today;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTimeStampWithFormat(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Timestamp timeStamp = new Timestamp(time);
        String today = sdf.format(timeStamp);

        return today;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getRemainTimeWithFormat(long time) {
        int ss = (int) (time / 1000 % 60);
        int mm = (int) (time / (1000 * 60) % 60);
        int hh = (int) (time / (60 * 60 * 1000));

        String today = hh + "시간 " + mm + "분 " + ss + "초 남음";

        return today;
    }
}
