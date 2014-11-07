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
import com.selfielock.utils.Password;
import com.selfielock.utils.Password.PasswordStrength;
import com.selfielock.bluetooth.*;

public class LockPage extends Activity {

	private EditText codeText = null;
	private Button btnSendCode = null;
	
	private Thread wallpaperThread = null;
	private Drawable actualWallPaper = null;
	private WallpaperManager wallpaper = null;
	
	private Password pass = null;
	
	private ImageView imgOtherPerson;
	private TextView secretCode;
	
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
		
		
		InitialiseControls();
		Initialize();
		//SetupWallpaperAndPassword();
	}
	
	@Override
	public void onBackPressed() {
	    return;
	}
	
	@Override
    public void onDestroy() {   
        super.onDestroy();
    }
	
    private OnClickListener btnSendCodeListener = new OnClickListener() {
		  
	    @Override
	    public void onClick(View v) {
	    	
	    	if (secretCode.getText().toString().trim().equals(codeText.getText().toString().trim()))
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
				}
	    		
	    		// Go back to MainPage
	    		Intent intent = new Intent(LockPage.this, MainActivity.class);
	    		// TODO: putExtra that will show a message in a special box on the MainPage
		    	startActivity(intent);*/
	    	    
	    	    
	    	    BlueUtility.setEndOfLockPage(true);
		    	
		    	LockPage.this.finish();
	    	}
	    	else
	    	{
	    		Toast.makeText(LockPage.this, getResources().getString(R.string.badCode), Toast.LENGTH_SHORT).show();
	    	}
	    	
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
	                    sleep(10000);
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
		  
		// Assign a function to them
		btnSendCode.setOnClickListener(btnSendCodeListener);
		
		imgOtherPerson = (ImageView) findViewById(R.id.imgOtherPerson);
		secretCode = (TextView) findViewById(R.id.secretCode);
    }
    
    private void SetupWallpaperAndPassword()
    {
    	// Get the actual home wallpaper
    	wallpaper = WallpaperManager.getInstance(getApplicationContext());
    	actualWallPaper = wallpaper.getDrawable();
    			
    	// Temporary initialise a password
    	pass = new Password(9, PasswordStrength.lowerCaselettersOnly);				
    	codeText.setText(pass.GetPassword());
    			
    	// Switch the background image automatically. Stops when user send the right code.
    	InitialiseWallpaperThread();
    	wallpaperThread.start();
    }
    
    /*******************************/
    /****** Bluetooth section ******/
    /*******************************/
    
    private void Initialize()
    {
        BlueUtility bluetoothUtil = (BlueUtility) getIntent().getSerializableExtra("BlueUtility");
        
        socket = BlueUtility.bts;
        
        BluetoothSocketListener bsl = new BluetoothSocketListener(socket, handler, secretCode, imgOtherPerson);
        Thread messageListener = new Thread(bsl);
        messageListener.start();
    }
}
