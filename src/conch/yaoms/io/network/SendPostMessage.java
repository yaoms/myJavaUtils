package conch.yaoms.io.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import conch.yaoms.io.StreamUtils;

public class SendPostMessage {

	public byte[] send(String url, byte[] data) {
		try {
			URLConnection connection = new URL(url).openConnection();
			connection.setDoOutput(true);
			OutputStream os = connection.getOutputStream();
			os.write(data);
			os.flush();
			os.close();
			return StreamUtils.getBytes(connection.getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
