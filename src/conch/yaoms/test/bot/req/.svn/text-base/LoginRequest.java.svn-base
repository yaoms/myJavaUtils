package conch.yaoms.test.bot.req;

public class LoginRequest {

	private static final String tpl = "<req>\n	<type>LoginReq</type>\n	<uid>%s</uid>\n	<pw>%s</pw>\n	<plat>%s</plat>\n	<KKplat>%s</KKplat>\n	<lcd>%s</lcd>\n	<cid>%s</cid>\n	<lsn>%s</lsn>\n	<imsi>%s</imsi>\n	<cell>%d</cell>\n	<lac>%d</lac>\n	<mcc>%d</mcc>\n	<mnc>%d</mnc>\n	<ver>%d</ver>\n</req>\n";

	private String type;
	private String userId;
	private String password;
	private String platform;
	private String kkPlatform;
	private String lcdSize;
	private String clientID;
	private String lsn;
	private String imsi;
	private int cell;
	private int lac;
	private int mcc;
	private int mnc;
	private int version;

	public byte[] encode() {
		return String.format(tpl, userId, password, platform, kkPlatform,
				lcdSize, clientID, lsn, imsi, cell, lac, mcc, mnc, version)
				.getBytes();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPlatform() {
		return platform;
	}

	public String getKkPlatform() {
		return kkPlatform;
	}

	public void setKkPlatform(String kkPlatform) {
		this.kkPlatform = kkPlatform;
	}

	public String getLcdSize() {
		return lcdSize;
	}

	public void setLcdSize(String lcdSize) {
		this.lcdSize = lcdSize;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
