package com.mclabor.mclaborandroidclient;

import java.io.Serializable;

public class Job implements Serializable {

	private int jobId;
	private String jobName;
	private String refCode;	
	
	public Job() {
		this.jobId = -1;
		this.jobName = "";
		this.refCode = "";
	}
	
	public Job(int jobId, String jobName, String refCode) {
		this.jobId = jobId;
		this.jobName = jobName;
		this.refCode = refCode;
	}
	
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	
	public int getJobId() {
		return this.jobId;
	}
	
	public void setJobName(String name) {
		this.jobName = name;
	}
	
	public String getJobName() {
		return this.jobName;		
	}
	
	public void setRefCode(String code) {
		this.refCode = code;
	}
	
	public String getRefCode() {
		return this.refCode;
	}	
	
	@Override
	public String toString() {
		return this.jobName + " (" + this.refCode + ")";
	}
}