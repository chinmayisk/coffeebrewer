package com.saakshin.coffee;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.os.Bundle;


import android.bluetooth.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;


public class MainActivity extends ActionBarActivity {
	BluetoothAdapter bluetooth;
	BluetoothSocket btSocket;
	OutputStream outStream;
	DataOutputStream os;
	Button triggerbutton;
	Button Alarmbutton;
	AlertDialog alertDialog;
	String laptopaddress, deviceaddress;
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //set bluetoothadapter 
        bluetooth = BluetoothAdapter.getDefaultAdapter();
       
         //button click handlers 
         buttonClickHandler();
         
         //setting address; 
         laptopaddress = "A8:BB:CF:11:D5:1F";
         deviceaddress = "98:D3:31:30:17:30";
        
    }
 
	public void buttonClickHandler(){
    	      
    	triggerbutton = (Button) findViewById(R.id.triggerbutton);
    	triggerbutton.setOnClickListener(new OnClickListener() {
    		 
		public void onClick(View arg0) {
			
	       	//bluetooth enabled code 	       	       
			        if (bluetooth.isEnabled()) {
			        	
			     
			        	Connect();
			        }
			        else
			        {
			        	createAlertDialog();
			        	alertDialog.show();
			          
			        }
			}
 
		});
    	Alarmbutton = (Button)findViewById(R.id.laterbutton);
    	Alarmbutton.setOnClickListener(new OnClickListener() {	 
		public void onClick(View arg0) {
			Intent newpage = new Intent(getBaseContext(),LaterActivity.class);
			startActivity(newpage);
			}
		});
    }
    @SuppressWarnings("deprecation")
	private void createAlertDialog() {
    	alertDialog = new AlertDialog.Builder(this).create();
    	alertDialog.setTitle("Bluetooth Status");
	       
        alertDialog.setMessage("Please Switch on Bluetooth");
	    alertDialog.setButton("OK", new DialogInterface.OnClickListener() { 

				@Override
				public void onClick(final DialogInterface arg0, final int arg1) {
					alertDialog.dismiss();
					
				}
	     });
		
	}
	public void Connect() {
    	
		BluetoothDevice device = bluetooth.getRemoteDevice(deviceaddress);
		Log.d("", "Connecting to ... " + device);
		//bluetooth.cancelDiscovery();
		 Method m;

	        try{
	           
	            m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});

	            BluetoothSocket clientSocket =  (BluetoothSocket) m.invoke(device, 1);

	            clientSocket.connect();

	            os = new DataOutputStream(clientSocket.getOutputStream());

	            new clientSock().start();
	        } catch (Exception e) {
	            e.printStackTrace();
	            Log.e("BLUETOOTH", e.getMessage());
	        }
	    }
		
    public class clientSock extends Thread {
        public void run () {
            try {
                os.writeBytes("13"); // anything you want
                os.flush();
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
        }
    }
		
}
  

