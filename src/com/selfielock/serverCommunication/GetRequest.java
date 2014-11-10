package com.selfielock.serverCommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class GetRequest extends AsyncTask <String, Void, String> {

	private String url;
	public GetRequest(String url) {
		this.url = url;
	}
	@Override
	protected String doInBackground(String... params) {
		InputStream inputStream = null;
        String answer = "";
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();;
            if(httpEntity != null)
            {
                answer = convertInputStreamToString(httpEntity.getContent());
            }
            else
            {
                answer = "Did not work!";
            }
        } catch (Exception e) {
            Log.d("GET", e.getLocalizedMessage());
        }

        return answer;

	}

	//TODO: A faire
    protected void onPostExecute(String result) {
        //a la fin de l'execution  
    	//Log.d("GET Responses", result);
    }
    
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
