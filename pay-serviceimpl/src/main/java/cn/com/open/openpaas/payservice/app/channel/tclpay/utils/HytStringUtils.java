package cn.com.open.openpaas.payservice.app.channel.tclpay.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

/**
 * 名称：Hyt字符串工具换
 * 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */

public class HytStringUtils {

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	public static boolean isBlank(String str)
	  {
	    int strLen;
	    if ((str == null) || ((strLen = str.length()) == 0)) {
	      return true;
	    }
	    for (int i = 0; i < strLen; i++) {
	      if (!Character.isWhitespace(str.charAt(i))) {
	        return false;
	      }
	    }
	    return true;
	  }
	public static boolean equals(String str1, String str2)
	  {
	    return str1 == null ? false : str2 == null ? true : str1.equals(str2);
	  }
	
	public static String[] split(String str, String separatorChars)
	  {
	    return splitWorker(str, separatorChars, -1, false);
	  }
	private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens)
	  {
	    if (str == null) {
	      return null;
	    }
	    int len = str.length();
	    if (len == 0) {
	      return ArrayUtils.EMPTY_STRING_ARRAY;
	    }
	    List list = new ArrayList();
	    int sizePlus1 = 1;
	    int i = 0; int start = 0;
	    boolean match = false;
	    boolean lastMatch = false;
	    if (separatorChars == null)
	    {
	      while (i < len)
	        if (Character.isWhitespace(str.charAt(i))) {
	          if ((match) || (preserveAllTokens)) {
	            lastMatch = true;
	            if (sizePlus1++ == max) {
	              i = len;
	              lastMatch = false;
	            }
	            list.add(str.substring(start, i));
	            match = false;
	          }
	          i++; start = i;
	        }
	        else {
	          lastMatch = false;

	          match = true;
	          i++;
	        }
	    } else if (separatorChars.length() == 1)
	    {
	      char sep = separatorChars.charAt(0);
	      while (i < len)
	        if (str.charAt(i) == sep) {
	          if ((match) || (preserveAllTokens)) {
	            lastMatch = true;
	            if (sizePlus1++ == max) {
	              i = len;
	              lastMatch = false;
	            }
	            list.add(str.substring(start, i));
	            match = false;
	          }
	          i++; start = i;
	        }
	        else {
	          lastMatch = false;

	          match = true;
	          i++;
	        }
	    }
	    else {
	      do
	        if (separatorChars.indexOf(str.charAt(i)) >= 0) {
	          if ((match) || (preserveAllTokens)) {
	            lastMatch = true;
	            if (sizePlus1++ == max) {
	              i = len;
	              lastMatch = false;
	            }
	            list.add(str.substring(start, i));
	            match = false;
	          }
	          i++; start = i;
	        }
	        else {
	          lastMatch = false;

	          match = true;
	          i++;
	        }
	      while (i < len);
	    }

	    if ((match) || ((preserveAllTokens) && (lastMatch))) {
	      list.add(str.substring(start, i));
	    }
	    return (String[])list.toArray(new String[list.size()]);
	  }

	public static int indexOf(String str, String searchStr)
	  {
	    if ((str == null) || (searchStr == null)) {
	      return -1;
	    }
	    return str.indexOf(searchStr);
	  }
	public static int indexOf(String str, char searchChar)
	  {
	    if (isEmpty(str)) {
	      return -1;
	    }
	    return str.indexOf(searchChar);
	  }
	public static String substring(String str, int start, int end)
	  {
	    if (str == null) {
	      return null;
	    }

	    if (end < 0) {
	      end = str.length() + end;
	    }
	    if (start < 0) {
	      start = str.length() + start;
	    }

	    if (end > str.length()) {
	      end = str.length();
	    }

	    if (start > end) {
	      return "";
	    }

	    if (start < 0) {
	      start = 0;
	    }
	    if (end < 0) {
	      end = 0;
	    }

	    return str.substring(start, end);
	  }
	public static String substring(String str, int start)
	  {
	    if (str == null) {
	      return null;
	    }

	    if (start < 0) {
	      start = str.length() + start;
	    }

	    if (start < 0) {
	      start = 0;
	    }
	    if (start > str.length()) {
	      return "";
	    }

	    return str.substring(start);
	  }

}
