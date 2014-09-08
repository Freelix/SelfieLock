package com.selfielock.tabs;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.selfielock.R;
import com.selfielock.utils.ConnectionStatus;
import com.selfielock.views.LogInPage;
 
public class MainActivity extends Activity {
    // Declare Tab Variable
    ActionBar.Tab Tab1, Tab2, Tab3;
    Fragment fragmentTab1 = null;
    Fragment fragmentTab2 = null;
    Fragment fragmentTab3 = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_activity);
        
        if (ConnectionStatus.IsSignedIn())
        {
        	fragmentTab1 = new Tab1();
        	fragmentTab2 = new Tab2();
        	fragmentTab3 = new Tab3();
        	
	        ActionBar actionBar = getActionBar();
	 
	        // Hide Actionbar Icon
	        actionBar.setDisplayShowHomeEnabled(false);
	 
	        // Hide Actionbar Title
	        actionBar.setDisplayShowTitleEnabled(false);
	 
	        // Create Actionbar Tabs
	        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	 
	        // Set Tab Icon and Titles
	        Tab1 = actionBar.newTab().setText("Tab1");
	        Tab2 = actionBar.newTab().setText("Tab2");
	        Tab3 = actionBar.newTab().setText("Tab3");
	 
	        // Set Tab Listeners
	        Tab1.setTabListener(new TabListener(fragmentTab1));
	        Tab2.setTabListener(new TabListener(fragmentTab2));
	        Tab3.setTabListener(new TabListener(fragmentTab3)); 
	 
	        // Add tabs to actionbar
	        actionBar.addTab(Tab1);
	        actionBar.addTab(Tab2);
	        actionBar.addTab(Tab3);
        }
        else
        {	
        	Intent intent = new Intent(MainActivity.this, LogInPage.class);
	    	startActivity(intent);
        }
    }
}
