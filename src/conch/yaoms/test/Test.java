package conch.yaoms.test;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		provs();
	}

	/*
	 * "北京、上海、广东、云南、内蒙古、吉林、四川、天津、宁夏、安徽、山东、山西、广西、新疆、江苏、江西、河北、河南、浙江、海南、湖北、湖南、甘肃、福建、西藏、贵州、辽宁、重庆、陕西、青海、黑龙江、香港、澳门、台湾"
	 * "朝阳、海淀、东城、丰台、大兴、宣武、崇文、平谷、怀柔、房山、昌平、石景山、西城、通州、门头沟、顺义"
	 * "南汇、卢湾、嘉定、奉贤、宝山、徐汇、普陀、杨浦、松江、浦东、虹口、金山、长宁、闵行、闸北、青浦、静安、黄浦"
	 * "广州、深圳、珠海、东莞、韶关、中山、云浮、佛山、惠州、揭阳、梅州、汕头、汕尾、江门、河源、清远、湛江、潮州、肇庆、茂名、阳江"
	 * "大理、丽江、临沧、保山、德宏、怒江、文山、昆明、昭通、普洱、曲靖、楚雄、玉溪、红河、西双版纳、迪庆"
	 * "呼和浩特、呼伦贝尔、乌兰察布、乌海、兴安、包头、巴彦淖尔、赤峰、通辽、鄂尔多斯、锡林郭勒、阿拉善"
	 * "吉林、长春、四平、延边、松原、白城、白山、辽源、通化"
	 * "成都、攀枝花、乐山、内江、凉山、南充、宜宾、巴中、广元、广安、德阳、泸州、甘孜、眉山、绵阳、自贡、资阳、达州、遂宁、阿坝、雅安"
	 * "东丽、北辰、南开、和平、塘沽、大港、宝坻、武清、汉沽、河东、河北、河西、津南、红桥、西青" "银川、中卫、吴忠、固原、石嘴山"
	 * "合肥、亳州、六安、安庆、宣城、宿州、巢湖、池州、淮北、淮南、滁州、芜湖、蚌埠、铜陵、阜阳、马鞍山、黄山"
	 * "济南、济宁、青岛、东营、临沂、威海、德州、日照、枣庄、泰安、淄博、滨州、潍坊、烟台、聊城、莱芜、菏泽"
	 * "太原、临汾、吕梁、大同、忻州、晋中、晋城、朔州、运城、长治、阳泉"
	 * "南宁、桂林、北海、崇左、来宾、柳州、梧州、河池、玉林、百色、贵港、贺州、钦州、防城港"
	 * "乌鲁木齐、伊犁、克孜勒苏、克拉玛依、博尔塔拉、吐鲁番、和田、喀什、塔城、巴音郭楞、昌吉、阿克苏、阿勒泰"
	 * "南京、南通、宿迁、常州、徐州、扬州、无锡、泰州、淮安、盐城、苏州、连云港、镇江"
	 * "南昌、赣州、上饶、九江、吉安、宜春、抚州、新余、景德镇、萍乡、鹰潭" "石家庄、秦皇岛、张家口、保定、唐山、廊坊、承德、沧州、衡水、邢台、邯郸"
	 * "郑州、三门峡、信阳、南阳、周口、商丘、安阳、平顶山、开封、新乡、洛阳、漯河、濮阳、焦作、许昌、驻马店、鹤壁"
	 * "杭州、丽水、台州、嘉兴、宁波、温州、湖州、绍兴、舟山、衢州、金华" "海口、三亚"
	 * "武汉、荆州、荆门、十堰、咸宁、孝感、宜昌、恩施、襄樊、鄂州、随州、黄冈、黄石"
	 * "长沙、岳阳、娄底、常德、张家界、怀化、株洲、永州、湘潭、湘西、益阳、衡阳、邵阳、郴州"
	 * "兰州、临夏、嘉峪关、天水、定西、平凉、庆阳、张掖、武威、甘南、白银、酒泉、金昌、陇南" "福州、厦门、三明、南平、宁德、泉州、漳州、莆田、龙岩"
	 * "拉萨、山南地、日喀则地、昌都地、林芝地、那曲地、阿里地" "贵阳、遵义、六盘水、安顺、毕节、铜仁、黔东南、黔南、黔西南"
	 * "沈阳、大连、丹东、抚顺、朝阳、本溪、盘锦、营口、葫芦岛、辽阳、铁岭、锦州、阜新、鞍山"
	 * "万州、万盛、九龙坡、北碚、南岸、南川、双桥、合川、大渡口、巴南、永川、江北、江津、沙坪坝、涪陵、渝中、渝北、长寿、黔江"
	 * "西安、咸阳、商洛、安康、宝鸡、延安、榆林、汉中、渭南、铜川" "西宁、果洛、海东、海北、海南、海西、玉树、黄南"
	 * "哈尔滨、齐齐哈尔、大兴安岭、大庆、七台河、伊春、佳木斯、双鸭山、牡丹江、绥化、鸡西、鹤岗、黑河" "香港" "澳门" "台湾"
	 */

	public static void provs() {
		String[] provs = "北京、上海、广东、云南、内蒙古、吉林、四川、天津、宁夏、安徽、山东、山西、广西、新疆、江苏、江西、河北、河南、浙江、海南、湖北、湖南、甘肃、福建、西藏、贵州、辽宁、重庆、陕西、青海、黑龙江、香港、澳门、台湾"
				.split("、");

		String[] cities = new String[] {
				"朝阳、海淀、东城、丰台、大兴、宣武、崇文、平谷、怀柔、房山、昌平、石景山、西城、通州、门头沟、顺义",
				"南汇、卢湾、嘉定、奉贤、宝山、徐汇、普陀、杨浦、松江、浦东、虹口、金山、长宁、闵行、闸北、青浦、静安、黄浦",
				"广州、深圳、珠海、东莞、韶关、中山、云浮、佛山、惠州、揭阳、梅州、汕头、汕尾、江门、河源、清远、湛江、潮州、肇庆、茂名、阳江",
				"大理、丽江、临沧、保山、德宏、怒江、文山、昆明、昭通、普洱、曲靖、楚雄、玉溪、红河、西双版纳、迪庆",
				"呼和浩特、呼伦贝尔、乌兰察布、乌海、兴安、包头、巴彦淖尔、赤峰、通辽、鄂尔多斯、锡林郭勒、阿拉善",
				"吉林、长春、四平、延边、松原、白城、白山、辽源、通化",
				"成都、攀枝花、乐山、内江、凉山、南充、宜宾、巴中、广元、广安、德阳、泸州、甘孜、眉山、绵阳、自贡、资阳、达州、遂宁、阿坝、雅安",
				"东丽、北辰、南开、和平、塘沽、大港、宝坻、武清、汉沽、河东、河北、河西、津南、红桥、西青",
				"银川、中卫、吴忠、固原、石嘴山",
				"合肥、亳州、六安、安庆、宣城、宿州、巢湖、池州、淮北、淮南、滁州、芜湖、蚌埠、铜陵、阜阳、马鞍山、黄山",
				"济南、济宁、青岛、东营、临沂、威海、德州、日照、枣庄、泰安、淄博、滨州、潍坊、烟台、聊城、莱芜、菏泽",
				"太原、临汾、吕梁、大同、忻州、晋中、晋城、朔州、运城、长治、阳泉",
				"南宁、桂林、北海、崇左、来宾、柳州、梧州、河池、玉林、百色、贵港、贺州、钦州、防城港",
				"乌鲁木齐、伊犁、克孜勒苏、克拉玛依、博尔塔拉、吐鲁番、和田、喀什、塔城、巴音郭楞、昌吉、阿克苏、阿勒泰",
				"南京、南通、宿迁、常州、徐州、扬州、无锡、泰州、淮安、盐城、苏州、连云港、镇江",
				"南昌、赣州、上饶、九江、吉安、宜春、抚州、新余、景德镇、萍乡、鹰潭",
				"石家庄、秦皇岛、张家口、保定、唐山、廊坊、承德、沧州、衡水、邢台、邯郸",
				"郑州、三门峡、信阳、南阳、周口、商丘、安阳、平顶山、开封、新乡、洛阳、漯河、濮阳、焦作、许昌、驻马店、鹤壁",
				"杭州、丽水、台州、嘉兴、宁波、温州、湖州、绍兴、舟山、衢州、金华", "海口、三亚",
				"武汉、荆州、荆门、十堰、咸宁、孝感、宜昌、恩施、襄樊、鄂州、随州、黄冈、黄石",
				"长沙、岳阳、娄底、常德、张家界、怀化、株洲、永州、湘潭、湘西、益阳、衡阳、邵阳、郴州",
				"兰州、临夏、嘉峪关、天水、定西、平凉、庆阳、张掖、武威、甘南、白银、酒泉、金昌、陇南",
				"福州、厦门、三明、南平、宁德、泉州、漳州、莆田、龙岩", "拉萨、山南地、日喀则地、昌都地、林芝地、那曲地、阿里地",
				"贵阳、遵义、六盘水、安顺、毕节、铜仁、黔东南、黔南、黔西南",
				"沈阳、大连、丹东、抚顺、朝阳、本溪、盘锦、营口、葫芦岛、辽阳、铁岭、锦州、阜新、鞍山",
				"万州、万盛、九龙坡、北碚、南岸、南川、双桥、合川、大渡口、巴南、永川、江北、江津、沙坪坝、涪陵、渝中、渝北、长寿、黔江",
				"西安、咸阳、商洛、安康、宝鸡、延安、榆林、汉中、渭南、铜川", "西宁、果洛、海东、海北、海南、海西、玉树、黄南",
				"哈尔滨、齐齐哈尔、大兴安岭、大庆、七台河、伊春、佳木斯、双鸭山、牡丹江、绥化、鸡西、鹤岗、黑河",
				"香港",
				"澳门",
				"台湾" };

		System.out.println(provs.length);
		System.out.println(cities.length);

		for (int i = 0; i < cities.length; i++) {
			System.out.println(String.format(
					"<entry key=\"addr.%d\">%s</entry>", i, provs[i]));
			String[] citiesArray = cities[i].split("、");
			for (int j = 0; j < citiesArray.length; j++) {
				System.out.println(String.format(
						"<entry key=\"addr.%d.%d\">%s</entry>", i, j,
						citiesArray[j]));
			}
		}
	}

	public static void defaultFonts() {

		int width = 60, height = 20;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();

		System.out.println(g.getFont().getFontName());
		// // 生成随机类
		// Random random = new Random();
		// // 设置背景颜色
		// g.setColor(new Color(160, 200, 100));
		// g.fillRect(0, 0, width, height);
		// // 设置字体
		// // g.setFont(new Font("Times New Roman",Font.PLAIN,18));
		// // 随机产生50条干扰线，使图形中的验证码不易被其他的程序探测到
		// g.setColor(new Color(160, 200, 200));
		// for (int i = 0; i < 50; i++) {
		// int x = random.nextInt(width);
		// int y = random.nextInt(height);
		// int x1 = random.nextInt(width);
		// int y1 = random.nextInt(height);
		// g.drawLine(x, y, x + x1, y + y1);
		// }
		// // 随机产生验证码（4为数字）
		// String sRand = "";
		// for (int i = 0; i < 4; i++) {
		// String rand = String.valueOf(random.nextInt(10));
		// sRand += rand;
		// // 将验证码显示到图象
		// g.setColor(new Color(20 + random.nextInt(110), 20 + random
		// .nextInt(110), 20 + random.nextInt(110)));
		// g.drawString(rand, 13 * i + 6, 16);
		// }

	}

	public static void json() {

		Session session1 = new Session();
		session1.sessionId = "213";
		session1.map = new HashMap<String, String>();
		session1.map.put("userId", "345345");

		Session session2 = new Session();
		session2.sessionId = "345";
		session2.map = new HashMap<String, String>();
		session2.map.put("userId", "2345345");

		SessionManager sessionManager = new SessionManager();
		sessionManager.map = new HashMap<String, Session>();
		sessionManager.map.put(session1.sessionId, session1);
		sessionManager.map.put(session2.sessionId, session2);

		System.out.println(sessionManager.toString());
	}

	public void format() {
		System.out
				.println(String
						.format("<item DateTime=\"%1$tm月%1$td日 %1$tH:%1$tM\"  uid=\"%2$s\" name=\"%3$s\" MsgType=\"1\" msg=\"%4$s\" />",
								System.currentTimeMillis(), "23", "撒肥",
								"sdfgdgdfd"));

	}

	public static void count() {

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
			System.out.printf("最小时间: %.2fs\n", (minTime / 1000.0));
			System.out.printf("平均时间: %.2fs\n", (ascTime / 1000.0));
			System.out.printf("最大时间: %.2fs\n", (maxTime / 1000.0));
			System.out.printf("总消耗时间: %.2fs\n",
					((endTime - startTime) / 1000.0));

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
			if (element.getChild("assertionResult").getChild("failure")
					.getTextTrim().toLowerCase().equals("false")) {
				successCount++;
			}
		}
		return successCount;
	}

	private static long getMinTime(List<?> results)
			throws DataConversionException {
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

	private static long getMaxTime(List<?> results)
			throws DataConversionException {
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

	private static long getEndTime(List<?> results)
			throws DataConversionException {
		long endTime = -1;
		for (Object object : results) {
			Element element = (Element) object;
			long thisTime = element.getAttribute("ts").getLongValue()
					+ element.getAttribute("t").getLongValue();
			if (thisTime > endTime) {
				endTime = thisTime;
			}
		}
		return endTime;
	}

	private static long getStartTime(List<?> results)
			throws DataConversionException {
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

class Session {
	String sessionId;
	Map<String, String> map;

	public String toString() {
		return toJSONObject().toString();
	}

	public JSONObject toJSONObject() {
		JSONObject object = new JSONObject();
		JSONObject jsonMap = new JSONObject(map);
		try {
			object.put("sessionId", sessionId);
			object.put("map", jsonMap);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}

class SessionManager {
	Map<String, Session> map;

	public String toString() {
		JSONObject object = new JSONObject();
		Set<String> keys = map.keySet();
		for (String key : keys) {
			try {
				object.put(key, map.get(key).toJSONObject());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return object.toString();
	}
}
