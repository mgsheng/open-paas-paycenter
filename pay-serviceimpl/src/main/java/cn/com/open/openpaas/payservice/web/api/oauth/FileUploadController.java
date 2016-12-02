package cn.com.open.openpaas.payservice.web.api.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.openpaas.payservice.app.channel.SFTPUtils;

import com.jcraft.jsch.ChannelSftp;



public class FileUploadController  {
	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
	 /**
     * yeepay定时下载对账单
     * @return Json
     */
    public void orderAutoSend() {
    	ChannelSftp a=SFTPUtils.connectSFTP("120.27.166.169","120140804","","e:/yeepaykey/120140804","",21234);
    	SFTPUtils.download("/online_pay/onlinepayorder/201611","2016-11-14.csv","e:/yeepaydata",a);
    	String path1=Thread.currentThread().getContextClassLoader().getResource("").getPath();
    	String relativelyPath=System.getProperty("user.dir");
    	System.out.println("path1:"+path1);
    	System.out.println("relativelyPath:"+relativelyPath);
    }
	
}
