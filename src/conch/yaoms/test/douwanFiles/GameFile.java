package conch.yaoms.test.douwanFiles;

public class GameFile {

	private int fileId;
	private int gameId;
	private int adpId;
	private String name;
	private int size;
	private String url;
	private long utime;

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getAdpId() {
		return adpId;
	}

	public void setAdpId(int adpId) {
		this.adpId = adpId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getUtime() {
		return utime;
	}

	public void setUtime(long utime) {
		this.utime = utime;
	}

	public String getEntry() {
		return name == null ? name : (name.contains(".") ? name.substring(0,
				name.lastIndexOf(".")) : name);
	}

	@Override
	public String toString() {
		return String.format("<file size=\"%d\" name=\"%s\" url=\"%s\"/>",
				size, name, url);
	}

}
