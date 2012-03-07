package conch.yaoms.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import conch.yaoms.test.douwanFiles.Game;
import conch.yaoms.test.douwanFiles.GameFile;
import conch.yaoms.test.douwanFiles.GameHall;
import conch.yaoms.test.douwanFiles.GamePic;
import conch.yaoms.test.douwanFiles.GameSerivce;

public class InsertUtils {

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) {
		try {

			// insertDouwan(true);

			insertNewGameFiles(true);
			// insertGameHall(true);

			// asciiTest();

			// modifyUser(true);

			testDB();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void modifyUser(boolean insert)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Connection connection = getConnection();

		Statement statement1 = connection.createStatement();

		ResultSet userSet = statement1
				.executeQuery("select user_id from dw_user order by user_id");

		PreparedStatement updateStatement = connection
				.prepareStatement("update dw_user set is_online=?,lltime=?,loc_id=?,game_id=?, game_mode=?,"
						+ " leave_train_count=?,powers=?,lv=?,exp=?,beans=?,freeze_beans=?,lotteries=? where user_id=?");

		while (userSet.next()) {
			int userId = userSet.getInt(1);
			System.out.println("更新用户" + userId);
			Statement statement2 = connection.createStatement();
			ResultSet state = statement2
					.executeQuery("select is_online,lltime,loc_id,game_id, game_mode, leave_train_count,powers from dw_user_state where user_id="
							+ userId);
			state.next();
			updateStatement.setInt(1, state.getInt(1));
			updateStatement.setLong(2, state.getLong(2));
			updateStatement.setInt(3, state.getInt(3));
			updateStatement.setInt(4, state.getInt(4));
			updateStatement.setInt(5, state.getInt(5));
			updateStatement.setInt(6, state.getInt(6));
			updateStatement.setInt(7, state.getInt(7));

			Statement statement3 = connection.createStatement();
			ResultSet wealth = statement3
					.executeQuery("select lv,exp,beans,freeze_beans,lotteries from dw_user_wealth where user_id="
							+ userId);
			wealth.next();

			updateStatement.setInt(8, wealth.getInt(1));
			updateStatement.setInt(9, wealth.getInt(2));
			updateStatement.setInt(10, wealth.getInt(3));
			updateStatement.setInt(11, wealth.getInt(4));
			updateStatement.setInt(12, wealth.getInt(5));
			updateStatement.setInt(13, userId);

			try {
				updateStatement.execute();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	public static void insertGameHall(boolean insert) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		int adpId = 2;

		Connection connection = getConnection();
		if (connection != null) {

			List<Game> games = new ArrayList<Game>();

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select * from dw_game order by game_id;");
			Game game = null;
			List<GameFile> gameFiles = null;
			GameFile gameFile = null;
			while (resultSet.next()) {
				game = new Game();
				game.setGameId(resultSet.getInt("game_id"));
				game.setEntry(resultSet.getString("entry"));
				game.setName(resultSet.getString("name"));
				game.setInfo(resultSet.getString("info"));
				game.setPic(resultSet.getInt("pic"));
				// 获取当前平台最新版本
				Statement statement2 = connection.createStatement();
				ResultSet resultSet2 = statement2
						.executeQuery("select max(version) version from dw_game_version where game_id="
								+ game.getGameId() + " and adp_id=" + adpId);
				resultSet2.next();
				game.setVersion(resultSet2.getInt("version"));
				// 获取当前平台最新版本的文件
				gameFiles = new ArrayList<GameFile>();
				Statement statement3 = connection.createStatement();
				ResultSet resultSet3 = statement3
						.executeQuery("select * from dw_game_files where game_id="
								+ game.getGameId()
								+ " and adp_id="
								+ adpId
								+ " and version=" + game.getVersion());
				while (resultSet3.next()) {
					gameFile = new GameFile();
					gameFile.setSize(resultSet3.getInt("file_size"));
					gameFile.setName(resultSet3.getString("file_name"));
					gameFile.setUrl(resultSet3.getString("file_url"));
					gameFiles.add(gameFile);
				}
				game.setFiles(gameFiles);
				games.add(game);
			}

			GamePic gamePic = new GamePic();

			statement = connection.createStatement();
			resultSet = statement
					.executeQuery("select * from dw_game_pics where adp_id="
							+ adpId + " order by version desc limit 1");
			resultSet.next();
			gamePic.setAdpId(adpId);
			gamePic.setSize(resultSet.getInt("size"));
			gamePic.setUrl(resultSet.getString("url"));
			gamePic.setVersion(resultSet.getInt("version"));

			int version = 0;
			statement = connection.createStatement();
			resultSet = statement
					.executeQuery("select max(version)+1 version from dw_game_hall where adp_id="
							+ adpId);
			resultSet.next();
			version = resultSet.getInt("version");

			PreparedStatement preparedStatement = connection
					.prepareStatement("insert into dw_game_hall (adp_id, version, xml_data) values (?, ?, ?)");

			GameHall gameHall = GameSerivce
					.getGameHall(version, gamePic, games);
			System.out.println("游戏大厅 插入数据\n" + gameHall.toString());

			preparedStatement.setInt(1, adpId);
			preparedStatement.setInt(2, gameHall.getVersion());
			preparedStatement.setString(3, gameHall.toString());

			if (insert) {
				preparedStatement.execute();
			}

			connection.close();

			System.out.println("游戏大厅 插入完毕");
		} else {
			System.err.println("不能连接到数据库");
		}
	}

	public static void insertNewGameFiles(boolean insert) throws SQLException,
			MalformedURLException, IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		Connection connection = getConnection();
		if (connection != null) {

			int adpId = 2;

			// /////////需要插入的文件
			List<GameFile> gameFiles = new ArrayList<GameFile>();

			// GameFile eggKkrFile = new GameFile();
			// eggKkrFile.setAdpId(adpId);
			// eggKkrFile.setGameId(0);
			// eggKkrFile.setName("egg.kkr");
			// eggKkrFile
			// .setUrl("http://dw3.tisgame.cn/g/egg/20120223/egg.kkr.zip");
			// eggKkrFile.setSize(getFileSize(eggKkrFile.getUrl()));
			// eggKkrFile.setUtime(System.currentTimeMillis());
			// gameFiles.add(eggKkrFile);
			//
			// GameFile eggBinFile = new GameFile();
			// eggBinFile.setAdpId(adpId);
			// eggBinFile.setGameId(0);
			// eggBinFile.setName("egg.bin");
			// eggBinFile
			// .setUrl("http://dw3.tisgame.cn/g/egg/20120223/egg.bin.zip");
			// eggBinFile.setSize(getFileSize(eggBinFile.getUrl()));
			// eggBinFile.setUtime(System.currentTimeMillis());
			// gameFiles.add(eggBinFile);
			//
			// GameFile pplKkrFile = new GameFile();
			// pplKkrFile.setAdpId(adpId);
			// pplKkrFile.setGameId(1);
			// pplKkrFile.setName("ppl.kkr");
			// pplKkrFile
			// .setUrl("http://dw3.tisgame.cn/g/ppl/20120223/ppl.kkr.zip");
			// pplKkrFile.setSize(getFileSize(pplKkrFile.getUrl()));
			// pplKkrFile.setUtime(System.currentTimeMillis());
			// gameFiles.add(pplKkrFile);
			//
			// GameFile pplBinFile = new GameFile();
			// pplBinFile.setAdpId(adpId);
			// pplBinFile.setGameId(1);
			// pplBinFile.setName("ppl.bin");
			// pplBinFile
			// .setUrl("http://dw3.tisgame.cn/g/ppl/20120223/ppl.bin.zip");
			// pplBinFile.setSize(getFileSize(pplBinFile.getUrl()));
			// pplBinFile.setUtime(System.currentTimeMillis());
			// gameFiles.add(pplBinFile);

			GameFile escapeKkrFile = new GameFile();
			escapeKkrFile.setAdpId(adpId);
			escapeKkrFile.setGameId(2);
			escapeKkrFile.setName("escape.kkr");
			escapeKkrFile
					.setUrl("http://dw3.tisgame.cn/g/escape/20120223-1/escape.kkr.zip");
			escapeKkrFile.setSize(getFileSize(escapeKkrFile.getUrl()));
			escapeKkrFile.setUtime(System.currentTimeMillis());
			gameFiles.add(escapeKkrFile);

			GameFile escapeBinFile = new GameFile();
			escapeBinFile.setAdpId(adpId);
			escapeBinFile.setGameId(2);
			escapeBinFile.setName("escape.bin");
			escapeBinFile
					.setUrl("http://dw3.tisgame.cn/g/escape/20120223-1/escape.bin.zip");
			escapeBinFile.setSize(getFileSize(escapeBinFile.getUrl()));
			escapeBinFile.setUtime(System.currentTimeMillis());
			gameFiles.add(escapeBinFile);

			// GameFile moleKkrFile = new GameFile();
			// moleKkrFile.setAdpId(adpId);
			// moleKkrFile.setGameId(3);
			// moleKkrFile.setName("mole.kkr");
			// moleKkrFile
			// .setUrl("http://dw3.tisgame.cn/g/mole/20120223/mole.kkr.zip");
			// moleKkrFile.setSize(getFileSize(moleKkrFile.getUrl()));
			// moleKkrFile.setUtime(System.currentTimeMillis());
			// gameFiles.add(moleKkrFile);
			//
			// GameFile moleBinFile = new GameFile();
			// moleBinFile.setAdpId(adpId);
			// moleBinFile.setGameId(3);
			// moleBinFile.setName("mole.bin");
			// moleBinFile
			// .setUrl("http://dw3.tisgame.cn/g/mole/20120223/mole.bin.zip");
			// moleBinFile.setSize(getFileSize(moleBinFile.getUrl()));
			// moleBinFile.setUtime(System.currentTimeMillis());
			// gameFiles.add(moleBinFile);
			//
			// GameFile bsbollKkrFile = new GameFile();
			// bsbollKkrFile.setAdpId(adpId);
			// bsbollKkrFile.setGameId(4);
			// bsbollKkrFile.setName("bsboll.kkr");
			// bsbollKkrFile
			// .setUrl("http://dw3.tisgame.cn/g/bsboll/20120223/bsboll.kkr.zip");
			// bsbollKkrFile.setSize(getFileSize(bsbollKkrFile.getUrl()));
			// bsbollKkrFile.setUtime(System.currentTimeMillis());
			// gameFiles.add(bsbollKkrFile);
			//
			// GameFile bsbollBinFile = new GameFile();
			// bsbollBinFile.setAdpId(adpId);
			// bsbollBinFile.setGameId(4);
			// bsbollBinFile.setName("bsboll.bin");
			// bsbollBinFile
			// .setUrl("http://dw3.tisgame.cn/g/bsboll/20120223/bsboll.bin.zip");
			// bsbollBinFile.setSize(getFileSize(bsbollBinFile.getUrl()));
			// bsbollBinFile.setUtime(System.currentTimeMillis());
			// gameFiles.add(bsbollBinFile);

			PreparedStatement preparedStatementForNewGameVersion = connection
					.prepareStatement("select max(version)+1 from dw_game_version where adp_id=? and game_id=?");

			PreparedStatement preparedStatementForInsertVersion = connection
					.prepareStatement("insert into dw_game_version (version,game_id,adp_id,mtime) values (?,?,?,?)");

			PreparedStatement preparedStatementForInsertFile = connection
					.prepareStatement("insert into dw_game_files (version,game_id,adp_id,file_name,file_size,file_url,utime) values (?,?,?,?,?,?,?)");

			Map<Integer, Integer> insertedVersionGameIds = new HashMap<Integer, Integer>();
			ResultSet resultSet = null;
			for (GameFile gameFile : gameFiles) {
				if (!insertedVersionGameIds.containsKey(gameFile.getGameId())) {
					// 插入游戏新版本
					preparedStatementForNewGameVersion.setInt(1,
							gameFile.getAdpId());
					preparedStatementForNewGameVersion.setInt(2,
							gameFile.getGameId());

					resultSet = preparedStatementForNewGameVersion
							.executeQuery();

					resultSet.next();
					int newVersion = resultSet.getInt(1);

					preparedStatementForInsertVersion.setInt(1, newVersion);
					preparedStatementForInsertVersion.setInt(2,
							gameFile.getGameId());
					preparedStatementForInsertVersion.setInt(3,
							gameFile.getAdpId());
					preparedStatementForInsertVersion.setLong(4,
							System.currentTimeMillis());

					if (insert) {
						preparedStatementForInsertVersion.execute();
					}

					insertedVersionGameIds
							.put(gameFile.getGameId(), newVersion);
				}

				preparedStatementForInsertFile.setInt(1,
						insertedVersionGameIds.get(gameFile.getGameId()));
				preparedStatementForInsertFile.setInt(2, gameFile.getGameId());
				preparedStatementForInsertFile.setInt(3, gameFile.getAdpId());
				preparedStatementForInsertFile.setString(4, gameFile.getName());
				preparedStatementForInsertFile.setInt(5, gameFile.getSize());
				preparedStatementForInsertFile.setString(6, gameFile.getUrl());
				preparedStatementForInsertFile.setLong(7,
						System.currentTimeMillis());

				if (insert) {
					preparedStatementForInsertFile.execute();
				}

			}

			connection.close();

			System.out.println("新游戏文件 插入完毕");
		} else {
			System.err.println("不能连接到数据库");
		}
	}

	public static void insertDouwan(boolean insert) throws SQLException,
			MalformedURLException, IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		Connection connection = getConnection();
		if (connection != null) {

			int adpId = 2;

			int version = 0;
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select max(version)+1 version from dw_douwan_ver where adp_id="
							+ adpId);
			resultSet.next();
			version = resultSet.getInt("version");

			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO dw_douwan_ver (adp_id, version, xml_data) VALUES (?, ?, ?)");

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("<filelist>\n");

			// ////////////////////////////////////////
			// ///////////
			// ////////////////////////////////////////
			GameFile fontFile = new GameFile();
			fontFile.setName("simsun14");
			fontFile.setUrl("http://dw3.tisgame.cn/f/20111128/simsun14.zip");
			fontFile.setSize(getFileSize(fontFile.getUrl()));
			stringBuffer.append("  ").append(fontFile.toString()).append('\n');

			GameFile resFile = new GameFile();
			resFile.setName("resall");
			resFile.setUrl("http://dw3.tisgame.cn/f/20120302-1/res.bin");
			resFile.setSize(getFileSize(resFile.getUrl()));
			stringBuffer.append("  ").append(resFile.toString()).append('\n');

			GameFile kkrFile = new GameFile();
			kkrFile.setName("douwan.kkr");
			kkrFile.setUrl("http://dw3.tisgame.cn/f/20120302-1/douwan.kkr.zip");
			kkrFile.setSize(getFileSize(kkrFile.getUrl()));
			stringBuffer.append("  ").append(kkrFile.toString()).append('\n');
			// ////////////////////////////////////////
			// ///////////
			// ////////////////////////////////////////

			stringBuffer.append("</filelist>\n");

			String xmlData = stringBuffer.toString();

			System.out.println("新版本：" + version);

			System.out.println("游戏大厅 插入数据\n" + xmlData);

			preparedStatement.setInt(1, adpId);
			preparedStatement.setInt(2, version);
			preparedStatement.setString(3, xmlData);

			if (insert) {
				preparedStatement.execute();
			}

			connection.close();

			System.out.println("游戏大厅 插入完毕");
		} else {
			System.err.println("不能连接到数据库");
		}
	}

	private static int getFileSize(String url) throws MalformedURLException,
			IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url)
				.openConnection();
		connection.setRequestMethod("HEAD");
		return connection.getContentLength();
	}

	public static void testDB() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		Connection connection = getConnection();

		Statement statement = connection.createStatement();

		ResultSet resultSet = statement
				.executeQuery("select * from douwan3.dw_game where name like '炸弹%'");

		System.out.println(resultSet.getFetchSize());
		while (resultSet.next()) {
			System.out.println(resultSet.getInt(1));
			System.out.println(resultSet.getString(2));
			System.out.println(resultSet.getString(3));
			System.out.println(resultSet.getString(4));
			System.out.println(resultSet.getString(5));
			System.out.println(resultSet.getString(6));
		}

		connection.close();
	}

	private static Connection getConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		String dbDriver = "com.mysql.jdbc.Driver";

		// String dbUrl =
		// "jdbc:mysql://127.0.0.1:3306/douwan3?useUnicode=true&characterEncoding=UTF-8";
		// String dbUrl =
		// "jdbc:mysql://dw3.tisgame.cn:3306/douwan3?useUnicode=true&characterEncoding=UTF-8";
		String dbUrl = "jdbc:mysql://116.204.67.182:3306/douwan3?useUnicode=true&characterEncoding=UTF-8";
		// String dbUser = "douwan";
		// String dbPassword = "dw123456";
		String dbUser = "root";
		String dbPassword = "kkfun";
		Class.forName(dbDriver).newInstance();
		Connection connection = DriverManager.getConnection(dbUrl, dbUser,
				dbPassword);
		return connection;
	}

	public static void asciiTest() {
		System.out.printf("%1$d:%1$c:\n", 32);
	}

	public static void proper() throws InvalidPropertiesFormatException,
			IOException {
		Properties properties = new Properties();
		properties.loadFromXML(InsertUtils.class
				.getResourceAsStream("/conch/yaoms/test/provs.xml"));
	}

	public static void updateAddress() throws InvalidPropertiesFormatException,
			IOException, SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		Properties properties = new Properties();
		properties.loadFromXML(InsertUtils.class
				.getResourceAsStream("provs.xml"));

		Connection connection = getConnection();
		if (connection != null) {

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select user_id, prov, city from dw_user where prov!=-1 and city!=-1");

			int userId = -1;
			int prov = -1;
			int city = -1;
			String address = null;

			PreparedStatement preparedStatement = connection
					.prepareStatement("update dw_user set address=? where user_id=?");

			while (resultSet.next()) {
				userId = resultSet.getInt(1);
				prov = resultSet.getInt(2);
				city = resultSet.getInt(3);

				address = properties.getProperty(String.format("addr.%d.%d",
						prov, city));

				System.out.println(String.format("user %d, is from %s", userId,
						address));

				preparedStatement.setString(1, address);
				preparedStatement.setInt(2, userId);
				preparedStatement.execute();
			}

			connection.close();

		} else {
			System.err.println("不能连接到数据库");
		}
	}

}
