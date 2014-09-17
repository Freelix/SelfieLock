package com.selfielock.views;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.selfielock.R;
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

	Button btnTemp = null;
	
	public View getRootView()
	{
		return rootView;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		rootView = inflater.inflate(R.layout.main_page, container, false);	
		
	    InitialiseControls();
	    ConfigureLayout();

	    return rootView;
	}
	
	private OnClickListener imgOnOffListener = new OnClickListener() {
		  
	    @Override
	    public void onClick(View v) {
	    	Spanned txt = Html.fromHtml(getResources().getString(intSearchingForConnOff));
	    	
	    	if (txtSearching.getText().toString().equals(txt.toString()))
	    	{ 
	    		// Change to Active
	    		imgOnOff.setImageResource(R.drawable.on_button);
	    		SetProperColorToMessage(txtSearching, intSearchingForConnOn);
	    		
	    		// TODO: Initialise Bluetooth Search
	    	}
	    	else
	    	{
	    		// Change to Inactive
	    		imgOnOff.setImageResource(R.drawable.off_button);
	    		SetProperColorToMessage(txtSearching, intSearchingForConnOff);
	    	}
	    }
    };
    
    private OnClickListener btnTempListener = new OnClickListener() {
		  
	    @Override
	    public void onClick(View v) {    	
	    	Intent intent = new Intent(getActivity(), LockPage.class);
	    	startActivity(intent);
	    	
	    	getActivity().finish();
	    }
    };
  
    private void InitialiseControls()
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
	    	
	    	btnTemp = (Button) rootView.findViewById(R.id.btnTemp);
	    	btnTemp.setOnClickListener(btnTempListener);
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
}
