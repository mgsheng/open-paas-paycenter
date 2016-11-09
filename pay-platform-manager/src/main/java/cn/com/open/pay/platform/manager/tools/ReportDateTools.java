package cn.com.open.pay.platform.manager.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ReportDateTools {
	//获取一个时间段内的自然周集合
	public static List<String> getWeek(String startDate,String endDate) throws ParseException{
		List<String> list=new ArrayList<String>();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
	    Date d1 = sdf.parse(startDate);//定义起始日期
	    Date d2 = sdf.parse(endDate);//定义结束日期
	    Calendar dd = Calendar.getInstance();//定义日期实例
	    Calendar   calendar   =   new   GregorianCalendar();
	    calendar.setTime(d2); 
	    calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
	    d2=calendar.getTime();  
	    dd.setTime(d1);//设置日期起始时间
		String startend="";
	    while(dd.getTime().before(d2)){//判断是否到结束日期
		    Calendar cal = Calendar.getInstance();//定义日期实例
		    cal.setTime(dd.getTime());
		    // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
		    int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
		    if (1 == dayWeek) {  
		       cal.add(Calendar.DAY_OF_MONTH, -1);  
		    }  
		    cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一   
		    int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天 
		    cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
	     	startDate=sdf.format(cal.getTime());
		    cal.add(Calendar.DATE, 6);  
		    endDate=sdf.format(cal.getTime());
		    String ss=startDate+"~"+endDate;
		    if(startend.equals(ss)){
		    	dd.add(Calendar.DATE, 1);
		    }else{
		    	startend=startDate+"~"+endDate;
		    	list.add(startend);
		    	dd.add(Calendar.DATE, 1);
		    }		    
	    }
		return list;
	}

	//获取一个时间段内的月集合
	public static List<String> getMonth(String startDate,String endDate) throws ParseException{
		List<String> list=new ArrayList<String>();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
	    Date d1 = sdf.parse(startDate);//定义起始日期
	    Date d2 = sdf.parse(endDate);//定义结束日期
	    Calendar dd = Calendar.getInstance();//定义日期实例
	    Calendar   calendar   =   new   GregorianCalendar();
	    calendar.setTime(d2); 
	    calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
	    d2=calendar.getTime();  
	    dd.setTime(d1);//设置日期起始时间
		String startend="";
	    while(dd.getTime().before(d2)){//判断是否到结束日期
		    Calendar cal = Calendar.getInstance();//定义日期实例
		    cal.setTime(dd.getTime());
		    //获取当前月第一天：    
		    cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		    startDate = sdf.format(cal.getTime());
		    //获取当前月最后一天
		    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
		    endDate = sdf.format(cal.getTime());
		    System.out.println("该日期所在月开始结束日期-------StartDate:"+startDate+"  endDate:"+endDate);
		    startend=startDate+"~"+endDate;
		    list.add(startend);
		    dd.add(Calendar.MONTH, 1);
	    }
		return list;
	}

	//获取一个时间段内的年集合
	public static List<String> getYear(String startDate,String endDate) throws ParseException{
		List<String> list=new ArrayList<String>();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
	    Date d1 = sdf.parse(startDate);//定义起始日期
	    Date d2 = sdf.parse(endDate);//定义结束日期
	    Calendar dd = Calendar.getInstance();//定义日期实例
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(d2); 
	    calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
	    d2=calendar.getTime();  
	    dd.setTime(d1);//设置日期起始时间
		String startend="";
	    while(dd.getTime().before(d2)){//判断是否到结束日期
		    Calendar cal = Calendar.getInstance();//定义日期实例
		    cal.setTime(dd.getTime());
		    startDate = cal.get(Calendar.YEAR)+"-01-01";
		    endDate = cal.get(Calendar.YEAR)+"-12-31";
		    System.out.println("该日期所在年开始结束日期-------StartDate:"+startDate+"  endDate:"+endDate);
		    startend=startDate+"~"+endDate;
		    list.add(startend);
		    dd.add(Calendar.YEAR, 1);
	    }
		return list;
	}	
}
