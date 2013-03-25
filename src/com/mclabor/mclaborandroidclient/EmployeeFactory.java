package com.mclabor.mclaborandroidclient;

public class EmployeeFactory {
	public static Employee getEmployee(String empString) {
		
		Employee emp = new Employee();
		
		String[] empValues = empString.split("=");
		
		String empIdString = empValues[1].substring(0,empValues[1].indexOf(";"));
		String firstName = empValues[2].substring(0,empValues[2].indexOf(";"));
		String isClockedInString = empValues[3].substring(0,empValues[3].indexOf(";"));
		String lastName = empValues[4].substring(0,empValues[4].indexOf(";"));
		String refCode = empValues[6].substring(0,empValues[6].indexOf(";"));
		
		int empId = Integer.parseInt(empIdString);
		
		if(firstName.equals("null")) {
			firstName = "";
		}
		
		boolean isClockedIn = false;
		if(isClockedInString.equals("true"))
		{
			isClockedIn = true;
		}
		
		if(lastName.equals("null")) {
			lastName = "";
		}
		
		if(refCode.equals("null")) {
			refCode = "";
		}
		
		emp.setEmployeeId(empId);
		emp.setFirstName(firstName);
		emp.setLastName(lastName);
		emp.setClockedIn(isClockedIn);
		emp.setRefCode(refCode);
		
		return emp;
	}
}

//anyType{EmployeeId=-1; FirstName=null; IsClockedIn=false; LastName=null; LoginId=0; RefCode=null; }