package com.selfielock.utils;

import com.selfielock.views.SettingsPage;

import android.app.Activity;
import android.os.Bundle;

public class SetSettings extends Activity {

	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		  
		  super.onCreate(savedInstanceState);
		  
		  getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new SettingsPage()).commit();
	 }
}
