package com.selfielock.utils;

import java.security.SecureRandom;
import java.util.Random;

public class Password {
	
	private String password;
	private int length;
	private int strength;
	
	private static final Random RANDOM = new SecureRandom();
	
	public Password()
	{
		length = 5;
		strength = PasswordStrength.numbersOnly;
		password = GeneratePassword();
	}
	
    /***
     * 
     * @param length The number of characters the password will have (between 3 and 15 inclusively)
     * @param strength Use the static class PasswordStrength to set this value
     */
	public Password(int length, int strength)
	{		
		this.length = length;
		this.strength = strength;
		password = GeneratePassword();
	}
	
	public String GetPassword()
	{
		return password;
	}
	
	private String GeneratePassword()
	{
		String pass = "";
	    String letters = GetCharByStrength();
	    
	    // We add some limits
	    if (length < 3 || length > 15)
	    	length = 6;

	    for (int i = 0; i < length; i++)
	    {
	        int index = (int)(RANDOM.nextDouble() * letters.length());
	        pass += letters.substring(index, index+1);
	    }
		
		return pass;
		
	}
	
	private String GetCharByStrength()
	{	
		if (strength == PasswordStrength.numbersOnly)
			return "1234567890";
		else if (strength == PasswordStrength.lowerCaselettersOnly)
			return "abcdefghijklmnopqrstuvwxyz";
		else if (strength == PasswordStrength.lettersOnly)
			return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		else
			return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	}
	
	public static class PasswordStrength
	{
		public static int numbersOnly = 1;
		public static int lowerCaselettersOnly = 2;
		public static int lettersOnly = 3;
		public static int alphaNumeric = 4;
		
	};
}
