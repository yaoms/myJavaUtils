package conch.yaoms.connection;

import conch.yaoms.message.DWMessage;

public interface IHttpListener {
	
	abstract void OnFinished(DWMessage msg);
		
	abstract void OnError(int errorCode);
}
