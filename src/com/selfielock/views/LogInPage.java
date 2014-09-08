package com.selfielock.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.selfielock.R;
import com.selfielock.tabs.MainActivity;
import com.selfielock.utils.ConnectionStatus;

public class LogInPage extends Activity {
	
	private Button btnProfile = null;
	private Button btnConnect = null;
	private EditText loginEmailText = null;
	private EditText loginPasswordText = null;
	
	private Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.login_page);
		context = LogInPage.this;
	    InitialiseControls();
	}
    
    private OnClickListener btnProfileListener = new OnClickListener() {
		  
	    @Override
	    public void onClick(View v) {
	    	Intent intent = new Intent(context, ProfilePage.class);
	    	startActivity(intent);
	    }
    };
  
    private OnClickListener btnConnectListener = new OnClickListener() {
	  
	    @Override
	    public void onClick(View v) {		    
	    	// TODO: Correct that part
	    	if (!loginEmailText.getText().toString().matches("") && !loginPasswordText.getText().toString().matches(""))
	    	{    		
	    		// Signing in
	    		ConnectionStatus.SetIsSignedIn(true);
	    		
	    		// Redirect to MainActivity
	    		Intent intent = new Intent(context, MainActivity.class);
		    	startActivity(intent);
	    	}
	    	else
	    	{
	    		Toast.makeText(context, getResources().getString(R.string.LoginError), Toast.LENGTH_SHORT).show();
	    	}
	    }
    };
  
    private void InitialiseControls()
    {  
		    // Get controls
		    btnProfile = (Button) findViewById(R.id.btnProfile);
		    btnConnect = (Button) findViewById(R.id.btnConnect);
		    loginEmailText = (EditText) findViewById(R.id.loginEmailText);
		    loginPasswordText = (EditText) findViewById(R.id.loginPasswordText);
		  
		    // Assign a function to them
		    btnProfile.setOnClickListener(btnProfileListener);
		    btnConnect.setOnClickListener(btnConnectListener);
    }
}
