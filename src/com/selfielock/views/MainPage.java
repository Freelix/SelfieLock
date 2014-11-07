package com.selfielock.views;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.selfielock.R;
import com.selfielock.achievement.CustomToast;
import com.selfielock.bluetooth.BlueUtility;
import com.selfielock.bluetooth.BluetoothMessage;
import com.selfielock.bluetooth.DiscoveryFinishThread;
import com.selfielock.bluetooth.MessageType;
import com.selfielock.utils.Constants;
import com.selfielock.utils.CustomBluetoothManager;

public class MainPage extends Fragment{
	
	private View rootView = null;
	private TextView txtOnOff = null;
	private ImageView imgOnOff = null;
	private TextView txtSearching = null;
	private TextView txtBluetoothOnOff = null;
	
	private int intBluetoothOff;
	private int intBluetoothOn;
	private int intSearchingForConnOn;
	private int intSearchingForConnOff;
	private String noBluetoothMessage = null;
	
	/*******************************/
    /*** Variables for bluetooth ***/
    /*******************************/
    
    private static BluetoothAdapter mBluetoothAdapter;
    private static int DISCOVERY_REQUEST = 1;
    
    // Waiting time (in seconds) for listening
    private int WAITING_TIME = 300;
    private static DiscoveryFinishThread threadForDiscovery;
    
    // Socket
    private BluetoothSocket socketListen;
    private BluetoothSocket socketSend;
    
    private BluetoothServerSocket btserver = null;
    
    // Progress window
    private static ProgressDialog mProgressDlg;
    
    // Found devices
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    
    // Stock the transfered text and image in those controls
    private TextView mMessageText;
    private ImageView mImage;
	
    /*****************/
    /*** Functions ***/
    /*****************/
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		rootView = inflater.inflate(R.layout.main_page, container, false);	
		
		InitializeBluetooth();
	    ConfigureLayout();

	    return rootView;
	}
	
	private OnClickListener imgOnOffListener = new OnClickListener() {
		  
	    @Override
	    public void onClick(View v) {
	    	Spanned txt = Html.fromHtml(getResources().getString(intSearchingForConnOff));
	    	
	    	if (txtSearching.getText().toString().equals(txt.toString()))
	    	{
	    	    // Start Bluetooth
	    	    //ActivateBluetooth();
	    	    
	    		// Change to Active
	    		imgOnOff.setImageResource(R.drawable.on_button);
	    		SetProperColorToMessage(txtSearching, intSearchingForConnOn);
	    		
	    		// Initialize socket for reception
	    		Intent disc = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                disc.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, WAITING_TIME);
                startActivityForResult(disc, DISCOVERY_REQUEST);
                
                // Initialize socket for sending data
                mBluetoothAdapter.startDiscovery();
                
                threadForDiscovery = new DiscoveryFinishThread(WAITING_TIME);
                threadForDiscovery.start();

                // Set the listener on the object. Created as anonymous
                threadForDiscovery.setListener(new DiscoveryFinishThread.Listener() {
                    @Override
                    public void onThreadEnding() {
                        threadForDiscovery.setIsAlive(false);
                        mBluetoothAdapter.cancelDiscovery();
                        mProgressDlg.dismiss();
                        
                        threadForDiscovery.getHandler().post(new Runnable(){
                            public void run() {
                                imgOnOff.setImageResource(R.drawable.off_button);
                                SetProperColorToMessage(txtSearching, intSearchingForConnOff);
                            }
                        });
                    }
                });
	    	}
	    	else
	    	{
	    		// Change to Inactive
	    		imgOnOff.setImageResource(R.drawable.off_button);
	    		SetProperColorToMessage(txtSearching, intSearchingForConnOff);
	    		
	    		// Cancel bluetooth discovery
	    		if (mBluetoothAdapter != null) {
	                if (mBluetoothAdapter.isDiscovering()) {
	                    mBluetoothAdapter.cancelDiscovery();
	                    try {
                            btserver.close();
                        } catch (IOException e) {
                            Log.i("BLUETOOTH", "Cannot close bluetooth socket in MainPage");
                        }
	                }
	            }
	    	}
	    }
    };

    private void InitializeControls()
    {  
	    if (rootView != null)
	    {
	    	// Get controls
	    	txtOnOff = (TextView) rootView.findViewById(R.id.textOnOffMessage);
	    	imgOnOff = (ImageView) rootView.findViewById(R.id.imgButtonOnOff);
	    	txtSearching = (TextView) rootView.findViewById(R.id.textSearchingMessage);
	    	txtBluetoothOnOff = (TextView) rootView.findViewById(R.id.bluetoothOnOff);
 	    	
	    	// Get messages
	    	intBluetoothOff = R.string.bluetoothOff;
	    	intBluetoothOn = R.string.bluetoothOn;
	    	intSearchingForConnOn = R.string.searchingForConnOn;
	    	intSearchingForConnOff = R.string.searchingForConnOff;
	    	noBluetoothMessage = getResources().getString(R.string.noBluetooth);
	    	
	    	// Assign a function to them
	    	imgOnOff.setOnClickListener(imgOnOffListener);
	    }
    }
    
    private void SetProperColorToMessage(TextView txtToChange, int strToChange)
    {
    	String strToShow = getResources().getString(strToChange);
	    txtToChange.setText(Html.fromHtml(strToShow));
    }
    
    private void ConfigureLayout()
    {
    	// Bluetooth Message
    	if (CustomBluetoothManager.IsBluetoothActivated(getActivity(), noBluetoothMessage))
    		SetProperColorToMessage(txtBluetoothOnOff, intBluetoothOn);
    	else
    		SetProperColorToMessage(txtBluetoothOnOff, intBluetoothOff);
    	
        // Connection Message
    	SetProperColorToMessage(txtSearching, intSearchingForConnOff);
    }
    
    /****************************************/
    /****** Bluetooth section - Server ******/
    /****************************************/
    
    @Override
    public void onDestroy() {
        try
        {
            getActivity().unregisterReceiver(mReceiver);
        }
        catch(Exception ex)
        {
            Log.i("BLUETOOTH", "No receiver to unregister in MainPage.onDestroy()");
        }
        
        super.onDestroy();
    }
    
    @Override
    public void onPause() {
        super.onPause();
    }
    
    @Override
    public void onResume() {
        
        if (BlueUtility.isEndOfLockPage())
        {
            // Change to Inactive
            imgOnOff.setImageResource(R.drawable.off_button);
            SetProperColorToMessage(txtSearching, intSearchingForConnOff);
            BlueUtility.setEndOfLockPage(false);
            
            // Show the appropriate toast
            String[] message = BlueUtility.getMessage();
            CustomToast toast = new CustomToast(getActivity(), message[0], message[1]);
            toast.ShowToast();
        }
        
        super.onResume();
    }

    private void InitializeBluetooth()
    {
        InitializeControls();
        
        // Initialize bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        if (mBluetoothAdapter == null) 
            ShowToast("Bluetooth is not supported on this device");
        else 
        {
            ActivateBluetooth();
        }
        
        // Initialize the searching popup
        InitializePopup();
        
        // register mReceiver
        AddReceiver();
    }
    
    private void ActivateBluetooth()
    {
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
            startActivityForResult(intent, 1000);
        }
    }
    
    private void InitializePopup()
    {
        mProgressDlg = new ProgressDialog(getActivity());
        
        mProgressDlg.setMessage("Scanning...");
        mProgressDlg.setCancelable(false);
        
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                
                mBluetoothAdapter.cancelDiscovery();
                threadForDiscovery.interrupt();
                imgOnOff.setImageResource(R.drawable.off_button);
                SetProperColorToMessage(txtSearching, intSearchingForConnOff);
            }
        });
    }
    
    private void AddReceiver()
    {
        IntentFilter filter = new IntentFilter();
        
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);
    }
    
    private void ShowToast(final String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();                
    }
    
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() 
    {
        public void onReceive(Context context, Intent intent) 
        {          
            String action = intent.getAction();
            
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                 
                if (state == BluetoothAdapter.STATE_ON) {
                    SetProperColorToMessage(txtBluetoothOnOff, intBluetoothOn);
                    ShowToast("Enabled");
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                // Searching for devices
                mDeviceList = new ArrayList<BluetoothDevice>();
                
                mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (threadForDiscovery.getIsAlive())
                {
                    if (mDeviceList.size() > 0)
                    {
                        threadForDiscovery.interrupt();
                        mProgressDlg.dismiss();
                        InitializePairing();
                    }
                    else
                    {
                        mBluetoothAdapter.startDiscovery();
                    }
                }             
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Dynamically add devices to the list
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
 
                if (BlueUtility.verifyIfPhoneHaveTheApp(device))
                    mDeviceList.add(device);
            }
        }
    };
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        if (requestCode == DISCOVERY_REQUEST) 
        {
            boolean isDiscoverable = resultCode > 0;
            
            if (isDiscoverable) 
            {
                String name = "bluetoothserver";
                try 
                {
                    // Create a bluetooth socket for listening
                    btserver = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(name, Constants.uuid);
                    
                    AsyncTask<Integer, Void, BluetoothSocket> acceptThread = new AsyncTask<Integer, Void, BluetoothSocket>() 
                    {              
                        @Override
                        protected BluetoothSocket doInBackground(Integer... params) 
                        {   
                            try
                            {
                                // Start the socket to accept a connexion (server)
                                socketListen = btserver.accept(params[0] * 1000); // timeout
                                return socketListen;
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
                                // Start the activity who locks the app
                                Intent newIntent = new Intent(getActivity(), LockPage.class);
                                
                                BlueUtility bluetoothUtil = new BlueUtility();
                                newIntent.putExtra("BlueUtility", bluetoothUtil);
                                
                                BlueUtility.bts = socketListen;
                                startActivity(newIntent);
                            }
                        }
                    };
                
                    acceptThread.execute(resultCode);
                } 
                catch (IOException e) 
                {
                    Log.d("BLUETOOTH", e.getMessage());
                }
            }
        }
    }
    
    /****************************************/
    /****** Bluetooth section - Client ******/
    /****************************************/
    
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
            socketSend = device.createInsecureRfcommSocketToServiceRecord(Constants.uuid);
            socketSend.connect();
             
            // Construct and send the message to the other device
            BluetoothMessage btMessage = new BluetoothMessage(getResources(), socketSend);
            
            btMessage.constructMessages();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
