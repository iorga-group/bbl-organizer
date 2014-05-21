package com.iorga.bblorganizer.model.entity;

public class Vote {
	private String baggerName;
	private String sessionTitle;
	private String userName;

	public Vote() {
	}

	public String getBaggerName() {
		return baggerName;
	}
	public void setBaggerName(String baggerName) {
		this.baggerName = baggerName;
	}
	public String getSessionTitle() {
		return sessionTitle;
	}
	public void setSessionTitle(String sessionTitle) {
		this.sessionTitle = sessionTitle;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
