package com.mclabor.mclaborandroidclient;

import java.io.Serializable;

public class Employee implements Serializable{
	
	private int employeeId = -1;
	private String firstName = "";
	private String lastName = "";
	private String refCode = "";
	private boolean isClockedIn = false;
	
	public Employee() {
		
	}

	public Employee(int employeeId, String firstName, String lastName, String refCode, boolean isClockedIn) {
		
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.refCode = refCode;
		this.isClockedIn = isClockedIn;
		
	}
	
	public int getEmployeeId() {
		return this.employeeId;
	}
	
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getRefCode() {
		return this.refCode;
	}
	
	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}
	
	public boolean isClockedIn() {
		return this.isClockedIn;
	}
	
	public void setClockedIn(boolean isClockedIn) {
		this.isClockedIn = isClockedIn;
	}
	
	public String getFullName() {
		
		return this.firstName + " " + this.lastName;
	}
	
	@Override
	public String toString() {
		return this.firstName + " " + this.lastName + " (" + this.refCode + ")"; 
	}
	
}
