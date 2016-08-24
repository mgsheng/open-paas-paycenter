package cn.com.open.user.platform.manager.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类
 * 
 * @see
 */
public class ValidateUtil {
	/***************************************************************************
	 * 验证是否为空字符串 去空格 true:是空字符串,false：不是空字符串
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNullString(Object str) {
		if (str != null && str.toString().trim().length() > 0) {
			return false;
		}
		return true;
	}
	/**
	 * 验证日期格式是否正确
	 * @param date
	 * @return
	 */
	public static boolean checkDate(String date) {
		String eL = "^((//d{2}(([02468][048])|([13579][26]))[//-/////s]?((((0?[13578])|(1[02]))[//-/////s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[//-/////s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[//-/////s]?((0?[1-9])|([1-2][0-9])))))|(//d{2}(([02468][1235679])|([13579][01345789]))[//-/////s]?((((0?[13578])|(1[02]))[//-/////s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[//-/////s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[//-/////s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(//s(((0?[0-9])|([1][0-9])|([2][0-3]))//:([0-5]?[0-9])((//s)|(//:([0-5]?[0-9])))))?$";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(date);
		return m.matches();
	}
}
