package cn.com.open.payservice;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class DateTools {

	// 格式：年－月－日 小时：分钟：秒
    public static final String FORMAT_ONE = "yyyy-MM-dd HH:mm:ss";

    // 格式：年－月－日 小时：分钟
    public static final String FORMAT_TWO = "yyyy-MM-dd HH:mm";

    // 格式：年月日 小时分钟秒
    public static final String FORMAT_THREE = "yyyyMMdd-HHmmss";
      // 格式：年－月－日 小时：分钟：秒
    public static final String FORMAT_FOUR = "yyyyMMddHHmmss";
    // 格式：年－月－日
    public static final String LONG_DATE_FORMAT = "yyyy-MM-dd";

    // 格式：月－日
    public static final String SHORT_DATE_FORMAT = "MM-dd";

    // 格式：小时：分钟：秒
    public static final String LONG_TIME_FORMAT = "HH:mm:ss";

    //格式：年-月
    public static final String MONTG_DATE_FORMAT = "yyyy-MM";
    

    // 年的加减
    public static final int SUB_YEAR = Calendar.YEAR;

    // 月加减
    public static final int SUB_MONTH = Calendar.MONTH;

    // 天的加减
    public static final int SUB_DAY = Calendar.DATE;

    // 小时的加减
    public static final int SUB_HOUR = Calendar.HOUR;

    // 分钟的加减
    public static final int SUB_MINUTE = Calendar.MINUTE;

    // 秒的加减
    public static final int SUB_SECOND = Calendar.SECOND;

    static final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四",
            "星期五", "星期六" };

    @SuppressWarnings("unused")
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public DateTools() {
    }

    /**
     * 把符合日期格式的字符串转换为日期类型
     */
    public static Date stringtoDate(String dateStr, String format) {
        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            formater.setLenient(false);
            d = formater.parse(dateStr);
        } catch (Exception e) {
            // log.error(e);
            d = null;
        }
        return d;
    }

    /**
     * 把符合日期格式的字符串转换为日期类型
     */
    public static Date stringtoDate(String dateStr, String format,
            ParsePosition pos) {
        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            formater.setLenient(false);
            d = formater.parse(dateStr, pos);
        } catch (Exception e) {
            d = null;
        }
        return d;
    }

    /**
     * 把日期转换为字符串
     */
    public static String dateToString(java.util.Date date, String format) {
        String result = "";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            result = formater.format(date);
        } catch (Exception e) {
            // log.error(e);
        }
        return result;
    }

    /**
     * 获取当前时间的指定格式
     */
    public static String getCurrDate(String format) {
        return dateToString(new Date(), format);
    }
    
    /**
     * 获得昨天的指定格式
     * 
     * @return
     */
    public static String getYesterDay(String format) {
    	Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE,-1);
	    return new SimpleDateFormat(format).format(c.getTime());
    }
    
    /**
     * 获得昨天的开始时间
     * 
     * @return
     */
    public static Date getYesterDayStartTime() {
    	Calendar c = getYesterDayInstance();
	    String yesterdayStart = new SimpleDateFormat( "yyyy-MM-dd 00:00:00").format(c.getTime());
        return stringtoDate(yesterdayStart, "yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 获得昨天的结束时间
     * 
     * @return
     */
    public static Date getYesterDayEndTime() {
    	Calendar c = getYesterDayInstance();
	    String yesterdayEnd = new SimpleDateFormat( "yyyy-MM-dd 23:59:59").format(c.getTime());
        return stringtoDate(yesterdayEnd, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期计算返回String
     * @param dateKind 类型（SUB_DAY...）
     * @param dateStr 日期
     * @param amount 增量
     * @return
     */
    public static String dateSub(int dateKind, String dateStr, int amount) {
        Date date = stringtoDate(dateStr, FORMAT_ONE);
        return dateSub(dateKind, date, amount, FORMAT_ONE);
    }
    
    /**
     * 日期计算返回String
     * @param dateKind 类型（SUB_DAY...）
     * @param date 日期
     * @param amount 增量
     * @return
     */
    public static String dateSub(int dateKind, Date date, int amount) {
        return dateSub(dateKind, date, amount, FORMAT_ONE);
    }
    
    /**
     * 日期计算返回String
     * @param dateKind 类型（SUB_DAY...）
     * @param date 日期
     * @param amount 增量
     * @param format 返回格式
     * @return
     */
    public static String dateSub(int dateKind, Date date, int amount, String format) {
        return dateToString(dateSubToDate(dateKind, date, amount), format);
    }
    
    /**
     * 日期计算返回Date
     * @param dateKind
     * @param date 日期
     * @param amount 增量
     * @return
     */
    public static Date dateSubToDate(int dateKind, Date date, int amount) {
    	 Calendar calendar = Calendar.getInstance();
         calendar.setTime(date);
         calendar.add(dateKind, amount);
         return calendar.getTime();
    }
    
    /**
     * 两个日期相减
     * @return 相减得到的秒数
     */
    public static long timeSub(String firstTime, String secTime) {
        long first = stringtoDate(firstTime, FORMAT_ONE).getTime();
        long second = stringtoDate(secTime, FORMAT_ONE).getTime();
        return (second - first) / 1000;
    }
    /**
     * 两个日期相减
     * @return 相减得到的秒数
     */
    public static long timeSub2(String firstTime, String secTime) {
        long first = stringtoDate(firstTime, FORMAT_FOUR).getTime();
        long second = stringtoDate(secTime, FORMAT_FOUR).getTime();
        return (second - first) / 1000;
    }

    /**
     * 获得某月的天数
     */
    public static int getDaysOfMonth(String year, String month) {
        int days = 0;
        if (month.equals("1") || month.equals("3") || month.equals("5")
                || month.equals("7") || month.equals("8") || month.equals("10")
                || month.equals("12")) {
            days = 31;
        } else if (month.equals("4") || month.equals("6") || month.equals("9")
                || month.equals("11")) {
            days = 30;
        } else {
            if ((Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0)
                    || Integer.parseInt(year) % 400 == 0) {
                days = 29;
            } else {
                days = 28;
            }
        }

        return days;
    }

    /**
     * 获取某年某月的天数
     */
    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得当前日期
     */
    public static int getToday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获得当前月份
     */
    public static int getToMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当前年份
     */
    public static int getToYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 返回日期的天
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 返回日期的年
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 返回日期的月份，1-12
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 计算两个日期相差的天数，如果date2 > date1 返回正数，否则返回负数
     */
    public static long dayDiff(Date date1, Date date2) {
        return (date2.getTime() - date1.getTime()) / 86400000;
    }

    /**
     * 比较两个日期的年差
     */
    public static int yearDiff(String before, String after) {
        Date beforeDay = stringtoDate(before, LONG_DATE_FORMAT);
        Date afterDay = stringtoDate(after, LONG_DATE_FORMAT);
        return getYear(afterDay) - getYear(beforeDay);
    }

    /**
     * 比较指定日期与当前日期的差
     */
    public static int yearDiffCurr(String after) {
        Date beforeDay = new Date();
        Date afterDay = stringtoDate(after, LONG_DATE_FORMAT);
        return getYear(beforeDay) - getYear(afterDay);
    }
    
    /**
     * 比较指定日期与当前日期的差long数据类型
     */
    public static long timeDiffCurr(long after) {
        return (new Date().getTime()) - after;
    }
    
    /**
     * 获取每月的第一周
     */
    public static int getFirstWeekdayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
        c.set(year, month - 1, 1);
        return c.get(Calendar.DAY_OF_WEEK);
    }
    /**
     * 获取每月的最后一周
     */
    public static int getLastWeekdayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
        c.set(year, month - 1, getDaysOfMonth(year, month));
        return c.get(Calendar.DAY_OF_WEEK);
    }
    
    /**
     * 获取当周内第一天的日期
     * @param date
     * @return
     */
    public static Date getFirstWeekdayOfWeek(Date date){
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		if(dayOfWeek==0){
			dayOfWeek = 7;
		}
		return dateSubToDate(DateTools.SUB_DAY, date, -(dayOfWeek-1));
    }
    
    public static Date getLastWeekdayOfWeek(Date date){
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		if(dayOfWeek==0){
			dayOfWeek = 7;
		}
		return dateSubToDate(DateTools.SUB_DAY, date, (7-dayOfWeek));
    }

    /**
     * 获得当前日期字符串，格式"yyyy-MM-dd HH:mm:ss"
     * 
     * @return
     */
    public static String getNow() {
        Calendar today = Calendar.getInstance();
        return dateToString(today.getTime(), FORMAT_ONE);
    }

    /**
     * 判断日期是否有效,包括闰年的情况
     * 
     * @param date
     *          YYYY-mm-dd
     * @return
     */
    public static boolean isDate(String date) {
        StringBuffer reg = new StringBuffer(
                "^((\\d{2}(([02468][048])|([13579][26]))-?((((0?");
        reg.append("[13578])|(1[02]))-?((0?[1-9])|([1-2][0-9])|(3[01])))");
        reg.append("|(((0?[469])|(11))-?((0?[1-9])|([1-2][0-9])|(30)))|");
        reg.append("(0?2-?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12");
        reg.append("35679])|([13579][01345789]))-?((((0?[13578])|(1[02]))");
        reg.append("-?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");
        reg.append("-?((0?[1-9])|([1-2][0-9])|(30)))|(0?2-?((0?[");
        reg.append("1-9])|(1[0-9])|(2[0-8]))))))");
        Pattern p = Pattern.compile(reg.toString());
        return p.matcher(date).matches();
    }
    
    /**
     * 把毫秒转化成日期
     * @param dateFormat(日期格式，例如：MM/ dd/yyyy HH:mm:ss)
     * @param millSec(毫秒数)
     * @return
     */
    public static String longToDate(String dateFormat,Long millSec){
	    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	    Date date= new Date(millSec);
	    return sdf.format(date);
    }
    
    /**
     * 比较两Date相差几天、几周、几月、几年
     * @param fDate
     * @param oDate
     * @param type
     * @return
     */
	public static int daysOfTwo(Date fDate, Date oDate, int type) {
		long timeLong = 0;
		int dayNum = 0;
		switch (type) {
			case Calendar.HOUR:
				timeLong = oDate.getTime() - fDate.getTime();
				dayNum = (int) ((timeLong / 1000) / 3600);
				return dayNum;
			case Calendar.DAY_OF_YEAR:
				timeLong = oDate.getTime() - fDate.getTime();
				dayNum = (int) (((timeLong / 1000) / 3600) / 24);
				return dayNum;
			case Calendar.WEEK_OF_YEAR:
				timeLong = oDate.getTime() - fDate.getTime();
				dayNum = (int) (((timeLong / 1000) / 3600) / 24 / 7);
				return dayNum;
			case Calendar.MONTH:
				timeLong = oDate.getTime() - fDate.getTime();
				dayNum = (int) (((timeLong / 1000) / 3600) / 24 / 31);
				return dayNum;
			case Calendar.YEAR:
				timeLong = oDate.getTime() - fDate.getTime();
				dayNum = (int) (((timeLong / 1000) / 3600) / 24 / 360);
				return dayNum;
		}
		return 0;
	}
	/**
	 * 获取UTC时间 格式：YYYY-MM-DDThh:mm:ssZ
	 * @param date
	 * @return
	 */
	public static String getSolrDate(Date date) {  
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");  
		//sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));  
		String result = sdf1.format(date) + "T" + sdf2.format(date) + "Z";  
		return result; 
    }
	public static String utc2Local(String utcTime, String utcTimePatten,
			   String localTimePatten) {
			  SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
			  utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
			  Date gpsUTCDate = null;
			  try {
			   gpsUTCDate = utcFormater.parse(utcTime);
			  } catch (Exception e) {
			   e.printStackTrace();
			  }
			  SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
			  localFormater.setTimeZone(TimeZone.getDefault());
			  String localTime = localFormater.format(gpsUTCDate.getTime());
			  return localTime;
	}
	private static Calendar getYesterDayInstance(){
		Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE,-1);
	    return c;
	}
	/**
	 * long 转date  
	 * @param dateFormat
	 * @param millSec
	 * @return
	 */
    private String transferLongToDate(String dateFormat,Long millSec){
	    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	    Date date= new Date(millSec);
	    return sdf.format(date);
    }
	
}
