package ca.ubc.cs304.database;

import ca.ubc.cs304.model.Video;
import ca.ubc.cs304.model.Watch;
import ca.ubc.cs304.util.PrintablePreparedStatement;
import ca.ubc.cs304.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;

/**
 * This class handles all database related transactions
 */
// this code is coped from demo project for setup reason
// make sure everyone in the group have same setup right now
public class DatabaseConnectionHandler {
	// Use this version of the ORACLE_URL if you are running the code off of the server
//	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
	// Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";

	private Connection connection = null;

	public DatabaseConnectionHandler() {
		try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}
	// DELETE
	// remove video with given link
	public void deleteVideo(String videoLink) throws SQLException {
		try {
			String query = "DELETE FROM Video_postedBy_sponsoredBy WHERE videoLink = ?";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, videoLink);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Video " + videoLink + " does not exist!");
				throw new SQLException("Video does not exist");
			}

			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			throw e;
		}
	}

	// INSERT
	// insert given Video into Video_postedBy_sponsoredBy table
	public void insertVideo(Video videoPostBySponsoredBy) throws SQLException {
		try {
			String query = "INSERT INTO video_postedBy_sponsoredBy VALUES (?,?,?,?,?)";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, videoPostBySponsoredBy.getVideoLink());
			ps.setString(2, videoPostBySponsoredBy.getTitle());
			ps.setInt(3, videoPostBySponsoredBy.getLikes());
			ps.setString(4, videoPostBySponsoredBy.getEmail());
			if (videoPostBySponsoredBy.getSponsor() == "") {
				ps.setNull(5, Types.VARCHAR);
			} else {
				ps.setString(5, videoPostBySponsoredBy.getSponsor());
			}

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			throw e;
		}
	}

	public void insertWatch(Watch watch) {
		String videoLink = "";
		try {
			String query = "INSERT INTO Watch VALUES (?,?,?)";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, watch.getVideoLink());
			ps.setString(2, watch.getEmail());
			ps.setInt(3, watch.getWatchTime());

			videoLink = watch.getVideoLink();

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
		// System.out.println("check point 1");
		updateVideoPlus(videoLink);
		// System.out.println("check point 2");
	}

	public Watch[] getWatchInfo() {
		ArrayList<Watch> result = new ArrayList<Watch>();

		try {
			String query = "SELECT " + "*" + " FROM Watch";
			// System.out.println("check point 0");
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			// System.out.println("check point 1");
			while(rs.next()) {
				Watch model = new Watch(rs.getString("videoLink"),
						rs.getInt("watchTime"),
						rs.getString("email"));
				result.add(model);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}

		return result.toArray(new Watch[result.size()]);
	}

	public String[][] selectVideoInfo(String sql) throws SQLException{
		ArrayList<String[]> result = new ArrayList<>();

		try {
			String query = "SELECT * FROM video_postedBy_sponsoredBy WHERE" + " " + sql;
			// System.out.println("check point 0");
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			// System.out.println("check point 1");
			while(rs.next()) {
				String[] temp = {rs.getString("videoLink"),
						rs.getString("title"),
						rs.getString("likes"),
						rs.getString("email"),
						rs.getString("sponsorName")};
				result.add(temp);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			throw e;
		}

		return result.toArray(new String[result.size()][5]);
	}

	public String[][] projectTable(String table, String[] attributes) {
		ArrayList<String[]> result = new ArrayList<>();
		String[] desiredAttributes = attributes;
		int attributeCount = desiredAttributes.length;
		try {
			String query = "SELECT ";

			String bob = String.join(", ", desiredAttributes);
			query = query + bob + " FROM " + table;

			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				String[] temp = new String[attributeCount];
				for(int i = 0; i < attributeCount; i++) {
					temp[i] = (rs.getString(desiredAttributes[i]));
				}
				result.add(temp);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}

		return result.toArray(new String[result.size()][attributeCount]);

	}



	public Video[] getVideoInfo() {
		ArrayList<Video> result = new ArrayList<Video>();

		try {
			String query = "SELECT " + "*" + " FROM video_postedBy_sponsoredBy";
			// System.out.println("check point 0");
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			// System.out.println("check point 1");
			while(rs.next()) {
				Video model = new Video(rs.getString("videoLink"),
						rs.getString("title"),
						rs.getInt("likes"),
						rs.getString("email"),
						rs.getString("sponsorName"));
				result.add(model);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}

		return result.toArray(new Video[result.size()]);
	}

	public Video findVideoGivenLink(String vLink) {
		Video vid = null;
		try {
			String query = "SELECT " + "*" + " FROM video_postedBy_sponsoredBy WHERE videoLink = " + "'" + vLink + "'";
			// System.out.println("check point 0");
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			// System.out.println("check point 1");
			while(rs.next()) {
				vid = new Video(rs.getString("videoLink"),
						rs.getString("title"),
						rs.getInt("likes"),
						rs.getString("email"),
						rs.getString("sponsorName"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return vid;
	}

	public String[][] joinVideo(String vLink) throws SQLException {
		ArrayList<String[]> result = new ArrayList<>();

		try {
			String query = "SELECT Video_postedBy_sponsoredBy.videoLink, Video_postedBy_sponsoredBy.title" +
					", SUM(Watch.watchTime) AS TotalWatchTime FROM Video_postedBy_sponsoredBy"
					+ " JOIN Watch ON Video_postedBy_sponsoredBy.videoLink = Watch.videoLink WHERE Video_postedBy_sponsoredBy.videoLink = ?" +
					" GROUP BY Video_postedBy_sponsoredBy.videoLink, Video_postedBy_sponsoredBy.title";
			;
			// System.out.println("check point 0");
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, vLink);
			ResultSet rs = ps.executeQuery();

			// System.out.println("check point 1");
			while(rs.next()) {
				String[] temp = {rs.getString("videoLink"),
						rs.getString("title"),
						rs.getString("TotalWatchTime")};
				result.add(temp);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			throw e;
		}

		return result.toArray(new String[result.size()][3]);
	}

	public void updateVideo(String videoLink, String title, int likes, String email, String sp) throws SQLException {
		try {
			StringBuilder temp = new StringBuilder();
			boolean firstCondition = true;

			if (!title.isEmpty()) {
				temp.append(firstCondition ? "SET " : ", ");
				temp.append("title = '").append(title).append("'");
				firstCondition = false;
			}

			if (likes != -1) {
				temp.append(firstCondition ? "SET " : ", ");
				temp.append("likes = ").append(likes);
				firstCondition = false;
			}

			if (!email.isEmpty()) {
				temp.append(firstCondition ? "SET " : ", ");
				temp.append("email = '").append(email).append("'");
				firstCondition = false;
			}

			if (!sp.isEmpty()) {
				temp.append(firstCondition ? "SET " : ", ");
				temp.append("sponsorName = '").append(sp).append("'");
			}

			String query = "UPDATE Video_postedBy_sponsoredBy " + temp.toString() + " WHERE videoLink = ?";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, videoLink);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " videoLink " + videoLink + " error");
			}

			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			throw e;
		}
	}

	public void updateVideoPlus(String videoLink) {
		Video result = null;

		try {
			String query = "SELECT " + "*" + " FROM video_postedBy_sponsoredBy WHERE videoLink = ?";
			// System.out.println("check point 0");
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, videoLink);
			ResultSet rs = ps.executeQuery();

			// System.out.println("check point 1");
			while(rs.next()) {
				Video model = new Video(rs.getString("videoLink"),
						rs.getString("title"),
						rs.getInt("likes"),
						rs.getString("email"),
						rs.getString("sponsorName"));
				result = model;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}

		String vLink = result.getVideoLink();
		int vLikes = result.getLikes() + 1;


		try {
			StringBuilder temp = new StringBuilder();
			boolean firstCondition = true;

			temp.append(firstCondition ? "SET " : ", ");
			temp.append("likes = ").append(vLikes);

			String query = "UPDATE Video_postedBy_sponsoredBy " + temp.toString() + " WHERE videoLink = ?";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, vLink);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " videoLink " + vLink + " error");
			}

			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}

	}

	public boolean login(String username, String password) {
		try {
			if (connection != null) {
				connection.close();
			}

			connection = DriverManager.getConnection(ORACLE_URL, username, password);
			connection.setAutoCommit(false);

			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}

	private void rollbackConnection() {
		try  {
			connection.rollback();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public void databaseSetup() {
		try {
			File file = new File("./src/ca/ubc/cs304/sqlScripts/videoSystem.sql");
			BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String line = null;
			String ls = System.getProperty("line.separator");
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty() || line.startsWith("--")) {
					continue;
				}
				try {
					String q = line.substring(0, line.length() - 1);
					PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(q), q, false);

					ps.execute();
					connection.commit();
					ps.close();
				}
				catch (SQLException e) {
					System.out.println(EXCEPTION_TAG + " " + e.getMessage());
					rollbackConnection();
				}
			}
			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	// Aggregation using GROUP BY
	// return channel and all its accumulated likes
	public String[][] channelsTotalLikes() {
		ArrayList<String[]> result = new ArrayList<>();
		try {
			String query = "SELECT email AS accountEmail, SUM(likes) AS likesTotal FROM Video_postedBy_sponsoredBy GROUP BY email";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, true);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				String[] temp = {rs.getString("accountEmail"), Integer.toString(rs.getInt("likesTotal"))};
				result.add(temp);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new String[result.size()][2]);
	}

	// Aggregation using HAVING
	// return emails of channels with more than one subscriber
	public String[][] manySubscribers() {
		ArrayList<String[]> result = new ArrayList<>();
		try {
			String query = "SELECT channelEmail AS channelEmail, COUNT(*) AS subscriberCount FROM SubscribesTo GROUP BY channelEmail HAVING COUNT(*) > 1";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, true);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				String[] temp = {rs.getString("channelEmail"), Integer.toString(rs.getInt("subscriberCount"))};
				result.add(temp);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new String[result.size()][2]);
	}

	// Nested Aggregation
	// returns average watch time of most watched videos
	public int mostWatchedWatchTime() {
		int result = -1;
		try {
			String query = "SELECT AVG(watchTime) AS watchTime FROM WATCH W1 WHERE" +
					"(SELECT COUNT(*) FROM Watch W2 WHERE W1.videoLink = W2.videoLink) <= ALL" +
					"(SELECT COUNT(*) FROM Watch W3 GROUP BY W3.videoLink)";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, true);
			ResultSet rs = ps.executeQuery();

			rs.next();
			result = rs.getInt("watchTime");
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result;
	}

	// DIVISION
	// returns emails of accounts that have watched every video on the platform
	public String[] intentWatchers() {
		ArrayList<String> result = new ArrayList<>();
		try {
			String query =
					"SELECT W1.email AS email FROM Watch W1 " +
							"WHERE NOT EXISTS (SELECT V.videoLink FROM Video_postedBy_sponsoredBy V WHERE NOT EXISTS (SELECT * FROM Watch W2 WHERE W2.email = W1.email AND V.videoLink = W2.videoLink))";
			System.out.print(query);
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, true);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				System.out.print(rs.getString("email"));
				result.add(rs.getString("email"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result.toArray(new String[result.size()]);
	}

	public void insertAccount(Account a) {
		try {
			String query = "INSERT INTO Account_watches VALUES (?,?,?,?)";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ps.setString(1, a.getEmail());
			ps.setString(2, a.getAlias());
			ps.setString(3, a.getPassword());
			//ps.setString(4, a.getLivestreamLink());
			ps.setNull(4, Types.VARCHAR);

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}


	public void insertChannel(Channel c) {
		insertAccount(c);
		try {
			String query = "INSERT INTO Channel_owns VALUES (?,?,?,?)";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ps.setString(1,c.getEmail());
			ps.setInt(2, c.getUserID());
			ps.setString(3,c.getDescription());
			ps.setInt(4, c.getSubscribers());

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	public void insertUser(User u) {
		try {
			String query = "INSERT INTO User1 VALUES (?,?,?)";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);

			ps.setInt(1,u.getUserID());
			ps.setString(2, u.getFirstName());
			ps.setString(3,u.getLastName());

			ps.executeUpdate();
			connection.commit();

			ps.close();
		}
		catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

}
