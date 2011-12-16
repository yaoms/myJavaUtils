package conch.yaoms.tools.socket.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import conch.yaoms.tools.IBackgroundThread;
import conch.yaoms.tools.IServerMonitor;

public class SocketDebugServer extends Thread implements IServerMonitor,
		IBackgroundThread {

	/**
	 * 监控器
	 */
	private IServerMonitor monitor;


	/**
	 * 运行开关
	 */
	private boolean running = true;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SocketDebugServer().start();
	}

	private SocketDebugServer() {
		this.monitor = this;
	}

	public SocketDebugServer(IServerMonitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void run() {
		try {
			// 初始化服务器
			ServerSocket ssock = new ServerSocket(51234);
			monitor.trace("Listhenning at " + ssock.getLocalPort());
			Socket socket;
			String remoteIP;
			while (running) {
				// 干正事的地方
				socket = ssock.accept();// 阻塞

				remoteIP = socket.getInetAddress().getHostAddress();
				monitor.trace("Remote IP: " + remoteIP);

				// 读取输入
				InputStream inputStream = socket.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
				byte[] buf = new byte[256];
				int length = 0;
				while ((length = inputStream.read(buf)) != -1) {
					baos.write(buf, 0, length);
					if (inputStream.available() == 0) {
						break;
					}
				}

				String requeString = new String(baos.toByteArray());
				monitor.trace("\n" + requeString + "\n");
				baos.reset();

				PrintStream printStream = new PrintStream(
						socket.getOutputStream());
				printStream.print("HTTP/1.1 200 OK\r\n");
				printStream.print("Server: conch-yaoms/1.0\r\n");
				printStream.print("Date: " + new Date() + "\r\n");
				printStream.print("Content-Type: text/plain\r\n");
				printStream.print("Connection: close\r\n");
				printStream.print("\r\n");
				printStream.print("Your name is jerry.\r\n");

				socket.close();
			}
			// 清理服务器
			ssock.close();
			monitor.trace("Local Port " + ssock.getLocalPort() + " released.");
		} catch (IOException e) {
			monitor.trace(e.getMessage());
		}
	}

	@Override
	public void trace(String msg) {
		System.out.println(new Date() + " " + msg);
	}

	@Override
	public void shutdown() {
		this.running = false;
	}

}
