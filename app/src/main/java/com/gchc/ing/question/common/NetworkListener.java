package com.gchc.ing.question.common;

public abstract class NetworkListener
{
	public abstract void onResponse(Record a_oRecord);
    public abstract void onNetworkException(Record a_oRecord);
}
