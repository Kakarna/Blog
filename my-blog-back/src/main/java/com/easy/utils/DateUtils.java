package com.easy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {

    // 锁对象，确保线程安全
    private static final Object lockObj = new Object();

    // 每种 pattern 对应一个 ThreadLocal<SimpleDateFormat>
    private static final Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();

    // 获取线程安全的 SimpleDateFormat 实例
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }
        return tl.get();
    }

    // 日期格式化
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    // 日期解析
    public static Date parse(String dateStr, String pattern) {
        try {
            return getSdf(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
