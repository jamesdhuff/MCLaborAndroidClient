package com.mclabor.mclaborandroidclient;

public class WorkSiteFactory {
	public static WorkSite getWorkSite(String wsString) {
		WorkSite ws = new WorkSite();
		String[] wsValues = wsString.split("=");
		
		if(wsValues.length >= 5) {
			String refCode = wsValues[2].substring(0,wsValues[2].indexOf(";"));
			String wsIdString = wsValues[3].substring(0,wsValues[3].indexOf(";"));
			String wsName = wsValues[4].substring(0,wsValues[4].indexOf(";"));
			
			int wsId = Integer.parseInt(wsIdString);
			
			if(refCode.equals("null")) {
				refCode = "";
			}
			
			if(wsName.equals("null")) {
				wsName = "";
			}
			
			ws.setWorkSiteId(wsId);
			ws.setWorkSiteName(wsName);
			ws.setRefCode(refCode);
		}
		
		return ws;
		
	}
}
