package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.JoinVideo;
import ca.ubc.cs304.model.Video;
import ca.ubc.cs304.model.Watch;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.TerminalTransactions;

/**
 * This is the main controller class that will orchestrate everything.
 */
// this code is coped from demo project for setup reason
// make sure everyone in the group have same setup right now
public class VideoSystem implements LoginWindowDelegate, TerminalTransactionsDelegate {
	private DatabaseConnectionHandler dbHandler = null;
	private LoginWindow loginWindow = null;

	public VideoSystem() {
		dbHandler = new DatabaseConnectionHandler();
	}
	
	private void start() {
		loginWindow = new LoginWindow();
		loginWindow.showFrame(this);
	}
	
	/**
	 * LoginWindowDelegate Implementation
	 * 
     * connects to Oracle database with supplied username and password
     */ 
	public void login(String username, String password) {
		// easy login for test
		// boolean didConnect = dbHandler.login("ora_dennis58", "a47936935");
		boolean didConnect = dbHandler.login(username, password);
		if (didConnect) {
			// Once connected, remove login window and start text transaction flow
			loginWindow.dispose();

			TerminalTransactions transaction = new TerminalTransactions();
			transaction.setupDatabase(this); // can be set up in SQLPULS in VsCode
			transaction.showMainMenu(this);
		} else {
			loginWindow.handleLoginFailed();

			if (loginWindow.hasReachedMaxLoginAttempts()) {
				loginWindow.dispose();
				System.out.println("You have exceeded your number of allowed attempts");
				System.exit(-1);
			}
		}
	}
	
	/**
	 * TermainalTransactionsDelegate Implementation
	 * 
	 * Insert a branch with the given info
	 */
    public void insertVideo(Video video) {
//    	dbHandler.insertVideo(video);
    }

	public void insertWatch(Watch watch) {
		dbHandler.insertWatch(watch);
	}

    /**
	 * TermainalTransactionsDelegate Implementation
	 * 
	 * Delete branch with given branch ID.
	 */ 
    public void deleteVideo(String videoLink) {
//    	dbHandler.deleteVideo(videoLink);
    }
    
    /**
	 * TermainalTransactionsDelegate Implementation
	 * 
	 * Update the branch name for a specific ID
	 */

	public void joinVideo(String vLink) {
//		JoinVideo[] joinVideos = dbHandler.joinVideo(vLink);
//
//		// System.out.println("check point 1" + videos.length);
//		for (int i = 0; i < joinVideos.length; i++) {
//			JoinVideo video = joinVideos[i];
//			System.out.printf("%-20.20s", video.getVideoLink());
//			System.out.printf("%-30.30s", video.getTitle());
//			System.out.printf("%-30.30s", video.getTotalWatchTime());
//			System.out.println();
//		}
	}
    public void updateVideo(String videoLink, String title, int likes, String email, String sponsorName) {
//    	dbHandler.updateVideo(videoLink, title, likes, email, sponsorName);
    }

	public String[][] channelsTotalLikes() {
		return dbHandler.channelsTotalLikes();
	}

	public String[][] manySubscribers() {
		return dbHandler.manySubscribers();
	}

	public int mostWatchedWatchTime() {
		return dbHandler.mostWatchedWatchTime();
	}

	public String[] intentWatchers() {
		return dbHandler.intentWatchers();
	}

	public String[][] projectTable(String table, String[] attributes) { return dbHandler.projectTable(table, attributes);}

    /**
	 * TermainalTransactionsDelegate Implementation
	 * 
	 * Displays information about varies bank branches.
	 */
//	public void selectionVideo(String sql) {
//		Video[] videos = dbHandler.selectVideoInfo(sql);
//
//		for (int i = 0; i < videos.length; i++) {
//			Video video = videos[i];
//			System.out.printf("%-30.30s", video.getTitle());
//			System.out.printf("%-20.20s", video.getVideoLink());
//			System.out.printf("%-20.20s", video.getEmail());
//			System.out.printf("%-20.20s", video.getLikes());
//			if (video.getSponsor() == null) {
//				System.out.printf("%-20.20s", " ");
//			} else {
//				System.out.printf("%-20.20s", video.getSponsor());
//			}
//
//			System.out.println();
//		}
//	}

	public void projectVideo(String sql) {
		Video[] videos = dbHandler.getVideoInfo();

		// System.out.println("check point 1" + videos.length);
		for (int i = 0; i < videos.length; i++) {
			Video video = videos[i];
			System.out.printf("%-30.30s", video.getTitle());
			System.out.printf("%-20.20s", video.getVideoLink());
			System.out.printf("%-20.20s", video.getEmail());
			System.out.printf("%-20.20s", video.getLikes());
			if (video.getSponsor() == null) {
				System.out.printf("%-20.20s", " ");
			} else {
				System.out.printf("%-20.20s", video.getSponsor());
			}

			System.out.println();
		}
	}

    public void showVideo() {
		Video[] videos = dbHandler.getVideoInfo();

		// System.out.println("check point 1" + videos.length);
		for (int i = 0; i < videos.length; i++) {
			// System.out.println("check point 2");
			Video video = videos[i];
			// System.out.println("check point 3");
			System.out.printf("%-30.30s", video.getTitle());
			System.out.printf("%-20.20s", video.getVideoLink());
			System.out.printf("%-20.20s", video.getEmail());
			System.out.printf("%-20.20s", video.getLikes());
			if (video.getSponsor() == null) {
				System.out.printf("%-20.20s", " ");
			} else {
				System.out.printf("%-20.20s", video.getSponsor());
			}

			System.out.println();
		}

		Watch[] watchs = dbHandler.getWatchInfo();

		for (int i = 0; i < watchs.length; i++) {
			// System.out.println("check point 2");
			Watch watch = watchs[i];
			// System.out.println("check point 3");
			System.out.printf("%-30.30s", watch.getVideoLink());
			System.out.printf("%-20.20s", watch.getEmail());
			System.out.printf("%-20.20s", watch.getWatchTime());
			System.out.println();
		}
    }
	
    /**
	 * TerminalTransactionsDelegate Implementation
	 * 
     * The TerminalTransaction instance tells us that it is done with what it's 
     * doing so we are cleaning up the connection since it's no longer needed.
     */ 
    public void terminalTransactionsFinished() {
    	dbHandler.close();
    	dbHandler = null;
    	
    	System.exit(0);
    }
    
    /**
	 * TerminalTransactionsDelegate Implementation
	 * 
     * The TerminalTransaction instance tells us that the user is fine with dropping any existing table
     * called branch and creating a new one for this project to use
     */ 
	public void databaseSetup() {
		dbHandler.databaseSetup();;
		
	}
    
	/**
	 * Main method called at launch time
	 */
	public static void main(String args[]) {
		VideoSystem videoSystem = new VideoSystem();
		videoSystem.start();
	}
}
