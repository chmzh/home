package com.cmz.web.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cmz.web.constant.GlobalConstant;

public class HiveJDBCClinet {

	static String JDBCDriver = "com.cloudera.hive.jdbc41.HS2Driver";
	private static final String CONNECTION_URL = GlobalConstant.HIVE_CONNECTION_URL;
	
	private Connection con = null;
	
	public HiveJDBCClinet(){
		try {
			Class.forName(JDBCDriver);
			con = DriverManager.getConnection(CONNECTION_URL);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public boolean query(String query){
		boolean rs = false;
		
		try {
			Statement stmt = con.createStatement();
			rs = stmt.execute(query);
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public void closeConn(){
		if(con != null){
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public ResultSet executeQuery(String query){
		ResultSet rs = null;
		
		try {
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		return rs;
	}
}