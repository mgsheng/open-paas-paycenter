package cn.com.open.openpaas.payservice.app.channel;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import cn.com.open.openpaas.payservice.app.channel.unionpay.sdk.CertUtil;
import cn.com.open.openpaas.payservice.app.tools.PropertiesTool;

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
		return  CertUtil.class.getClassLoader().getResource("").getPath()+ File.separator
				+ CERT + File.separator+PropertiesTool.getAppPropertieByKey("acpsdk.signCert.path");
	}
    public static void main(String[] args) throws Exception{  
    	ChannelSftp a=connectSFTP("120.27.166.169","120140804","","e:/yeepaykey/120140804","",21234);
    	download("/online_pay/onlinepayorder/201611","2016-11-14.csv","e:/yeepaydata",a);
    	String relativelyPath=System.getProperty("user.dir");
    	System.out.println(relativelyPath);
       }  
    
    
}