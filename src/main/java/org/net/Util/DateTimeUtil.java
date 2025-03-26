package org.net.Util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public  class DateTimeUtil {

    LocalDate currentDate = LocalDate.now();

    /**
     * 获取指定偏移量和时间单位的日期，并返回格式化后的字符串
     *
     * @param amount   偏移量
     * @param direction 偏移方向（before 或者 after）
     * @param unit      时间单位 ChronoUnit（day, week, month, year）
     * @param pattern   日期格式 默认yyyy-MM-dd
     * @return 格式化后的日期字符串
     */
    public static String getFormattedDate(int amount, String direction, ChronoUnit unit, String pattern) {
        if(StringUtils.isNullorEmpty(direction)){
            direction="before";
        }
        if(StringUtils.isNullorEmpty(pattern)){
            pattern="yyyy-MM-dd";
        }
        LocalDate currentDate = LocalDate.now();
        if ("before".equals(direction)) {
            currentDate = currentDate.minus(amount, unit);
        } else if ("after".equals(direction)) {
            currentDate = currentDate.plus(amount, unit);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return currentDate.format(formatter);
    }



    public static void main(String[] args) {
        System.out.println(getFormattedDate(1, "before", ChronoUnit.WEEKS, "yyyy-MM-dd"));
    }
}
