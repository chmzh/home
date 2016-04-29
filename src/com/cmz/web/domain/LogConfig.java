package com.cmz.web.domain;

public class LogConfig {
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLogtypename() {
		return logtypename;
	}
	public void setLogtypename(String logtypename) {
		this.logtypename = logtypename;
	}
	public String getLogtypeflag() {
		return logtypeflag;
	}
	public void setLogtypeflag(String logtypeflag) {
		this.logtypeflag = logtypeflag;
	}
	public String getLogfields() {
		return logfields;
	}
	public void setLogfields(String logfields) {
		this.logfields = logfields;
	}
	
	public String getSelector() {
		return selector;
	}
	public void setSelector(String selector) {
		this.selector = selector;
	}
	
	private int id;
	private String selector;
	private String logtypename;
	private String logtypeflag;
	private String logfields;

}
