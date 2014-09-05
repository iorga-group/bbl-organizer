package com.iorga.bblorganizer.model.entity;

public class Vote {
	private long idSessionMetadata;
	private String userName;

	public Vote() {
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
    public long getIdSessionMetadata() {
        return idSessionMetadata;
    }
    public void setIdSessionMetadata(long idSessionMetadata) {
        this.idSessionMetadata = idSessionMetadata;
    }
}
