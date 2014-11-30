package com.selfielock.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.commons.validator.routines.EmailValidator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
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
    
    /** Read the object from Base64 string. */
    public static Object fromString( String s ) throws IOException ,
                                                        ClassNotFoundException {
         byte [] data = Base64.decode(s, 0);
         ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
         Object o  = ois.readObject();
         ois.close();
         return o;
    }

     /** Write the object to a Base64 string. */
     public static String toString( Serializable o ) throws IOException {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         ObjectOutputStream oos = new ObjectOutputStream( baos );
         oos.writeObject( o );
         oos.close();
         return new String( Base64.encode(baos.toByteArray(), 0));
     }
     
     public static boolean isOnline(Context context) {
         ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo netInfo = cm.getActiveNetworkInfo();
         return netInfo != null && netInfo.isConnectedOrConnecting();
     }
}
