package cn.com.open.pay.order.service.tools;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 
 */
public abstract class WebUtils {


    //private
    private WebUtils() {
    }


    public static void writeJson(HttpServletResponse response, JSON json) {
        response.setContentType("application/json;charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            json.write(writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException("Write json to response error", e);
        }

    }
    
    public static void writeJson(HttpServletResponse response,String str) {
    	try {
			response.getWriter().write(str);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void writeJsonToMap(HttpServletResponse response,Map<String,Object> map) {
    	try {
			JsonConfig jsonConfig = new JsonConfig();
			// 排除,避免循环引用 There is a cycle in the hierarchy!
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
			jsonConfig.setIgnoreDefaultExcludes(true);
			jsonConfig.setAllowNonStringKeys(true);
			writeJson(response, JSONObject.fromObject(map,jsonConfig));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static boolean nullEmptyBlankJudge(String str){
	    return null==str||str.isEmpty()||"".equals(str.trim());
	}
 
	 public static boolean paraMandatoryCheck(List<String> params){
	    for(String param:params){
	        if(nullEmptyBlankJudge(param)){
	            return false;
	        }
	    }
	    return true;
	}
 
	public static void paraMandaChkAndReturn(int errorNum,HttpServletResponse response,String errMsg){
	    Map<String, Object> map=paraMandaChkAndReturnMap(errorNum, response, errMsg);
	    writeErrorJson(response,map);
	}

	public static Map<String, Object> paraMandaChkAndReturnMap(int errorNum,HttpServletResponse response,String errMsg){
	    Map<String, Object> map=new HashMap<String,Object>();
	    map.clear();
	    map.put("status", "0");
	    map.put("error_code", errorNum);
	    map.put("errMsg", errMsg);
	    return map;
	}

	public static void writeSuccessJson(HttpServletResponse response, Map map){
		writeJson(response, JSONObject.fromObject(map));
	}

	public static void writeErrorJson(HttpServletResponse response, Map map){
		writeJson(response, JSONObject.fromObject(map));
	}
	
	/**
     * 
     * 验证密码为 6～20位,字母、数字或者英文符号，最短6位，区分大小写
     * @param value
     * @return
     */
    public static int judgeInputNotNo(String value){
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
    
	public static Date StrtoDate(String DateString,String format) throws ParseException {
    	SimpleDateFormat sdf=new SimpleDateFormat(format);
    	Date cd=sdf.parse(DateString);
    	return cd;
    }
    
}