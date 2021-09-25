package com.gchc.ing.content.common;

/**
 * AsyncTask 관리
 *
 */
public interface HttpAsyncTaskInterface {
	
	public void onPreExecute();
	public void onPostExecute(String data);
	public void onError();
}
