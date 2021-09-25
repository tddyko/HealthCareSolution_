package com.gchc.ing.food;

public interface HttpAsyncTaskInterface {
	
	public void onPreExecute();
	public void onPostExecute(String data);
	public void onError();
	public void onFileUploaded(String result);
}
