package com.gchc.ing.question.common;

/**
 * Created by Administrator on 2015-05-11.
 */
public class Record
{
	private static final long serialVersionUID = -2873344754738311951L;
	private EServerAPI eServerAPI = null;
	private byte[] data = null;
	private Object requestData = null;
	private NetworkListener networkListener = null;
	private Object returnData = null;
	private String charSet = "UTF-8";
	public int stateCode = 0;

	public EServerAPI getEServerAPI()
	{
		return eServerAPI;
	}

	public void setEServerAPI(EServerAPI eServerAPI)
	{
		this.eServerAPI = eServerAPI;
	}

	public Object getRequestData()
	{
		return requestData;
	}

	public void setRequestData(Object requestData)
	{
		this.requestData = requestData;
	}

	public NetworkListener getNetworkListener()
	{
		return networkListener;
	}

	public void setNetworkListener(NetworkListener networkListener)
	{
		this.networkListener = networkListener;
	}

	public byte[] getData()
	{
		return data;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

	public Object getReturnData()
	{
		return returnData;
	}

	public void setReturnData(Object returnData)
	{
		this.returnData = returnData;
	}

	public String getCharSet()
	{
		return charSet;
	}

	public void setCharSet(String charSet)
	{
		this.charSet = charSet;
	}
}
