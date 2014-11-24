package com.selfielock.bluetooth;

import java.io.DataInputStream;
import java.io.IOException;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BlueAcknowledge implements Runnable {
    
    private BluetoothSocket socket;
    
    public BlueAcknowledge(BluetoothSocket socket, BluetoothSocket socketSend) {
        this.socket = socket;
    }
    
    public BlueAcknowledge(BluetoothSocket socket) {
        this.socket = socket;
    }
    
    public void run() {
        try 
        {
            // Server            
            DataInputStream instream = new DataInputStream(socket.getInputStream());
            String messageType;
            
            byte[] byteMessage = null;
            
            while (true) 
            {    
                Log.d("BLUETOOTH", "Waiting for a message.......");
                
                int length = instream.readInt();
                messageType = Byte.toString(instream.readByte());

                if (length > 0)
                {
                    byteMessage = new byte[length];
                    instream.readFully(byteMessage, 0, byteMessage.length - 1);
                }
                
                if (messageType.equals(MessageType.ConnectionRequest.getType()))
                {   
                    if (!BlueUtility.acceptConnection) {
                        BlueUtility.acceptConnection = true;
                        BlueUtility.connectionFound = 1;
                    }
                    else 
                        BlueUtility.connectionFound = 0;
                }
                
                socket.getInputStream();
            }
        } 
        catch (IOException e)
        {
            Log.d("BLUETOOTH_COMMS", e.getMessage());
        }
    }
}
