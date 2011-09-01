package conch.yaoms.io.GPRSConnection;

import java.io.IOException;
import java.io.OutputStream;


public class GPRSOutputStream extends OutputStream {
	
	private OutputStream outputStream;
	
	private BufWriter bw;
	
	public GPRSOutputStream(OutputStream outputStream) {
		this(outputStream, 5);
	}

	public GPRSOutputStream(OutputStream outputStream, int speed) {
		this.outputStream = outputStream;
		bw = new BufWriter(speed);
	}

	@Override
	public void write(int arg0) throws IOException {
		if (bw.isfull()) {
			pushBytes();
		}
		bw.write(arg0);
	}
	
	public void write(byte[] b) throws IOException {
		for (byte c : b) {
			write(c);
		}
	}
	
	public void write(byte[] b, int off, int length) throws IOException {
		for (int i = off, j = off + length; i < j; i++) {
			write(b[i]);
		}
	}

	/**
	 * 延时写入
	 * @throws IOException
	 */
	private synchronized void pushBytes() throws IOException {
		try {
			outputStream.write(bw.toByteArray());
			bw.reset();
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void flush() throws IOException {
		pushBytes();
		super.flush();
	}
	
	public void close() throws IOException {
		flush();
		super.close();
	}

}
