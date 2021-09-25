package com.gchc.ing.content.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

import com.gchc.ing.food.HttpAsyncTaskInterface;

/**
 * AsyncTask 관리
 *
 */
public class HttpAsyncTask extends AsyncTask<String, Void, String> {
	
	private HttpAsyncTaskInterface atv;
	private String url = "";
	private String param = "";

	public HttpAsyncTask(HttpAsyncTaskInterface atv, String url) {
		this.atv = atv;
		this.url = url;
	}
		
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}



	@Override
	protected void onPreExecute() {
		atv.onPreExecute();
	}

	@Override
	protected String doInBackground(String... urls) {
		return getJSONData(); 
	}

	@Override
	protected void onPostExecute(String data)  {
//		if(Utils.TEST){
//			Log.e(Utils.TAG, "async url : "+url);
//			Log.e(Utils.TAG, "async data : "+data);
//		}
		atv.onPostExecute(data);
	}
	
	
	
    public String getJSONData() {
		URL mURL = null;
		HttpURLConnection mHURLConn = null;
		int mIntResponse = 0;
		String resultOfQuery = "";
		try{
			mURL = new URL(url);
			mHURLConn = (HttpURLConnection)mURL.openConnection();
			mHURLConn.setConnectTimeout(30000); 
			mHURLConn.setRequestMethod("POST");
			mHURLConn.setDoOutput(true);
			
			OutputStream out_stream = mHURLConn.getOutputStream();
		    out_stream.write( param.getBytes("EUC-KR") );
		    out_stream.flush();
		    out_stream.close();			
			mIntResponse = mHURLConn.getResponseCode();
		} catch(Exception e) {
			if(Utils.TEST)
				Log.d("hsh", "getJSONData ex"+e);
			mIntResponse = 1000;
		}
		
		if(mIntResponse == HttpURLConnection.HTTP_OK) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(mHURLConn.getInputStream()));
				for (;;)
				{
					String line = null;
					try {
						line = br.readLine();							
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (line == null) break;
					resultOfQuery +=line;
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			atv.onError();
		}
		return resultOfQuery;
	} 	
	
}
