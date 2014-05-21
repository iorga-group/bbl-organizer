package com.iorga.bblorganizer.model.entity;

import java.util.Date;

public class AchievedSession {
	private String baggerName;
	private String title;
	private Date achievementDate;
	
	public String getBaggerName() {
		return baggerName;
	}
	public void setBaggerName(String baggerName) {
		this.baggerName = baggerName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getAchievementDate() {
		return achievementDate;
	}
	public void setAchievementDate(Date achievementDate) {
		this.achievementDate = achievementDate;
	}
}
