package android.os;

import conch.yaoms.connection.HttpInfo;

public abstract class Handler {

	public Message obtainMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}

	public Message obtainMessage(byte connectFail, HttpInfo httpInfo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public abstract void handleMessage(Message message);

}
