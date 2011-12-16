package conch.yaoms.test.douwanFiles;
public class test {
	private static GameItem[] gameItems = new GameItem[10];
	static {
		gameItems[0] = new GameItem(0, "功夫小鸡", "egg", "…", 6, 4);
		gameItems[1] = new GameItem(1, "泡泡龙", "ppl", "…", 0, 4);
		gameItems[2] = new GameItem(2, "月下销魂", "escape", "…", 4, 4);
		gameItems[3] = new GameItem(3, "打地鼠", "mole", "…", 2, 4);
		gameItems[4] = new GameItem(4, "实况投篮", "mole", "…", 8, 3);
		gameItems[5] = new GameItem(5, "连连看", "mole", "…", 7, 3);
		gameItems[6] = new GameItem(6, "旋风小子", "mole", "…", 3, 3);
		gameItems[7] = new GameItem(7, "百米飞人", "mole", "…", 5, 3);
		gameItems[8] = new GameItem(8, "宝石迷阵", "mole", "…", 1, 3);
		gameItems[9] = new GameItem(9, "王牌飞行员", "mole", "…", 0, 3);
	}
/*
( 0  ,2,'egg.kkr',15183,'http://211.154.137.11/g/egg/20111129/egg.kkr.zip', unix_timestamp(now())*1000),
( 0  ,2,'egg.bin',10044,'http://211.154.137.11/g/egg/20111129/egg.bin.zip', unix_timestamp(now())*1000),
( 1  ,2,'ppl.kkr',16830,'http://211.154.137.11/g/ppl/20111129/ppl.kkr.zip', unix_timestamp(now())*1000),
( 1  ,2,'ppl.bin',12710,'http://211.154.137.11/g/ppl/20111129/ppl.bin.zip', unix_timestamp(now())*1000),
( 2  ,2,'escape.kkr',14475,'http://211.154.137.11/g/escape/20111129/escape.kkr.zip', unix_timestamp(now())*1000),
( 2  ,2,'escape.bin',13812,'http://211.154.137.11/g/escape/20111129/escape.bin.zip', unix_timestamp(now())*1000),
( 3  ,2,'mole.kkr',14338,'http://211.154.137.11/g/mole/20111129/mole.kkr.zip', unix_timestamp(now())*1000),
( 3  ,2,'mole.bin',34642,'http://211.154.137.11/g/mole/20111129/mole.bin.zip', unix_timestamp(now())*1000),
( 4  ,2,'mole.kkr',14338,'http://211.154.137.11/g/mole/20111129/mole.kkr.zip', unix_timestamp(now())*1000),
( 4  ,2,'mole.bin',34642,'http://211.154.137.11/g/mole/20111129/mole.bin.zip', unix_timestamp(now())*1000),
( 5  ,2,'mole.kkr',14338,'http://211.154.137.11/g/mole/20111129/mole.kkr.zip', unix_timestamp(now())*1000),
( 5  ,2,'mole.bin',34642,'http://211.154.137.11/g/mole/20111129/mole.bin.zip', unix_timestamp(now())*1000),
( 6  ,2,'mole.kkr',14338,'http://211.154.137.11/g/mole/20111129/mole.kkr.zip', unix_timestamp(now())*1000),
( 6  ,2,'mole.bin',34642,'http://211.154.137.11/g/mole/20111129/mole.bin.zip', unix_timestamp(now())*1000),
( 7  ,2,'mole.kkr',14338,'http://211.154.137.11/g/mole/20111129/mole.kkr.zip', unix_timestamp(now())*1000),
( 7  ,2,'mole.bin',34642,'http://211.154.137.11/g/mole/20111129/mole.bin.zip', unix_timestamp(now())*1000),
( 8  ,2,'mole.kkr',14338,'http://211.154.137.11/g/mole/20111129/mole.kkr.zip', unix_timestamp(now())*1000),
( 8  ,2,'mole.bin',34642,'http://211.154.137.11/g/mole/20111129/mole.bin.zip', unix_timestamp(now())*1000),
( 9  ,2,'mole.kkr',14338,'http://211.154.137.11/g/mole/20111129/mole.kkr.zip', unix_timestamp(now())*1000),
( 9  ,2,'mole.bin',34642,'http://211.154.137.11/g/mole/20111129/mole.bin.zip', unix_timestamp(now())*1000),
*/
/*

 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (GameItem gameItem : gameItems) {
			System.out.println(String.format("(%d, '%s', '%s', %d, '%s'),",
					gameItem.getGameId(),
					gameItem.getGameName(),
					gameItem.getGameInfo(),
					gameItem.getPicId(),
					gameItem.getGameCode()));
		}
	}
}