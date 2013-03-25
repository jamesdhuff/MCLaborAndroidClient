package com.mclabor.mclaborandroidclient;

public class TimeCardFactory {
	public static TimeCard getTimeCard(String timeCardString) {
		TimeCard tc = new TimeCard();
		
		String[] tcValues = timeCardString.split("=");
		
		if(tcValues.length >= 8) {
			String empIdString = tcValues[1].substring(0,tcValues[1].indexOf(";"));
			String endDate = tcValues[2].substring(0,tcValues[2].indexOf(";"));
			String jobName = tcValues[3].substring(0,tcValues[3].indexOf(";"));			
			String tcIdString = tcValues[4].substring(0,tcValues[4].indexOf(";"));
			String startDate = tcValues[5].substring(0,tcValues[5].indexOf(";"));
			String ttlHrsString = tcValues[6].substring(0,tcValues[6].indexOf(";"));
			String workSiteName = tcValues[7].substring(0,tcValues[7].indexOf(";"));
			
			int tcId = Integer.parseInt(tcIdString);
			int empId = Integer.parseInt(empIdString);
			double ttlHrs = Double.parseDouble(ttlHrsString);
			
			tc.setLaborDetailID(tcId);
			tc.setEmployeeID(empId);
			tc.setJobName(jobName);
			tc.setWorkSiteName(workSiteName);
			tc.setStartDateTime(startDate);
			tc.setEndDateTime(endDate);
			tc.setTotalHours(ttlHrs);			
		}
		
		return tc;
	}
}

//anyType{EmployeeID=2; EndDateTime=anyType{}; JobName=Estimate; LaborDetailID=88; StartDateTime=3/23/2013 3:08 PM; TotalHours=0; WorkSiteName=Yard; }
