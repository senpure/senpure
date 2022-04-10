package com.senpure.base.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class DateFormatUtil {
    /**
     * DATE FORMAT PATTERN YEAR TO MILLISECOND like 2017-12-12 12:45:50:123
     */
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
    /**
     * DATE FORMAT PATTERN YEAR TO SECOND like 2017-12-12
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * DATE FORMAT PATTERN YEAR TO Day like 2017-12-12
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    private static final ThreadLocal<Map<String, DateFormat>> threadLocal = ThreadLocal.withInitial(() -> new HashMap<>());

    public static DateFormat getDateFormat(String pattern) {
        Map<String, DateFormat> dateFormatMap = threadLocal.get();
        DateFormat dateFormat = dateFormatMap.get(pattern);
        if (dateFormat != null) {
            return dateFormat;
        }
        dateFormat = new SimpleDateFormat(pattern);
        dateFormatMap.put(pattern, dateFormat);
        return dateFormatMap.get(pattern);
    }

    public static String format(Date date, String pattern) {

        return getDateFormat(pattern).format(date);
    }
    public static String format(Date date) {

        return getDateFormat(YYYY_MM_DD_HH_MM_SS).format(date);
    }
}
