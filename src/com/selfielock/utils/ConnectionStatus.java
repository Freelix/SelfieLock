package com.selfielock.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ConnectionStatus {
	
    private static final String SHARED_PREF_CONNECTION_STATUS = "sharedPrefConnexionStatus";
    
    public static SharedPreferences getSharedPref(Activity activity)
    {
        return activity.getSharedPreferences(SHARED_PREF_CONNECTION_STATUS, Context.MODE_PRIVATE);
    }
	
	public static boolean IsSignedIn(Activity activity)
	{
	    return getSharedPref(activity).getBoolean(Constants.IS_SIGNED_IN, false);
	}
	
	public static String getUserSignedIn(Activity activity)
	{
        return getSharedPref(activity).getString(Constants.SIGNED_IN_USER_EMAIL, null);
	}
	
	public static void SignIn(Activity activity, String email)
	{
        SharedPreferences.Editor editor = getSharedPref(activity).edit();
        editor.putBoolean(Constants.IS_SIGNED_IN, true);
        editor.putString(Constants.SIGNED_IN_USER_EMAIL, email);
        editor.commit();    
	}
	
	public static void SignOut(Activity activity)
    {
        SharedPreferences.Editor editor = getSharedPref(activity).edit();
        editor.putBoolean(Constants.IS_SIGNED_IN, false);
        editor.commit();       
    }
}
