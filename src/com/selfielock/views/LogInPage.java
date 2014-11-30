package com.selfielock.views;

import java.util.Iterator;
import java.util.List;
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
import com.selfielock.database.UserEntity;
import com.selfielock.database.UserTransactions;
import com.selfielock.serverCommunication.RequestConstants;
import com.selfielock.serverCommunication.SerializeToJson;
import com.selfielock.tabs.MainActivity;
import com.selfielock.utils.ConnectionStatus;
import com.selfielock.utils.Cryptography;
import com.selfielock.utils.SLUtils;

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
	
	// Send all user data to server
	private void uploadUsersToServer()
	{
	    if (SLUtils.isOnline(context)) {
            UserTransactions ut = new UserTransactions(context);
            List<UserEntity> listUsers = ut.getAllUsers();
            
            for(Iterator<UserEntity> i = listUsers.iterator(); i.hasNext(); ) 
            {
                UserEntity user = i.next();
                SerializeToJson stj = new SerializeToJson(user, RequestConstants.CREATE_USER);
                stj.toJson();
            }
	    }
	}
    
    private OnClickListener btnProfileListener = new OnClickListener() {
		  
	    @Override
	    public void onClick(View v) {
	        if (SLUtils.isOnline(context)) {
    	    	Intent intent = new Intent(context, ProfilePage.class);
    	    	startActivity(intent);
    	    	
    	    	LogInPage.this.finish();
	        }
	        else
	            Toast.makeText(context, getResources().getString(R.string.noConnectionCreateProfile), Toast.LENGTH_SHORT).show();
	    }
    };
  
    private OnClickListener btnConnectListener = new OnClickListener() {
	  
	    @Override
	    public void onClick(View v) {		    	        
	        UserTransactions ut = new UserTransactions(context);
	        UserEntity user = ut.getUserByEmail(loginEmailText.getText().toString());
	        
	        String clearTextPass = null;
	        
	        try 
	        {
	            if (user != null)
	                clearTextPass = Cryptography.decrypt(Cryptography.MASTERPASSWORD, user.getPassword());
	            else {
	                if (SLUtils.isOnline(context)) {
    	                SerializeToJson stj = new SerializeToJson(loginEmailText.getText().toString(), RequestConstants.SELECT_USER);
    	                stj.toJson();
    	                user = (UserEntity) stj.toObject();
    	                
    	                if (user != null) {
    	                    clearTextPass = Cryptography.decrypt(Cryptography.MASTERPASSWORD, user.getPassword());
                            ut.AddUser(user);
    	                }
	                }
	            }
            } 
	        catch (Exception e) {
                e.printStackTrace();
            }
	        
	        if (clearTextPass != null && clearTextPass.equals(loginPasswordText.getText().toString()))
	        {
	            // Signing in
                ConnectionStatus.SignIn(LogInPage.this, loginEmailText.getText().toString());
                
                // Redirect to MainActivity
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                
                LogInPage.this.finish();
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
