package com.selfielock.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GeolocationManager implements LocationListener{
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;
	private LocationManager locationManager;
	private Context context;

	private Location actualLocation = null;
	private double lat = 0, lon = 0;
	
	private boolean isGPSEnabled = false;
	private boolean isNetworkEnabled = false;
	private boolean locationAvailable = true;
	
	public GeolocationManager(Context context)
	{
		this.context = context;

		getCurrentLocation();

	}
	
	private void initManager()
	{
        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        Log.v("isGPSEnabled", "=" + isGPSEnabled);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.v("isNetworkEnabled", "=" + isNetworkEnabled);
		
	}
	
	public Location getCurrentLocation()
	{		
		actualLocation = null;
		locationAvailable = false;
		
		try
		{
			initManager();
			
			if(isGPSEnabled)
			{
				actualLocation = fetchLocation(LocationManager.GPS_PROVIDER);
				locationAvailable = true;
			}
			else if(isNetworkEnabled)
			{
				actualLocation = fetchLocation(LocationManager.NETWORK_PROVIDER);
				locationAvailable = true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return actualLocation;
	}
	
	public String getStringFormatedLocation() {		
		return getCurrentLocation().toString();
	}
	
	private Location fetchLocation(String method)
	{
        Location location=null;
		if(method.equals(LocationManager.GPS_PROVIDER) ||
				method.equals(LocationManager.NETWORK_PROVIDER))
		{

	        locationManager.requestLocationUpdates(
	                method,
	                MINIMUM_TIME_BETWEEN_UPDATES,
	                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);
	        Log.d("Location method", method);
	        if (locationManager != null) {
	            location = locationManager
	                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	            if (location != null) {
	                lat = location.getLatitude();
	                lon = location.getLongitude();
	            }
	        }
		}
		return location;
	}
	
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GeolocationManager.this);
        }
    }

    /*public double getLatitude() {
        if (actualLocation != null) {
            lat = actualLocation.getLatitude();
        }
        return lat;
    }

    public double getLongitude() {
        if (actualLocation != null) {
            lon = actualLocation.getLongitude();
        }
        return lon;
    }*/

    public boolean isGeolocationAvailable() {
        return this.locationAvailable;
    }
	

	@Override
	public void onLocationChanged(Location location) {
		// Auto-generated method stub
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Auto-generated method stub	
	}
	@Override
	public void onProviderEnabled(String provider) {
		// Auto-generated method stub	
	}
	@Override
	public void onProviderDisabled(String provider) {
		// Auto-generated method stub	
	}
}
