package com.selfielock.tabs;


import com.selfielock.achievement.Achievement;
import com.selfielock.achievement.AchievementAdapter;
import com.selfielock.R;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
 
public class Tab3 extends Fragment {
	
	private ListView achvlistView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        View rootView = inflater.inflate(R.layout.achievement_page, container, false);
        
        Achievement achievement_data[] = new Achievement[]
        {
            new Achievement(R.drawable.medal_silver, "Achievement 1"),
            new Achievement(R.drawable.medal_silver, "Achievement 2"),
            new Achievement(R.drawable.medal_silver, "Achievement 3"),
            new Achievement(R.drawable.medal_silver, "Achievement 4"),
            new Achievement(R.drawable.medal_silver, "Achievement 5")
        };
        
        AchievementAdapter adapter = new AchievementAdapter(this.getActivity().getApplicationContext(), 
                R.layout.achievement_listview_item_row, achievement_data);    
        
        achvlistView = (ListView) rootView.findViewById(R.id.listViewAchievement);
         
        //View header = (View)getLayoutInflater().inflate(R.layout.achievement_listview_header_row, null);
        View headerView = inflater.inflate(R.layout.achievement_listview_header_row, null);
        achvlistView.addHeaderView(headerView);
        
        achvlistView.setAdapter(adapter);
        
        return rootView;
    }
}
