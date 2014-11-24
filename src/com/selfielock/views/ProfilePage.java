package com.selfielock.views;

import com.selfielock.R;
import com.selfielock.database.UserEntity;
import com.selfielock.database.UserTransactions;
import com.selfielock.tabs.MainActivity;
import com.selfielock.utils.ConnectionStatus;
import com.selfielock.utils.Constants;
import com.selfielock.utils.Cryptography;
import com.selfielock.utils.SLUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ProfilePage extends Activity {
    
    private ImageView imgProfile;
    private EditText textFirstName;
    private EditText textLastName;
    private EditText textEmail;
    private EditText textPassword;
    
    private RadioGroup groupRadioSexe;
    private RadioButton groupRadioSexeBtn;
    
    private Button btnSaveProfile;
    
    private Context context;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.profile_page);
	    
	    InitializeControls();
	}
	
	@Override
    public void onBackPressed() {
	    // Redirect to LoginPage
        Intent intent = new Intent(context, LogInPage.class);
        startActivity(intent);
        
        super.onBackPressed();
    }
	  
	private void InitializeControls()
	{  
	    // Get context
	    context = ProfilePage.this;
	    //context.deleteDatabase("SelfieLock.db");
	    
        // Get controls
	    imgProfile = (ImageView) findViewById(R.id.imgProfile);
	    textFirstName = (EditText) findViewById(R.id.textFirstName);
	    textLastName = (EditText) findViewById(R.id.textLastName);
	    textEmail = (EditText) findViewById(R.id.textEmail);
	    textPassword = (EditText) findViewById(R.id.textPassword);
	    
	    groupRadioSexe = (RadioGroup) findViewById(R.id.groupRadioSexe);
	    
	    btnSaveProfile = (Button) findViewById(R.id.btnSaveProfile);
      
        // Assign a function to them
	    btnSaveProfile.setOnClickListener(saveProfileListener);
	    imgProfile.setOnClickListener(imgProfileListener);
        
	}
	
	private OnClickListener saveProfileListener = new OnClickListener() {
	      
        @Override
        public void onClick(View v) {
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.RelativeLayoutProfilePage);
            
            if (SLUtils.IsFormCompleted(viewGroup))
            {
                if (SLUtils.IsEmailValid(textEmail.getText().toString()))
                {
                    byte[] byteImage = null;
                    String encryptedPassword = null;
                    
                    try {
                        byteImage = SLUtils.ImageToByte(imgProfile);
                        encryptedPassword = Cryptography.encrypt(Cryptography.MASTERPASSWORD, textPassword.getText().toString());
                    } catch (Exception e) {
                        Log.d("ProfilePage", "Problem with ImageToByte " + e.getMessage());
                    }
                    
                    if (encryptedPassword != null && byteImage != null)
                    {
                        int selectedId = groupRadioSexe.getCheckedRadioButtonId();
                        groupRadioSexeBtn = (RadioButton) findViewById(selectedId);
                        
                        // We create the transaction context
                        UserTransactions ut = new UserTransactions(context);
                        
                        // We check if this email already exists
                        if (ut.getUserByEmail(textEmail.getText().toString()) == null)
                        {
                            UserEntity newUser = new UserEntity(textFirstName.getText().toString(),
                                    textLastName.getText().toString(),
                                    groupRadioSexeBtn.getText().toString(),
                                    textEmail.getText().toString(),
                                    byteImage,
                                    encryptedPassword);
                            
                            ut.AddUser(newUser);
                            
                            // TODO: Add an email confirmation with a custom message
                            
                            // Signing in
                            ConnectionStatus.SignIn(ProfilePage.this, textEmail.getText().toString());
                            
                            // Redirect to MainActivity
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            
                            ProfilePage.this.finish();
                        }
                        else
                            Toast.makeText(context, getResources().getString(R.string.EmailAlreadyExist), Toast.LENGTH_SHORT).show(); 
                    }
                }
                else
                    Toast.makeText(context, getResources().getString(R.string.EmailNotValid), Toast.LENGTH_SHORT).show();             
            }
            else
                Toast.makeText(context, getResources().getString(R.string.SaveError), Toast.LENGTH_SHORT).show();
        }
    };
    
    private OnClickListener imgProfileListener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {       
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select a profile picture"), Constants.SELECT_PICTURE);
        }
    };
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                imgProfile.setImageURI(selectedImageUri);
            }
        }
    }
}
