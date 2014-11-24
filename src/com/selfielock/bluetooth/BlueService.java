package com.selfielock.bluetooth;

import java.io.IOException;
import java.util.ArrayList;

import com.selfielock.database.UserEntity;
import com.selfielock.database.UserTransactions;
import com.selfielock.utils.ConnectionStatus;
import com.selfielock.utils.Constants;
import com.selfielock.utils.Password;
import com.selfielock.views.LockPage;

import android.app.Activity;
import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class BlueService extends IntentService {
    
    private BluetoothServerSocket btserver = null;
    private BluetoothAdapter mBluetoothAdapter;
    private AsyncTask<Integer, Void, BluetoothSocket> acceptThread;
    private BluetoothSocket socketListen;
    private BluetoothSocket socketSend;
    private Password password;
    private ArrayList<BluetoothDevice> mDeviceList;
    private Activity activity;
    
    public BlueService() {
        super("BlueIntentService");
    }
    
    
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
        //Bundle extras = intent.getExtras(); 
        //if(extras != null)
        //{
            //password = (Password) extras.getSerializable("pass");
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mDeviceList = BlueUtility.mDeviceList;
            activity = BlueUtility.act;
        //}
    }

    @Override
    protected void onHandleIntent(Intent intent) {
     // Create a bluetooth socket for listening
        /*String name = "bluetoothserver";
        try {
            btserver = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(name, Constants.uuid);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        acceptThread = new AsyncTask<Integer, Void, BluetoothSocket>() 
        {              
            @Override 
            protected BluetoothSocket doInBackground(Integer... params) 
            {   
                try
                {
                    // Start the socket to accept a connexion (server)
                    //while (true) {
                        socketListen = btserver.accept();
                        //socketListen = btserver.accept(params[0] * 1000); // timeout
                        return socketListen;
                    //}
                } 
                catch (IOException e)
                {
                    Log.d("BLUETOOTH", e.getMessage());
                }
    
                return null; 
            }
            
            @Override
            protected void onPostExecute(BluetoothSocket result) 
            {        
                if (result != null)
                {
                    //mWaitingDlg.dismiss();
                    
                    // Start the activity who locks the app
                    Intent newIntent = new Intent(getBaseContext(), LockPage.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    
                    BlueUtility bluetoothUtil = new BlueUtility();
                    newIntent.putExtra("BlueUtility", bluetoothUtil);
                    newIntent.putExtra("Password", password.GetPassword());
                    
                    BlueUtility.bts = socketListen;
                    startActivity(newIntent);
                }
            }
        };*/
        
        InitializePairing();
    }

    private void InitializePairing()
    {
        // TODO: Make a random
        int position = 0;
        BluetoothDevice device = mDeviceList.get(position);
        pairDevice(device);
    }
    
    private void pairDevice(BluetoothDevice device) 
    {
        try 
        {
            // Client
            //while(true) {
                socketSend = device.createInsecureRfcommSocketToServiceRecord(Constants.uuid);
                socketSend.connect();
                
                UserTransactions ut = new UserTransactions(getBaseContext());
                UserEntity user = ut.getUserByEmail(ConnectionStatus.getUserSignedIn(activity));
                byte[] profileImage = user.getImage();
                 
                // Construct and send the message to the other device
                BluetoothMessage btMessage = new BluetoothMessage(getResources(), socketSend, password.GetPassword(), profileImage);
                
                btMessage.constructMessages();
            //}
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

}
