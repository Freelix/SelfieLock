package com.selfielock.serverCommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class PostRequest extends AsyncTask <String, Void, String> {

	private String url;
	private List<NameValuePair> postData;
	
	public PostRequest(String url,List<NameValuePair> postData) {
		this.url = url;
		this.postData = postData;
	}
	
	@Override
	protected String doInBackground(String... params) {
    	InputStream inputStream = null;
        String answer = "";
    	HttpClient httpClient = new DefaultHttpClient();
    	HttpPost post = new HttpPost(url);
    	HttpResponse httpResponse = null;
    	try {
			post.setEntity(new UrlEncodedFormEntity(postData));
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		}
    	try {
			httpResponse = httpClient.execute(post);
			HttpEntity httpEntity = httpResponse.getEntity();
			if(httpEntity != null)
			{
				answer = convertInputStreamToString(httpEntity.getContent());
			}
		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
			Log.d("POST", cpe.getLocalizedMessage());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			Log.d("POST", ioe.getLocalizedMessage());
		}
    	return answer;
	}
	
    public String POST(String url, List<NameValuePair> param)
    {
    	InputStream inputStream = null;
        String answer = "";
    	HttpClient httpClient = new DefaultHttpClient();
    	HttpPost post = new HttpPost(url);
    	HttpResponse httpResponse = null;
    	try {
			post.setEntity(new UrlEncodedFormEntity(param));
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		}
    	try {
			httpResponse = httpClient.execute(post);
			HttpEntity httpEntity = httpResponse.getEntity();
			if(httpEntity != null)
			{
				answer = convertInputStreamToString(httpEntity.getContent());
			}
		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
			Log.d("POST", cpe.getLocalizedMessage());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			Log.d("POST", ioe.getLocalizedMessage());
		}
    	return answer;
    }
    
	//TODO: A faire
    protected void onPostExecute(String result) {
        //a la fin de l'execution  
    	//Log.d("POST Responses", result);
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
