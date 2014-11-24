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
    
    public void sendConnectionRequest()
    {
        DataOutputStream outStream;
        
        try 
        {
            String msg = "1";
            outStream = new DataOutputStream(socket.getOutputStream());
            
            byte[] byteString = new String(msg + " ").getBytes();
            
            outStream.writeInt(byteString.length + 1);
            outStream.writeByte(Integer.parseInt(MessageType.ConnectionRequest.getType()));
            outStream.write(byteString);
            outStream.flush();
        } 
        catch (IOException e) 
        {
            Log.d("BLUETOOTH", e.getMessage());
        }
    }
}
