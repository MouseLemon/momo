package com.joysupply.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName MyCanledar
 * @Author MaZhuli
 * @Date 2018/11/14 15:11
 * @Description ad
 * @Version 1.0
 **/
public class MyCalendar {

    public static void main(String[] args) throws ParseException {
        //周日是1,周六是7
        System.out.println(weekCalendar(2018, 10, 16));
    }

    public static List<List<Map<String, Object>>> monthCalendar(Integer year, Integer month, Integer today) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatWord = new SimpleDateFormat("MM月dd日");
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH, 1);
        cale.set(Calendar.YEAR, year);
        cale.set(Calendar.MONTH, month);
        int currentMonth = cale.get(Calendar.MONTH);
        List<List<Map<String, Object>>> monthList = new ArrayList();
        labelA:
        for (int row = 0; row < 6; row++) {
            List<Map<String, Object>> weekList = new ArrayList();
            if (currentMonth < cale.get(Calendar.MONTH)) {
                break labelA;
            }
            for (int col = 0; col < 8; col++) {
                Map day = new HashMap();
                int weekday = cale.get(Calendar.DAY_OF_WEEK);
                if (col == 0) {
                    weekList.add(day);
                    continue;
                }
                if (row == 0) {
                    if (weekday == 2 || cale.get(Calendar.DAY_OF_MONTH) == 1) {
                        weekList.get(0).put("startDate", formatWord.format(cale.getTime()));
                    }
                    if (weekday == 1) {
                        weekList.get(0).put("endDate", formatWord.format(cale.getTime()));
                    }
                    if (col == 7 && weekday == 1) {
                        day.put("classDate", format.format(cale.getTime()));
                        day.put("day", cale.get(Calendar.DAY_OF_MONTH));
                        cale.add(Calendar.DATE, 1);
                    } else if (col == weekday - 1) {
                        day.put("classDate", format.format(cale.getTime()));
                        day.put("day", cale.get(Calendar.DAY_OF_MONTH));
                        cale.add(Calendar.DATE, 1);
                    } else {
                        day.put("classDate", "");
                        day.put("day", "");
                    }
                } else {
                    if (currentMonth < cale.get(Calendar.MONTH) || (cale.get(Calendar.MONTH) == 0) && currentMonth != 0) {
                        if (col == 7 || col == 1) {
                            day.put("classDate", "");
                            day.put("day", "");
                            weekList.add(day);
                            monthList.add(weekList);
                            break labelA;
                        }
                        day.put("classDate", "");
                        day.put("day", "");
                    } else {
                        if (weekday == 2) {
                            weekList.get(0).put("startDate", formatWord.format(cale.getTime()));
                        }
                        if (weekday == 1 || cale.get(Calendar.DAY_OF_MONTH) == cale.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                            weekList.get(0).put("endDate", formatWord.format(cale.getTime()));
                        }
                        day.put("classDate", format.format(cale.getTime()));
                        day.put("day", cale.get(Calendar.DAY_OF_MONTH));
                        cale.add(Calendar.DATE, 1);
                    }
                }
                weekList.add(day);
            }
            monthList.add(weekList);
        }
        return monthList;
    }

    public static List<Map<String, Object>> weekCalendar(Integer year, Integer month, Integer day) throws ParseException {
        List<Map<String, Object>> list = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.YEAR, year);
        cale.set(Calendar.MONTH, month);
        cale.set(Calendar.DAY_OF_MONTH, day);
        for (int i = 0; i < 7; i++) {
            Map weekDay = new HashMap();
            switch (i) {
                case 0:
                    weekDay.put("weekDay", "星期一");
                    weekDay.put("date", format.format(cale.getTime()));
                    weekDay.put("classDate", formatYear.format(cale.getTime()));
                    cale.add(Calendar.DAY_OF_MONTH, 1);
                    list.add(weekDay);
                    break;
                case 1:
                    weekDay.put("weekDay", "星期二");
                    weekDay.put("date", format.format(cale.getTime()));
                    weekDay.put("classDate", formatYear.format(cale.getTime()));
                    cale.add(Calendar.DAY_OF_MONTH, 1);
                    list.add(weekDay);
                    break;
                case 2:
                    weekDay.put("weekDay", "星期三");
                    weekDay.put("date", format.format(cale.getTime()));
                    weekDay.put("classDate", formatYear.format(cale.getTime()));
                    cale.add(Calendar.DAY_OF_MONTH, 1);
                    list.add(weekDay);
                    break;
                case 3:
                    weekDay.put("weekDay", "星期四");
                    weekDay.put("date", format.format(cale.getTime()));
                    weekDay.put("classDate", formatYear.format(cale.getTime()));
                    cale.add(Calendar.DAY_OF_MONTH, 1);
                    list.add(weekDay);
                    break;
                case 4:
                    weekDay.put("weekDay", "星期五");
                    weekDay.put("date", format.format(cale.getTime()));
                    weekDay.put("classDate", formatYear.format(cale.getTime()));
                    cale.add(Calendar.DAY_OF_MONTH, 1);
                    list.add(weekDay);
                    break;
                case 5:
                    weekDay.put("weekDay", "星期六");
                    weekDay.put("date", format.format(cale.getTime()));
                    weekDay.put("classDate", formatYear.format(cale.getTime()));
                    cale.add(Calendar.DAY_OF_MONTH, 1);
                    list.add(weekDay);
                    break;
                case 6:
                    weekDay.put("weekDay", "星期日");
                    weekDay.put("date", format.format(cale.getTime()));
                    weekDay.put("classDate", formatYear.format(cale.getTime()));
                    cale.add(Calendar.DAY_OF_MONTH, 1);
                    list.add(weekDay);
                    break;
                default:
                    break;
            }
        }
        return list;
    }
}