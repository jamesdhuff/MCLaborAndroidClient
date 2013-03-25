package com.mclabor.mclaborandroidclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ClockOutResultDialog extends DialogFragment {		
	
    public interface ClockOutResultDialogListener {        
    	public void onClockOutResultClick(DialogFragment dialog);
    }
	
    ClockOutResultDialogListener mListener;
    
    @Override    
    public void onAttach(Activity activity) {        
    	super.onAttach(activity);        
    	// Verify that the host activity implements the callback interface        
    	try {            
    		// Instantiate the NoticeDialogListener so we can send events to the host            
    		mListener = (ClockOutResultDialogListener) activity;        
    	} 
    	catch (ClassCastException e) {            
    		// The activity doesn't implement the interface, throw exception            
    		throw new ClassCastException(activity.toString() + " must implement ClockOutResultDialogListener");        
    	}    
    }    	
    
	@Override    
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		Bundle args = this.getArguments();
				
		String startTime = args.getString("startTime");
		String endTime = args.getString("endTime");
		String ttlHrs = String.format("%1$,.2f", args.getDouble("ttlHrs"));
		
		// Use the Builder class for convenient dialog construction        
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());        
		builder.setMessage("Time Card Details...\n\nClock In:     " + startTime + "\nClock Out:  " + endTime + "\n\nTotal Hours: " + ttlHrs + "\n")               
		.setPositiveButton("Continue", 
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener.onClockOutResultClick(ClockOutResultDialog.this);
					}
				});
		
		// Create the AlertDialog object and return it        
		return builder.create();    
	}
}	
