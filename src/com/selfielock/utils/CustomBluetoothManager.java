/**
 * 
 */
package com.selfielock.utils;

import com.selfielock.R;

import android.bluetooth.*;
import android.content.Context;
import android.widget.Toast;

/**
 * @author user
 *
 */
public class CustomBluetoothManager {
	
	private BluetoothAdapter bluetooth;
	
	public CustomBluetoothManager()
	{
		bluetooth = BluetoothAdapter.getDefaultAdapter();
		if(bluetooth != null)
		{
			String status = "";
		    // Continue with bluetooth setup.
			if(bluetooth.isEnabled())
			{
				String mydeviceaddress = bluetooth.getAddress();
			    String mydevicename = bluetooth.getName();
			    status = mydevicename + " : " + mydeviceaddress;
			}
		}
		else
		{
			//Bluetooth not supported on this device.
		}
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
