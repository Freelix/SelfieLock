package com.selfielock.views;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;
import android.app.Activity;
import android.app.WallpaperManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.selfielock.R;
import com.selfielock.tabs.MainActivity;
import com.selfielock.utils.ConnectionStatus;
import com.selfielock.utils.Password;
import com.selfielock.utils.Password.PasswordStrength;
import com.selfielock.bluetooth.*;
import com.selfielock.database.StatsEntity;
import com.selfielock.database.StatsTransactions;
import com.selfielock.database.UserEntity;
import com.selfielock.database.UserTransactions;

public class LockPage extends Activity {

	private EditText codeText = null;
	private Button btnSendCode = null;
	private Button btnRageQuit = null;
	
	private Thread wallpaperThread = null;
	private Drawable actualWallPaper = null;
	private WallpaperManager wallpaper = null;
	
	private Password pass = null;
	
	private ImageView imgOtherPerson;
	private TextView secretCode;
	private TextView secretCodeOfOtherPerson = null;
	
	/*******************************/
    /*** Variables for bluetooth ***/
    /*******************************/
    
    private BluetoothSocket socket;
    private Handler handler = new Handler();;
	
    /*****************/
    /*** Functions ***/
    /*****************/
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.lock_page);
		
		String password = getIntent().getExtras().getString("Password");
		
		InitialiseControls();
		InitializeListener();
		SetupWallpaperAndPassword(password);
	}
	
	@Override
	public void onBackPressed() {
	    return;
	}
	
	@Override
    public void onDestroy() {   
        super.onDestroy();
    }
	
	private void updateStats(boolean success)
	{
	    UserTransactions ut = new UserTransactions(getApplicationContext());
        UserEntity user = ut.getUserByEmail(ConnectionStatus.getUserSignedIn(LockPage.this));
        
        StatsTransactions st = new StatsTransactions(getApplicationContext());
        StatsEntity stats = st.getStatsByUser(user);
        
        if (success)
            stats.setNumberOfWin(stats.getNumberOfWin() + 1);
        else
            stats.setNumberOfFail(stats.getNumberOfFail() + 1);
        
        stats.setTimesPlayed(stats.getTimesPlayed() + 1);
        
        st.updateUserStats(stats);
	}
	
    private OnClickListener btnSendCodeListener = new OnClickListener() {
		  
	    @Override
	    public void onClick(View v) {
	    	
	    	if (secretCodeOfOtherPerson.getText().toString().trim().equals(codeText.getText().toString().trim()))
	    	{
	    		// Stop the wallpaper thread
	    		/*wallpaperThread.interrupt();
	    		
	    		// Put back the proper background	    		
	    		try 
	    		{
	    			Bitmap bitmap = ((BitmapDrawable) actualWallPaper).getBitmap();
					wallpaper.setBitmap(bitmap);
				} 
	    		catch (IOException e) 
	    		{
					e.printStackTrace();
				}*/
	    	    
	    	    // Update stats for a user
	    	    updateStats(true);
	    		
	    		// Go back to MainPage
	    	    BlueUtility.setEndOfLockPage(true, true);
	    	    
	    	    
	    	    
		    	LockPage.this.finish();
	    	}
	    	else
	    	{
	    		Toast.makeText(LockPage.this, getResources().getString(R.string.badCode), Toast.LENGTH_SHORT).show();
	    	}
	    	
	    }
    };
    
    private OnClickListener btnRageQuitListener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            // Update stats for a user
            updateStats(false);
            
            BlueUtility.setEndOfLockPage(true, false);
            
            /*Intent newIntent = new Intent(LockPage.this, MainActivity.class);
            startActivity(newIntent);*/
            
            LockPage.this.finish();
        }
    };
    
    private void ChangeWallpaper()
    {
        try 
        {
        	wallpaper.setResource(R.drawable.ic_launcher);
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
    }
    
    private void InitialiseWallpaperThread()
    {
    	wallpaperThread = new Thread()
	    {
	        @Override
	        public void run() 
	        {
	            try 
	            {
	                while(true) 
	                {
	                    sleep(20000);
	                    ChangeWallpaper();                    
	                }
	            } 
	            catch (InterruptedException e) 
	            {
	                e.printStackTrace();
	            }
	        }
	    };
    }
    
    private void InitialiseControls()
    {  
        // Get controls
    	codeText = (EditText) findViewById(R.id.codeText);
		btnSendCode = (Button) findViewById(R.id.btnSendCode);
		btnRageQuit = (Button) findViewById(R.id.btnRageQuit);
		  
		// Assign a function to them
		btnSendCode.setOnClickListener(btnSendCodeListener);
		btnRageQuit.setOnClickListener(btnRageQuitListener);
		
		imgOtherPerson = (ImageView) findViewById(R.id.imgOtherPerson);
		secretCode = (TextView) findViewById(R.id.secretCode);
		secretCodeOfOtherPerson = (TextView) findViewById(R.id.secretCodeOfOtherPerson);
    }
    
    private void SetupWallpaperAndPassword(String password) 
    {
    	// Get the actual home wallpaper
    	wallpaper = WallpaperManager.getInstance(getApplicationContext());
    	actualWallPaper = wallpaper.getDrawable(); 
    							
    	secretCode.setText(password);
    			
    	// Switch the background image automatically. Stops when user send the right code.
    	//InitialiseWallpaperThread();
    	//wallpaperThread.start();
    }
    
    /*******************************/
    /****** Bluetooth section ******/
    /*******************************/
    
    private void InitializeListener()
    {
        //BlueUtility bluetoothUtil = (BlueUtility) getIntent().getSerializableExtra("BlueUtility");
        
        socket = BlueUtility.bts;
        
        BluetoothSocketListener bsl = new BluetoothSocketListener(socket, handler, secretCodeOfOtherPerson, imgOtherPerson);
        Thread messageListener = new Thread(bsl);
        messageListener.start();
    }
}
