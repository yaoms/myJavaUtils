package conch.yaoms.io.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import conch.yaoms.io.StreamUtils;

public class SendPostMessage {

	public byte[] send(String url, byte[] data) {
		HttpURLConnection hrc = null;
		try {
			hrc = (HttpURLConnection) new URL(url).openConnection();
			hrc.setReadTimeout(3000);// 超时设置3秒
			HttpURLConnection.setFollowRedirects(true);

			hrc.setDoInput(true);
			hrc.setDoOutput(true);
			hrc.setRequestMethod("POST");

			hrc.setUseCaches(false);
			hrc.connect();

			OutputStream os = hrc.getOutputStream();
			os.write(data);

			byte[] received = StreamUtils.getBytes(hrc.getInputStream());

			hrc.disconnect();

			return received;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
