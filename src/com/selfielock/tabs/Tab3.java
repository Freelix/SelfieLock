package com.selfielock.tabs;


import java.util.Iterator;
import java.util.List;

import com.selfielock.achievement.Achievement;
import com.selfielock.achievement.AchievementAdapter;
import com.selfielock.database.AchievementCollection;
import com.selfielock.database.AchievementTransactions;
import com.selfielock.database.UserEntity;
import com.selfielock.database.UserTransactions;
import com.selfielock.serverCommunication.RequestConstants;
import com.selfielock.serverCommunication.SerializeToJson;
import com.selfielock.utils.ConnectionStatus;
import com.selfielock.utils.SLUtils;
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
import android.widget.Toast;
import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
 
public class Tab3 extends Fragment {
	
	private ListView achvlistView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        View rootView = inflater.inflate(R.layout.achievement_page, container, false);
        
        // Request to database
        AchievementTransactions at = new AchievementTransactions(getActivity());
        
        UserTransactions ut = new UserTransactions(getActivity());   
        UserEntity user = ut.getUserByEmail(ConnectionStatus.getUserSignedIn(getActivity()));
        
        // Get achievements to show for a user
        AchievementCollection ac = at.getAllAchievementsByUserEmail(user.getEmail());
        
        if (ac.getListAchievements().size() == 0 && SLUtils.isOnline(getActivity()))
        {        
            // Select achievements
            SerializeToJson stj = new SerializeToJson(user.getEmail(), RequestConstants.SELECT_ACHIEVEMENTS);
            stj.toJson();
            List<Achievement> list = (List<Achievement>) stj.toObject();
            
            // Save it to local database here
            ac = new AchievementCollection(user.getEmail(), list);
            at.AddAchievementToUser(user, ac);
        }
        
        if (ac.getListAchievements().size() > 0) 
        {
            Achievement[] achievementArray = new Achievement[ac.getListAchievements().size()];
            
            int position = 0;
            for(Iterator<Achievement> i = ac.getListAchievements().iterator(); i.hasNext(); ) {
                Achievement item = i.next();
                achievementArray[position] = item;
                position++;
            }
            
            AchievementAdapter adapter = new AchievementAdapter(this.getActivity().getApplicationContext(), 
                    R.layout.achievement_listview_item_row, achievementArray, ac);    
            
            achvlistView = (ListView) rootView.findViewById(R.id.listViewAchievement);
             
            //View header = (View)getLayoutInflater().inflate(R.layout.achievement_listview_header_row, null);
            View headerView = inflater.inflate(R.layout.achievement_listview_header_row, null);
            achvlistView.addHeaderView(headerView);
            
            achvlistView.setAdapter(adapter);
        }
        else
            Toast.makeText(getActivity(), getResources().getString(R.string.noConnectionAchievements), Toast.LENGTH_SHORT).show();
        
        return rootView;
    }
}
