package conch.yaoms.test.bot;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class MonkeyTest {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		Properties properties = new Properties();
		try {
			properties.loadFromXML(MonkeyTest.class.getResource("config.xml")
					.openStream());
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		iAnima kong = new Monkey(properties);

		MainFrame mainFrame = new MainFrame("测试机器人", kong);

		mainFrame.setVisible(true);
	}

}
