package cn.com.open.pay.order.service.tools;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.open.pay.order.service.web.FileUploadController;

import net.sf.json.JSONObject;

public class BaseControllerUtil {
	
	private static String STATEMENT = "statement";
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
	 public static String getSignCertPath() {
			return  BaseControllerUtil.class.getClassLoader().getResource("").getPath()+ File.separator+ STATEMENT ;
		}
	 
	/**
	 * 
	 * 检验参数是否为空
	 * @param params
	 * @return
	 */
	public boolean paraMandatoryCheck(List<String> params){
        for(String param:params){
            if(nullEmptyBlankJudge(param)){
                return false;
            }
        }
        return true;
    }
	/**
	 * 检验字符串是否为空
	 * @param str
	 * @return
	 */
	 public static boolean nullEmptyBlankJudge(String str){
	        return null==str||str.isEmpty()||"".equals(str.trim());
	  }
	  /**
	     * 
	     * 验证密码为 6～20位,字母、数字或者英文符号，最短6位，区分大小写
	     * @param value
	     * @return
	     */
	    public int judgeInputNotNo(String value){
	    	int returnValue=0;
	    	if(value.length()>20||value.length()<6){
	    		returnValue=1;
	    		return returnValue;
	    	}else{
	    	//Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z0-9]{5,20}"); 
	    		Pattern p = Pattern.compile("[0-9A-Za-z_]*");
	    	//Pattern p = Pattern.compile("^[a-zA-Z]/w{5,17}$");
	    	Matcher m = p.matcher(value);
	    	boolean chinaKey = m.matches();
	    	if(chinaKey){
	    		returnValue=0;
	    	} else{
	    		returnValue=1;
	    		return returnValue;
	    	 }
	    	}
	    	return  returnValue;
	    }
	    /**
	     * 
	     * 验证密码为 6～20位
	     * @param value
	     * @return
	     */
	    public int judgePwdNo(String value){
	    	int returnValue=0;
	    	if(value.length()>20||value.length()<6){
	    		returnValue=1;
	    		return returnValue;
	    	}
	    	return  returnValue;
	    }
		/**
		 *验证密码不能为纯数字
		 * @param str
		 * @return
		 */
		public static boolean isNumeric(String str){ 
			Pattern pattern = Pattern.compile("[0-9]*"); 
			Matcher isNum = pattern.matcher(str);
			if(!isNum.matches()){
				return false; 
			} 
			return true; 
		}
		/**
	     * 验证用户名必须为英文、数字、下划线的组合
	     * @param value
	     * @return
	     */
	    public  int  judgeInput(String value){
	    	int returnValue=0;
	    	if(value.length()>20||value.length()<5){
	    		returnValue=1;
	    		return returnValue;
	    	}else{
	    	Pattern p = Pattern.compile("[0-9A-Za-z_]*");
	    	//Pattern p = Pattern.compile("^[a-zA-Z]/w{5,17}$");
	    	Matcher m = p.matcher(value);
	    	boolean chinaKey = m.matches();
	    	if(chinaKey){
	    		returnValue=0;
	    	} else{
	    		returnValue=2;
	    		return returnValue;
	    	}
	    	Pattern pattern1 = Pattern.compile("[0-9]*"); 
			Matcher isNum = pattern1.matcher(value);
			if(!isNum.matches()){
				 returnValue=0 ; 
			} else{
				returnValue=3; 
				return  returnValue;
			}
	    	}
	    	return  returnValue;
	    	
	    }
	    
	    /**
	     * 返回成功json
	     * @param response
	     * @param obj 数据集
	     */
	    public void writeSuccessJson(HttpServletResponse response, Map map){
	    	WebUtils.writeJson(response, JSONObject.fromObject(map));
	    }
	    
	    /**
	     * 返回失败json
	     * @param response
	     * @param error_code 错误码
	     */
	    public void writeErrorJson(HttpServletResponse response, Map map){
	    	WebUtils.writeJson(response, JSONObject.fromObject(map));
	    }
	    /**
	     * 向指定URL发送GET方法的请求
	     * 
	     * @param url
	     *            发送请求的URL
	     * @param param
	     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	     * @return URL 所代表远程资源的响应结果
	     */
	    public static String sendGet(String url, String param) {
	        String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url + "?" + param;
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	               result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return result;
	    }
	 
	
	 /**
	  * 判断字符串是否存在于字符串数组中
	  * @param stringArray
	  * @param source
	  * @return
	  */
	public static boolean contains(String[] stringArray, String source) {
	    	  // 转换为list
	    	  List<String> tempList = Arrays.asList(stringArray);
	    	  // 利用list的包含方法,进行判断
	    	  if(tempList.contains(source))
	    	  {
	    	   return true;
	    	  } else {
	    	   return false;
	    	  }
	  }     
	   
	 /**
	  * 根据错误码以及错误讯息向前端返回信息
	  * @param errorNum
	  * @param response
	  * @param errMsg
	  */
    public void paraMandaChkAndReturn(int errorNum,HttpServletResponse response,String errMsg){
        Map<String, Object> map=paraMandaChkAndReturnMap(errorNum, response, errMsg);
        writeErrorJson(response,map);
    }
    
    public Map<String, Object> paraMandaChkAndReturnMap(int errorNum,HttpServletResponse response,String errMsg){
        Map<String, Object> map=new HashMap<String,Object>();
        map.clear();
        map.put("state", "error");
        map.put("errorCode", errorNum);
        map.put("errMsg", errMsg);
        return map;
    }
   /**
	  * 向前端返回信息
	  * @param errorNum
	  * @param response
	  * @param errMsg
	  */
 public void writeMsgToClient(String state ,int stateNum,HttpServletResponse response,String searchMsg){
     Map<String, Object> map=writeMsgToClientMap(state,stateNum, response, searchMsg);
     writeErrorJson(response,map);
 }
 
 public Map<String, Object> writeMsgToClientMap(String state,int stateNum,HttpServletResponse response,String searchMsg){
     Map<String, Object> map=new HashMap<String,Object>();
     map.clear();
     map.put("state", state);
     map.put("stateCode", stateNum);
     map.put("searchMsg", searchMsg);
     return map;
 }
    
    /**
     * 
     * 验证密码强度
     * @param value
     * @return
     */
    public int verifyPassWordStrength(String value){
    	int returnValue=0;
    	if(verifyPassWordString(value)==0){
    		returnValue=1;
    		return returnValue;
    	}if(verifyPassWordSAI(value)==0){
    		returnValue=2;
    		return returnValue;
    	}if(verifyPassWordSAT(value)==0){
    		returnValue=3;
    		return returnValue;
    	}
    	return  returnValue;
    }
    
    /**
     * 
     * 验证密码是否为纯字母
     * @param value
     * @return
     */
    public int verifyPassWordString(String value){
    	int returnValue=0;
    
        Pattern p = Pattern.compile("[a-zA-Z]+");
    	Matcher m = p.matcher(value);
    	boolean chinaKey = m.matches();
    	if(chinaKey){
    		returnValue=0;
    	} else{
    		returnValue=1;
    		return returnValue;
    	 }
    	return  returnValue;
    }
    /**
     * 
     * 验证密码是否为数字和字母组合
     * @param value
     * @return ^\w+$
     */
    public int verifyPassWordSAI(String value){
    	int returnValue=0;
    
        Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
    	Matcher m = p.matcher(value);
    	boolean chinaKey = m.matches();
    	if(chinaKey){
    		returnValue=0;
    	} else{
    		returnValue=1;
    		return returnValue;
    	 }
    	return  returnValue;
    }
    /**
     * 
     * 验证密码是否为数字、字符、特殊字符组成切密码大于6位
     * @param value
     * @return^\w+$
     */
    public int verifyPassWordSAT(String value){
    	int returnValue=0;
    
        Pattern p = Pattern.compile("(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,}");
    	Matcher m = p.matcher(value);
    	boolean chinaKey = m.matches();
    	if(chinaKey){
    		returnValue=0;
    	} else{
    		returnValue=1;
    		return returnValue;
    	 }
    	return  returnValue;
    }
    public static Safelevel GetPwdSecurityLevel(String pPasswordStr) {
        Safelevel safelevel = Safelevel.VERY_WEAK;
        if (pPasswordStr == null) {
            return safelevel;
        }
        int grade = 0;
        int index = 0;
        char[] pPsdChars = pPasswordStr.toCharArray();
 
        int numIndex = 0;
        int sLetterIndex = 0;
        int lLetterIndex = 0;
        int symbolIndex = 0;
 
        for (char pPsdChar : pPsdChars) {
            int ascll = pPsdChar;
            /*
             * 数字 48-57 <a href="https://www.baidu.com/s?wd=A-Z&tn=44039180_cpr&fenlei=mv6quAkxTZn0IZRqIHckPjm4nH00T1Y4njwBuHn3nHuWrARYuHfk0AP8IA3qPjfsn1bkrjKxmLKz0ZNzUjdCIZwsrBtEXh9GuA7EQhF9pywdQhPEUiqkIyN1IA-EUBtdP16LP163PWn" target="_blank" class="baidu-highlight">A-Z</a> 65 - 90 <a href="https://www.baidu.com/s?wd=a-z&tn=44039180_cpr&fenlei=mv6quAkxTZn0IZRqIHckPjm4nH00T1Y4njwBuHn3nHuWrARYuHfk0AP8IA3qPjfsn1bkrjKxmLKz0ZNzUjdCIZwsrBtEXh9GuA7EQhF9pywdQhPEUiqkIyN1IA-EUBtdP16LP163PWn" target="_blank" class="baidu-highlight">a-z</a> 97 - 122 !"#$%&'()*+,-./ (ASCII码：33~47)
             * :;<=>?@ (ASCII码：58~64) [\]^_` (ASCII码：91~96) {|}~
             * (ASCII码：123~126)
             */
            if (ascll >= 48 && ascll <= 57) {
                numIndex++;
            } else if (ascll >= 65 && ascll <= 90) {
                lLetterIndex++;
            } else if (ascll >= 97 && ascll <= 122) {
                sLetterIndex++;
            } else if ((ascll >= 33 && ascll <= 47)
                    || (ascll >= 58 && ascll <= 64)
                    || (ascll >= 91 && ascll <= 96)
                    || (ascll >= 123 && ascll <= 126)) {
                symbolIndex++;
            }
        }
        if(isContinuous(pPasswordStr)){
        	index = 25;
        }else{
        /*
         * 一、密码长度: 5 分: 小于等于 4 个字符 10 分: 5 到 7 字符 25 分: 大于等于 8 个字符
         */
        if (pPsdChars.length <= 8) {
            index = 5;
        } else if (pPsdChars.length <= 16) {
            index = 20;
        } else {
            index = 30;
        }
        grade += index;
        /**
         * 
         */
      
        /*
         * 二、字母: 0 分: 没有字母 10 分: 全都是小（大）写字母 20 分: 大小写混合字母
         */
        if (lLetterIndex == 0 && sLetterIndex == 0) {
            index = 0;
        } else if (lLetterIndex != 0 && sLetterIndex != 0) {
            index = 20;
        } else {
            index = 10;
        }
        grade += index;
        
        /*
         * 三、数字: 0 分: 没有数字 10 分: 1 个数字 20 分: 大于 1 个数字
         */
        if (numIndex == 0) {
            index = 0;
        } else {
            index = 20;
        }
        grade += index;
 
        /*
         * 四、符号: 0 分: 没有符号 20 分: 1 个符号 25 分: 大于 1 个符号
         */
        if (symbolIndex == 0) {
            index = 0;
        } else if (symbolIndex == 1) {
            index = 20;
        } else {
            index = 25;
        }
        grade += index;
        /*
         * 五、奖励: 2 分: 字母和数字 3 分: 字母、数字和符号 5 分: 大小写字母、数字和符号
         */
        if (sLetterIndex != 0 && lLetterIndex != 0 && numIndex != 0
                && symbolIndex != 0) {
            index = 5;
        } else if ((sLetterIndex != 0 || lLetterIndex != 0) && numIndex != 0
                && symbolIndex != 0) {
            index = 3;
        }else if ((sLetterIndex != 0 || lLetterIndex != 0) && numIndex != 0) {
            index = 2;
        } else{
        	index=0;
        }
        grade += index;
        }

        /*
         * 最后的评分标准: >= 90: 非常安全 >= 80: 安全（Secure） >= 70: 非常强 >= 60: 强（Strong） >=
         * 50: 一般（Average） >= 25: 弱（Weak） >= 0: 非常弱
         */    
        if(grade >=90){
            safelevel = Safelevel.VERY_SECURE;
        }else if(grade >= 70){
            safelevel = Safelevel.VERY_STRONG;
        }else if(grade >= 50){
            safelevel = Safelevel.STRONG;
        }else if(grade >= 10){
            safelevel = Safelevel.WEAK; 
        }
        return safelevel;
    }
     
    public enum Safelevel {
        VERY_WEAK, /* 非常弱 */
        WEAK, /* 弱 */
        AVERAGE, /* 一般 */
        STRONG, /* 强 */
        VERY_STRONG, /* 非常强 */
        SECURE, /* 安全 */
        VERY_SECURE /* 非常安全 */
    }
    public static Boolean isContinuous(String str){
        Boolean allEquals = true;  
        String element=str.substring(0,1);
        for ( int i = 0; i < str.length(); i++) {  
            if (str.substring(i,i+1) != element) {  
                allEquals = false;  
                break;  
            }  
            element = str.substring(i,i+1);  
        }  
    	return allEquals;
    }
    /**
     * IP地址黑白名单
     * @param request
     * @return
     */
    public String getRemortIP(HttpServletRequest request) { 
   	    if (request.getHeader("x-forwarded-for") == null) { 
   	        return request.getRemoteAddr(); 
   	    } 
   	    return request.getHeader("x-forwarded-for"); 
   	}  
    public String getIpAddr(HttpServletRequest request) { 
   	    String ip = request.getHeader("x-forwarded-for"); 
   	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
   	        ip = request.getHeader("Proxy-Client-IP"); 
   	    } 
   	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
   	        ip = request.getHeader("WL-Proxy-Client-IP"); 
   	    } 
   	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
   	        ip = request.getRemoteAddr(); 
   	    } 
   	    return ip; 
   	}   
    
	/** 
     * 发送POST请求 
     *  
     * @param url 
     *            目的地址 
     * @param parameters 
     *            请求参数，Map类型。 
     * @return 远程响应结果 
     */  
    public static String sendPost(String url, SortedMap<Object, Object> params2) {  
        String result = "";// 返回的结果  
        BufferedReader in = null;// 读取响应输入流  
        PrintWriter out = null;  
        StringBuffer sb = new StringBuffer();// 处理请求参数  
        String params = "";// 编码之后的参数  
        SortedMap<Object, Object> parameters = params2;
        try {  
            // 编码请求参数  
            if (parameters.size() == 1) {  
                for (Object name : parameters.keySet()) {  
                    sb.append(name).append("=").append(  
                            java.net.URLEncoder.encode((String) parameters.get(name),  
                                    "UTF-8"));  
                }  
                params = sb.toString();  
            } else {  
                for (Object name : parameters.keySet()) {  
                    sb.append(name).append("=").append(  
                           parameters.get(name)).append("&");  
                }  
                String temp_params = sb.toString();  
                params = temp_params.substring(0, temp_params.length() - 1);  
            }  
            // 创建URL对象  
            java.net.URL connURL = new java.net.URL(url);  
            // 打开URL连接  
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL  
                    .openConnection();  
            // 设置通用属性  
            httpConn.setRequestProperty("Accept", "*/*");  
            httpConn.setRequestProperty("Connection", "Keep-Alive");  
            httpConn.setRequestProperty("User-Agent",  
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");  
            // 设置POST方式  
            httpConn.setDoInput(true);  
            httpConn.setDoOutput(true);  
            // 获取HttpURLConnection对象对应的输出流  
            out = new PrintWriter(httpConn.getOutputStream());  
            // 发送请求参数  
            //out.write(params); 
            out.write(params);  
            // flush输出流的缓冲  
            out.flush();  
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式  
            in = new BufferedReader(new InputStreamReader(httpConn  
                    .getInputStream(), "UTF-8"));  
            String line;  
            // 读取返回的内容  
            while ((line = in.readLine()) != null) {  
                result += line;  
                
            }  
            System.out.println(result);
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return result;  
    }  	  
    /** 
     * 发送POST请求 
     *  
     * @param url 
     *            目的地址 
     * @param parameters 
     *            请求参数，Map类型。 
     * @return 远程响应结果 
     */  
    public static String sendPost1(String url, Map<String, String> params2) {  
        String result = "";// 返回的结果  
        BufferedReader in = null;// 读取响应输入流  
        PrintWriter out = null;  
        StringBuffer sb = new StringBuffer();// 处理请求参数  
        String params = "";// 编码之后的参数  
        Map<String, String> parameters = params2;
        try {  
            // 编码请求参数  
            if (parameters.size() == 1) {  
                for (Object name : parameters.keySet()) {  
                    sb.append(name).append("=").append(  
                            java.net.URLEncoder.encode((String) parameters.get(name),  
                                    "UTF-8"));  
                }  
                params = sb.toString();  
            } else {  
                for (Object name : parameters.keySet()) {  
                    sb.append(name).append("=").append(  
                           parameters.get(name)).append("&");  
                }  
                String temp_params = sb.toString();  
                params = temp_params.substring(0, temp_params.length() - 1);  
            }  
            // 创建URL对象  
            java.net.URL connURL = new java.net.URL(url);  
            // 打开URL连接  
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL  
                    .openConnection();  
            // 设置通用属性  
            httpConn.setRequestProperty("Accept", "*/*");  
            httpConn.setRequestProperty("Connection", "Keep-Alive");  
            httpConn.setRequestProperty("User-Agent",  
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");  
            // 设置POST方式  
            httpConn.setDoInput(true);  
            httpConn.setDoOutput(true);  
            // 获取HttpURLConnection对象对应的输出流  
            out = new PrintWriter(httpConn.getOutputStream());  
            // 发送请求参数  
            //out.write(params); 
            out.write(params);  
            // flush输出流的缓冲  
            out.flush();  
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式  
            in = new BufferedReader(new InputStreamReader(httpConn  
                    .getInputStream(), "UTF-8"));  
            String line;  
            // 读取返回的内容  
            while ((line = in.readLine()) != null) {  
                result += line;  
                
            }  
            System.out.println(result);
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return result;  
    } 
    /**
		 * 生成加密串
		 * @param characterEncoding
		 * @param parameters
		 * @return
		 */
		public static String createSign(SortedMap<Object,Object> parameters){
			StringBuffer sb = new StringBuffer();
			Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
			Iterator it = es.iterator();
			while(it.hasNext()) {
				Map.Entry entry = (Map.Entry)it.next();
				String k = (String)entry.getKey();
				Object v = entry.getValue();
				if(null != v && !"".equals(v)&& !"null".equals(v) 
						&& !"sign".equals(k) && !"key".equals(k)) {
					sb.append(k + "=" + v + "&");
				}
			}
			 String temp_params = sb.toString();  
			return sb.toString().substring(0, temp_params.length()-1);
		}
		public static Map<String, String> getPartner(String other){
			if(other==null&&"".equals(other)){
				return null;
			}else{
			String others []=other.split("#");
			Map<String, String> sParaTemp = new HashMap<String, String>();
			for (int i=0;i<others.length;i++){
				String values []=others[i].split(":");
				   sParaTemp.put(values[0], values[1]);  
			}
			
			return sParaTemp;
			}
		}
		public static Boolean analysisValue(JSONObject obj ){
		    	String state = obj.getString("status");
				if(!state.equals("1")){
					return false;
				}else{
					return true;
				}
		    }
		 /** 
	     * 发送POST请求 
	     *  
	     * @param url 
	     *            目的地址 
	     * @param parameters 
	     *            请求参数，Map类型。 
	     * @return 远程响应结果 
	     */  
	    public static String sendOrderPost(String url, SortedMap<Object,Object> sParaTemp) {  
	        String result = "";// 返回的结果  
	        BufferedReader in = null;// 读取响应输入流  
	        PrintWriter out = null;  
	        StringBuffer sb = new StringBuffer();// 处理请求参数  
	        String params="";
	        try {  
			Set es = sParaTemp.entrySet();//所有参与传参的参数按照accsii排序（升序）
			Iterator it = es.iterator();
			while(it.hasNext()) {
				Map.Entry entry = (Map.Entry)it.next();
				String k = (String)entry.getKey();
				Object v = entry.getValue();
				if(null != v && !"".equals(v) 
						&& !"sign".equals(k) && !"key".equals(k)) {
					String value=URLEncoder.encode((String)v, "UTF-8");
					sb.append(k + "=" + value + "&");
				}
			  }
			   String temp_params = sb.toString(); 
			   params = temp_params.substring(0, temp_params.length() - 1);  
	            // 创建URL对象  
	            java.net.URL connURL = new java.net.URL(url);  
	            // 打开URL连接  
	            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL  
	                    .openConnection();  
	            // 设置通用属性  
	            httpConn.setRequestProperty("Accept", "*/*");  
	            httpConn.setRequestProperty("Connection", "Keep-Alive");  
	            httpConn.setRequestProperty("User-Agent",  
	                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");  
	            // 设置POST方式  
	            httpConn.setDoInput(true);  
	            httpConn.setDoOutput(true);  
	            // 获取HttpURLConnection对象对应的输出流  
	            out = new PrintWriter(httpConn.getOutputStream());  
	            // 发送请求参数  
	            //out.write(params); 
	            out.write(params);  
	            // flush输出流的缓冲  
	            out.flush();  
	            // 定义BufferedReader输入流来读取URL的响应，设置编码方式  
	            in = new BufferedReader(new InputStreamReader(httpConn  
	                    .getInputStream(), "UTF-8"));  
	            String line;  
	            // 读取返回的内容  
	            while ((line = in.readLine()) != null) {  
	                result += line;  
	                
	            }  
	            System.out.println(result);
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            return null;
	        } finally {  
	            try {  
	                if (out != null) {  
	                    out.close();  
	                }  
	                if (in != null) {  
	                    in.close();  
	                }  
	            } catch (IOException ex) {  
	                ex.printStackTrace();  
	            }  
	        }  
	        return result;  
	    }  	

		

}
