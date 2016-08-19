package com.yxh.ryt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/7/12.
 */
public class DateUtil {

    public static String[] WEEK = new String[]{"天", "一", "二", "三", "四", "五", "六"};

    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = ONE_SECOND * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;

    /**
     * String 转换 Date
     *
     * @param str
     * @param format
     * @return
     */
    public static Date string2Date(String str, String format) {
        try {
            return new SimpleDateFormat(format).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * Date（long） 转换 String
     *
     * @param time
     * @param format
     * @return
     */
    public static String date2String(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(time);
        return s;
    }

    /**
     * long 去除 时分秒
     * 时分秒全部为0
     *
     * @param date
     * @return
     */
    public static long getYearMonthDay(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取目标时间和当前时间之间的差距
     *
     * @param date
     * @return
     */
    public static String getTimestampString(Date date) {
        Date curDate = new Date();
        long splitTime = curDate.getTime() - date.getTime();
        if (splitTime < (30 * ONE_DAY)) {
            if (splitTime < ONE_MINUTE) {
                return "刚刚";
            }
            if (splitTime < ONE_HOUR) {
                return String.format("%d分钟前", splitTime / ONE_MINUTE);
            }

            if (splitTime < ONE_DAY) {
                return String.format("%d小时前", splitTime / ONE_HOUR);
            }

            return String.format("%d天前", splitTime / ONE_DAY);
        }
        String result;
        result = "M月d日 HH:mm";
        return (new SimpleDateFormat(result, Locale.CHINA)).format(date);
    }

    /**
     * 24小时制 转换 12小时制
     *
     * @param time
     * @return
     */
    public static String time24To12(String time) {
        String str[] = time.split(":");
        int h = Integer.valueOf(str[0]);
        int m = Integer.valueOf(str[1]);
        String sx;
        if (h < 1) {
            h = 12;
            sx = "上午";
        } else if (h < 12) {
            sx = "上午";
        } else if (h < 13) {
            sx = "下午";
        } else {
            sx = "下午";
            h -= 12;
        }
        return String.format("%d:%02d%s", h, m, sx);
    }

    /**
     * Date 转换 HH
     *
     * @param date
     * @return
     */
    public static String date2HH(Date date) {
        return new SimpleDateFormat("HH").format(date);
    }

    /**
     * Date 转换 HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String date2HHmm(Date date) {
        return new SimpleDateFormat("HH:mm").format(date);
    }

    /**
     * Date 转换 HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String date2HHmmss(Date date) {
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    /**
     * Date 转换 MM.dd
     *
     * @param date
     * @return
     */
    public static String date2MMdd(Date date) {
        return new SimpleDateFormat("MM.dd").format(date);
    }

    /**
     * Date 转换 yyyy.MM.dd
     *
     * @param date
     * @return
     */
    public static String date2yyyyMMdd(Date date) {
        return new SimpleDateFormat("yyyy.MM.dd").format(date);
    }

    /**
     * Date 转换 MM月dd日 星期
     *
     * @param date
     * @return
     */
    public static String date2MMddWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return new SimpleDateFormat("MM月dd日 星期").format(date) + WEEK[dayOfWeek - 1];
    }

    /**
     * Date 转换 yyyy年MM月dd日 星期
     *
     * @param date
     * @return
     */
    public static String date2yyyyMMddWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return new SimpleDateFormat("yyyy年MM月dd日 星期").format(date) + WEEK[dayOfWeek - 1];
    }

    /**
     * * @param millis * 要转化的毫秒数。
     * * @param isWhole * 是否强制全部显示小时/分。
     * * @param isFormat * 时间数字是否要格式化，如果true：少位数前面补全；如果false：少位数前面不补全。
     * * @return 返回时间字符串：小时/分/秒/毫秒的格式（如：24903600 --> 06小时55分钟）。
     */
    public static String millisToStringShort(long millis, boolean isWhole, boolean isFormat) {
        String h = "";
        String m = "";
        String d = "";
        String s = "";
        if (isWhole) {
            h = isFormat ? "00天" : "0天";
            h = isFormat ? "00时" : "0小";
            m = isFormat ? "00分" : "0分";
            s = isFormat ? "00秒" : "0秒";
        }
        long temp = millis;
        long dper = 24 * 60 * 60 * 1000;
        long hper = 60 * 60 * 1000;
        long mper = 60 * 1000;
        long sper = 1000;
        if (temp / dper > 0) {
            d = temp / dper + "天";
        }
        temp = temp % dper;
        if (temp / hper > 0) {
            if (isFormat) {
                h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";
            } else {
                h = temp / hper + "";
            }
            h += "时";
        }
        temp = temp % hper;
        if (temp / mper > 0) {
            if (isFormat) {
                m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";
            } else {
                m = temp / mper + "";
            }
            m += "分";
        }
        temp = temp % mper;
        if (temp / sper > 0) {
            if (isFormat) {
                s = temp / sper < 10 ? "0" + temp / sper : temp / sper + "";
            } else {
                s = temp / sper + "";
            }
            s += "秒";
        }
        return d + h + m + s;
    }

    /**
     * *几天前
     * *只显示天、时、分
     */
    public static String millionToNearly(long millions) {
        long currentTime = System.currentTimeMillis();
        long timeGap = currentTime - millions;
        if (timeGap >= 1000 * 60 * 60 * 24) {
            String s = timeGap / 1000 / 60 / 60 / 24 + "天前";
            return s;
        } else if (timeGap < 1000 * 60 * 60 * 24 && timeGap >= 1000 * 60 * 60) {
            String s = timeGap / 1000 / 60 / 60 + "小时前";
            return s;
        } else if (timeGap < 1000 * 60 * 60 && timeGap >= 1000 * 60) {
            String s = timeGap / 1000 / 60 + "分种前";
            return s;
        } else {
            String s = timeGap / 1000 + "秒前";
            return s;
        }
    }

    /**
     * *几天后截止
     * *只显示天、时、分
     */
    public static String millionToDead(long millions) {
        long currentTime = System.currentTimeMillis();
        long timeGap = millions - currentTime;
        if (timeGap >= 1000 * 60 * 60 * 24) {
            String s = timeGap / 1000 / 60 / 60 / 24 + "天";
            return s;
        } else if (timeGap < 1000 * 60 * 60 * 24 && timeGap >= 1000 * 60 * 60) {
            String s = timeGap / 1000 / 60 / 60 + "小时";
            return s;
        } else if (timeGap < 1000 * 60 * 60 && timeGap >= 1000 * 60) {
            String s = timeGap / 1000 / 60 + "分种";
            return s;
        } else {
            String s = timeGap / 1000 + "秒";
            return s;
        }
    }
}
