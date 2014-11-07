package com.selfielock.bluetooth;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class BackgroundImagePoster implements Runnable {
    private ImageView imageView;
    private byte[] message;
    
    public BackgroundImagePoster(ImageView imageView, byte[] message) 
    {
        this.imageView = imageView;
        this.message = message;
    }
    
    public void run()
    {
        Bitmap bmp = BitmapFactory.decodeByteArray(message, 0, message.length);
        imageView.setImageBitmap(bmp);
    }
}
