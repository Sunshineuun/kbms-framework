/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: DateUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;


/**
 * 
 * @version 1.0 日期工具类
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
    /** 预设日期格式 */
    private static final String DEFAULTDATEPATTERN = "yyyy-MM-dd";

    /** 预设日期时间格式 */
    private static final String DEFAULTDATETIMEPATTERN = "yyyyMMddhhmmss";


    /**
     * 获得当前时间，格式 yyyy/MM/dd HH:mm:ss
     * 
     * @return
     */
    public static String getCurrDateTime() {
        return format(Calendar.getInstance().getTime(), DEFAULTDATETIMEPATTERN);
    }


    /**
     * 根据时间格式获得当前时间
     */
    public static String getCurrDateTime(String partten) {
        return format(Calendar.getInstance().getTime(), partten);
    }


    /**
     * 根据默认格式获得格式化的时间,格式：yyyy/MM/dd HH:mm:ss
     * 
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        return format(date, DEFAULTDATETIMEPATTERN);
    }


    /**
     * 根据默认格式获得格式化的时间,格式：yyyy/MM/dd HH:mm:ss
     * 
     * @param millis
     * @return
     */
    public static String formatDateTime(long millis) {
        return format(millis, DEFAULTDATETIMEPATTERN);
    }


    /**
     * 根据默认格式获得格式化的日期，格式:yyyy/MM/dd
     * 
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return format(date, DEFAULTDATEPATTERN);
    }


    /**
     * 根据默认格式获得格式化的日期，格式:yyyy/MM/dd
     * 
     * @param millis
     * @return
     */
    public static String formatDate(long millis) {
        return format(millis, DEFAULTDATEPATTERN);
    }


    /**
     * 获得格式化的时间
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return format(date.getTime(), pattern);
    }


    /**
     * 获得格式化的时间
     * 
     * @param millis
     * @param pattern
     * @return
     */
    public static String format(long millis, String pattern) {
        return DateFormatUtils.format(millis, pattern);
    }


    /**
     * 根据string类型的日期，添加天数
     * 
     * @param dateStr
     * @param pattern
     * @param amount
     * @return
     * @throws ParseException
     */
    public static String addDays(String dateStr, String pattern, int amount)
        throws ParseException {
        return format(addDays(parse(dateStr, pattern), amount), pattern);
    }


    /**
     * 根据日期，添加天数
     * 
     * @param dateStr
     * @param pattern
     * @param amount
     * @return
     * @throws ParseException
     */
    public static String addDays(Date date, String pattern, int amount) {
        return format(addDays(date, amount), pattern);
    }


    /**
     * 获得两个时间的间隔,可以按秒、分钟、小时、天来获取
     * 
     * @param date1
     *        大的日期在前
     * @param date2
     * @return
     */
    public static int elapsed(Date date1, Date date2, int field) {
        if (date1 == null || date2 == null)
            throw new IllegalArgumentException("The date must not be null");

        long elapsed = date1.getTime() - date2.getTime();
        switch (field) {
            case Calendar.SECOND:
                return ((Number) (elapsed / 1000f)).intValue();
            case Calendar.MINUTE:
                return ((Number) (elapsed / (1000f * 60f))).intValue();
            case Calendar.HOUR:
                return ((Number) (elapsed / (1000f * 60f * 60f))).intValue();
            case Calendar.DATE:
                return ((Number) (elapsed / (1000f * 60f * 60f * 24f)))
                        .intValue();
            case Calendar.MONTH:
                return ((Number) (elapsed / (1000f * 60f * 60f * 24f * 30f)))
                        .intValue();
            case Calendar.YEAR:
                // return ((Number) (elapsed / (1000f * 60f * 60f * 24f * 30f *
                // 12f))).intValue();
                // 上面的方法 2015-07-24 减去 1948-08-10 得到67,导致错误，现修改为如下
                return getAgeByTwoDate(date1, date2);
        }

        return ((Number) (elapsed / (1000 * 60 * 60 * 24))).intValue();
    }


    /**
     * 获得两个时间的间隔,可以按秒、分钟、小时、天来获取
     * 
     * @param date1
     *        大的日期在前
     * @param date2
     * @return
     * @throws ParseException
     */
    public static int elapsed(String strDate1, String strDate2, String pattern,
        int field) throws ParseException {
        return elapsed(parse(strDate1, pattern), parse(strDate2, pattern),
                field);
    }


    /**
     * 使用参数Format将字符串转为Date
     * 
     * @param strDate
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(String strDate, String pattern)
        throws ParseException {
        return StringUtils.isEmpty(strDate) ? null : new SimpleDateFormat(
                pattern).parse(strDate);
    }


    /**
     * 按照默认格式（YYYY/MM/DD）将字符串转为Date
     * 
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static Date parse(String strDate) throws ParseException {
        return parse(strDate, DEFAULTDATEPATTERN);
    }


    /**
     * 获取传入月份的最大天数
     * 
     * @param date
     * @return
     */
    public static Date getMaxDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }


    /**
     * 获取两个日期的间隔天数
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDaysBetween(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (toCalendar.getTime().getTime() - fromCalendar.getTime()
                .getTime()) / (1000 * 60 * 60 * 24);
    }


    /**
     * 获取月的第一天
     * 
     * @return
     */
    public static String getMonthFirstDay(String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return new SimpleDateFormat(pattern).format(cal.getTime());
    }


    /**
     * 获取月的最后一天
     * 
     * @return
     */
    public static String getMonthLastDay(String pattern) {
        return new SimpleDateFormat(pattern)
                .format(getMaxDateOfMonth(new Date()));
    }


    public static boolean isSameDay(String dateStr1, String dateStr2,
        String pattern) throws ParseException {
        return isSameDay(parse(dateStr1, pattern), parse(dateStr2, pattern));
    }


    public static boolean isBetweenTwoHours(Date currDate, int beginHour,
        int endHour) {
        Calendar curr = Calendar.getInstance();
        curr.setTime(currDate);
        int currHour = curr.get(Calendar.HOUR_OF_DAY);
        int period1 =
                currHour - beginHour < 0 ? (currHour - beginHour + 24)
                        : (currHour - beginHour);
        int period2 =
                endHour - beginHour < 0 ? (endHour - beginHour + 24)
                        : (endHour - beginHour);

        if (period1 <= period2) {
            return true;
        }
        return false;
    }


    /**
     * 根据日期计算年龄
     * 
     * @Description:
     * @param date1
     *        大日期(入院日期)
     * @param date2
     *        小日期(出生日期)
     * @return 正常返回正整数，日期为空时=-1
     * @author yuxiangtong
     * @date 2015-7-24 下午3:57:38
     */
    public static int getAgeByTwoDate(Date date1, Date date2) {
        int age = -1;
        if (null != date1 && null != date2) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

            cal.setTime(date2);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

            age = yearNow - yearBirth;

            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        age--;
                    }
                }
                else {
                    age--;
                }
            }
        }
        return age;
    }


    /**
     * 根据日期计算时间间隔
     * 
     * @param date1
     *        小日期(出生日期)
     * @param date2
     *        大日期(入院日期)
     * @return <br>
     *         正常：返回正整数，日期为空时=-1
     * @author wfm
     */
    public static double getAgeByTwoDateToRetainOneDecimal(Date date1,
        Date date2, String unit) {
        double age = -1;
        if (null != date1 && null != date2) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date2);
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
            int dayOfYearNow = cal.get(Calendar.DAY_OF_YEAR);
            Long millisNow = cal.getTimeInMillis();
            cal.setTime(date1);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            int dayOfYearBirth =
                    (yearNow % 4 == 0 ? 366 : 365)
                            - cal.get(Calendar.DAY_OF_YEAR);
            Long millisBirth = cal.getTimeInMillis();
            // 以年为单位计算年龄
            if (StringUtils.equals("Y", unit)) {
                age = yearNow - yearBirth;

                if (monthNow <= monthBirth) {
                    if (monthNow == monthBirth) {
                        if (dayOfMonthNow < dayOfMonthBirth) {
                            age--;
                        }
                    }
                    else {
                        age--;
                    }
                }
                // 计算年龄的小数位
                // 临床建议修复：1）系统年龄算法不变，但精确到小数点后一位(采用进位法，保留一位小数，如，1.11，保留一位后为：1.2；
                // 当小数点后一位已经为9时，不允许再进位，如：1.99，不可再进位，保留为：1.9；而1.81，可以进位为：1.9。)。
                // 1.9999999999
                double decimal =
                        MathUtils.div(dayOfYearNow + dayOfYearBirth, 365);
                if ((decimal + "").indexOf(".9") > -1)
                    decimal = 0.9;
                else
                    decimal = MathUtils.round(decimal - (int) decimal, 1);
                age = age + decimal;
                // 以月为单位计算年龄
            }
            else if (StringUtils.equals("M", unit)) {
                int year = yearNow - yearBirth;
                int mouth = monthNow - monthBirth;
                int day = dayOfMonthNow - dayOfMonthBirth;
                if (year > 0)
                    age = year * 12;
                if (mouth > 0) {
                    age = age + mouth;
                    if (day < 0)
                        age = age - 1;
                }
                else {
                    age = age - 1;
                }
                // 以日为单位计算年龄
            }
            else if (StringUtils.equals("D", unit)) {
                // age = dayOfYearNow + dayOfYearBirth;
                age = (millisNow - millisBirth) / (1000 * 60 * 60 * 24);
            }
        }
        return age;
    }


    public static double getAgeByTwoDateToRetainOneDecimal(Date date1,
        Date date2) {
        return getAgeByTwoDateToRetainOneDecimal(date1, date2, "Y");
    }


    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
//        return;
    	Date date1 = new Date();
    	try {
			Date date2 = DateUtils.parse("2014-9-18 02:20:00");
			System.out.println(getDaysBetween(date1, date2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }


    /**
     * 判断日期1是否在日期2、日期3之间
     * 
     * @param date1
     * @param date2
     *        区间小日期
     * @param date3
     *        区间大日期
     * @param
     * @return <br>
     *         boolean
     * @author wfm
     */
    public static boolean isBetween(Date date1, Date date2, Date date3) {
        if (date1 == null || date2 == null || date3 == null)
            return false;
        Long long1 = date1.getTime();
        Long long2 = date2.getTime();
        Long long3 = date3.getTime();
        if (long1 <= long2 && long2 <= long3)
            return true;
        return false;
    }
    
    
    
    /**
     * <p>功能:计算年龄。</p> 
     * <p>说明:应临床要求,类型1:计算年龄后年龄为XX/xxx,代表XX岁xxx天。类型2:具体的天数 </p> 
     * <p>备注:无。 </p>
     * @param date1(大日期,入/出院日期) date2(小日期,出生日期)
     * @return string
     * @author caikang
     */
    public static String getAgeInYearAndDay(Date date1 ,Date date2 ,String type) {
    	if(StringUtils.isEmpty(type)) {
    		return null;
    	}
    	int year = -1;
    	int day = -1;
    	String str = null;
    	if(null != date1 && null != date2){
    		Calendar cal1 = Calendar.getInstance();
    		
    		cal1.setTime(date1);
    		int yearNow = cal1.get(Calendar.YEAR);
    		int monthNow = cal1.get(Calendar.MONTH);
    		int dayNow = cal1.get(Calendar.DAY_OF_MONTH);
    		
    		Calendar cal2 = Calendar.getInstance();

    		cal2.setTime(date2);
    		int yearBirth = cal2.get(Calendar.YEAR);
    		int monthBirth = cal2.get(Calendar.MONTH);
    		int dayBirth = cal2.get(Calendar.DAY_OF_MONTH);
    		
    		//判断年份,年相同的情况
    		if(yearNow <= yearBirth){
    			//判断月份
    			if(monthNow == monthBirth){
    				if (dayNow > dayBirth){
    					year = 0;
    					day = dayNow - dayBirth;
    				}
    			}else if(monthNow > monthBirth){
    				year = 0;
    				day = (int)((date1.getTime() - date2.getTime())/86400000);
    			}
    			if(type.equals("1")) {
    				str = year+"/"+day+"/";
    			}else if(type.equals("2") && day > 0) {
    				str = day + "";
    			}
    		}else{
    			//年不同的情况,先判断月,再判断天
    			if(type.equals("2")) {
    				day = (int)((cal1.getTimeInMillis() - cal2.getTimeInMillis())/86400000);
    				str = day + "";
    			}else if(type.equals("1")) {
	    			year = yearNow - yearBirth;
	    			if(monthNow > monthBirth){
	    				cal1.set(yearBirth, monthNow, dayNow);
	    				day = (int)((cal1.getTimeInMillis() - cal2.getTimeInMillis())/86400000);
	    			}else if(monthNow < monthBirth) {
	    				year = year -1;
	    				cal1.set(yearBirth+1, monthNow, dayNow);
	    				day = (int)((cal1.getTimeInMillis() - cal2.getTimeInMillis())/86400000);
	    			}else if(monthNow == monthBirth){
	    				if(dayNow >= dayBirth){
	    					day = dayNow - dayBirth;
	    				}else if(dayNow < dayBirth){
	    					year = year -1;
	        				cal1.set(yearBirth+1, monthNow, dayNow);
	        				day = (int)((cal1.getTimeInMillis() - cal2.getTimeInMillis())/86400000);
	    				}
	    			}
	    			str = year+"/"+day+"/";
    			}
    		}
    	}
    	//年岁为负数,则返回空
    	if(type.equals("1") && year < 0 || (year == 0 && day <= 0)) {
    		return null;
    	}else if(type.equals("2") && day <= 0) {
    		return null;
    	}
    	return str;
    }
    
    /**
     * <p> MethodName: getDayFromTowDate </p>
     * <p> Description: 获取两个日期中相差的天数（例如：1999-12-12和1999-12-15） </p>
     * <p> Create_By: YangHao </p>
     * <p> Create_Date: 2017年2月10日 下午1:21:37 </p>
     * <p> Modification with annotation or not </p>
     * 
     * @param date1
     * @param date2
     * @return
     * @throws ParseException  
    */
    public static int getDayFromTowDate(Date date1, Date date2){
    	if(date1 != null && date2 != null){
    		SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
    		try {
				date1 = sdf.parse(sdf.format(date1));
				date2 = sdf.parse(sdf.format(date2));
				return ((Number) ((date1.getTime()-date2.getTime()) / (1000f * 60f * 60f * 24f)))
						.intValue() + 1;
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
    	return 0;
    }
    
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */