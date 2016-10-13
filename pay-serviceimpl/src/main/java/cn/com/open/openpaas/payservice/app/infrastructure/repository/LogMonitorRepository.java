package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import cn.com.open.openpaas.payservice.app.log.model.LogMonitor;

/**
 * 
 */
public interface LogMonitorRepository extends Repository {
	
	void insert(LogMonitor logMonitor);
	
	Integer connectionTest();
}