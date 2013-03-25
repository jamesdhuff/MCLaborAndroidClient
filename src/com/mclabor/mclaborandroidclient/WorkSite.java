package com.mclabor.mclaborandroidclient;

import java.io.Serializable;

public class WorkSite implements Serializable {

	private int workSiteId;
	private String workSiteName;
	private String refCode;	
	
	public WorkSite() {
		this.workSiteId = -1;
		this.workSiteName = "";
		this.refCode = "";
	}
	
	public WorkSite(int workSiteId, String workSiteName, String refCode) {
		this.workSiteId = workSiteId;
		this.workSiteName = workSiteName;
		this.refCode = refCode;
	}
	
	public void setWorkSiteId(int workSiteId) {
		this.workSiteId = workSiteId;
	}
	
	public int getWorkSiteId() {
		return this.workSiteId;
	}
	
	public void setWorkSiteName(String name) {
		this.workSiteName = name;
	}
	
	public String getWorkSiteName() {
		return this.workSiteName;		
	}
	
	public void setRefCode(String code) {
		this.refCode = code;
	}
	
	public String getRefCode() {
		return this.refCode;
	}
	
	@Override
	public String toString() {
		return this.workSiteName + " (" + this.refCode + ")";
	}
}
