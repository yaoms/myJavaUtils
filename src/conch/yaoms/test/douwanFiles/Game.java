package conch.yaoms.test.douwanFiles;

import java.util.List;

public class Game {

	private int gameId;
	private String name;
	private String info;
	private int pic;
	private int version;
	private String entry;
	private List<GameFile> files;

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getPic() {
		return pic;
	}

	public void setPic(int pic) {
		this.pic = pic;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public List<GameFile> getFiles() {
		return files;
	}

	public void setFiles(List<GameFile> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		if (files != null) {
			for (GameFile file : files) {
				stringBuffer.append(file.toString()).append("\n");
			}
		}
		return String
				.format("<item gid=\"%d\" pic=\"%d\" ver=\"%d\" name=\"%s\" desc=\"%s\" entry=\"%s\">\n<filelist>\n%s</filelist>\n</item>",
						gameId, pic, version, name, info, entry,
						stringBuffer.toString());
	}
}
