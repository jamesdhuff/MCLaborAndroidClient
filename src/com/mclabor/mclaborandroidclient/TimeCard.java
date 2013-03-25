package com.mclabor.mclaborandroidclient;

import java.io.Serializable;


public class TimeCard implements Serializable {

	private int laborDetailId;
	private int employeeId;
	private String jobName;
	private String workSiteName;
	private String startDateTime;
	private String endDateTime;
	private double totalHours;
	
	public TimeCard() {
		this.laborDetailId = -1;
		this.employeeId = -1;
		this.jobName = "";
		this.workSiteName = "";
		this.startDateTime = "";
		this.endDateTime = "";
		this.totalHours = 0;
	}
	
	public void setLaborDetailID(int laborDetailId) {
		this.laborDetailId = laborDetailId;
	}
	
	public int getLaborDetailID() {
		return this.laborDetailId;
	}
	
	public void setEmployeeID(int employeeId) {
		this.employeeId = employeeId;
	}
	
	public int getEmployeeID() {
		return this.employeeId;	
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	public String getJobName() {
		return this.jobName;
	}
	
	public void setWorkSiteName(String workSiteName) {
		this.workSiteName = workSiteName;
	}
	
	public String getWorkSiteName() {
		return this.workSiteName;
	}
	
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}
	
	public String getStartDateTime() {
		return this.startDateTime;
	}
	
	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	public String getEndDateTime() {
		return this.endDateTime;
	}
	
	
	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}
	
	public double getTotalHours() {
		return this.totalHours;
	}
	
	
}
