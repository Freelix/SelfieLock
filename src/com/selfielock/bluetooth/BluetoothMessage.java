package com.selfielock.bluetooth;

import java.io.DataOutputStream;
import java.io.IOException;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothMessage {
    
    private BluetoothSocket socket;    
    private String txtMsg;
    private byte[] backgroundImg;
    
    public BluetoothMessage(BluetoothSocket socket, String txtMsg, byte[] backgroundImg)
    {
        this.socket = socket;
        this.txtMsg = txtMsg;
        this.backgroundImg = backgroundImg;
    }
    
    /***********************************************/
    /****** Bluetooth - Construct the message ******/
    /***********************************************/
    
    public void constructMessages()
    {
        sendMessage(txtMsg);
        sendImage(backgroundImg);
    }
    
    private void sendMessage(String msg) 
    {
        DataOutputStream outStream;
        
        try 
        {
            outStream = new DataOutputStream(socket.getOutputStream());
            
            byte[] byteString = new String(msg + " ").getBytes();
            
            outStream.writeInt(byteString.length + 1);
            outStream.writeByte(Integer.parseInt(MessageType.CodeMessage.getType()));
            outStream.write(byteString);
            outStream.flush();
        } 
        catch (IOException e) 
        {
            Log.d("BLUETOOTH", e.getMessage());
        }
    }
    
    private void sendImage(byte[] backgroundImg) 
    {
        DataOutputStream outStream;
        
        try 
        {
            outStream = new DataOutputStream(socket.getOutputStream());

            outStream.writeInt(backgroundImg.length + 1);
            outStream.writeByte(Integer.parseInt(MessageType.BackgroundImgMessage.getType()));
            outStream.write(backgroundImg);
            outStream.flush();
        } 
        catch (IOException e) 
        {
            Log.d("BLUETOOTH", e.getMessage());
        }
    }
}
