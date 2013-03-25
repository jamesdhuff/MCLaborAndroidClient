package com.mclabor.mclaborandroidclient;

public class JobFactory {
	public static Job getJob(String jobString) {
		Job job = new Job();
		
		String[] jobValues = jobString.split("=");
		
		if(jobValues.length >= 5) {
			String jobIdString = jobValues[2].substring(0,jobValues[2].indexOf(";"));
			String jobName = jobValues[3].substring(0,jobValues[3].indexOf(";"));
			String refCode = jobValues[4].substring(0,jobValues[4].indexOf(";"));			
			
			int jobId = Integer.parseInt(jobIdString);
			
			if(refCode.equals("null")) {
				refCode = "";
			}
			
			if(jobName.equals("null")) {
				jobName = "";
			}
			
			job.setJobId(jobId);
			job.setJobName(jobName);
			job.setRefCode(refCode);
		}
		
		return job;
	}
}

//anyType{Job=anyType{Description=null; JobId=2; JobName=Estimate; RefCode=Job 1; description=null; jobId=2; jobName=Estimate; refCode=Job 1; }; Job=anyType{Description=null; JobId=1; JobName=Finisher; RefCode=Job 2; description=null; jobId=1; jobName=Finisher; refCode=Job 2; }; }
