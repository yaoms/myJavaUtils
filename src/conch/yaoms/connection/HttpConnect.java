package conch.yaoms.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import android.os.Message;

public class HttpConnect extends Thread {

	public static int HTTP_TimeOut = 30 * 1000;

	public static final byte CONNECT_SUCCESS = 1;

	public static final byte CONNECT_FAIL = 0;

	private HttpInfo httpInfo;

	public HttpInfo getHttpInfo() {
		return httpInfo;
	}

	public void setHttpInfo(HttpInfo httpInfo) {
		this.httpInfo = httpInfo;
	}

	public byte getConCount() {
		return conCount;
	}

	public void setConCount(byte conCount) {
		this.conCount = conCount;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Handler getmHandler() {
		return mHandler;
	}

	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	private byte conCount = 0;

	private boolean cancel = false;

	private boolean finished = false;

	private Handler mHandler;

	public HttpConnect(HttpInfo httpInfo, Handler handler) {
		this.httpInfo = httpInfo;
		this.mHandler = handler;
	}

	private void connect() throws IOException {

		OutputStream os = null;
		InputStream is = null;

		try {
			URL url = new URL(httpInfo.url);
			HttpURLConnection hrc = (HttpURLConnection) url.openConnection();
			hrc.setReadTimeout(HTTP_TimeOut);// 超时设置
			HttpURLConnection.setFollowRedirects(true);

			if (httpInfo.isGet) {
				hrc.setRequestMethod("GET");
			} else {
				hrc.setDoInput(true);
				hrc.setDoOutput(true);
				hrc.setRequestMethod("POST");
			}

			hrc.setUseCaches(false);
			hrc.connect();

			if (httpInfo.isGet) {
				os = hrc.getOutputStream();
				os.write(httpInfo.sendData);
			}

			httpInfo.respCode = hrc.getResponseCode();
			Message msg = mHandler.obtainMessage();

			if (httpInfo.respCode == HttpURLConnection.HTTP_OK) {
				int length = hrc.getContentLength();
				is = hrc.getInputStream();
				httpInfo.receiveData = receiveData(is, length);
				msg.what = CONNECT_SUCCESS;
			} else {
				msg.what = CONNECT_FAIL;
			}

			msg.obj = httpInfo;
			mHandler.sendMessage(msg);

			hrc.disconnect();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			httpInfo.respCode = 4444;
			mHandler.obtainMessage(CONNECT_FAIL, httpInfo);
		} finally {
			if (os != null) {
				os.close();
			}

			if (is != null) {
				is.close();
			}
		}
	}

	/******************************************************************
	 * 功能描述: 从input中读取size个字节的数据<br>
	 ******************************************************************* */
	private byte[] receiveData(InputStream input, int size) throws IOException {

		if (input == null || size <= 0) {
			return null;
		}

		int pos = 0; // 目前读取到的位置
		int reads; // 每次读取的byte数

		byte[] datas = new byte[size];

		while (!cancel) {
			int len = datas.length - pos; // 未读取的字节数
			len = len > 1024 ? 1024 : len; // 每次可读取的字节数
			if (len <= 0)
				break;
			reads = input.read(datas, pos, len);

			if (reads <= 0)
				break;
			pos += reads;
		}

		return datas;
	}

	public void run() {
		try {
			connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
