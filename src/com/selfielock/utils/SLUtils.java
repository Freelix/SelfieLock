package com.selfielock.utils;

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
    
}
