package cn.com.open.pay.order.service.web;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.order.service.dev.ChannelCategory;
import cn.com.open.pay.order.service.dev.PayserviceDev;
import cn.com.open.pay.order.service.dev.StatementType;
import cn.com.open.pay.order.service.paymax.model.StatementDownload;
import cn.com.open.pay.order.service.tools.BaseControllerUtil;
import cn.com.open.pay.order.service.tools.DBUtil;
import cn.com.open.pay.order.service.tools.DateTools;


@Controller
@RequestMapping("/paymax/file/upload/")
public class PayMaxFileUploadController  extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(PayMaxFileUploadController.class);
	 @Autowired
	 private PayserviceDev payserviceDev;

	/** 
	 * 拉卡拉对账单下载
	 */
    public void fileUploadStatement() {
    	  Map<String, Object> statementMap = new HashMap<String, Object>();
    	  String yesterday=DateTools.getYesterdayTime("yyyyMMdd");
    	 // String yesterday="20170701";
    	  //奥鹏教育网关支付
    	  statementMap.put("appointDay", yesterday);
          statementMap.put("channelCategory", ChannelCategory.LAKALA.getValue());
          statementMap.put("statementType",StatementType.SUCCESS.getValue());
	      String secert_key=payserviceDev.getSecert_key();
	      String private_key=payserviceDev.getPrivate_key();
	      String paymax_public_key=payserviceDev.getPaymax_public_key();
          String result = "";
          //
          	log.info("~~~~~~~~~~~~~~~~~~~~~~open lakala pay start~~~~~~~~~~~~~~~~~~~~~~~~");
	    	try {
	    		result= StatementDownload.download(statementMap,secert_key,private_key,paymax_public_key);
			} catch (Exception e) {
				// TODO: handle exception
			}
	    	saveOrderStatement(result);
	    	log.info("~~~~~~~~~~~~~~~~~~~~~~open lakala pay end~~~~~~~~~~~~~~~~~~~~~~~~");
	    	//奥鹏教育微信公众号
	    	statementMap.clear();
	        statementMap.put("appointDay", yesterday);
	        statementMap.put("channelCategory", ChannelCategory.WECHAT.getValue());
	        statementMap.put("statementType", StatementType.WECHAT_CSB.getValue());
	        try {
	    		 result = StatementDownload.download(statementMap,secert_key,private_key,paymax_public_key);
			} catch (Exception e) {
				// TODO: handle exception
			}
	        saveWechatCsbStatement(result);
	        log.info("~~~~~~~~~~~~~~~~~~~~~~open weixin pay end~~~~~~~~~~~~~~~~~~~~~~~~");
	        //TCL网关支付
	        secert_key=payserviceDev.getTcl_secert_key();
	        private_key=payserviceDev.getTcl_private_key();
	        paymax_public_key=payserviceDev.getTcl_paymax_public_key();
	        statementMap.clear();
	        statementMap.put("appointDay", yesterday);
	        statementMap.put("channelCategory", ChannelCategory.LAKALA.getValue());
	        statementMap.put("statementType",StatementType.SUCCESS.getValue());
	        try {
	    		 result = StatementDownload.download(statementMap,secert_key,private_key,paymax_public_key);
			} catch (Exception e) {
				// TODO: handle exception
			}
	        saveOrderStatement(result);
	        log.info("~~~~~~~~~~~~~~~~~~~~~~tcl lakala pay end~~~~~~~~~~~~~~~~~~~~~~~~");
	        //TCL微信扫码
	        statementMap.clear();
	        statementMap.put("appointDay", yesterday);
	        statementMap.put("channelCategory", ChannelCategory.WECHAT.getValue());
	        statementMap.put("statementType", StatementType.WECHAT_CSB.getValue());
	        try {
	    		 result = StatementDownload.download(statementMap,secert_key,private_key,paymax_public_key);
			} catch (Exception e) {
				// TODO: handle exception
			}
	        saveWechatCsbStatement(result);
	        log.info("~~~~~~~~~~~~~~~~~~~~~~tcl weixin pay end~~~~~~~~~~~~~~~~~~~~~~~~");
    	
    }
    /**
     * 保存到对账单表中
     */
	 public void saveWechatCsbStatement (String paymaxResult){
		 if(!nullEmptyBlankJudge(paymaxResult)){
         int i = 1;  
         String strLine = null;  
         Statement ps = null;  
         Connection conn = null;  
         String sql = null;  
         try{  
             conn = DBUtil.getConnection();  
             conn.setAutoCommit(false);  
             ps = conn.createStatement(); 
             //解析拉卡拉返回的字符串
             String results[]=paymaxResult.split("\r\n");
             String orderValue="";
          
             System.out.println(paymaxResult);
             while (results!=null&&i<results.length-2) {
            	 orderValue= results[i];
                 if(!nullEmptyBlankJudge(orderValue)){
                 String values[]=orderValue.split(",");
                 String cell0 = values[6];  
                 String cell1 = values[5];  
                 String cell2 = values[9];  
                 String cell4 = values[11];   
                 String cell5 ="";
                 String cell6 ="";
                 String cell7 ="3";
                 if(!nullEmptyBlankJudge(values[13])){
                	 cell5 =DateTools.dateToString(DateTools.stringtoDate(values[13],DateTools.FORMAT_FOUR),DateTools.FORMAT_ONE);
                	 cell6=cell5;
                 }
                 sql = String.format("INSERT INTO PAY_ORDER_STATEMENT(merchant_order_id,pay_order_id,order_amount,pay_charge,create_order_date,pay_order_date,parmenter1) VALUES ('%s','%s','%s','%s','%s','%s','%s')",  
                         cell0,cell1,cell2,cell4,cell5,cell6,cell7);  
                 ps.executeUpdate(sql); 
                }
                 if(i%500 == 0){  
                     //500条记录提交一次  
                     conn.commit();  
                     System.out.println("已成功提交"+i+"行!");  
                 } 
                 i++;
             }  
             if(i%500 != 0){  
                 //不够500条的再提交一次（其实不用判断，直接提交就可以，不会重复提交的）  
                 conn.commit();  
                 System.out.println("已成功提交"+i+"行!");  
             }  
               
         }catch(Exception ex){  
             System.out.println("导出第"+(i+1)+"条时出错，数据是" + strLine);  
             System.out.println("出错的sql语句是：" + sql);  
             System.out.println("错误信息：");  
             ex.printStackTrace();  
             try {  
                 if (conn != null) {  
                     conn.rollback();  
                 }  
             } catch (Exception e) {  
                 e.printStackTrace();  
             }  
         } finally{  
             try {  
                 if (ps != null) {  
                     ps.close();  
                 }  
             } catch (Exception ex) {  
                 ex.printStackTrace();  
             }  
             try {  
                 if (conn != null){  
                     conn.close();  
                 }  
             } catch (Exception ex) {  
                 ex.printStackTrace();  
             }  
         }  
		}
	 }
	 /**
	     * 保存到对账单表中
	     */
		 public void saveOrderStatement (String paymaxResult){
			 if(!nullEmptyBlankJudge(paymaxResult)){
	         int i = 1;  
	         String strLine = null;  
	         Statement ps = null;  
	         Connection conn = null;  
	         String sql = null;  
	         try{  
	             conn = DBUtil.getConnection();  
	             conn.setAutoCommit(false);  
	             ps = conn.createStatement(); 
	             //解析拉卡拉返回的字符串
	             String results[]=paymaxResult.split("\r\n");
	             String orderValue="";
	          
	             System.out.println(paymaxResult);
	             while (results!=null&&i<results.length-2) {
	            	 orderValue= results[i];
	                 if(!nullEmptyBlankJudge(orderValue)){
	                 String values[]=orderValue.split(",");
	                 String cell0 = values[2];  
	                 String cell1 = values[3];  
	                 String cell2 = values[5];  
	                 String cell4 = values[6];  
	                 String cell5 ="";
	                 String cell6 ="";
	                 String cell7 ="3";
	                 if(!nullEmptyBlankJudge(values[1])){
	                	 cell5 =DateTools.dateToString(DateTools.stringtoDate(values[1],DateTools.FORMAT_FOUR),DateTools.FORMAT_ONE);
	                	 cell6=cell5;
	                 }
	                 sql = String.format("INSERT INTO PAY_ORDER_STATEMENT(merchant_order_id,pay_order_id,order_amount,pay_charge,create_order_date,pay_order_date,parmenter1) VALUES ('%s','%s','%s','%s','%s','%s','%s')",  
	                         cell0,cell1,cell2,cell4,cell5,cell6,cell7);  
	                 ps.executeUpdate(sql); 
	                }
	                 if(i%500 == 0){  
	                     //500条记录提交一次  
	                     conn.commit();  
	                     System.out.println("已成功提交"+i+"行!");  
	                 } 
	                 i++;
	             }  
	             if(i%500 != 0){  
	                 //不够500条的再提交一次（其实不用判断，直接提交就可以，不会重复提交的）  
	                 conn.commit();  
	                 System.out.println("已成功提交"+i+"行!");  
	             }  
	               
	         }catch(Exception ex){  
	             System.out.println("导出第"+(i+1)+"条时出错，数据是" + strLine);  
	             System.out.println("出错的sql语句是：" + sql);  
	             System.out.println("错误信息：");  
	             ex.printStackTrace();  
	             try {  
	                 if (conn != null) {  
	                     conn.rollback();  
	                 }  
	             } catch (Exception e) {  
	                 e.printStackTrace();  
	             }  
	         } finally{  
	             try {  
	                 if (ps != null) {  
	                     ps.close();  
	                 }  
	             } catch (Exception ex) {  
	                 ex.printStackTrace();  
	             }  
	             try {  
	                 if (conn != null){  
	                     conn.close();  
	                 }  
	             } catch (Exception ex) {  
	                 ex.printStackTrace();  
	             }  
	         }  
			}
		 }
}
