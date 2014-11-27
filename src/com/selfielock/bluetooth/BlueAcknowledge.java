package com.selfielock.bluetooth;

import java.io.DataInputStream;
import java.io.IOException;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BlueAcknowledge implements Runnable {
    
    private BluetoothSocket socket;
    private BluetoothSocket socketSend;
    private boolean valid;
    
    public BlueAcknowledge(BluetoothSocket socket, BluetoothSocket socketSend) {
        this.socket = socket;
        this.socketSend = socketSend;
        this.valid = false;
    }
    
    public void run() {
        try 
        {
            // Server            
            DataInputStream instream = new DataInputStream(socket.getInputStream());
            String messageType;
            String message;
            
            while (true) 
            {    
                Log.d("BLUETOOTH", "Waiting for a message.......");
                
                int length = instream.readInt();
                messageType = Byte.toString(instream.readByte());
                message = Byte.toString(instream.readByte());
                
                if (messageType.equals(MessageType.ConnectionRequest.getType()))
                {   
                    if (!BlueUtility.acceptConnection) {
                        BlueUtility.acceptConnection = true;
                        BlueUtility.connectionFound = 1;
                        
                        BlueAckMessage bam = new BlueAckMessage(socketSend);
                        bam.sendConnectionRequest(1);
                    }
                    else 
                        BlueUtility.connectionFound = 0;
                }
                
                /*if (messageType.equals(MessageType.ConnectionRequest.getType()))
                {   
                    
                    if (message.equals("1") && !BlueUtility.acceptConnection)
                    {
                        BlueAckMessage bam = new BlueAckMessage(socketSend);
                        bam.sendConnectionRequest(2);
                    }
                    else if (message.equals("2") && !BlueUtility.acceptConnection) {
                        BlueUtility.acceptConnection = true;
                        BlueUtility.connectionFound = 1;
                    }
                    else 
                        BlueUtility.connectionFound = 0;
                }*/
                
                //socket.getInputStream();
            }
        } 
        catch (IOException e)
        {
            Log.d("BLUETOOTH_COMMS", e.getMessage());
        }
    }
}
