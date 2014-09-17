package com.selfielock.views;

import java.io.IOException;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.selfielock.R;
import com.selfielock.tabs.MainActivity;
import com.selfielock.utils.Password;
import com.selfielock.utils.Password.PasswordStrength;

public class LockPage extends Activity {

	private EditText codeText = null;
	private Button btnSendCode = null;
	
	private Thread wallpaperThread = null;
	private Drawable actualWallPaper = null;
	private WallpaperManager wallpaper = null;
	
	private Password pass = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.lock_page);
		
		InitialiseControls();
		SetupWallpaperAndPassword();
	}
	
	@Override
	public void onBackPressed() {    
	    return;
	}
	
    private OnClickListener btnSendCodeListener = new OnClickListener() {
		  
	    @Override
	    public void onClick(View v) {
	    	
	    	if (pass.GetPassword().equals(codeText.getText().toString().trim()))
	    	{
	    		// Stop the wallpaper thread
	    		wallpaperThread.interrupt();
	    		
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
		    	startActivity(intent);
		    	
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
}
