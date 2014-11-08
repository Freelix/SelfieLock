package com.selfielock.utils;

public class ConnectionStatus {
	
	private static boolean isSignedIn = false;
	
	public static boolean IsSignedIn()
	{
		// TODO: Make a request to a file/server to know if the user is signed in.
		// For the purpose of testing, we put this in a static variable
		return isSignedIn;
	}
	
	public static void SetIsSignedIn(boolean nisSignedIn)
	{
		isSignedIn = nisSignedIn;
	}
}
