package conch.yaoms.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

	public static byte[] getBytes(InputStream inputStream) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[256];
		int len = 0;
		while ((len = inputStream.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}

		return baos.toByteArray();
	}

}
