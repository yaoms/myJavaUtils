package conch.yaoms.test.douwanFiles;

public class GamePic {

	private int adpId;
	private int size;
	private int version;
	private String url;

	public int getAdpId() {
		return adpId;
	}

	public void setAdpId(int adpId) {
		this.adpId = adpId;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return String.format("<pic size=\"%d\" url=\"%s\" />", size, url);
	}

}
