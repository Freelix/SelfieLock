package com.selfielock.views;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.os.Parcelable;
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
import com.selfielock.achievement.Achievement;
import com.selfielock.achievement.CustomToast;
import com.selfielock.achievement.CustomToastPosition;
import com.selfielock.bluetooth.BlueAckMessage;
import com.selfielock.bluetooth.BlueAcknowledge;
import com.selfielock.bluetooth.BlueUtility;
import com.selfielock.bluetooth.BluetoothMessage;
import com.selfielock.bluetooth.DiscoveryFinishThread;
import com.selfielock.database.UserEntity;
import com.selfielock.database.UserTransactions;
import com.selfielock.utils.ConnectionStatus;
import com.selfielock.utils.Constants;
import com.selfielock.utils.CustomBluetoothManager;
import com.selfielock.utils.Password;
import com.selfielock.utils.Password.PasswordStrength;

public class MainPage extends Fragment{
	
	private View rootView = null;
	private TextView txtOnOff = null;
	private ImageView imgOnOff = null;
	private TextView txtSearching = null;
	private TextView txtBluetoothOnOff = null;
	private TextView textUser = null;
	private Button btnLogOut = null;
	
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
    private BluetoothSocket socketListen = null;
    private BluetoothSocket socketSend;
    
    private BluetoothServerSocket btserver = null;
    
    // Progress window
    private static ProgressDialog mProgressDlg;
    private static ProgressDialog mWaitingDlg;
    
    // Found devices
    private ArrayList<BluetoothDevice> mDeviceList;
    
    // Stock the transfered text and image in those controls
    private TextView mMessageText;
    private ImageView mImage;
    
    // Password
    private Password password;
    
    private AsyncTask<Integer, Void, BluetoothSocket> acceptThread;
	
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
	    	    if (!mBluetoothAdapter.isEnabled())
	    	        ActivateBluetooth();
	    	    
	    	    // Initialize password
	            password = new Password(4, PasswordStrength.numbersOnly);
	    	    
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
                
                // Create a bluetooth socket for listening
                String name = "bluetoothserver";
                try {
                    btserver = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(name, Constants.uuid);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
	    	}
	    	else
	    	{  
	    	    closeConnection();
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
    
    private OnClickListener btnLogOutListener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            
            // Log out
            ConnectionStatus.SignOut(getActivity());
            
            // Redirect to LogInPage
            Intent intent = new Intent(getActivity(), LogInPage.class);
            startActivity(intent);
            
            getActivity().finish();
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
	    	btnLogOut = (Button) rootView.findViewById(R.id.btnLogOut);
	    	
	    	// Show the message at the top of the screen
	    	textUser = (TextView) rootView.findViewById(R.id.textUser);
	    	UserTransactions ut = new UserTransactions(getActivity());
	    	UserEntity user = ut.getUserByEmail(ConnectionStatus.getUserSignedIn(getActivity()));
	    	textUser.setText(getResources().getString(R.string.textUser) + " " + user.getFirstName() + " " + user.getLastName());
 	    	
	    	// Get messages
	    	intBluetoothOff = R.string.bluetoothOff;
	    	intBluetoothOn = R.string.bluetoothOn;
	    	intSearchingForConnOn = R.string.searchingForConnOn;
	    	intSearchingForConnOff = R.string.searchingForConnOff;
	    	noBluetoothMessage = getResources().getString(R.string.noBluetooth);
	    	
	    	// Assign a function to them
	    	imgOnOff.setOnClickListener(imgOnOffListener);
	    	btnLogOut.setOnClickListener(btnLogOutListener);
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
    
    private void closeConnection()
    {
        try { 
            Log.i("BLUETOOTH", "Closing connection");
            btserver.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            if (socketListen != null)
                socketListen.close();
            if (socketSend != null)
                socketSend.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
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
            closeConnection();
            
            threadForDiscovery.interrupt();
            
            try {
                socketListen.close();
                socketSend.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            
            // Change to Inactive
            imgOnOff.setImageResource(R.drawable.off_button);
            SetProperColorToMessage(txtSearching, intSearchingForConnOff);
            
            // Show the appropriate toast
            String[] message = BlueUtility.getMessage();
            CustomToast toast;
            
            if (BlueUtility.isSuccess())
                toast = new CustomToast(getActivity(), message[0], message[1], R.drawable.success, true);
            else
                toast = new CustomToast(getActivity(), message[0], message[1], R.drawable.failed, false);
            
            toast.ShowToast();
            
            if (BlueUtility.getAchievements() != null) { 
                for(Iterator<Achievement> i = BlueUtility.getAchievements().iterator(); i.hasNext(); ) {
                    Achievement item = i.next();
                    
                    CustomToast toastAch = new CustomToast(getActivity(), "Achievement !", item.getDescription(), R.drawable.success, true);
                    toastAch.setPosition(CustomToastPosition.PositionTop.getPosition());
                    toastAch.ShowToast();
                    
                    try {
                        Thread.sleep(Toast.LENGTH_LONG);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }        
            }
            
            // Reinitialize the endOfLockPage
            BlueUtility.setEndOfLockPage(false, false);
            BlueUtility.setAchievementUnlocked(null);
        }
        
        super.onResume();
    }
    
    private void startTimer()
    {
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
                
                closeConnection();
                mBluetoothAdapter.cancelDiscovery();
                threadForDiscovery.interrupt();
                imgOnOff.setImageResource(R.drawable.off_button);
                SetProperColorToMessage(txtSearching, intSearchingForConnOff);
                
            }
        });
        
        mWaitingDlg = new ProgressDialog(getActivity());
        
        mWaitingDlg.setMessage("Establishing connexion...");
        mWaitingDlg.setCancelable(false);
        
        mWaitingDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                
                closeConnection();
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
                BlueUtility.connectionFound = -1;
                BlueUtility.acceptConnection = false;
                
                if (threadForDiscovery.getIsAlive())
                {
                    if (mDeviceList.size() > 0)
                    {
                        boolean sessionValid = true;
                        //int numberOfTime = 0;
                        int position = 0;
                        BluetoothDevice device = mDeviceList.get(position);
                        
                        try {
                            new ListenForConnection().execute();
                            socketSend = device.createInsecureRfcommSocketToServiceRecord(Constants.uuid);
                            socketSend.connect();
                            
                            BlueAckMessage bam = new BlueAckMessage(socketSend);
                            bam.sendConnectionRequest();
                            
                            while (BlueUtility.connectionFound == -1)
                            {
                                try {
                                    Thread.sleep(1000);
                                    //numberOfTime++;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            
                            /*if (numberOfTime > 30)
                                sessionValid = false;*/
                        } catch (IOException e) {
                            sessionValid = false;
                            e.printStackTrace();
                        }
                        
                        /*try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        
                        if (sessionValid)
                        {
                            thr.interrupt();
                            try {
                                socketSend.close();
                                socketListen.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            
                            if (BlueUtility.connectionFound == 1)
                            {
                                acceptThread.execute();
                                //threadForDiscovery.interrupt();
                                mProgressDlg.dismiss();
                                
                                InitializePairing();
                            }
                            else
                                mBluetoothAdapter.startDiscovery();
                        }
                        else 
                            mBluetoothAdapter.startDiscovery();
                    }
                    else
                        mBluetoothAdapter.startDiscovery();
                }             
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Dynamically add devices to the list
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Method getUuidsMethod = null;
                try {
                    getUuidsMethod = device.getClass().getMethod("getUuids", null);
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                ParcelUuid[] uuids = null;
                try {
                    uuids = (ParcelUuid[]) getUuidsMethod.invoke(device, null);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                for (ParcelUuid uuid: uuids) {
                    Log.d("BLUEEEE", "UUID: " + uuid.getUuid().toString());
                    
                    if (Constants.uuid.compareTo(uuid.getUuid()) == 0)  
                    {
                        Log.i("BLUETOOTH", "Device name: " + device.getName());
                        mDeviceList.add(device);
                    }
                }
                
                /*if (BlueUtility.verifyIfPhoneHaveTheApp(device)){
                    mDeviceList.add(device);
                }*/
            }
        }
    };
    
    private Thread thr;

    private class ListenForConnection extends AsyncTask<Void, Integer, BluetoothSocket> {

       @Override
       protected BluetoothSocket doInBackground(Void... params) {
           try {
               socketListen = btserver.accept(); //  3 sec
               BlueAcknowledge ba = new BlueAcknowledge(socketListen, socketSend);
               thr = new Thread(ba);
               thr.start();
           } 
           catch (IOException e) {
               e.printStackTrace();
               return null;          
           }
           
           return socketListen;
       }
   }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        if (requestCode == DISCOVERY_REQUEST) 
        {
            boolean isDiscoverable = resultCode > 0;
            
            if (isDiscoverable) {
            
                acceptThread = new AsyncTask<Integer, Void, BluetoothSocket>() 
                {              
                    @Override 
                    protected BluetoothSocket doInBackground(Integer... params) 
                    {   
                        try {
                            socketListen = btserver.accept();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
  
                        return socketListen;
                    }
                    
                    @Override
                    protected void onPostExecute(BluetoothSocket result) 
                    {        
                        if (result != null)
                        {
                            mWaitingDlg.dismiss();
                            
                            startTimer();
                            
                            // Start the activity who locks the app
                            Intent newIntent = new Intent(getActivity(), LockPage.class);
                            
                            BlueUtility bluetoothUtil = new BlueUtility();
                            newIntent.putExtra("BlueUtility", bluetoothUtil);
                            newIntent.putExtra("Password", password.GetPassword());
                            
                            BlueUtility.bts = socketListen;
                            startActivity(newIntent);
                        }
                    }
                };
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
            socketSend = device.createInsecureRfcommSocketToServiceRecord(Constants.uuid);
            socketSend.connect();
            
            UserTransactions ut = new UserTransactions(getActivity());
            UserEntity user = ut.getUserByEmail(ConnectionStatus.getUserSignedIn(getActivity()));
            byte[] profileImage = user.getImage();
             
            // Construct and send the message to the other device
            BluetoothMessage btMessage = new BluetoothMessage(getResources(), socketSend, password.GetPassword(), profileImage);
            
            btMessage.constructMessages();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
