package com.mclabor.mclaborandroidclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class LoginConfirmationDialog extends DialogFragment {		
	
    public interface LoginConfirmationDialogListener {        
    	public void onDialogPositiveClick(DialogFragment dialog);        
    	public void onDialogNegativeClick(DialogFragment dialog);    
    }
	
    LoginConfirmationDialogListener mListener;
    
    @Override    
    public void onAttach(Activity activity) {        
    	super.onAttach(activity);        
    	// Verify that the host activity implements the callback interface        
    	try {            
    		// Instantiate the NoticeDialogListener so we can send events to the host            
    		mListener = (LoginConfirmationDialogListener) activity;        
    	} 
    	catch (ClassCastException e) {            
    		// The activity doesn't implement the interface, throw exception            
    		throw new ClassCastException(activity.toString()                    + " must implement NoticeDialogListener");        
    	}    
    }    	
    
	@Override    
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		Bundle args = this.getArguments();
				
		String empName = args.getString("empName");			
		
		// Use the Builder class for convenient dialog construction        
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());        
		builder.setMessage("Are you " + empName + "?")               
		.setPositiveButton("Yes", 
				new DialogInterface.OnClickListener() {                   
					public void onClick(DialogInterface dialog, int id) {
						mListener.onDialogPositiveClick(LoginConfirmationDialog.this);
					}               
				})               
		.setNegativeButton("No", 
				new DialogInterface.OnClickListener() {                   
					public void onClick(DialogInterface dialog, int id) {
						mListener.onDialogNegativeClick(LoginConfirmationDialog.this);
					}
				});  
		
		// Create the AlertDialog object and return it        
		return builder.create();    
	}
}	

