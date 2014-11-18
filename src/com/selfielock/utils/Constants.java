package com.selfielock.utils;

import java.util.UUID;

public class Constants {
    
    // Used for bluetooth sockets connection
    public final static UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    
    // Gender
    public static final String MAN = "Man";
    public static final String WOMAN = "Woman";
    
    // To know if the user is signed in
    public static final String IS_SIGNED_IN = "SignedIn";
    public static final String SIGNED_IN_USER_EMAIL ="SignedInUserEmail";
    
    // Used to browse images
    public static final int SELECT_PICTURE = 1;
    
    public static final String SERVER_URL = "http://104.131.44.82:8000/";
    public static final String POST_SIGNUP = "signup/";
    
}
