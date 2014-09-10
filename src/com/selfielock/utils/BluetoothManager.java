/**
 * 
 */
package com.selfielock.utils;

import android.bluetooth.*;
import android.bluetooth.BluetoothAdapter;

/**
 * @author user
 *
 */
public class BluetoothManager {
	private BluetoothAdapter bluetooth;
	BluetoothManager()
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
	
    public boolean IsBluetoothActivated()
    {
    	// TODO: Implement that function
    	boolean activated = false;
    	return activated;
    }
}
