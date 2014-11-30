package com.selfielock.bluetooth;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;
import com.selfielock.achievement.Achievement;
import com.selfielock.utils.Constants;
import com.selfielock.utils.SLUtils;

public class BlueUtility implements Serializable {
    
    /***********************/
    /****** Variables ******/
    /***********************/
    
    private static final long serialVersionUID = 1L;

    // Link the MainPage with the LockPage via this socket
    public static BluetoothSocket bts;
    public static BluetoothServerSocket btss;
    
    // Indicates if we quit the lockPage
    private static boolean endOfLockPage = false;
    
    // Indicates if the user win
    private static boolean success = false;
    
    // Indicates if the user unlocked an achievement
    private static List<Achievement> achievements = null;
    
    // Show a message when user go back to MainPage
    private static String[] message = new String[2];
    
    // The actual time spent to trying to find the secret code 
    public static int actualTime;
    
    // The maximum time to wait till the user loses
    public static int maxTimeToWait;
    
    /*****************************/
    /****** Getter / Setter ******/
    /*****************************/
    
    public static boolean isEndOfLockPage()
    {
        return endOfLockPage;
    }

    public static void setEndOfLockPage(boolean end, boolean successOrNot)
    {
        endOfLockPage = end;
        success = successOrNot;
        setMessage();
    }
    
    public static boolean isSuccess()
    {
        return success;
    }
    
    public static void setAchievementUnlocked(List<Achievement> listAchievements)
    {
        achievements = listAchievements;
    }
    
    public static List<Achievement> getAchievements()
    {
        return achievements;
    }
    
    public static String[] getMessage()
    {
        return message;
    }
    
    private static void setMessage()
    {
        if (actualTime < maxTimeToWait && success)
        {
            int[] time = SLUtils.getTimeFromSeconds(actualTime);
            message[0] = "Succeed";
            message[1] = "Congratulations, it takes you " + time[0] + "m " + time[1] + "s";
        }
        else
        {
            message[0] = "Failed";
            message[1] = "Sorry, better chance next time !";
        }
    }
    
    /***********************/
    /****** Functions ******/
    /***********************/
    
    public static boolean verifyIfPhoneHaveTheApp(BluetoothDevice device)
    {
        ParcelUuid[] uuids = BlueUtility.servicesFromDevice(device);

        if (uuids != null)
        {
            for (ParcelUuid id: uuids) {
                Log.d("BLUETOOTH", "UUID: " + id.getUuid().toString());
                
                if (Constants.uuid.compareTo(id.getUuid()) == 0)  
                {
                    Log.i("BLUETOOTH", "Device name: " + device.getName());
                   
                    return true;
                }
            }
        }
        
        return false;
    }
    
    @SuppressWarnings("rawtypes")
    private static ParcelUuid[] servicesFromDevice(BluetoothDevice device) 
    {
        try 
        {
            // Using reflexion because it supports older APIs
            Class<?> cl = Class.forName("android.bluetooth.BluetoothDevice");
            Class[] par = {};
            Method method = cl.getMethod("getUuids", par);
            Object[] args = {};
            ParcelUuid[] retval = (ParcelUuid[]) method.invoke(device, args);
            return retval;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    }
}
