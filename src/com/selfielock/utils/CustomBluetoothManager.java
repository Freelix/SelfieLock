package com.selfielock.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.widget.Toast;

public class CustomBluetoothManager {
	
	private BluetoothAdapter bluetooth;
	private String status;
	
	public CustomBluetoothManager()
	{
		bluetooth = BluetoothAdapter.getDefaultAdapter();
		
		if(bluetooth != null)
		{
			if(bluetooth.isEnabled())
			{
				String mydeviceaddress = bluetooth.getAddress();
			    String mydevicename = bluetooth.getName();
			    setStatus(mydevicename + " : " + mydeviceaddress);
			}
		}
		else
		{
			//Bluetooth not supported on this device.
		}
	}
	
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static boolean IsBluetoothActivated(Context context, String blueText)
    {    	
    	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	
    	if (mBluetoothAdapter == null) 
    	{
    		Toast.makeText(context, blueText, Toast.LENGTH_SHORT).show();
    	} 
    	else 
    	{
    	    if (mBluetoothAdapter.isEnabled()) 
    	    	return true;
    	}
    	
    	return false;
    }
    
 	public static boolean setBluetooth(boolean enable) {
         BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
         boolean isEnabled = bluetoothAdapter.isEnabled();
         
         if (enable && !isEnabled) 
             return bluetoothAdapter.enable(); 
         else if(!enable && isEnabled)
             return bluetoothAdapter.disable();
         
         return true;
     }
}
