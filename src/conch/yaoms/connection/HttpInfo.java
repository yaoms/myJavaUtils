package conch.yaoms.connection;

public class HttpInfo {
	
	public enum E_HTTP_ERROR{
		HTTP_ERROR_INIT,      //初始化失败
		HTTP_ERROR_TIMEOUT,   //超时
		HTTP_ERROR_DATA,      //收到的数据错误
		HTTP_ERROR_CONNECT,   //连接失败
		HTTP_ERROR_FORMAT,    //报文格式错误
		HTTP_ERROR_SESSION,   //会话过期
		HTTP_ERROR_SIGN,      //签名错误
		HTTP_ERROR_SERVER     //服务端错误
	};
	
	public enum E_HTTP_STATE {
		HTTP_STATE_READY,       //就绪
		HTTP_TATE_CONNECTING,   //连接中
		HTTP_TATE_FINISHED,	    //完成
		HTTP_TATE_CANCEL    	//取消
	}
	
	public enum E_DOWN_TYPE {
		DOWN_TYPE_REQ,
		DOWN_TYPE_POLL,
		DOWN_TYPE_DOWNFILE
	}
	
	int downID;
	String url;                     //url地址
	boolean isGet;       			//是否get请求
	E_HTTP_STATE httpState;         //连接状态
	E_DOWN_TYPE type;
	byte[] sendData;              	//发送的数据
	//int sendLen;				    //发送数据长度	
	byte[] receiveData;               //收到的数据
	int receiveLen;                 //已接受数据长度
	int totalLen;					//接受数据总长度
	
	int respCode;
	IHttpListener  listener;
	
	public HttpInfo(){
		httpState = E_HTTP_STATE.HTTP_STATE_READY;
		receiveData = null;
	}
}
