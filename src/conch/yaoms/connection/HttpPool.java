package conch.yaoms.connection;

import android.os.Handler;
import android.os.Message;
import conch.yaoms.connection.HttpInfo.E_HTTP_STATE;
import conch.yaoms.message.DWMessage;
import conch.yaoms.message.DWMessageFactory;

public class HttpPool extends Thread {

	private static final int POLL_TIME = 30 * 1000; // 心跳机制

	private static final String DW_SERVER_URL = "";

	private Handler mHandler;

	private volatile boolean abort;

	private long intevalTime; // 轮询间隔时间计数

	private static HttpPool instance;

	private HttpConnect httpConnect;

	private HttpInfo heartInfo;
	private boolean startHeart;

	private E_HTTP_STATE httpState;

	private DWQueue httpQueue;
	private DWQueue backHttpQueue; // 后台连接

	private HttpPool() {
		intevalTime = 0;
		abort = false;
		startHeart = false;
		httpConnect = null;
		heartInfo = null;
		httpQueue = new DWQueue();
		backHttpQueue = new DWQueue();
		initHandler();
	}

	public static synchronized HttpPool getInstance() {
		if (instance == null) {
			instance = new HttpPool();
			instance.start();
		}
		return instance;
	}

	/**
	 * 发送消息
	 * 
	 * @param message
	 *            消息
	 * @param listener
	 *            处理监听
	 */
	public void sendMessage(DWMessage message, IHttpListener listener) {
		if (message == null) {
			return;
		}

		HttpInfo httpInfo = new HttpInfo();
		httpInfo.isGet = false;
		httpInfo.url = DW_SERVER_URL;
		httpInfo.listener = listener;
		httpInfo.sendData = message.encode();

		httpQueue.push(httpInfo);
	}

	public void testConnect() {
		HttpInfo httpInfo = new HttpInfo();
		httpInfo.isGet = true;
		httpInfo.url = "wap.baidu.com";
		httpInfo.listener = null;
		httpQueue.push(httpInfo);
	}

	public void run() {
		while (!abort) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}

			if (httpConnect != null
					&& httpState != E_HTTP_STATE.HTTP_TATE_CONNECTING) {
				System.out.println(this.getClass().getName()
						+ " httpConnect.close()!");
				// httpConnect.close();
				httpConnect = null;
			}

			if (httpConnect != null) {
				continue;
			}

			HttpInfo httpInfo = (HttpInfo) httpQueue.pop();
			if (httpInfo != null) {
				sendHttpInfo(httpInfo);
				continue;
			}

			httpInfo = (HttpInfo) backHttpQueue.pop();
			if (httpInfo != null) {
				sendHttpInfo(httpInfo);
				continue;
			}

			// 如果大于30秒，就发送心跳连接
			if (System.currentTimeMillis() - intevalTime > POLL_TIME) {
				intevalTime = System.currentTimeMillis();
				if (heartInfo != null && startHeart) {
					httpConnect = new HttpConnect(heartInfo, mHandler);
					httpConnect.run();
					System.out.println("HttpPool send HeartBeat message!");
				}
			}
		}

	}

	public void close() {
		httpConnect = null;
		heartInfo = null;
		httpQueue = null;
		backHttpQueue = null;
		mHandler = null;
	}

	private void sendHttpInfo(HttpInfo httpInfo) {
		intevalTime = System.currentTimeMillis();
		httpConnect = new HttpConnect(httpInfo, mHandler);
		httpConnect.run();
	}

	private void initHandler() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {// 此方法在ui线程运行
				HttpInfo httpInfo = (HttpInfo) msg.obj;
				switch (msg.what) {
				case HttpConnect.CONNECT_SUCCESS:

					System.out.println("receive data = "
							+ httpInfo.receiveData.toString());
					DWMessage message = DWMessageFactory
							.createReMessage(httpInfo.receiveData);

					if (httpInfo.listener != null) {
						httpInfo.listener.OnFinished(message);
					}
					break;

				case HttpConnect.CONNECT_FAIL:
					System.out.println("receive error code = "
							+ httpInfo.respCode);
					if (httpInfo.listener != null) {
						httpInfo.listener.OnError(httpInfo.respCode);
					}
					break;
				}
			}
		};
	}

}
