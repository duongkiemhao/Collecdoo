package com.collecdoo.helper;

import com.collecdoo.MyApplicationContext;
import com.collecdoo.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class DateHelper {
    public static String DD_MM_YYYY = "dd/MM/yyyy";
    public static String MM_DD_YYYY = "MM/dd/yyyy";
    public static String DD_MMM_YYYY = "dd/MMM/yyyy";
    public static String DDMMMYYYY = "dd MMM yyyy";

    public static String SQLITE_SHORT_FORMAT = "yyyy-MM-dd";
    public static String YYYY_FORMAT = "yyyy";
    public static String MMM_YYYY_FORMAT = "MMM yyyy";
    public static String SQLITE_COMPARE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String EEE_MMM_DD_YYYY = "EEE, MMM dd, yyyy";
    public static String SQLITE_DATEPART = "yyyy-MM-dd 00:00:00";

    // public static String SQL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static String SERVER_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static String HH_MM = "HH:mm";
    public static String DD_MM = "dd/MM";
    public static String SERVER_DATE_FORMAT= "yyyy-MM-dd HH:mm";

    // public static String DD_MM_YYYY_HH_mm_YYYY = "dd/MM/yyyy HH:mm:ss";

    public static String getParaDateSQL(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                SQLITE_SHORT_FORMAT);

        return simpleDateFormat.format(date);
    }

    public static String formatDateToServer(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SERVER_FORMAT);
        return simpleDateFormat.format(date);
    }

    public static String formatDate(String date, String pattern) {
        Date d = parseStringToDate(date, SERVER_FORMAT);
        return parseDateToString(d, pattern);
    }

    public static String formatDate(String date, String fromPattern,
                                    String toPattern) {
        if (date == null || date.equals("") || date.equals("null"))
            return "";
        Date d = parseStringToDate(date, fromPattern);
        return parseDateToString(d, toPattern);
    }

    // public static Date formatSQLiteToDate(String date) {
    // SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SQL_FORMAT);
    // try {
    // if (date != null)
    // return simpleDateFormat.parse(date);
    // else
    // return null;
    // } catch (ParseException e) {
    // return null;
    // }
    // }

    // public static String parseSQLToServer(String date) {
    // Date serverDate= formatSQLiteToDate(date);
    // return formatDateToServer(serverDate);
    // }

    public static String formatDDorMMString(int month) {
        if (month < 10)
            return "0" + month;
        else
            return month + "";
    }

    // 1:> ,0:= ,-1<
    public static int compare2DatePart(Date start, Date end) {
        if (start.getYear() != end.getYear())
            return start.getYear() - end.getYear();
        if (start.getMonth() != end.getMonth())
            return start.getMonth() - end.getMonth();
        return start.getDate() - end.getDate();

    }

    public static int compareMMYYYYPart(Date start, Date end) {
        if (start.getYear() != end.getYear())
            return start.getYear() - end.getYear();
        if (start.getMonth() != end.getMonth())
            return start.getMonth() - end.getMonth();
        return 0;

    }

    public static int compareYYYYPart(Date start, Date end) {
        if (start.getYear() != end.getYear())
            return start.getYear() - end.getYear();
        return 0;

    }

    // 1:> ,0:= ,-1<
    public static int compareDDMMPart(Date start, Date end) {

        if (start.getMonth() != end.getMonth())
            return start.getMonth() - end.getMonth();
        return start.getDate() - end.getDate();
    }

    public static Date parseStringToDate(String date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            Utility.showException(MyApplicationContext.getInstance()
                    .getAppContext(), e);
            return null;
        }
    }

    public static Date parseStringToDateUTC(String date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            Utility.showException(MyApplicationContext.getInstance()
                    .getAppContext(), e);
            return null;
        }
    }

    public static String parseDateToString(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(date);

    }

    public static Date parseDateToUTCDate(Date date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SERVER_FORMAT);
        String strDate = simpleDateFormat.format(date);
        simpleDateFormat = new SimpleDateFormat(SERVER_FORMAT);
        try {
            return simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            Utility.showException(MyApplicationContext.getInstance()
                    .getAppContext(), e);
            return null;
        }
    }

    public static Date parseStringToUTCDate(String date, String fromPattern,
                                            String toPattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(toPattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String parseStringToString(String strDate,
                                             String fromPattern, String toPattern) {
        SimpleDateFormat fromFormat = new SimpleDateFormat(fromPattern);
        SimpleDateFormat toFormat = new SimpleDateFormat(toPattern);

        try {
            Date date = fromFormat.parse(strDate);
            return toFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String parseDateToStringUTC(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date);

    }

    public static String parseUTCToLocal(Date date, String format) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(date.getTime());
            calendar.add(Calendar.MILLISECOND,
                    tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date parseDate = (Date) calendar.getTime();
            return sdf.format(parseDate);
        } catch (Exception e) {
        }
        return "";
    }

    public static String getcurrentDateString() {
        return parseDateToString(new Date(), SERVER_FORMAT);
    }

    public static String getcurrentDateStringUTC() {
        SimpleDateFormat sourceFormat = new SimpleDateFormat(SERVER_FORMAT);
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sourceFormat.format(new Date());
    }

    // public static String getcurrentDatePartString(){
    // SimpleDateFormat sourceFormat = new SimpleDateFormat(SERVER_FORMAT);
    // sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    // return sourceFormat.format(new Date());
    // }

    public static String formatInUTC(Date date) {
        SimpleDateFormat sourceFormat = new SimpleDateFormat(SERVER_FORMAT);
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sourceFormat.format(date);
    }

    public static int getInsertIdOnMilisecond() {
        Date date = new Date();
        return Integer.parseInt(date.getTime() / 1000 + "");
    }

    public static Date[] getDaysInWeek(Date date) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);

        Date[] days = new Date[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; // add 2 if
        // your week
        // start on
        // monday
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = now.getTime();
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }
}
