package com.cmz.web.constant;

public class GlobalConstant {
	
	public final static String LOGIN_KEY = "dgfergjdfhgfgkurti";
	public final static String WEBROOT = "webapp.root";
	public final static String GAME_KEY = "123456";
	//登陆会话
	public final static String LOGIN_NAME = "login_name";
	public final static String LOGIN_TIME = "login_time";
	public final static String LOGIN_IP = "login_ip";
	public final static String LOGIN_SIGN = "login_sign";
	public final static String LOGIN_POWER = "login_power";
	
	//HBASE zookeeper
	public final static String ZKQUORUM = "master1,master2,slave1,slave2,slave3";
	public final static String HBASE_HOST = "master1";
	
	//public final static String HIVE_CONNECTION_URL = "jdbc:hive2://192.168.21.34:10000/";
	public final static String HIVE_CONNECTION_URL = "jdbc:hive2://master1:10000/";

	public static final String IMPALA_CONNECTION_URL = "jdbc:hive2://slave1:21050/;auth=noSasl";
	
	
	//邮件服务器
	public final static String MAIL_HOST = "smtp.*.*.*";
	public final static int MAIL_PORT = 25;
	public final static String MAIL_USER = "abc@abc.com";
	public final static String MAIL_PWD = "agferwae";
	
}
