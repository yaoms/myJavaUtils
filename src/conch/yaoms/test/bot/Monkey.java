package conch.yaoms.test.bot;

import it.sauronsoftware.base64.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.kkfun.douwan.protocol.utils.Dencryptor;
import com.kkfun.douwan.protocol.utils.MD5CheckSum;
import com.kkfun.douwan.protocol.utils.StringUtils;

import conch.yaoms.io.network.SendPostMessage;
import conch.yaoms.test.bot.req.LoginRequest;

/**
 * 猴子 嘎嘎 三点法
 * 
 * @author yaoms
 * 
 */
public class Monkey implements iAnima {

	private ITracker tracker = new DefaultTracker(Monkey.class.getSimpleName());

	private IControler controler;

	private boolean running;

	private boolean login;

	private String sessionId;

	private SendPostMessage sender = new SendPostMessage();

	private MD5CheckSum md5CheckSum = new MD5CheckSum();

	private Dencryptor dencryptor = new Dencryptor();

	private Properties properties;

	private long period;

	private String url;

	private String userId;

	private String password;

	private String platform;

	private String lcdSize;

	private String kkPlatform;

	private String clientId;

	private String lsn;

	private String imsi;

	private int cell;

	private int lac;

	private int mcc;

	private int mnc;

	private int ver;

	public Monkey(Properties properties) {
		setProperties(properties);
	}

	@Override
	public void run() {

		while (running) {

			heartbeat();

			try {
				Thread.sleep(period);
			} catch (InterruptedException e) {
				tracker.error("出错了", e);
			}
		}
	}

	@Override
	public void birth() {
		tracker.info("初始化，配置变量");
		setPeriod(Long.parseLong(properties
				.getProperty("loop.period", "480000")));
		setUrl(properties.getProperty("server.url"));

		setUserId(properties.getProperty("user.id"));
		setPassword(properties.getProperty("user.password"));
		setPlatform(properties.getProperty("user.platform"));
		setKkPlatform(properties.getProperty("user.kkPlatform"));
		setLcdSize(properties.getProperty("user.lcdSize"));
		setClientId(properties.getProperty("user.clientId"));
		setLsn(properties.getProperty("user.lsn"));
		setImsi(properties.getProperty("user.imsi"));
		setCell(Integer.parseInt(properties.getProperty("user.cell", "23")));
		setLac(Integer.parseInt(properties.getProperty("user.lac", "23")));
		setMcc(Integer.parseInt(properties.getProperty("user.mcc", "2")));
		setMnc(Integer.parseInt(properties.getProperty("user.mnc", "23")));
		setVer(Integer.parseInt(properties.getProperty("user.ver", "0")));

		if (!checkConfig()) {
			tracker.error("配置文件有误");
			return;
		}

		controler.addCallHandler("重新登录", "login", this);
		controler.addCallHandler("心跳循环开始", "alive", this);
		controler.addCallHandler("心跳循环停止", "death", this);
		controler.addCallHandler("附近列表", "nearBy", this);
		controler.addCallHandler("练习场列表", "trainList", this);
		controler.addCallHandler("练习场排名", "trainTop", this);
		controler.addCallHandler("练习场规则", "trainRule", this);
		controler.addCallHandler("开始练习游戏0", "trainStart", this);
		controler.addCallHandler("结束PK0", "pkOver", this);
		controler.addCallHandler("擂台列表", "lordList", this);
		controler.addCallHandler("开始擂台游戏0", "lordStart", this);
		controler.addCallHandler("提交擂台得分0", "lordOver", this);
		controler.addCallHandler("团体赛活动", "groupList", this);
		controler.addCallHandler("开始团体赛游戏0", "groupStart", this);
		controler.addCallHandler("提交团体赛得分0", "groupOver", this);
		controler.addCallHandler("商城图片包", "goodsPic", this);
		controler.addCallHandler("商城主页", "market", this);
		controler.addCallHandler("黑名单", "blacklist", this);
		controler.addCallHandler("加黑", "addBlack", this);
		controler.addCallHandler("去黑", "cancelBlack", this);
		controler.addCallHandler("成就", "honor", this);
		controler.addCallHandler("已安装游戏列表", "myGames", this);
		controler.addCallHandler("查看充值记录", "rechargeLog", this);
		controler.addCallHandler("阵营列表", "groupListReq", this);
		controler.addCallHandler("发送消息", "sendMsg", this);
		controler.addCallHandler("搜索在线用户", "searchOnlineUser", this);
		controler.addCallHandler("搜索用户", "searchUserByName", this);
		controler.addCallHandler("用户资料", "userInfo", this);
		controler.addCallHandler("加为好友", "addFriend", this);

		login = login();
	}

	@Override
	public void alive() {
		if (login) {
			tracker.info("开始心跳线程");
			running = true;
			new Thread(this).start();
		} else {
			tracker.error("尚未登录");
		}
	}

	@Override
	public void death() {
		tracker.info("结束心跳线程");
		running = false;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public ITracker getTracker() {
		return tracker;
	}

	@Override
	public void setTracker(ITracker tracker) {
		this.tracker = tracker;
	}

	public IControler getControler() {
		return controler;
	}

	public void setControler(IControler controler) {
		this.controler = controler;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getLcdSize() {
		return lcdSize;
	}

	public void setLcdSize(String lcdSize) {
		this.lcdSize = lcdSize;
	}

	public String getKkPlatform() {
		return kkPlatform;
	}

	public void setKkPlatform(String kkPlatform) {
		this.kkPlatform = kkPlatform;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getLsn() {
		return lsn;
	}

	public void setLsn(String lsn) {
		this.lsn = lsn;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public int getCell() {
		return cell;
	}

	public void setCell(int cell) {
		this.cell = cell;
	}

	public int getLac() {
		return lac;
	}

	public void setLac(int lac) {
		this.lac = lac;
	}

	public int getMcc() {
		return mcc;
	}

	public void setMcc(int mcc) {
		this.mcc = mcc;
	}

	public int getMnc() {
		return mnc;
	}

	public void setMnc(int mnc) {
		this.mnc = mnc;
	}

	public int getVer() {
		return ver;
	}

	public void setVer(int ver) {
		this.ver = ver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	private boolean checkConfig() {

		if (getUrl() == null) {
			tracker.error("url 错误");
			return false;
		}

		if (getUserId() == null) {
			tracker.error("userId 错误");
			return false;
		}

		if (getPassword() == null) {
			tracker.error("userId 错误");
			return false;
		}

		if (getPlatform() == null) {
			tracker.error("platform 错误");
			return false;
		}

		if (getKkPlatform() == null) {
			tracker.error("kkPlatform 错误");
			return false;
		}

		if (getLcdSize() == null) {
			tracker.error("lcdSize 错误");
			return false;
		}

		if (getClientId() == null) {
			tracker.error("clientId 错误");
			return false;
		}

		if (getLsn() == null) {
			tracker.error("lsn 错误");
			return false;
		}

		if (getImsi() == null) {
			tracker.error("imsi 错误");
			return false;
		}

		return true;
	}

	@Override
	public boolean login() {
		tracker.info("登录, 建立会话");

		LoginRequest request = new LoginRequest();
		String userId = new String(Base64.encode(dencryptor.encrypt(this.userId
				.getBytes())));
		String password = new String(Base64.encode(dencryptor
				.encrypt(this.password.getBytes())));
		tracker.info("用户: " + userId);
		tracker.info("密码: " + password);
		request.setUserId(userId);
		request.setPassword(password);
		request.setPlatform(platform);
		request.setKkPlatform(kkPlatform);
		request.setLcdSize(lcdSize);
		request.setClientID(clientId);
		request.setLsn(lsn);
		request.setImsi(imsi);
		request.setCell(cell);
		request.setLac(lac);
		request.setMcc(mcc);
		request.setMnc(mnc);
		request.setVersion(ver);

		byte[] data = request.encode();

		ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length + 16);
		try {
			baos.write(data);
			baos.write(md5CheckSum.getMd5sumByPrivateKey(data));

			tracker.info("请求报文:\n" + baos.toString());

			byte[] response = sender.send(url, baos.toByteArray());

			tracker.info("响应报文:\n" + new String(response));

			ByteArrayOutputStream baos2 = new ByteArrayOutputStream();

			baos2.write("<body>".getBytes());
			baos2.write(response);
			baos2.write("</body>".getBytes());

			Document document = new SAXBuilder()
					.build(new ByteArrayInputStream(baos2.toByteArray()));

			Element rootElement = document.getRootElement().getChild("rsp");

			String sidString = rootElement.getChildTextTrim("sid");

			if (!StringUtils.isEmpty(sidString)) {
				setSessionId(sidString);
				tracker.info("建立会话成功 " + sessionId);

				return true;
			} else {
				tracker.warn("建立会话失败\n" + baos2.toString());
			}
		} catch (Exception e) {
			tracker.error("出错了", e);
		}
		return false;
	}

	@Override
	public void heartbeat() {
		tracker.info("心跳");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>	<type>UpdateMsgReq</type>\n	<sid>%s</sid>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void nearBy() {
		tracker.info("附近列表");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n    <type>NearReq</type>\n    <sid>%s</sid>\n    <idx>0</idx>\n    <items>0</items>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void trainList() {
		tracker.info("练习场列表");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n    <type>TrainReq</type>\n    <sid>%s</sid>\n    <idx>0</idx>\n    <items>0</items>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void trainTop() {
		tracker.info("练习场排名");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>TrainTopReq</type>\n	<sid>%s</sid>\n	<gid>1</gid>\n	<idx>0</idx>\n	<items>10</items>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void trainRule() {
		tracker.info("练习场规则");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>TrainRuleReq</type>\n	<sid>%s</sid>	\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void getDWVer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void trainStart() {
		tracker.info("练习场开始");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>StartGameTrainReq</type>\n	<sid>%s</sid>\n	<gid>0</gid>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void trainOver() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pkList() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pkRule() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pkNear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pkUid() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pkOver() {
		tracker.info("PK结束");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>PKScoreReq</type>\n	<sid>%s</sid>\n	<pkid>0</pkid>\n	<score>8</score>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void lordList() {
		tracker.info("擂台列表");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>LordReq</type>\n	<sid>%s</sid>\n	<idx>0</idx>\n	<items>0</items>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void lordTop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void lordRule() {
		// TODO Auto-generated method stub

	}

	@Override
	public void lordStart() {
		tracker.info("开始擂台活动");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>StartGameLordReq</type>\n	<sid>%s</sid>\n	<gid>0</gid>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void lordOver() {
		tracker.info("提交擂台得分");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>LordScoreReq</type>\n	<sid>%s</sid>\n	<gid>0</gid>\n	<score>42</score>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void groupList() {
		tracker.info("团体赛活动");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>GroupReq</type>\n	<sid>%s</sid>\n	<idx>0</idx>\n	<items>0</items>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void groupTop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void groupRule() {
		// TODO Auto-generated method stub

	}

	@Override
	public void groupStart() {
		tracker.info("开始团体赛活动");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>StartGameGroupReq</type>\n	<sid>%s</sid>\n	<gid>0</gid>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void groupOver() {
		tracker.info("提交团体赛得分");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>GroupScoreReq</type>\n	<sid>%s</sid>\n	<gid>0</gid>\n	<score>434</score>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void myInfo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateMyInfo(String name, int age) {
		// TODO Auto-generated method stub

	}

	@Override
	public void friendList() {
		// TODO Auto-generated method stub

	}

	@Override
	public void userInfo(int userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addFriend(int userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delFriend(int userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void searchUser() {
		// TODO Auto-generated method stub

	}

	@Override
	public void goodsPic() {
		tracker.info("商城图片包");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>GoodsPicReq</type>\n	<sid>%s</sid>\n	<ver>0</ver>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void market() {
		tracker.info("商城主页");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>MarketReq</type>\n	<sid>%s</sid>\n	<idx>0</idx>\n	<items>10</items>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void buyATGoods() {
		tracker.info("购买现实物品");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>BuyATGoodsReq</type>\n	<sid>%s</sid>\n	<goods>1</goods>\n	<name>方法</name>\n	<call>13800138000</call>\n	<>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void blacklist() {
		tracker.info("黑名单");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>BlacklistReq</type>\n	<sid>%s</sid>\n    <idx>0</idx>\n	<items>10</items>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void addBlack() {
		tracker.info("加黑");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>AddBlackReq</type>\n	<sid>%s</sid>\n    <uid>10004</uid>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void cancelBlack() {
		tracker.info("去黑");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>CancelBlackReq</type>\n	<sid>%s</sid>\n    <uid>10004</uid>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void honor() {
		tracker.info("成就");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>HonorReq</type>\n	<sid>%s</sid>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void myGames() {
		tracker.info("已安装游戏列表");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>MyGamesReq</type>\n	<sid>%s</sid>\n	<gid>0 1 2</gid>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void rechargeLog() {
		tracker.info("充值记录");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>GetChargeLogReq</type>\n	<sid>%s</sid>\n    <idx>0</idx>\n	<items>10</items>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void groupListReq() {
		tracker.info("阵营列表");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>GroupListReq</type>\n	<sid>%s</sid>\n</req>\n",
								sessionId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void sendMsg() {

		int userId = 10001;
		String msg = "送风口的后果块";

		tracker.info("发送消息到 " + userId);
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>SendMsgReq</type>\n	<sid>%s</sid>\n	<uid>%d</uid>\n	<msg>%s</msg>\n</req>\n",
								sessionId, userId, msg).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void searchOnlineUser() {
		tracker.info("搜索在线用户");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>ConditionSearchUserReq</type>\n	<sid>%s</sid>\n	<sex>男</sex>\n	<lbs></lbs>\n	<idx>0</idx>\n	<items>10</items>\n</req>\n",
								sessionId, userId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void searchUserByName() {
		tracker.info("搜索用户");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>SearchNameReq</type>\n	<sid>%s</sid>\n	<name>快乐就快乐</name>\n	<idx>0</idx>\n	<items>10</items>\n</req>\n",
								sessionId, userId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void userInfo() {
		tracker.info("用户资料");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>UserInfoReq</type>\n        <sid>%s</sid>\n        <uid>10195</uid>\n        <gid>0</gid>\n</req>\n",
								sessionId, userId).getBytes());
		tracker.info("\n" + new String(response));
	}

	@Override
	public void addFriend() {
		tracker.info("加为好友");
		byte[] response = sender
				.send(url,
						String.format(
								"<req>\n	<type>AddFriendReq</type>\n	<sid>%s</sid>\n	<uid>10197</uid>\n	<msg>米饭</msg>\n</req>\n",
								sessionId, userId).getBytes());
		tracker.info("\n" + new String(response));
	}

	// TODO: 文件尾部
}
