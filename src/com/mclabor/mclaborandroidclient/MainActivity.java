package com.mclabor.mclaborandroidclient;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity 	extends Activity 
							implements LoginConfirmationDialog.LoginConfirmationDialogListener {

	private static final String NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_ACTION = "http://tempuri.org/ILaborService/";
    //private static final String URL = "http://10.0.2.2/MCLaborServer/LaborService.svc";
    private static final String URL = "http://marshallconcrete.no-ip.org/MCLaborServer/LaborService.svc";
    private static final int SOAP_VERSION = SoapEnvelope.VER11;
    
    private Employee employee;
    private ArrayList<WorkSite> workSiteList;
    private ArrayList<Job> jobList;
    private TimeCard latestOpenTimeCard;
	
    private class GetEmployeeTask extends AsyncTask<String, Void, Object> {     		        						        	       
                
    	private String method = "";
        private String action = "";

        public ProgressDialog progressDialog;
        
        public GetEmployeeTask(Activity activity, String method) {
        	this.method = method;
        	this.action = SOAP_ACTION + this.method;
        	this.progressDialog = new ProgressDialog(activity);
        }
    	        
        protected void onPreExecute() {
        	Button okBtn = (Button)findViewById(R.id.btnLoginGo);
        	okBtn.setEnabled(false);
        	this.progressDialog.setMessage(getResources().getString(R.string.progress_dialog_message));
        	this.progressDialog.show();
        }
        
    	protected void onPostExecute(Object result) {
    		String resultTxt = result.toString();
    		System.out.println(resultTxt);
    		
    		if(resultTxt.startsWith("Exception")) {
    			//TODO pop dialog with exception message
    		}
    		
    		employee = EmployeeFactory.getEmployee(resultTxt);
    		
    		if(this.progressDialog.isShowing()) {
    			this.progressDialog.dismiss();	
    		}
    		
    		if(employee != null && employee.getEmployeeId() > 0) {
    			LoginConfirmationDialog confirmationDialog = new LoginConfirmationDialog();
    			Bundle args = new Bundle();
    			args.putString("empName", employee.getFullName());
    			confirmationDialog.setArguments(args);
    			confirmationDialog.show(getFragmentManager(), "loginConfirmationDialog");   
    			resetMainActivity();
    		}
    		else {
    			InvalidLoginDialog dialog = new InvalidLoginDialog();
    			dialog.show(getFragmentManager(), "invalidLoginDialog");
    			resetMainActivity();
    		}
    	}    	
    	
    	@Override
    	protected Object doInBackground(String... args) {							
    				
    		System.out.println("Calling getEmployee");
    		SoapObject request = new SoapObject(NAMESPACE, this.method);
    		
    		if(this.method.equalsIgnoreCase("getEmployee") && args.length > 0) {
    			request.addProperty("loginId", Integer.parseInt(args[0]));
    		}
    		
    		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
    		envelope.dotNet = true;
    		envelope.setOutputSoapObject(request);
    		
    		System.out.println("getEmployee request = " + request);
    		System.out.println("getEmployee envelope = " + envelope.env);
    		
    		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		
    		try {
    			androidHttpTransport.call(this.action, envelope);
    			Object result = envelope.getResponse();
    			String resultData = result.toString();			
    			System.out.println("getEmployee result = " + resultData);
    			return resultData;
    		}
    		catch (Exception ex) {
    			System.out.println("Exception during call to getEmployee\n" + ex.toString());
    			return "Exception occurred while logging in:\n\n " + ex.toString();
    		}    		
    	}

    }
    
    private class GetWorkSiteListTask extends AsyncTask<String, Void, Object> {     		        						        	       
        
    	private String method = "";
        private String action = "";
        private Activity activity;

        public ProgressDialog progressDialog;
        
        public GetWorkSiteListTask(Activity activity, String method) {
        	this.method = method;
        	this.action = SOAP_ACTION + this.method;
        	this.progressDialog = new ProgressDialog(activity);
        	this.activity = activity;
        }
    	        
        protected void onPreExecute() {
        	this.progressDialog.setMessage(getResources().getString(R.string.progress_dialog_message));
        	this.progressDialog.show();
        }
        
    	protected void onPostExecute(Object result) {
    		
    		workSiteList = new ArrayList<WorkSite>();
    		
    		String resultTxt = result.toString();
    		    		
    		if(this.progressDialog.isShowing()) {
    			this.progressDialog.dismiss();	
    		}
    		
    		String[] results = resultTxt.split("WorkSite=anyType");    		
    		
    		if(results.length > 0) {
    		
	    		for(int i = 0; i < results.length; i++) {
	    			String wsString = results[i];
	    			WorkSite ws = WorkSiteFactory.getWorkSite(wsString);
	    			System.out.println("WorkSite " + ws.getWorkSiteName());
	    			if(ws.getWorkSiteId() > 0) {
	    				workSiteList.add(ws);
	    				System.out.println("WorkSite " + ws.getWorkSiteName() + " added to workSiteList");
	    			}
	    			
	    		}
    		}
    		
    		GetJobListTask jobListTask = new GetJobListTask(activity, "getJobList");
			jobListTask.execute(Integer.toString(employee.getEmployeeId()));
    		
    		//anyType{WorkSite=anyType{Active=true; RefCode=12-100; WorkSiteId=2; WorkSiteName=Yard; }; WorkSite=anyType{Active=true; RefCode=12-021; WorkSiteId=6; WorkSiteName=BC Schools; }; WorkSite=anyType{Active=true; RefCode=12-024; WorkSiteId=10; WorkSiteName=Petoskey waterfront; }; WorkSite=anyType{Active=true; RefCode=12-025; WorkSiteId=11; WorkSiteName=Lears Road; }; WorkSite=anyType{Active=true; RefCode=12-026; WorkSiteId=12; WorkSiteName=Rogers City; }; }
    	}    	
    	
    	@Override
    	protected Object doInBackground(String... args) {							
    				
    		SoapObject request = new SoapObject(NAMESPACE, this.method);
    		
    		if(this.method.equalsIgnoreCase("getEmployee") && args.length > 0) {
    			request.addProperty("loginId", Integer.parseInt(args[0]));
    		}
    		
    		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
    		envelope.dotNet = true;
    		envelope.setOutputSoapObject(request);
    		
    		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		
    		try {
    			androidHttpTransport.call(this.action, envelope);
    			Object result = envelope.getResponse();
    			String resultData = result.toString();			
    			System.out.println(resultData);
    			return resultData;
    		}
    		catch (Exception ex) {
    			System.out.println(ex.toString());
    		}
    		
    		return null;
    	}

    }
        
    private class GetJobListTask extends AsyncTask<String, Void, Object> {     		        						        	       
        
    	private String method = "";
        private String action = "";

        public ProgressDialog progressDialog;
        
        public GetJobListTask(Activity activity, String method) {
        	this.method = method;
        	this.action = SOAP_ACTION + this.method;
        	this.progressDialog = new ProgressDialog(activity);
        }
    	        
        protected void onPreExecute() {
        	this.progressDialog.setMessage(getResources().getString(R.string.progress_dialog_message));
        	this.progressDialog.show();
        }
        
    	protected void onPostExecute(Object result) {
    		
    		jobList = new ArrayList<Job>();
    		
    		String resultTxt = result.toString();
    		    		
    		if(this.progressDialog.isShowing()) {
    			this.progressDialog.dismiss();	
    		}
    		
    		String[] results = resultTxt.split("Job=anyType");    		
    		
    		if(results.length > 0) {
    		
	    		for(int i = 0; i < results.length; i++) {
	    			String jobString = results[i];
	    			Job job = JobFactory.getJob(jobString);
	    			System.out.println("Job " + job.getJobName());
	    			if(job.getJobId() > 0) {
	    				jobList.add(job);
	    				System.out.println("Job " + job.getJobName() + " added to jobList");
	    			}	    			
	    		}
    		}
    		
    		navigateClockIn();
    	}
    	
    	@Override
    	protected Object doInBackground(String... args) {							
    				
    		SoapObject request = new SoapObject(NAMESPACE, this.method);
    		
    		if(this.method.equalsIgnoreCase("getEmployee") && args.length > 0) {
    			request.addProperty("loginId", Integer.parseInt(args[0]));
    		}
    		if(this.method.equalsIgnoreCase("getJobList") && args.length > 0) {
    			request.addProperty("employeeId", Integer.parseInt(args[0]));
    		}
    		
    		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
    		envelope.dotNet = true;
    		envelope.setOutputSoapObject(request);
    		
    		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		
    		try {
    			androidHttpTransport.call(this.action, envelope);
    			Object result = envelope.getResponse();
    			String resultData = result.toString();			
    			System.out.println(resultData);
    			return resultData;
    		}
    		catch (Exception ex) {
    			//TODO pop dialog with exception message
    			System.out.println(ex.toString());
    		}
    		
    		return null;
    	}

    }

    private class GetLatestOpenTimeCardTask extends AsyncTask<String, Void, Object> {     		        						        	       
        
    	private String method = "";
        private String action = "";

        public ProgressDialog progressDialog;
        
        public GetLatestOpenTimeCardTask(Activity activity, String method) {
        	this.method = method;
        	this.action = SOAP_ACTION + this.method;
        	this.progressDialog = new ProgressDialog(activity);
        }
    	        
        protected void onPreExecute() {        	
        	this.progressDialog.setMessage(getResources().getString(R.string.progress_dialog_message));
        	this.progressDialog.show();
        }
        
    	protected void onPostExecute(Object result) {
    		String resultTxt = result.toString();
    		System.out.println(resultTxt);
    		
    		latestOpenTimeCard = TimeCardFactory.getTimeCard(resultTxt);
    		
    		if(this.progressDialog.isShowing()) {
    			this.progressDialog.dismiss();	
    		}
    		
    		navigateClockOut();
    	}
    	
      	@Override
    	protected Object doInBackground(String... args) {							
    				
      		System.out.println("Calling getLatestOpenTimeCard()");
    		SoapObject request = new SoapObject(NAMESPACE, this.method);
    		    		
    		request.addProperty("employeeId", employee.getEmployeeId());
    		    		
    		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SOAP_VERSION);
    		envelope.dotNet = true;
    		envelope.setOutputSoapObject(request);
    		
    		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    		
    		try {
    			androidHttpTransport.call(this.action, envelope);
    			Object result = envelope.getResponse();
    			String resultData = result.toString();			
    			return resultData;
    		}
    		catch (Exception ex) {
    			System.out.println("Exception in getLatestOpenTimeCard()\n" + ex.toString());
    		}
    		
    		return null;
    	}

    }
    	
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		
	@Override    
	public void onDialogPositiveClick(DialogFragment dialog) {        
		
		resetMainActivity();
		
		if(employee.isClockedIn())  {
			GetLatestOpenTimeCardTask tcTask = new GetLatestOpenTimeCardTask(this, "getLatestOpenTimeCardAndroid");
			tcTask.execute("");
		}
		else {
			GetWorkSiteListTask wsListTask = new GetWorkSiteListTask(this, "getWorkSiteList");
			wsListTask.execute("");
		}
	}
	
	@Override    
	public void onDialogNegativeClick(DialogFragment dialog) {        
		resetMainActivity();
	}
	
	public void loginNumBtnClick(View sender) {
		
		TextView inputTxtView = (TextView)findViewById(R.id.txtLoginInput);
		
		Button btn = (Button)sender;
		
		String  inputTxt = inputTxtView.getText().toString();
		inputTxt += btn.getText().toString();
		
		inputTxtView.setText(inputTxt);		
	}
	
	public void loginClearBtnClick(View sender) {
		
		TextView inputTxtView = (TextView)findViewById(R.id.txtLoginInput);
		inputTxtView.setText("");
	}
	
	public void loginGoBtnClick(View sender) {
		
		TextView inputTxtView = (TextView)findViewById(R.id.txtLoginInput); 
		GetEmployeeTask myTask = new GetEmployeeTask(this, "getEmployee");
		
		String input = "0";
		
		if(inputTxtView.getText() != null && inputTxtView.getText().length() > 0) {
			input = inputTxtView.getText().toString();
		}				
		
		myTask.execute(input);
	}
	
	public void resetMainActivity() {
		Button okBtn = (Button)findViewById(R.id.btnLoginGo);
		okBtn.setEnabled(true);
		
		TextView inputTxtView = (TextView)findViewById(R.id.txtLoginInput);
		inputTxtView.setText("");
	}

	public void navigateClockIn() {
		Intent intent = new Intent(this, ClockInActivity.class);
		intent.putExtra("employee", employee);
		intent.putExtra("workSiteList", workSiteList);
		intent.putExtra("jobList", jobList);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		startActivity(intent);
	}
	
	public void navigateClockOut() {
		Intent intent = new Intent(this, ClockOutActivity.class);
		intent.putExtra("employee", employee);
		intent.putExtra("latestOpenTimeCard", latestOpenTimeCard);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); 
		intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		startActivity(intent);
	}
	
	public void onSettingsClick(MenuItem sender) {
		System.out.println("Settings clicked");	
	}
}


