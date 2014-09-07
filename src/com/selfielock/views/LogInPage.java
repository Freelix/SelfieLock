package com.selfielock.views;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.selfielock.R;

public class LogInPage extends Fragment {
	
	private Button btnProfile = null;
	private Button btnConnect = null;
	private EditText loginEmailText = null;
	private EditText loginPasswordText = null;
	private View rootView = null;
	
	public View getRootView()
	{
		return rootView;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		rootView = inflater.inflate(R.layout.login_page, container, false);	
		
	    InitialiseControls();
	  
	    return rootView;
	}
    
    private OnClickListener btnProfileListener = new OnClickListener() {
		  
	    @Override
	    public void onClick(View v) {
	    	Intent intent = new Intent(rootView.getContext(), ProfilePage.class);
	    	startActivity(intent);
	    }
    };
  
    private OnClickListener btnConnectListener = new OnClickListener() {
	  
	    @Override
	    public void onClick(View v) {		    	
	    	if (!loginEmailText.getText().toString().matches("") && !loginPasswordText.getText().toString().matches(""))
	    	{
	    		//String test = loginEmailText.getText().toString();
	    	}
	    	else
	    	{
	    		Toast.makeText(rootView.getContext(), getResources().getString(R.string.LoginError), Toast.LENGTH_SHORT).show();
	    	}
	    }
    };
  
    private void InitialiseControls()
    {  
	    if (rootView != null)
	    {
		    // Get controls
		    btnProfile = (Button) rootView.findViewById(R.id.btnProfile);
		    btnConnect = (Button) rootView.findViewById(R.id.btnConnect);
		    loginEmailText = (EditText) rootView.findViewById(R.id.loginEmailText);
		    loginPasswordText = (EditText) rootView.findViewById(R.id.loginPasswordText);
		  
		    // Assign a function to them
		    btnProfile.setOnClickListener(btnProfileListener);
		    btnConnect.setOnClickListener(btnConnectListener);
	    }
    }
}
