package com.selfielock.bluetooth;

import android.os.Handler;

public class DiscoveryFinishThread extends Thread {
    
    private int timeToWait;
    private int actualTime;
    private boolean isAlive;
    
    // Post to the UI Thread
    private Handler handler;
    
    public Listener mListener;
    
    public DiscoveryFinishThread(int timeToWait) 
    {
        this.timeToWait = timeToWait;
        this.actualTime = 0;
        this.handler = new Handler();
        BlueUtility.maxTimeToWait = this.timeToWait;
    }  
    
    // Getter, Setter
    public boolean getIsAlive()
    {
        return isAlive;
    }
    
    public void setIsAlive(boolean isAlive)
    {
        this.isAlive = isAlive;
    }
    
    public Handler getHandler()
    {
        return handler;
    }
    
    // Listener for update values at the end of the thread
    public interface Listener 
    {
         public void onThreadEnding();
    }

    public void setListener(Listener listener) 
    {
         mListener = listener;
    }
    
    @Override
    public void interrupt()
    {
        isAlive = false;
        super.interrupt();
    }

    @Override
    public void run()
    {
        actualTime = 0;
        isAlive = true;
        
        while (timeToWait > actualTime && isAlive)
        {
            try 
            {
                Thread.sleep(1000);
                actualTime += 1;
                BlueUtility.actualTime = this.actualTime;
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
        
        if (isAlive)
            mListener.onThreadEnding();
    }

}
