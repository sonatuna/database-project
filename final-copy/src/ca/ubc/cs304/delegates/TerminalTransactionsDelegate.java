package ca.ubc.cs304.delegates;

import ca.ubc.cs304.model.Video;
import ca.ubc.cs304.model.Watch;

/**
 * This interface uses the delegation design pattern where instead of having
 * the TerminalTransactions class try to do everything, it will only
 * focus on handling the UI. The actual logic/operation will be delegated to the 
 * controller class (in this case Bank).
 * 
 * TerminalTransactions calls the methods that we have listed below but 
 * Bank is the actual class that will implement the methods.
 */
// this code is coped from demo project for setup reason
// make sure everyone in the group have same setup right now
public interface TerminalTransactionsDelegate {
	public void databaseSetup();
	public void joinVideo(String vLink);
	public void deleteVideo(String videoLink);
	public void insertVideo(Video videoPostBySponsoredBy);
	public void insertWatch(Watch watch);
	public void showVideo();
	public String[][] projectTable(String table, String[] attributes); // projection
	// public void selectionVideo(String sql);
	public void updateVideo(String videoLink, String title, int likes, String email, String sponsorName);

	public String[][] channelsTotalLikes(); // Aggregation with GROUP BY

	public String[][] manySubscribers(); // Aggregation using HAVING

	public int mostWatchedWatchTime(); // Nested Aggregation // returns average watch time of most watched videos

	public String[] intentWatchers(); // DIVISION // returns emails of accounts that have watched every video on the platform
	
	public void terminalTransactionsFinished();
}
