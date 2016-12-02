package cn.com.open.pay.order.service.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.order.service.dev.PayserviceDev;
import cn.com.open.pay.order.service.merchant.service.MerchantInfoService;
import cn.com.open.pay.order.service.order.service.MerchantOrderInfoService;
import cn.com.open.pay.order.service.statement.service.PayOrderStatementService;
import cn.com.open.pay.order.service.tools.BaseControllerUtil;
import cn.com.open.pay.order.service.tools.DBUtil;
import cn.com.open.pay.order.service.tools.DateTools;
import cn.com.open.pay.order.service.tools.PropertiesTool;
import cn.com.open.pay.order.service.tools.SFTPUtils;
import com.jcraft.jsch.ChannelSftp;


@Controller
@RequestMapping("/file/upload/")
public class FileUploadController  extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
	 @Autowired
	 private PayserviceDev payserviceDev;
	 
	 
    public void fileUploadStatement() {
//    	ChannelSftp a=SFTPUtils.connectSFTP("120.27.166.169","120140804","","e:/yeepaykey/120140804","",21234);
//    	SFTPUtils.download("/online_pay/onlinepayorder/201611","2016-11-14.csv","e:/yeepaydata",a);
    	//从ftp远程服务器上下载昨天的.csv文件
    	String privateKeyPath=getSignCertPath()+ File.separator+PropertiesTool.getAppPropertieByKey("statement_privateKey_path");
    	privateKeyPath=privateKeyPath.substring(0,privateKeyPath.length());
    	System.out.println(privateKeyPath+"======privateKeyPath");
    	ChannelSftp sftp=SFTPUtils.connectSFTP(payserviceDev.getStatement_ip(),payserviceDev.getStatement_merchant_id(),"",privateKeyPath,"",Integer.parseInt(payserviceDev.getStatement_port()));
    	String fileName=DateTools.getYesterdayTime("yyyy-MM-dd")+".csv";
    	System.out.println(fileName+"======fileName");
    	
    	String pageName=DateTools.getYesterdayTime("yyyyMMdd").substring(0, 6);
    	System.out.println(pageName+"======pageName");
    	String clientPath=getSignCertPath().substring(0, getSignCertPath().length());
    	System.out.println("clientPath=="+clientPath);
    	SFTPUtils.download(payserviceDev.getStatement_server_path()+pageName,fileName,clientPath,sftp);
    	//payOrderStatementService.saveCSV("E:/yeepaydata/2016-11-14.csv", "PAY_ORDER_STATEMENT");\
    	//String filePath="E:/yeepaydata/2016-11-14.csv";
    	//读取下载到本地的.csv文件
    	String filePath=clientPath+"/"+fileName;
        File file = new File(filePath);  
        if (file.exists()) {  
            int i = 0;  
            String strLine = null;  
            Statement ps = null;  
            Connection conn = null;  
            String sql = null;  
            try{  
                conn = DBUtil.getConnection();  
                conn.setAutoCommit(false);  
                ps = conn.createStatement(); 
                BufferedReader bufferedreader = new BufferedReader(new FileReader(filePath));  
                while ((strLine = bufferedreader.readLine()) != null) {  
                    if (i == 0 ||isNullString(strLine)) {
                        // 跳过表头
                    	 i++;
                        continue;
                    }
                    String [] values = strLine.split(",");//逗号隔开的各个列  
                    String cell0 = values[0];  
                    String cell1 = values[1];  
                    String cell2 = values[2];  
                    String cell3 = values[3];  
                    String cell4 = values[4];  
                    String cell5 = values[5];  
                    String cell6 = values[6];  
                    sql = String.format("INSERT INTO PAY_ORDER_STATEMENT(merchant_order_id,pay_order_id,order_amount,feeType,pay_charge,create_order_date,pay_order_date) VALUES ('%s','%s','%s','%s','%s','%s','%s')",  
                            cell0,cell1,cell2,cell3,cell4,cell5,cell6);  
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
    }
   
    private InputStreamReader fr = null;  
    private BufferedReader br = null;  
  
    /** 
     * 解析csv文件 到一个list中 每个单元个为一个String类型记录，每一行为一个list。 再将所有的行放到一个总list中 
     */  
    public List<List<String>> readCSVFile(String filePath) throws IOException { 
    	fr = new InputStreamReader(new FileInputStream(filePath));  
        br = new BufferedReader(fr);  
        String rec = null;// 一行  
        String str;// 一个单元格  
        List<List<String>> listFile = new ArrayList<List<String>>();  
        try {  
            // 读取一行  
            while ((rec = br.readLine()) != null) {  
                Pattern pCells = Pattern  
                        .compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");  
                Matcher mCells = pCells.matcher(rec);  
                List<String> cells = new ArrayList<String>();// 每行记录一个list  
                // 读取每个单元格  
                while (mCells.find()) {  
                    str = mCells.group();  
                    str = str.replaceAll(  
                            "(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");  
                    str = str.replaceAll("(?sm)(\"(\"))", "$2");  
                    cells.add(str);  
                }  
                listFile.add(cells);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (fr != null) {  
                fr.close();  
            }  
            if (br != null) {  
                br.close();  
            }  
        }  
        return listFile;  
    }  
}
