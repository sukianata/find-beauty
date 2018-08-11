package com.example.util;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

public class JDBCUtil {
	
	private static final String URL=ConfigUtil.getProperty("url");
	
	private static final String USERNAME=ConfigUtil.getProperty("username");
	
	private static final String PASSWORD=ConfigUtil.getProperty("password");
	
	private static Logger logger=Logger.getLogger(JDBCUtil.class.getName());
	
	private static DataSource ds=null;
	
	static {
		try {
			Class.forName(ConfigUtil.getProperty("driver"));
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	private synchronized static void initDB(){
		//創建對象池
		ObjectPool<?> pool=new GenericObjectPool<>();
		ConnectionFactory connectionFactory=new DriverManagerConnectionFactory(URL, USERNAME, PASSWORD);
		PoolableConnectionFactory poolFactory=new PoolableConnectionFactory(connectionFactory, pool, null, null, false, true);
		ds=new PoolingDataSource(poolFactory.getPool());
	}
	
	public static Connection getConnection(){
		if(ds==null){
			initDB();
		}
		try {
			Connection conn=ds.getConnection();
			if (conn.isClosed()) {
				logger.error("the connection has already been closed......");
				return ds.getConnection();
			}else {
				return conn;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	

	
}
