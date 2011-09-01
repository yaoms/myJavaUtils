package conch.yaoms.utils.jmeter.javarequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import conch.yaoms.io.GPRSConnection.GPRSInputStream;
import conch.yaoms.io.GPRSConnection.GPRSOutputStream;
import conch.yaoms.utils.codecs.Response;

public class TestJmeterRequest extends AbstractJavaSamplerClient {

	private static Logger log = LoggingManager.getLoggerForClass();

	private static String label = "FunboxClientTest";

	@Override
	public SampleResult runTest(JavaSamplerContext arg0) {

		SampleResult sr;

		sr = new SampleResult();

		sr.setSampleLabel(label);

		log.info("test sampler: " + label);

		try { // 这里调用我们要测试的java类，这里我调用的是一个DatabaseTest类

			sr.sampleStart(); // 记录程序执行时间，以及执行结果

			URL url = new URL("http://x.funbox.cc/funboxclienttest/MainServlet");

			int speed = 10;

			URLConnection connection = url.openConnection();

			connection
					.setRequestProperty("User-Agent",
							"NokiaN70-1/5.0616.2.0.3 Series60/2.8 Profile/MIDP-2.0 Configuration/CLDC-1.1");
			connection.setDoOutput(true);

			OutputStream outputStream = new GPRSOutputStream(
					connection.getOutputStream(), speed);

			byte[] buf = new byte[256];
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
			InputStream inputStream = new FileInputStream(
					new File(
							"/home/yaoms/Documents/funbox服务器相关文档/client_request_msg.dat"));
			int len = 0;
			while ((len = inputStream.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}

			inputStream.close();
			baos.flush();

			long sendStartTime = System.currentTimeMillis();

			outputStream.write(baos.toByteArray());
			outputStream.close();

			long sendEndTime = System.currentTimeMillis();

			long sendCostTime = sendEndTime - sendStartTime;

			double sendSpeed = (baos.size() / 1024)
					/ ((sendCostTime + 0.0) / 1000);

			log.info("send bytes: " + baos.size());

			log.info(String.format("cost time: %.2fs",
					(sendCostTime + 0.0) / 1000));

			log.info(String.format("send speed: %.2fkpbs", sendSpeed));

			long receiveStartTime = System.currentTimeMillis();

			inputStream = new GPRSInputStream(connection.getInputStream(),
					speed);
			baos.reset();
			while ((len = inputStream.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}

			inputStream.close();
			baos.flush();
			
			byte[] receivedData = baos.toByteArray();

			long receiveEndTime = System.currentTimeMillis();

			long receiveCostTime = receiveEndTime - receiveStartTime;

			double receiveSpeed = (receivedData.length / 1024)
					/ ((receiveCostTime + 0.0) / 1000);

			log.info("receive bytes: " + receivedData.length);

			log.info(String.format("cost time: %.2fs",
					(receiveCostTime + 0.0) / 1000));

			log.info(String.format("receive speed: %.2fkbps", receiveSpeed));

			 Response response = new Response();
			 String respString = response.decode(baos.toByteArray(), null);
			
			 log.info("response text:\n" + respString);

			sr.setSuccessful(true);

			sr.setResponseCodeOK();

			sr.setResponseData(receivedData);

		} catch (Throwable e) {
			log.error("出错了！", e);
			sr.setSuccessful(false);

		} finally {
			log.info("成功了");
			sr.sampleEnd();
		}

		return sr;
	}

}
