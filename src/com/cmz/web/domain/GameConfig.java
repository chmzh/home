package com.cmz.web.domain;

public class GameConfig {
	private int id;
	private String gamename;
	private String gameflag;
	private String secretkey;
	private boolean alarm;
	private String mails;
	private boolean open;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGamename() {
		return gamename;
	}
	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	
	public String getGameflag() {
		return gameflag;
	}
	public void setGameflag(String gameflag) {
		this.gameflag = gameflag;
	}

	public String getSecretkey() {
		return secretkey;
	}
	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}
	public boolean isAlarm() {
		return alarm;
	}
	public void setAlarm(boolean alarm) {
		this.alarm = alarm;
	}
	public String getMails() {
		return mails;
	}
	public void setMails(String mails) {
		this.mails = mails;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
}
