package cn.com.open.pay.order.service.datasource;

import java.util.logging.Logger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {  
  
    @Override  
    protected Object determineCurrentLookupKey() {  
        return DataSourceSwitcher.getDataSource();  
    }

	public Logger getParentLogger() {
		// TODO Auto-generated method stub
		return null;
	}  

}
