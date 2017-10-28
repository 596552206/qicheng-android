package me.milechen.qicheng.Utils;

import java.text.SimpleDateFormat;

/**
 * Created by mile on 2017/7/12.
 */
public class TimeUtil {

    static public long MIMUTE = 60 * 1000;
    static public long WHILE = 3 * MIMUTE;
    static public long HOUR = 60 * MIMUTE;
    static public long DAY = 24 * HOUR;
    static public long WEEK = 7 * DAY;
    static public long MONTH = 30 * DAY;
    static public long YEAR = 365 * DAY;

    static public long getTimestamp() {
        return System.currentTimeMillis();
    }

    static public long formatTimestamp2JavaForm(String timestamp) {
        if (timestamp.length() == 13) {
            return Long.valueOf(timestamp);
        } else {
            timestamp = timestamp + "000";
            return Long.valueOf(timestamp);
        }
    }

    static public String generateSmartTime(long timestamp) {
        long now = System.currentTimeMillis();
        long dis = (now - timestamp);
        //Log.i("ii", dis + "");
        String words;
        if (dis > 0 && dis <= WHILE) { //小于三分钟
            words = "刚刚";
        } else if (dis > WHILE && dis <= HOUR) { //大于三分,小于一小时
            words = Math.round(dis / MIMUTE) == 60 ? "1小时前" : Math.round(dis / MIMUTE) + "分钟前";
        } else if (dis > HOUR && dis <= DAY) {//大于一小时,小于一天
            words = Math.round(dis / HOUR) == 24 ? "1天前" : Math.round(dis / HOUR) + "小时前";
        } else if (dis > DAY && dis <= WEEK) {//大于一天,小于一周
            words = Math.round(dis / DAY) == 7 ? "1周前" : Math.round(dis / DAY) + "天前";
        } else if (dis > WEEK && dis <= MONTH) {//大于一周,小于一个月
            words = Math.round(dis / WEEK) == 4 ? "1个月前" : Math.round(dis / WEEK) + "周前";
        } else if (dis > MONTH && dis <= YEAR) {//大于一月,小于一年
            words = Math.round(dis / MONTH) == 12 ? "1年前" : Math.round(dis / MONTH) + "个月前";
        } else if (dis > YEAR && dis < 10 * YEAR) {//大于一点,小于十年
            words = Math.round(dis / YEAR) + "年前";
        } else {
            SimpleDateFormat dateFm = new SimpleDateFormat("yyyy年MM月dd日"); //格式化当前系统日期
            words = dateFm.format(timestamp);
        }
        return words;
    }
}

