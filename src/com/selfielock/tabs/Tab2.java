package com.selfielock.tabs;

import com.selfielock.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
 
public class Tab2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	
        View rootView = inflater.inflate(R.layout.onglet2, container, false);
        return rootView;
    }
 
}
