package com.selfielock.achievement;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.selfielock.R;

public class CustomToast {
	
	private String title;
	private String description;
	private int imageRessId = 0;
	private Context context;
	private int backGroundColor;
	private String position;
	
	private TextView textTitle = null;
	private TextView textDescription = null;
	private ImageView imageToast = null;
	
	public CustomToast(Context context, String title, String description)
	{
		this.context = context;
		this.title = title;
		this.description = description;
		this.backGroundColor = 0xFF00FF00;
		this.position = CustomToastPosition.PositionBottom.getPosition();
	}
	
	public CustomToast(Context context, String title, String description, int image, boolean success)
	{
		this.context = context;
		this.title = title;
		this.description = description;
		this.imageRessId = image; // Example : R.drawable.medal_silver
		this.position = CustomToastPosition.PositionBottom.getPosition();
		
		if (success)
		    this.backGroundColor = 0xFF00FF00;
		else
		    this.backGroundColor = 0xFFFF0000;
	}
	
	public void setPosition(String position)
	{
	    this.position = position;
	}
	
	public void ShowToast()
	{
		Activity activity = (Activity) context;
		
		// Retrieve the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.custom_toast_layout_id));
        
        layout.setBackgroundColor(backGroundColor);
        layout.invalidate();
		
        // Retrieve the textView
        textTitle = (TextView) layout.findViewById(R.id.textTitle);
        textDescription = (TextView) layout.findViewById(R.id.textDescription);
        
        // Set the proper text
        textTitle.setText(title);
        textDescription.setText(description);
        
        if (imageRessId != 0)
        {
        	imageToast = (ImageView) layout.findViewById(R.id.imageToast);
        	imageToast.setImageResource(imageRessId);
        }

        Toast toast =new Toast(context);
        toast.setView(layout);
        
        toast.setDuration(Toast.LENGTH_LONG);
        
        setGravity(toast).show();
	}
	
	private Toast setGravity(Toast toast)
	{
	    if (position == CustomToastPosition.PositionBottom.getPosition())
	        toast.setGravity(Gravity.BOTTOM, 0, 0);
	    else if (position == CustomToastPosition.PositionTop.getPosition())
            toast.setGravity(Gravity.TOP, 0, 0);
	    
	    return toast;
	}
}
