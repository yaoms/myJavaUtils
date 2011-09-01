package conch.yaoms.io.GPRSConnection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GPRSInputStream extends InputStream {

	/**
	 * real inputStream
	 */
	private InputStream inputStream;

	private ByteArrayInputStream tmpin;

	private byte[] buf;

	public GPRSInputStream(InputStream inputStream) {
		this(inputStream, 5); // 5kbps default
	}

	public GPRSInputStream(InputStream inputStream, int speed) {
		this.inputStream = inputStream;
		tmpin = null;
		buf = new byte[speed];
	}

	@Override
	public int read() throws IOException {
		if (tmpin == null || tmpin.available() == 0) {
			getBytes();
		}
		return tmpin.read();
	}

	/**
	 * 延时读取
	 * 
	 * @throws IOException
	 */
	private synchronized void getBytes() throws IOException {
		try {
			int length = inputStream.read(buf);
			tmpin = new ByteArrayInputStream(buf, 0, length);
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int read(byte[] b) throws IOException {
		int i = -1;
		if (available() > 0) {
			for (i = 0; i < b.length; i++) {
				if (available() > 0) {
					b[i] = (byte) read();
				} else {
					break;
				}
			}
		}
		return i;
	}

	public int read(byte[] b, int off, int len) throws IOException {
		int i = -1;
		if (available() > 0) {
			for (i = 0; i < len; i++) {
				if (available() > 0) {
					b[off + i] = (byte) read();
				} else {
					break;
				}
			}
		}
		return i;
	}

	public int available() throws IOException {
		if (tmpin == null) {
			getBytes();
		}
		return inputStream.available() > 0 ? inputStream.available() : tmpin
				.available();
	}

}
