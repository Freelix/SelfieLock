package com.selfielock.utils;

import java.io.ByteArrayOutputStream;

import org.apache.commons.validator.routines.EmailValidator;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

public class SLUtils {
    
    /***
     * 
     * @param timeInSeconds The number of seconds
     * @return Minutes and seconds in a array
     */
    public static int[] getTimeFromSeconds(int timeInSeconds)
    {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds - minutes * 60;
        
        int[] ints = {minutes , seconds};
        
        return ints;
    }
    
    /***
     * 
     * @param imageId The image from layout
     * @param resources From which activity
     * @return Serialized image
     */
    public static byte[] ImageToByte(int imageId, Resources resources)
    {   
        Bitmap bitmap = BitmapFactory.decodeResource(resources, imageId);     
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
        return baos.toByteArray();
    }
    
    public static byte[] ImageToByte(ImageView image)
    {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.PNG, 100, baos);  
        return baos.toByteArray();
    }
    
    public static boolean IsFormCompleted(ViewGroup v)
    {
        for (int i = 0; i < v.getChildCount(); i++)
        {
            Object child = v.getChildAt(i);
            
            if (child instanceof EditText)
            {
                EditText e = (EditText) child;
                
                if (e.getText().length() == 0)
                    return false;
            }
            else if (child instanceof RadioGroup)
            {
                RadioGroup rb = (RadioGroup) child;
                
                if (rb.getCheckedRadioButtonId() < 0)
                    return false;
            }
        }
        
        return true;
    }
    
    public static boolean IsEmailValid(String email)
    {
        return EmailValidator.getInstance().isValid(email);
    }
}
