package com.selfielock.tabs;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.selfielock.R;
import com.selfielock.utils.ConnectionStatus;
import com.selfielock.views.LogInPage;
import com.selfielock.views.SettingsPage;
 
public class MainActivity extends Activity {
    
    private ActionBar.Tab Tab1, Tab2, Tab3;
    
    private Fragment fragmentTab1 = null;
    private Fragment fragmentTab2 = null;
    private Fragment fragmentTab3 = null;
 
    private ActionBar actionBar = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_activity);
        
        if (ConnectionStatus.IsSignedIn(MainActivity.this))
        {
        	fragmentTab1 = new Tab1();
        	fragmentTab2 = new Tab2();
        	fragmentTab3 = new Tab3();
        	
	        actionBar = getActionBar();
	 
	        // Hide Actionbar Icon
	        actionBar.setDisplayShowHomeEnabled(false);
	 
	        // Hide Actionbar Title
	        actionBar.setDisplayShowTitleEnabled(false);
	 
	        // Create Actionbar Tabs
	        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	 
	        // Set Tab Icon and Titles
	        Tab1 = actionBar.newTab().setText(getResources().getString(R.string.mainTab1));
	        Tab2 = actionBar.newTab().setText(getResources().getString(R.string.mainTab2));
	        Tab3 = actionBar.newTab().setText(getResources().getString(R.string.mainTab3));
	 
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
	    	
	    	this.finish();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    // The options when you click on the menu button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            
            // Redirect to preferences
	        case R.id.action_settings:
	            
	        	Fragment frag = new SettingsPage();
	        	
	        	try
	        	{
		        	if (actionBar == null)
		        		actionBar = getActionBar();
		    
			        actionBar.hide();
		        	
		        	getFragmentManager().beginTransaction().replace(R.id.layoutToReplace, frag).commit();	
	        	}
	        	catch(Exception ex)
	        	{
	        		Log.i("CustomError", "Error while opening preferences");
	        		actionBar.show();
	        	}
	        	
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
        }
    }
}
