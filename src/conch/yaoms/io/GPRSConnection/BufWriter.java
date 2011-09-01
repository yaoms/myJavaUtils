package conch.yaoms.io.GPRSConnection;

import java.io.ByteArrayOutputStream;

public class BufWriter extends ByteArrayOutputStream {

	private int speed;
	
	public BufWriter(int speed) {
		this.speed = speed;
	}
	
	public boolean isfull() {
		if (this.size()>= speed) {
			return true;
		} else {
			return false;
		}
	}

}
