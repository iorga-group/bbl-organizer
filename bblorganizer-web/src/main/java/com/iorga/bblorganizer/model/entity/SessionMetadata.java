package com.iorga.bblorganizer.model.entity;

import java.util.Date;

public class SessionMetadata {
    private long id;
	private String baggerName;
	private String title;
	private Date achievementDate;
    private Date creationDate;
    private Date plannedDate;
	
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
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public Date getPlannedDate() {
        return plannedDate;
    }
    public void setPlannedDate(Date plannedDate) {
        this.plannedDate = plannedDate;
    }
}
