package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.Video;
import ca.ubc.cs304.model.Watch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The class is only responsible for handling terminal text inputs. 
 */
// this code is coped from demo project for setup reason
// make sure everyone in the group have same setup right now
public class TerminalTransactions {
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	private static final int INVALID_INPUT = Integer.MIN_VALUE;
	private static final int EMPTY_INPUT = 0;
	private BufferedReader bufferedReader = null;
	private TerminalTransactionsDelegate delegate = null;

	public TerminalTransactions() {

	}
	
	/**
	 * Sets up the database to have a branch table with two tuples so we can insert/update/delete from it.
	 * Refer to the databaseSetup.sql file to determine what tuples are going to be in the table.
	 */

	public void setupDatabase(TerminalTransactionsDelegate delegate) {
		this.delegate = delegate;
		
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int choice = INVALID_INPUT;
		
		while(choice != 1 && choice != 2) {
			System.out.println("If you have a table called Branch in your database (capitialization of the name does not matter), it will be dropped and a new Branch table will be created.\nIf you want to proceed, enter 1; if you want to quit, enter 2.");
			choice = readInteger(false);
			if (choice != INVALID_INPUT) {
				switch (choice) {
				case 1:  
					delegate.databaseSetup();
					break;
				case 2:  
					handleQuitOption();
					break;
				default:
					System.out.println(WARNING_TAG + " The number that you entered was not a valid option.\n");
					break;
				}
			}
		}
	}

	/**
	 * Displays simple text interface
	 */

	public void showMainMenu(TerminalTransactionsDelegate delegate) {
		this.delegate = delegate;
		
	    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int choice = INVALID_INPUT;
		
		while (choice != 5) {
			System.out.println();
			System.out.println("1. Insert Video");
			System.out.println("6. Insert Watch");
			System.out.println("2. Delete Video");
			System.out.println("3. Update Video");
			System.out.println("4. Show Video and Watch");
			// System.out.println("7. Selection");
			System.out.println("5. Quit");
			System.out.print("Please choose one of the above 5 options: ");

			choice = readInteger(false);

			System.out.println(" ");

			if (choice != INVALID_INPUT) {
				switch (choice) {
				case 1:  
					handleInsertVideo();
					break;
				case 2:  
					handleDeleteOption(); 
					break;
				case 3: 
					handleUpdateOption();
					break;
				case 4:  
					delegate.showVideo();
					break;
				case 5:
					handleQuitOption();
					break;
				case 6:
					handleInsertWatch();
					break;
				case 7:
					// handleSelection();
					break;
				case 8:
					handleJoin();
					break;
				default:
					System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
					break;
				}
			}
		}		
	}

	private void handleJoin() {
		String vLink = null;
		while (vLink == null || vLink.length() <= 0) {
			System.out.print("Please enter video link: ");
			vLink = readLine().trim();
		}
		delegate.joinVideo(vLink);
	}


//	private void handleSelection() {
//		String sql = null;
//		while (sql == null || sql.length() <= 0) {
//			System.out.print("Please enter sql after where: ");
//			sql = readLine().trim();
//		}
//		delegate.selectionVideo(sql);
//	}
	
	private void handleDeleteOption() {
		String videoLink = "";
		while (videoLink == "") {
			System.out.print("Please enter the video link you wish to delete: ");
			videoLink =  readLine().trim();
			if (videoLink != "") {
				delegate.deleteVideo(videoLink);
			}
		}
	}
	
	private void handleInsertVideo() {
		int likes = INVALID_INPUT;
		while (likes == INVALID_INPUT) {
			System.out.print("Please enter likes: ");
			likes = readInteger(false);
		}
		
		String title = null;
		while (title == null || title.length() <= 0) {
			System.out.print("Please enter title: ");
			title = readLine().trim();
		}


		String videoLink = null;
		while (videoLink == null || videoLink.length() <= 0) {
			System.out.print("Please enter videoLink: ");
			videoLink = readLine().trim();
		}

		String email = null;
		while (email == null || email.length() <= 0) {
			System.out.print("Please enter email: ");
			email = readLine().trim();
		}

		String sponsorName = "";
		System.out.print("Please enter sponsor name: ");
		sponsorName = readLine().trim();

		Video videoPostBySponsoredBy = new Video(videoLink,
				title, likes, email, sponsorName);
		delegate.insertVideo(videoPostBySponsoredBy);
	}

	private void handleInsertWatch() {
		String videoLink = null;
		while (videoLink == null || videoLink.length() <= 0) {
			System.out.print("Please enter videoLink: ");
			videoLink = readLine().trim();
		}

		String email = null;
		while (email == null || email.length() <= 0) {
			System.out.print("Please enter email: ");
			email = readLine().trim();
		}

		int watchTime = INVALID_INPUT;
		while (watchTime == INVALID_INPUT) {
			System.out.print("Please enter watchTime: ");
			watchTime = readInteger(false);
		}

		Watch watch = new Watch(videoLink, watchTime, email);
		delegate.insertWatch(watch);
	}
	
	private void handleQuitOption() {
		System.out.println("Good Bye!");
		
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				System.out.println("IOException!");
			}
		}
		
		delegate.terminalTransactionsFinished();
	}
	
	private void handleUpdateOption() {
		String videoLink= "";
		while (videoLink == "") {
			System.out.print("Please enter the VIDEO LINK you wish to update: ");
			videoLink = readLine().trim();
		}

		String title= "";
		System.out.print("Please enter the TITLE you wish to update (leave empty for not update): ");
		title = readLine().trim();

		int likes = -1;
		System.out.print("Please enter the LIKES you wish to update (leave empty for not update): ");
		likes = readIntegerUpData(true);

		String email= "";
		System.out.print("Please enter the EMAIL you wish to update (leave empty for not update): ");
		email = readLine().trim();

		String sponsorName= "";
		System.out.print("Please enter the SPONSOR NAME you wish to update (leave empty for not update): ");
		sponsorName = readLine().trim();

		delegate.updateVideo(videoLink, title, likes, email, sponsorName);
	}

	private int readIntegerUpData(boolean allowEmpty) {
		String line = null;
		int input = INVALID_INPUT;
		try {
			line = bufferedReader.readLine();
			input = Integer.parseInt(line);
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		} catch (NumberFormatException e) {
			if (allowEmpty && line.length() == 0) {
				input = -1;
			} else {
				System.out.println(WARNING_TAG + " Your input was not an integer");
			}
		}
		return input;
	}
	
	private int readInteger(boolean allowEmpty) {
		String line = null;
		int input = INVALID_INPUT;
		try {
			line = bufferedReader.readLine();
			input = Integer.parseInt(line);
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		} catch (NumberFormatException e) {
			if (allowEmpty && line.length() == 0) {
				input = EMPTY_INPUT;
			} else {
				System.out.println(WARNING_TAG + " Your input was not an integer");
			}
		}
		return input;
	}
	
	private String readLine() {
		String result = null;
		try {
			result = bufferedReader.readLine();
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result;
	}
}
