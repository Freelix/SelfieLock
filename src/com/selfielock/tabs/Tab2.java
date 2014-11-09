package com.selfielock.tabs;

import com.selfielock.R;
import com.selfielock.database.StatsEntity;
import com.selfielock.database.StatsTransactions;
import com.selfielock.database.UserEntity;
import com.selfielock.database.UserTransactions;
import com.selfielock.utils.ConnectionStatus;
import com.selfielock.utils.Constants;
import com.selfielock.utils.SLUtils;
import com.selfielock.views.ProfilePage;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
 
public class Tab2 extends Fragment {
    
    private View rootView = null;
    
    // Profile
    private ImageView statsImgProfile = null;
    private TextView statsLabelName = null;
    private TextView statsLabelEmail = null;
    
    // Stats
    private TextView labelStatsSucceed = null;
    private TextView labelStatsFailed = null;
    private TextView labelStatsTimesPlayed = null;
    private TextView labelStatsNumberAchievements = null;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	
        rootView = inflater.inflate(R.layout.stats, container, false);
        
        InitializeControls();
        
        return rootView;
    }
    
    private void InitializeControls()
    {  
        // Get controls
        statsImgProfile = (ImageView) rootView.findViewById(R.id.statsImgProfile);
        statsLabelName = (TextView) rootView.findViewById(R.id.statsLabelName);
        statsLabelEmail = (TextView) rootView.findViewById(R.id.statsLabelEmail);
        labelStatsSucceed = (TextView) rootView.findViewById(R.id.labelStatsSucceed);
        labelStatsFailed = (TextView) rootView.findViewById(R.id.labelStatsFailed);
        labelStatsTimesPlayed = (TextView) rootView.findViewById(R.id.labelStatsTimesPlayed);
        labelStatsNumberAchievements = (TextView) rootView.findViewById(R.id.labelStatsNumberAchievements);
      
        // Assign a function to them
        statsImgProfile.setOnClickListener(statsImgProfileListener);
        
        FillLabels();
    }
    
    private void FillLabels()
    {
        UserTransactions ut = new UserTransactions(getActivity());
        UserEntity user = ut.getUserByEmail(ConnectionStatus.getUserSignedIn(getActivity()));
        
        // Set elements for profile
        if (user != null)
        {
            byte[] image = user.getImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            
            statsImgProfile.setImageBitmap(bmp);
            statsLabelName.setText(user.getFirstName() + " " + user.getLastName());
            statsLabelEmail.setText(user.getEmail());
            
            // Set elements for stats
            StatsTransactions st = new StatsTransactions(getActivity());
            StatsEntity statsForUser = st.getStatsByUser(user);
            
            if (statsForUser != null)
            {
                // Set elements for stats
                labelStatsSucceed.setText(getResources().getText(R.string.numberOfWin) + " " + statsForUser.getNumberOfWin());
                labelStatsFailed.setText(getResources().getText(R.string.numberOfLose) + " " + statsForUser.getNumberOfFail());
                labelStatsTimesPlayed.setText(getResources().getText(R.string.timesPlayed) + " " + statsForUser.getTimesPlayed());
                labelStatsNumberAchievements.setText(getResources().getText(R.string.numberOfAchievements) + " " + statsForUser.getAchievementsUnlocked());
            }
        }
    }
    
    private OnClickListener statsImgProfileListener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {       
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a profile picture"), Constants.SELECT_PICTURE);
        }
    };
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                statsImgProfile.setImageURI(selectedImageUri);
                
                UserTransactions ut = new UserTransactions(getActivity());
                UserEntity user = ut.getUserByEmail(ConnectionStatus.getUserSignedIn(getActivity()));
                user.setImage(SLUtils.ImageToByte(statsImgProfile));
                ut.updateUser(user);
            }
        }
    }
 
}
