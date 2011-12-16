package conch.yaoms.test.douwanFiles;

public class GameItem {

	private int gameId;
	private String gameName;
	private String gameCode;
	private String gameInfo;
	private int picId;
	private int version;

	public GameItem(int gameId, String gameName, String gameCode,
			String gameInfo, int picId, int version) {
		setGameId(gameId);
		setGameName(gameName);
		setGameCode(gameCode);
		setGameInfo(gameInfo);
		setPicId(picId);
		setVersion(version);
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getGameInfo() {
		return gameInfo;
	}

	public void setGameInfo(String gameInfo) {
		this.gameInfo = gameInfo;
	}

	public int getPicId() {
		return picId;
	}

	public void setPicId(int picId) {
		this.picId = picId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
