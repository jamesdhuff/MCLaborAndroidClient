package com.mclabor.mclaborandroidclient;

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
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ClockOutActivity extends Activity 
								implements ClockOutResultDialog.ClockOutResultDialogListener {

	private static final String NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_ACTION = "http://tempuri.org/ILaborService/";
    //private static final String URL = "http://10.0.2.2/MCLaborServer/LaborService.svc";
    private static final String URL = "http://marshallconcrete.no-ip.org/MCLaborServer/LaborService.svc";
    private static final int SOAP_VERSION = SoapEnvelope.VER11;
    	
	private Employee employee;
	private TimeCard openTimeCard;
	
    private class ClockOutTask extends AsyncTask<String, Void, Object> {     		        						        	       
        
    	private String method = "";
        private String action = "";
        
        private int employeeId = 0;
        private int laborDetailId = 0;
        
        private Activity activity;

        public ProgressDialog progressDialog;
	
	    public ClockOutTask(Activity activity, String method, int employeeId, int laborDetailId) {
	    	this.method = method;
	    	this.action = SOAP_ACTION + this.method;
	    	
	    	this.activity = activity;
	    	
	    	this.employeeId = employeeId;
	    	this.laborDetailId = laborDetailId;	    	
	    	
	    	this.progressDialog = new ProgressDialog(activity);	    	
	    }
		        
	    protected void onPreExecute() {
	    	System.out.println("ClockOutTask executing");
	    	Button clockOutBtn = (Button)findViewById(R.id.btnClockOut);
	    	clockOutBtn.setEnabled(false);
	    	
	    	this.progressDialog.show();
	    }
	    
		protected void onPostExecute(Object result) {
			   
			String resultTxt = "";
			TimeCard tc = new TimeCard();
			
			if(result != null) {
				resultTxt = result.toString();			
				tc = TimeCardFactory.getTimeCard(resultTxt);
			}

			if(this.progressDialog.isShowing()) {
				this.progressDialog.dismiss();	
			}
			
			ClockOutResultDialog resultDialog = new ClockOutResultDialog();
			Bundle args = new Bundle();
			args.putString("startTime", tc.getStartDateTime());
			args.putString("endTime", tc.getEndDateTime());
			args.putDouble("ttlHrs", tc.getTotalHours());
			resultDialog.setArguments(args);
			resultDialog.show(getFragmentManager(), "clockOutResultDialog");
		}
		
		@Override
		protected Object doInBackground(String... args) {							
					
			SoapObject request = new SoapObject(NAMESPACE, this.method);
			    		
			if(this.method.equalsIgnoreCase("doClockOutAndroid")) {
				request.addProperty("employeeId", this.employeeId);
				request.addProperty("laborDetailId", this.laborDetailId);				
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
		setContentView(R.layout.activity_clock_out);
		// Show the Up button in the action bar.
		setupActionBar();
		
		try {
			this.employee = (Employee)getIntent().getSerializableExtra("employee");
			this.openTimeCard = (TimeCard)getIntent().getSerializableExtra("latestOpenTimeCard");
		}
		catch(Exception ex) {
			System.out.println("Exception loading extras, employee and latestOpenTimeCard");
			System.out.println(ex.toString());
		}
		
		TextView txtEmpName = (TextView)findViewById(R.id.txtClockOutEmpName);
		txtEmpName.setText(employee.getFullName());
		
		TextView txtJobName = (TextView)findViewById(R.id.txtClockOutJobValue);
		txtJobName.setText(openTimeCard.getJobName());
		
		TextView txtWorkSiteName = (TextView)findViewById(R.id.txtClockOutWorkSiteValue);
		txtWorkSiteName.setText(openTimeCard.getWorkSiteName());
		
		TextView txtClockInTime = (TextView)findViewById(R.id.txtClockOutStartTimeValue);
		txtClockInTime.setText(openTimeCard.getStartDateTime());
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
		getMenuInflater().inflate(R.menu.clock_out, menu);
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
	
	
	public void btnClockOutClick(View sender) {
				
		ClockOutTask clockOutTask = new ClockOutTask(this, "doClockOutAndroid", 
										employee.getEmployeeId(), openTimeCard.getLaborDetailID());
		
		clockOutTask.execute("");
				
	}
	
	public void onClockOutResultClick(DialogFragment dlg) {
		Intent mainActIntent = new Intent(this, MainActivity.class);		
		mainActIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(mainActIntent);
	}	

}
