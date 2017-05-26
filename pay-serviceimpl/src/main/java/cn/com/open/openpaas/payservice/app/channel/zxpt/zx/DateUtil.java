package cn.com.open.openpaas.payservice.app.channel.zxpt.zx;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class DateUtil {

	public static final String DATE_FORMATE_1="yyyy-MM-dd";
	
	public static final String DATE_FORMATE_2="yyyyMMdd";
	
	public static final String DATE_FORMATE_3="yyyyMMddHH24mmss";
	
	public static final String DATE_FORMATE_4="yyyy.MM.dd";
	
	private DateUtil() {
	};

	
	public static String getCurrentDate() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	
	public static String getCurrentDate(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	
	public static String getCurrentTime() {
		return new SimpleDateFormat("HHmmss").format(new Date());
	}

	
	public static String getCurrentDateTime() {
		return new SimpleDateFormat("yyyyMMddHH24mmss").format(new Date());
	}

	
	public static String getDateTime(Date date) {
		return new SimpleDateFormat("yyyyMMddHH24mmss").format(date);
	}

	
	public static long getCurrentLongTime() {
		return new Date().getTime();
	}

	
	public static String getCurrentPrettyDateTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}


	public static String getDate(Date date) {
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}

	public static String getTime(Date date) {
		return new SimpleDateFormat("HH24mmss").format(date);
	}


	public static String getPrettyDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}


	public static String getPrettyDateTime(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}


	public static String getCurrentChnDate() {
		String strDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		strDate = strDate.substring(0, 4) + "年" + strDate.substring(4, 6) + "月" + strDate.substring(6) + "日";
		return strDate;
	}

	
	public static String getChnDate(Date date) {
		String strDate = new SimpleDateFormat("yyyyMMdd").format(date);
		strDate = strDate.substring(0, 4) + "年" + strDate.substring(4, 6) + "月" + strDate.substring(6) + "日";
		return strDate;
	}


	public static String getChnDate(String strDate) {
		return strDate.substring(0, 4) + "年" + strDate.substring(4, 6) + "月" + strDate.substring(6) + "日";
	}


	public static boolean isValidDate(String dateStr, String pattern) {
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(pattern);
		//来强调严格遵守该格式
		//df.setLenient(false);
		df.setLenient(true);
		try {
			df.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static int getMaxdayInAMonth(int year, int month) {
		Calendar time = Calendar.getInstance();
		time.clear();
		time.set(Calendar.YEAR, year); // year 为 int
		time.set(Calendar.MONTH, month - 1);// 注意,Calendar对象默认一月为0
		int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
		// 注：在使用set方法之前，必须先clear一下，否则很多信息会继承自系统当前时间
		return day;

	}

	// Calendar转化为Date
	public static Date CalendarToDate() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		return date;
	}

	// Date转化为Calendar
	public static Calendar DateToCalendar() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}


	public static Date addDays(Date date, int num) {
		if (date == null) {
			return null;
		}

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, num);

		return c.getTime();
	}


	public static Date addMonths(Date date, int num) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, num);
		return c.getTime();
	}


	public static Date addYears(Date date, int num) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, num);
		return c.getTime();
	}


	public static Date addSeconds(Date date, int num) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, num);
		return c.getTime();
	}

	public static Date getDate(String dateStr, String dateFormat) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		java.util.Date cDate = df.parse(dateStr);

		return cDate;
	}

	public static Date getDate(String dateStr) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		java.util.Date cDate = df.parse(dateStr);

		return cDate;
	}

	public static Date convertTimerToDate(Timestamp time) throws ParseException {
		Date dd = new Date(time.getTime());
		return dd;
	}

	public static long DateDiff(Date date1, Date date2) throws ParseException {
		return date1.getTime() - date2.getTime();
	}


	public static String getAfterMonthNowDate(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
		calendar.add(Calendar.MONTH, -1); //
		Date theDate = calendar.getTime();
		String s = df.format(theDate);
		return s;
	}

	public static boolean isDateBefore(Date Date1, String Date2) {
		try {
			DateFormat df = DateFormat.getDateTimeInstance();
			return Date1.before(df.parse(Date2));
		} catch (ParseException e) {
			return false;
		}
	}
}
