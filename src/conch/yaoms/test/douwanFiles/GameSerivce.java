package conch.yaoms.test.douwanFiles;

import java.util.List;

public class GameSerivce {

	public static GameHall getGameHall(int version, GamePic gamePic,
			List<Game> games) {

		GameHall gameHall = new GameHall();
		gameHall.setVersion(version);
		gameHall.setPic(gamePic);
		gameHall.setGames(games);
		
		return gameHall;
	}

}
