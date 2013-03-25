package com.mclabor.mclaborandroidclient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class InvalidLoginDialog extends DialogFragment {				
    
    
	@Override    
	public Dialog onCreateDialog(Bundle savedInstanceState) {						
		
		// Use the Builder class for convenient dialog construction        
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());        
		builder.setMessage("Invalid Login ID.  Please try again.")
		.setPositiveButton("OK", 
				new DialogInterface.OnClickListener() {                   
					public void onClick(DialogInterface dialog, int id) {					
					}               
				});  
		
		// Create the AlertDialog object and return it        
		return builder.create();    
	}
}	
