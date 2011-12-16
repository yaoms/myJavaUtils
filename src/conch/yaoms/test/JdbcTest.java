package conch.yaoms.test;

import java.io.IOException;
import java.net.HttpURLConnection;
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

public class JdbcTest {

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) {
		try {
			// insertDouwan(false);
			// insertGameHall(false);
			// insertNewGameFiles(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void proper() {
		Properties properties = new Properties();
		try {
			properties.loadFromXML(JdbcTest.class
					.getResourceAsStream("/conch/yaoms/test/provs.xml"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void updateAddress() {

		Properties properties = new Properties();
		try {
			properties.loadFromXML(JdbcTest.class
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

					address = properties.getProperty(String.format(
							"addr.%d.%d", prov, city));

					System.out.println(String.format("user %d, is from %s",
							userId, address));

					preparedStatement.setString(1, address);
					preparedStatement.setInt(2, userId);
					preparedStatement.execute();
				}

				connection.close();

			} else {
				System.err.println("不能连接到数据库");
			}
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void insertGameHall(boolean insert) throws SQLException {

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

	public static void insertNewGameFiles(boolean insert) throws SQLException {
		Connection connection = getConnection();
		if (connection != null) {

			int adpId = 2;

			// /////////需要插入的文件
			List<GameFile> gameFiles = new ArrayList<GameFile>();

			GameFile eggKkrFile = new GameFile();
			eggKkrFile.setAdpId(adpId);
			eggKkrFile.setGameId(0);
			eggKkrFile.setName("egg.kkr");
			eggKkrFile
					.setUrl("http://211.154.137.11/g/egg/20111215/egg.kkr.zip");
			eggKkrFile.setSize(getFileSize(eggKkrFile.getUrl()));
			eggKkrFile.setUtime(System.currentTimeMillis());
			gameFiles.add(eggKkrFile);

			GameFile eggBinFile = new GameFile();
			eggBinFile.setAdpId(adpId);
			eggBinFile.setGameId(0);
			eggBinFile.setName("egg.bin");
			eggBinFile
					.setUrl("http://211.154.137.11/g/egg/20111215/egg.bin.zip");
			eggBinFile.setSize(getFileSize(eggBinFile.getUrl()));
			eggBinFile.setUtime(System.currentTimeMillis());
			gameFiles.add(eggBinFile);

			GameFile pplKkrFile = new GameFile();
			pplKkrFile.setAdpId(adpId);
			pplKkrFile.setGameId(1);
			pplKkrFile.setName("ppl.kkr");
			pplKkrFile
					.setUrl("http://211.154.137.11/g/ppl/20111215/ppl.kkr.zip");
			pplKkrFile.setSize(getFileSize(pplKkrFile.getUrl()));
			pplKkrFile.setUtime(System.currentTimeMillis());
			gameFiles.add(pplKkrFile);

			GameFile pplBinFile = new GameFile();
			pplBinFile.setAdpId(adpId);
			pplBinFile.setGameId(1);
			pplBinFile.setName("ppl.bin");
			pplBinFile
					.setUrl("http://211.154.137.11/g/ppl/20111215/ppl.bin.zip");
			pplBinFile.setSize(getFileSize(pplBinFile.getUrl()));
			pplBinFile.setUtime(System.currentTimeMillis());
			gameFiles.add(pplBinFile);

			GameFile escapeKkrFile = new GameFile();
			escapeKkrFile.setAdpId(adpId);
			escapeKkrFile.setGameId(2);
			escapeKkrFile.setName("escape.kkr");
			escapeKkrFile
					.setUrl("http://211.154.137.11/g/escape/20111215/escape.kkr.zip");
			escapeKkrFile.setSize(getFileSize(escapeKkrFile.getUrl()));
			escapeKkrFile.setUtime(System.currentTimeMillis());
			gameFiles.add(escapeKkrFile);

			GameFile escapeBinFile = new GameFile();
			escapeBinFile.setAdpId(adpId);
			escapeBinFile.setGameId(2);
			escapeBinFile.setName("escape.bin");
			escapeBinFile
					.setUrl("http://211.154.137.11/g/escape/20111215/escape.bin.zip");
			escapeBinFile.setSize(getFileSize(escapeBinFile.getUrl()));
			escapeBinFile.setUtime(System.currentTimeMillis());
			gameFiles.add(escapeBinFile);

			GameFile moleKkrFile = new GameFile();
			moleKkrFile.setAdpId(adpId);
			moleKkrFile.setGameId(3);
			moleKkrFile.setName("mole.kkr");
			moleKkrFile
					.setUrl("http://211.154.137.11/g/mole/20111215/mole.kkr.zip");
			moleKkrFile.setSize(getFileSize(moleKkrFile.getUrl()));
			moleKkrFile.setUtime(System.currentTimeMillis());
			gameFiles.add(moleKkrFile);

			GameFile moleBinFile = new GameFile();
			moleBinFile.setAdpId(adpId);
			moleBinFile.setGameId(3);
			moleBinFile.setName("mole.bin");
			moleBinFile
					.setUrl("http://211.154.137.11/g/mole/20111215/mole.bin.zip");
			moleBinFile.setSize(getFileSize(moleBinFile.getUrl()));
			moleBinFile.setUtime(System.currentTimeMillis());
			gameFiles.add(moleBinFile);

			GameFile bsbollKkrFile = new GameFile();
			bsbollKkrFile.setAdpId(adpId);
			bsbollKkrFile.setGameId(4);
			bsbollKkrFile.setName("bsboll.kkr");
			bsbollKkrFile
					.setUrl("http://211.154.137.11/g/bsboll/20111215/bsboll.kkr.zip");
			bsbollKkrFile.setSize(getFileSize(bsbollKkrFile.getUrl()));
			bsbollKkrFile.setUtime(System.currentTimeMillis());
			gameFiles.add(bsbollKkrFile);

			GameFile bsbollBinFile = new GameFile();
			bsbollBinFile.setAdpId(adpId);
			bsbollBinFile.setGameId(4);
			bsbollBinFile.setName("bsboll.bin");
			bsbollBinFile
					.setUrl("http://211.154.137.11/g/bsboll/20111215/bsboll.bin.zip");
			bsbollBinFile.setSize(getFileSize(bsbollBinFile.getUrl()));
			bsbollBinFile.setUtime(System.currentTimeMillis());
			gameFiles.add(bsbollBinFile);

			PreparedStatement preparedStatementForNewGameVersion = connection
					.prepareStatement("select max(version)+1 from dw_game_version where adp_id=? and game_id=?");

			PreparedStatement preparedStatementForInsertVersion = connection
					.prepareStatement("insert into dw_game_version values (?,?,?,?)");

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

	public static void insertDouwan(boolean insert) throws SQLException {
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
			fontFile.setUrl("http://211.154.137.11/f/20111128/simsun14.zip");
			fontFile.setSize(getFileSize(fontFile.getUrl()));
			stringBuffer.append("  ").append(fontFile.toString()).append('\n');

			GameFile resFile = new GameFile();
			resFile.setName("res.bin");
			resFile.setUrl("http://211.154.137.11/f/20111215/res.bin.zip");
			resFile.setSize(getFileSize(resFile.getUrl()));
			stringBuffer.append("  ").append(resFile.toString()).append('\n');

			GameFile kkrFile = new GameFile();
			kkrFile.setName("douwan.kkr");
			kkrFile.setUrl("http://211.154.137.11/f/20111215/douwan.kkr.zip");
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

	private static int getFileSize(String url) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url)
					.openConnection();
			connection.setRequestMethod("HEAD");
			return connection.getContentLength();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private static Connection getConnection() {

		try {
			String dbDriver = "com.mysql.jdbc.Driver";

			// String dbUrl =
			// "jdbc:mysql://127.0.0.1:3306/douwan3?useUnicode=true&characterEncoding=UTF-8";
			String dbUrl = "jdbc:mysql://211.154.137.11:3306/douwan3?useUnicode=true&characterEncoding=UTF-8";
			String dbUser = "douwan";
			String dbPassword = "dw123456";
			Class.forName(dbDriver).newInstance();
			Connection connection = DriverManager.getConnection(dbUrl, dbUser,
					dbPassword);
			return connection;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void asciiTest() {
		for (int i = 0; i < 256; i++) {
			System.out.printf("%d: %c\n", i, i);
		}
	}
}
