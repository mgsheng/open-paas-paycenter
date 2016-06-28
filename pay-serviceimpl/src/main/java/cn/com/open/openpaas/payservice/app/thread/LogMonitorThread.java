package cn.com.open.openpaas.payservice.app.thread;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import cn.com.open.openpaas.payservice.app.log.model.LogMonitor;
import cn.com.open.openpaas.payservice.app.log.service.LogMonitorService;
import cn.com.open.openpaas.payservice.app.tools.DateTools;


public class LogMonitorThread implements Runnable {

	private LogMonitorService logMonitorService;
	String[] logArray = null;
	int index = 0;
	
	public LogMonitorThread(String[] logArray, int index, LogMonitorService logMonitorService){
		this.logArray = logArray;
		this.index = index;
		this.logMonitorService = logMonitorService;
	}
	
	@Override
	public void run() {
		if(logArray==null){
			return;
		}
		try {
			//记录日志
			LogMonitor logMonitor = new LogMonitor();
			//日期
			logMonitor.setLogTime(DateTools.stringtoDate(logArray[0], "yyyyMMddHHmmss"));
			//方法名
			logMonitor.setName(logArray[1]);
			//执行时间
			try {
				logMonitor.setExecTime(Long.parseLong(logArray[2]));
			} catch (NumberFormatException e) {
				logMonitor.setExecTime(-1);
			}
			//用户名
			logMonitor.setUsername(logArray[3]);
			//密码
			logMonitor.setPassword(logArray[4]);
			//应用ID
			try {
				logMonitor.setAppId(Integer.parseInt(logArray[5]));
			} catch (NumberFormatException e) {
				logMonitor.setAppId(-1);
			}
			//执行status
			logMonitor.setStatus(Integer.parseInt(logArray[6]));
			//错误号
			if(StringUtils.isNotBlank(logArray[7])){
				try {
					logMonitor.setErrorCode(Integer.parseInt(logArray[7]));
				} catch (NumberFormatException e) {
					logMonitor.setErrorCode(-1);
				}
			}
			logMonitor.setCreateTime(new Date());
			logMonitorService.insert(logMonitor);
		} catch (Exception e) {
			System.out.println("行"+index+"解析异常");
			e.printStackTrace();
		}
	}
}
