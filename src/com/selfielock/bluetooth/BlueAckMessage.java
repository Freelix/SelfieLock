package com.selfielock.bluetooth;

import java.io.DataOutputStream;
import java.io.IOException;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BlueAckMessage {
    
    private BluetoothSocket socket;
    
    public BlueAckMessage(BluetoothSocket socket)
    {
        this.socket = socket;
    }
    
    public void sendConnectionRequest(int message)
    {
        DataOutputStream outStream;
        
        try 
        {
            outStream = new DataOutputStream(socket.getOutputStream());
            
            outStream.writeInt(2);
            outStream.writeByte(Integer.parseInt(MessageType.ConnectionRequest.getType()));
            outStream.writeByte(message); // message
            outStream.flush();
        } 
        catch (IOException e) 
        {
            Log.d("BLUETOOTH", e.getMessage());
        }
    }
}
