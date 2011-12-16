package conch.yaoms.test.douwanFiles;

import java.util.List;

public class GameHall {

	private int version;
	private GamePic pic;
	private List<Game> games;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public GamePic getPic() {
		return pic;
	}

	public void setPic(GamePic pic) {
		this.pic = pic;
	}

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		if (games != null) {
			for (Game game : games) {
				stringBuffer.append(game.toString()).append("\n");
			}
		}
		return String
				.format("<rsp>\n<type>GameListRsp</type>\n<ver>%d</ver>\n%s\n<sum>%d</sum>\n<idx>0</idx>\n<items>%d</items>\n<list>\n%s</list>\n</rsp>\n",
						version, pic.toString(), games.size(),games.size(), stringBuffer.toString());
	}

}
