package com.selfielock.tabs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.selfielock.R;
import com.selfielock.views.MainPage;
 
public class Tab1 extends Fragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) { 
    	
    	super.onCreate(savedInstanceState);
    	
    	return inflater.inflate(R.layout.home_section_page, container, false);	
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        ShowProperPage();
    }

    private void ShowProperPage()
    {
    	Fragment frag = new MainPage();

	    if (frag != null)
	    	getFragmentManager().beginTransaction().replace(R.id.layoutToReplace, frag).commit();
    }
 
}
