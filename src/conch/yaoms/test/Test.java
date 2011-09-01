package conch.yaoms.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Document document = new SAXBuilder(false).build(new File(
					"/home/yaoms/1000users.xml"));
			Element rootElement = document.getRootElement();

			List<?> results = rootElement.getChildren();
			
			int sampleCount = results.size();
			long startTime = getStartTime(results);
			long endTime = getEndTime(results);
			long minTime = getMinTime(results);
			long maxTime = getMaxTime(results);
			long ascTime = minTime + (maxTime - minTime) / 2;
			long successCount = getSucessCount(results);
			double success = (successCount + 0.0) / sampleCount;
			
			System.out.println("总数: " + sampleCount);
			System.out.println("成功: " + successCount);
			System.out.println("失败: " + (sampleCount - successCount));
			System.out.printf("成功率: %.0f%%\n", (success * 100));
			System.out.println();
			System.out.printf("最小时间: %.2fs\n", (minTime/1000.0) );
			System.out.printf("平均时间: %.2fs\n", (ascTime/1000.0) );
			System.out.printf("最大时间: %.2fs\n", (maxTime/1000.0) );
			System.out.printf("总消耗时间: %.2fs\n", ((endTime - startTime)/1000.0) );

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static long getSucessCount(List<?> results) {
		long successCount = 0;
		for (Object object : results) {
			Element element = (Element) object;
			if (element.getChild("assertionResult").getChild("failure").getTextTrim().toLowerCase().equals("false")) {
				successCount++;
			}
		}
		return successCount;
	}

	private static long getMinTime(List<?> results) throws DataConversionException {
		long minTime = -1;
		for (Object object : results) {
			Element element = (Element) object;
			long thisTime = element.getAttribute("t").getLongValue();
			if (minTime == -1) {
				minTime = thisTime;
			} else if (thisTime < minTime) {
				minTime = thisTime;
			}
		}
		return minTime;
	}

	private static long getMaxTime(List<?> results) throws DataConversionException {
		long minTime = -1;
		for (Object object : results) {
			Element element = (Element) object;
			long thisTime = element.getAttribute("t").getLongValue();
			if (thisTime > minTime) {
				minTime = thisTime;
			}
		}
		return minTime;
	}

	private static long getEndTime(List<?> results) throws DataConversionException {
		long endTime = -1;
		for (Object object : results) {
			Element element = (Element) object;
			long thisTime = element.getAttribute("ts").getLongValue() + element.getAttribute("t").getLongValue();
			if (thisTime > endTime) {
				endTime = thisTime;
			}
		}
		return endTime;
	}

	private static long getStartTime(List<?> results) throws DataConversionException {
		long startTime = -1;
		for (Object object : results) {
			Element element = (Element) object;
			long thisTime = element.getAttribute("ts").getLongValue();
			if (startTime == -1) {
				startTime = thisTime;
			} else if (thisTime < startTime) {
				startTime = thisTime;
			}
		}
		return startTime;
	}

}
