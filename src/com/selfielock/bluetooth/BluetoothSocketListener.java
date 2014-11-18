package com.selfielock.bluetooth;

import java.io.DataInputStream;
import java.io.IOException;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class BluetoothSocketListener implements Runnable {
    private BluetoothSocket socket;
    private TextView textView;
    private ImageView imageView;
    private Handler handler;
    
    public BluetoothSocketListener(BluetoothSocket socket,
        Handler handler, TextView textView, ImageView imageView) {
        this.socket = socket;
        this.textView = textView;
        this.imageView = imageView;
        this.handler = handler;
    }
    
    public void run() {
        try 
        {
            // Server            
            DataInputStream instream = new DataInputStream(socket.getInputStream());
            String message;
            String messageType;
            
            byte[] byteMessage = null;
            
            while (true) 
            {    
                Log.d("BLUETOOTH", "Wainting for a message.......");
                
                int length = instream.readInt();
                messageType = Byte.toString(instream.readByte());

                if (length > 0)
                {
                    byteMessage = new byte[length];
                    instream.readFully(byteMessage, 0, byteMessage.length - 1);
                }
                
                if (messageType.equals(MessageType.CodeMessage.getType())) 
                {
                    message = new String(byteMessage);
                    
                    handler.post(new MessagePoster(textView, message));
                    socket.getInputStream(); 
                }
                else if (messageType.equals(MessageType.BackgroundImgMessage.getType()))
                {
                    handler.post(new BackgroundImagePoster(imageView, byteMessage));
                    socket.getInputStream();
                }
            }
        } 
        catch (IOException e)
        {
            Log.d("BLUETOOTH_COMMS", e.getMessage());
        }
    }
}
