package com.selfielock.bluetooth;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import android.bluetooth.BluetoothSocket;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.selfielock.R;

public class BluetoothMessage {
    
    private Resources resources;
    private BluetoothSocket socket;
    
    public BluetoothMessage(Resources resources, BluetoothSocket socket)
    {
        this.resources = resources;
        this.socket = socket;
    }
    
    /***********************************************/
    /****** Bluetooth - Construct the message ******/
    /***********************************************/
    
    public void constructMessages()
    {
        // TODO: Change for random
        String txtMsg = "12345"; 
        byte[] backgroundImg = ImageToByte(R.drawable.success);
        
        sendMessage(txtMsg);
        sendImage(backgroundImg);
    }
    
    private byte[] ImageToByte(int imageId)
    {   
        Bitmap bitmap = BitmapFactory.decodeResource(resources, imageId);     
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); 
        byte[] b = baos.toByteArray();
        return b;
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
