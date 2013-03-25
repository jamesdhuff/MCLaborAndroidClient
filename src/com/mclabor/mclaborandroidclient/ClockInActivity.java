package com.mclabor.mclaborandroidclient;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ClockInActivity extends Activity {

	private static final String NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_ACTION = "http://tempuri.org/ILaborService/";
    //private static final String URL = "http://10.0.2.2/MCLaborServer/LaborService.svc";
    private static final String URL = "http://marshallconcrete.no-ip.org/MCLaborServer/LaborService.svc";
    private static final int SOAP_VERSION = SoapEnvelope.VER11;
    	
	private Employee employee;
	private ArrayList<Job> jobList;
	private ArrayList<WorkSite> workSiteList;
	
    private class ClockInTask extends AsyncTask<String, Void, Object> {     		        						        	       
        
    	private String method = "";
        private String action = "";
        
        private int employeeId = 0;
        private int workSiteId = 0;
        private int jobId = 0;
        
        private Activity activity;

        public ProgressDialog progressDialog;
        
        public ClockInTask(Activity activity, String method, int employeeId, int workSiteId, int jobId) {
        	this.method = method;
        	this.action = SOAP_ACTION + this.method;
        	
        	this.activity = activity;
        	
        	this.employeeId = employeeId;
        	this.workSiteId = workSiteId;
        	this.jobId = jobId;
        	
        	this.progressDialog = new ProgressDialog(activity);
        	
        }
    	        
        protected void onPreExecute() {
        	System.out.println("ClockInTask executing");
        	Button clockInBtn = (Button)findViewById(R.id.btnClockIn);
        	clockInBtn.setEnabled(false);
        	
        	this.progressDialog.show();
        }
        
    	protected void onPostExecute(Object result) {
    		    		
    		//String resultTxt = result.toString();
    		    		
    		if(this.progressDialog.isShowing()) {
    			this.progressDialog.dismiss();	
    		}    		
    		
    		//TODO pop a dialog to show clock in result, and have the dialog reload MainActivity
    		Intent mainActIntent = new Intent(this.activity, MainActivity.class);
    		mainActIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(mainActIntent);    		
    	}
    	
    	@Override
    	protected Object doInBackground(String... args) {							
    				
    		SoapObject request = new SoapObject(NAMESPACE, this.method);
    		    		
    		if(this.method.equalsIgnoreCase("doClockInAndroid")) {
    			request.addProperty("employeeId", this.employeeId);
    			request.addProperty("workSiteId", this.workSiteId);
    			request.addProperty("jobId", this.jobId);
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clock_in);
		// Show the Up button in the action bar.
		setupActionBar();
		
		try {
			this.employee = (Employee)getIntent().getSerializableExtra("employee");
			this.workSiteList = (ArrayList<WorkSite>)getIntent().getSerializableExtra("workSiteList");
			this.jobList = (ArrayList<Job>)getIntent().getSerializableExtra("jobList");
		}
		catch(Exception ex) {
			System.out.println("Exception loading extras, employee, workSiteList and jobList");
			System.out.println(ex.toString());
		}
		
		TextView txtEmpName = (TextView)findViewById(R.id.txtClockInEmpName);
		txtEmpName.setText(employee.getFullName());		
		
		Spinner spinClockInWorkSite = (Spinner)findViewById(R.id.spinClockInWorkSite);
		ArrayAdapter spinClockInWorkSiteAdapter = new ArrayAdapter(this, R.layout.spinner_row, this.workSiteList);
		spinClockInWorkSite.setAdapter(spinClockInWorkSiteAdapter);
		
		Spinner spinClockInJob = (Spinner)findViewById(R.id.spinClockInJob);
		ArrayAdapter spinClockInJobAdapter = new ArrayAdapter(this, R.layout.spinner_row, this.jobList);
		spinClockInJob.setAdapter(spinClockInJobAdapter);

	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clock_in, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void btnClockInClick(View sender) {
		
		Spinner spinClockInWorkSite = (Spinner)findViewById(R.id.spinClockInWorkSite);
		Spinner spinClockInJob = (Spinner)findViewById(R.id.spinClockInJob);
		
		WorkSite ws = (WorkSite)spinClockInWorkSite.getSelectedItem();
		Job job = (Job)spinClockInJob.getSelectedItem();
		
		System.out.println("WorkSite = " + ws + ", Job = " + job);
				
		ClockInTask clockInTask = new ClockInTask(this, "doClockInAndroid", 
										employee.getEmployeeId(), ws.getWorkSiteId(), job.getJobId());
		
		clockInTask.execute("");
				
	}
}
