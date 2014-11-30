package com.selfielock.views;

import com.selfielock.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsPage extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	
	private Preference pref;
    private String summaryStr;
    private String newValue;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.settings_page);
        
        SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
        sharedPref.registerOnSharedPreferenceChangeListener(this);    
    }
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        //Get the current summary
        pref = findPreference(key);
        summaryStr = (String)pref.getSummary();
               
        int spaceIndex = summaryStr.indexOf(":");
        
        if (spaceIndex != -1)
        {
        	summaryStr = summaryStr.substring(0, spaceIndex).trim();
        }

        try
	    {
	        // Get the user input data
	        newValue = sharedPreferences.getString(key, "");
	
	        if (!newValue.matches(""))
	        {   	
	        	summaryStr += ": " + newValue.trim();
	        	pref.setSummary(summaryStr);
	        }
        }
        catch(Exception ex)
        {
        	// Get the user input data for boolean value
        	boolean prefixStrBool = sharedPreferences.getBoolean(key, false);
        	summaryStr += ": " + prefixStrBool;
        	pref.setSummary(summaryStr);
        }
    }
    
    public static String Read(Context context, final String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, "");
    }
 
    public static void Write(Context context, final String key, final String value) {
          SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
          SharedPreferences.Editor editor = settings.edit();
          editor.putString(key, value);
          editor.commit();        
    }
      
    public static boolean ReadBoolean(Context context, final String key, final boolean defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }
 
    public static void WriteBoolean(Context context, final String key, final boolean value) {
          SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
          SharedPreferences.Editor editor = settings.edit();
          editor.putBoolean(key, value);
          editor.commit();        
    }
}
