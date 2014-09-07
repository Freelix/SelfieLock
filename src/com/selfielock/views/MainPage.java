package com.selfielock.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selfielock.R;

public class MainPage extends Fragment{
	
	private View rootView = null;
	
	public View getRootView()
	{
		return rootView;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		rootView = inflater.inflate(R.layout.main_page, container, false);	
		
	    InitialiseControls();
	  
	    return rootView;
	}
  
    private void InitialiseControls()
    {  
	    if (rootView != null)
	    {
		  
	    }
    }
}
