package cn.com.open.openpaas.payservice.app.datasource;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;  

public class DynamicDataSource extends AbstractRoutingDataSource {  
  
    @Override  
    protected Object determineCurrentLookupKey() {  
        return DataSourceSwitcher.getDataSource();  
    }

	@Override
	public Logger getParentLogger() {
		// TODO Auto-generated method stub
		return null;
	}



//	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
