package cn.com.open.pay.order.service.infrastructure.repository;

import java.util.Map;

public class DataFileSqlProvider {
	public String getInsertDataByCSVFileSql(Map<String,Object> param){  
		
	    String filepath ="";
	    if(!nullEmptyBlankJudge(param.get("filepath").toString())){
	    	filepath= param.get("filepath").toString(); 
	    }
	    String tableName ="";
         if(!nullEmptyBlankJudge(param.get("tableName").toString())){
        	 tableName=param.get("tableName").toString();
	    }
	    String sql = "LOAD DATA LOCAL INFILE '" + filepath  
	            + "' INTO TABLE " + tableName + " "  
	            + " FIELDS TERMINATED BY '&%$'";  
	    return sql;  
	} 
	/**
	 * 检验字符串是否为空
	 * @param str
	 * @return
	 */
	 public static boolean nullEmptyBlankJudge(String str){
	        return null==str||str.isEmpty()||"".equals(str.trim());
	  }
}
