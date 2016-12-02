package cn.com.open.pay.order.service.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SFTPUtils {
	public static String CERT = "sftpfiles";

    /**
     * 获取连接
     * @return channel
     */
    public static ChannelSftp connectSFTP(String host,String username, String password,String privateKey,String passphrase,int port) {
        JSch jsch = new JSch();
        Channel channel = null;
        try {
            if (privateKey != null && !"".equals(privateKey)) {
                //使用密钥验证方式，密钥可以使有口令的密钥，也可以是没有口令的密钥
                if (passphrase != null && "".equals(passphrase)) {
                    jsch.addIdentity(privateKey, passphrase);
                } else {
                    jsch.addIdentity(privateKey);
                }
            }
            Session session = jsch.getSession(username, host, port);
            if (password != null && !"".equals(password)) {
                session.setPassword(password);
            }
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");// do not verify host key
            session.setConfig(sshConfig);
            // session.setTimeout(timeout);
            session.setServerAliveInterval(92000);
            session.connect();
            //参数sftp指明要打开的连接是sftp连接
            channel = session.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return (ChannelSftp) channel;
    }
    
    /**
     * 上传文件
     * 
     * @param directory
     *            上传的目录
     * @param uploadFile
     *            要上传的文件
     * @param sftp
     */
    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     * 
     * @param directory
     *            下载目录
     * @param downloadFile
     *            下载的文件
     * @param saveFile
     *            存在本地的路径
     * @param sftp
     */
    public static void download(String directory, String downloadFile,
            String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.get(downloadFile,saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     * 
     * @param directory
     *            要删除文件所在目录
     * @param deleteFile
     *            要删除的文件
     * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void disconnected(ChannelSftp sftp){
        if (sftp != null) {
            try {
                sftp.getSession().disconnect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
            sftp.disconnect();
        }
    }
    public static void  deleteFile(String filePath){
    	   File f = new File(filePath);  // 输入要删除的文件位置
    	    if(f.exists())
    	     f.delete();
    }
 
    public static String getSignCertPath() {
		return  SFTPUtils.class.getClassLoader().getResource("").getPath()+ File.separator
				+ CERT + File.separator+PropertiesTool.getAppPropertieByKey("acpsdk.signCert.path");
	}
    
    public static void fileload(String filePath){
    	
    	//读取下载到本地的.csv文件
    	//String filePath=clientPath+"/"+fileName;
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
    public static void main(String[] args) throws Exception{  
//    	ChannelSftp a=connectSFTP("120.27.166.169","120140804","","e:/yeepaykey/120140804","",21234);
//    	download("/online_pay/onlinepayorder/201611","2016-11-14.csv","e:/yeepaydata",a);
    	String filePath="E:/yeepaydata/2016-11-14.csv";
    	fileload(filePath);
       }  
    
    
}