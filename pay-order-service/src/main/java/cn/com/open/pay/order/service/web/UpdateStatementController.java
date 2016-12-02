package cn.com.open.pay.order.service.web;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.order.service.dev.PayserviceDev;
import cn.com.open.pay.order.service.statement.service.PayOrderStatementService;
import cn.com.open.pay.order.service.tools.BaseControllerUtil;
import cn.com.open.pay.order.service.tools.DBUtil;
import cn.com.open.pay.order.service.tools.DateTools;
import cn.com.open.pay.order.service.tools.SFTPUtils;


@Controller
@RequestMapping("/statement/update/")
public class UpdateStatementController  extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UpdateStatementController.class);
	
	 @Autowired
	 private PayOrderStatementService payOrderStatementService;
	 /**
	     * yeepay定时下载对账单
	     * @return Json
	     */
    public void updateStatement() {
    	String startTime="";
    	String endTime="";
    	String yesterday=DateTools.getYesterdayTime("yyyy-MM-dd");
    	//yesterday="2016-11-14";
    	startTime = yesterday+" 00:00:00";
		endTime = yesterday+" 23:59:59";
		List<Map<String, Object>> list=payOrderStatementService.getOrderIdByTime(startTime, endTime);
		if(list!=null&&list.size()>0){
            int i = 0;  
            String strLine = null;  
            Statement ps = null;  
            Connection conn = null;  
            String sql = null;  
            try{  
                conn = DBUtil.getConnection();  
                conn.setAutoCommit(false);  
                ps = conn.createStatement(); 
            	for(int j=0;j<list.size();j++){
            		i++;
                    String merchantOrderId=list.get(j).get("merchantOrderId").toString();
                    sql = "update  PAY_ORDER_STATEMENT set statement_status=1 where merchant_order_id="+merchantOrderId;
                    ps.executeUpdate(sql);  
                    if(i%500 == 0){  
                        //500条记录提交一次  
                        conn.commit();  
                        System.out.println("已成功提交"+i+"行!");  
                    }  
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
	  //	SFTPUtils.deleteFile("E:/yeepaydata/2016-11-14.csv");
		String fileName=DateTools.getYesterdayTime("yyyy-MM-dd")+".csv";
		String filePath=getSignCertPath().substring(1, getSignCertPath().length())+ File.separator+fileName;
		deleteFile(filePath);
    }
    public static void  deleteFile(String filePath){
 	   File f = new File(filePath);  // 输入要删除的文件位置
 	    if(f.exists())
 	     f.delete();
     }
    
}
