package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Arrays;


// use of Java swing partially adapted from past work in CPSC 210
// use of JTable based on https://www.geeksforgeeks.org/java-swing-jtable/

/**
 * This is the main controller class that will orchestrate everything.
 */
// this code is coped from demo project for setup reason
// make sure everyone in the group have same setup right now
public class GUI extends JFrame implements LoginWindowDelegate, WindowListener {
	private DatabaseConnectionHandler dbHandler = null;
	private LoginWindow loginWindow = null;
	private static final int WIDTH = 1500;
	private static final int HEIGHT = 900;
	private JPanel desktop;
	private JInternalFrame controlPanel;
	private JLabel errorMessage;
	JFrame f;
	JTable j;
	String table = "";

	public GUI() {
		addWindowListener(this);
		dbHandler = new DatabaseConnectionHandler();
		desktop = new JPanel();
		desktop.addMouseListener(new DesktopFocusAction());
		desktop.setBackground(new Color(158, 240, 180));
		errorMessage = new JLabel();
		errorMessage.setIcon(new ImageIcon(System.getProperty("user.dir") + "/imageResources/ErrorMessage.png"));

		controlPanel = new JInternalFrame("Control Panel", false, false, false, false);
		controlPanel.setLayout(new BorderLayout());

		setContentPane(desktop);
		setTitle("Video System Database User Interface");
		setSize(WIDTH, HEIGHT);

		addButtonPanel();

		controlPanel.pack();
		controlPanel.setVisible(true);

		desktop.add(controlPanel);
		setVisible(true);
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
		boolean didConnect = dbHandler.login(username, password);

		if (didConnect) {
			// Once connected, remove login window and start text transaction flow
			loginWindow.dispose();

			JOptionPane.showMessageDialog(this, "Connected to Oracle!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
			databaseSetup();
		} else {
			loginWindow.handleLoginFailed();
			JOptionPane.showMessageDialog(this, "Fail to connected to Oracle", "Login Failed", JOptionPane.INFORMATION_MESSAGE);
			if (loginWindow.hasReachedMaxLoginAttempts()) {
				loginWindow.dispose();
				JOptionPane.showMessageDialog(this, "You have exceeded your number of allowed attempts", "Login Failed", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
		}
	}
	
	private void addButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(220, 247, 181));
		buttonPanel.setLayout(new GridLayout(9,2));
		buttonPanel.add(new JButton(new TotalLikesAction()));
		buttonPanel.add(new JButton(new SubscriberAction()));
		buttonPanel.add(new JButton(new MostWatchedAction()));
		buttonPanel.add(new JButton(new DeleteVideoAction()));
		buttonPanel.add(new JButton(new InsertVideoAction()));
		buttonPanel.add(new JButton(new GetVideosAction()));
		buttonPanel.add(new JButton(new UpdateAction()));
		buttonPanel.add(new JButton(new GetWatchsAction()));
		buttonPanel.add(new JButton(new InsertWatchsAction()));
		buttonPanel.add(new JButton(new IntentWatchersAction()));
		buttonPanel.add(new JButton(new SelectionAction()));
		buttonPanel.add(new JButton(new ProjectionAction()));
		buttonPanel.add(new JButton(new JoinAction()));
		controlPanel.add(buttonPanel, BorderLayout.WEST);
	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	private class UpdateAction extends AbstractAction {
		UpdateAction() {
			super("Update a video's information");
			}
		@Override
		public void actionPerformed(ActionEvent e) {
			getVideoInfo();
			Video video;
			String link = JOptionPane.showInputDialog(null,
					"Enter link of video to update",
					"Input",
					JOptionPane.QUESTION_MESSAGE);
			if (link == null || link.isEmpty()) {
				displayErrorMessage("Video must have link");
				return;
			}
			String title = "";
			int likes = -1;
			String email = "";
			String sponsor = "";

			String temp = JOptionPane.showInputDialog(null,
					"Update title of video (leave blank otherwise)",
					"Input",
					JOptionPane.QUESTION_MESSAGE);
			if (temp != null || temp.isEmpty()) {
				title = temp;
			}

			try {
				String l = JOptionPane.showInputDialog(null,
						"Update number of likes (leave blank otherwise)",
						"Input",
						JOptionPane.QUESTION_MESSAGE);
				if ((l != null) && !l.isEmpty()) {
					int a = Integer.parseInt(l);
					if (a < 0) {
						displayErrorMessage("Entry must be natural number (>= 0)");
						return;
					}
					likes = a;
				}
			} catch (NumberFormatException err) {
				displayErrorMessage("Entry was not a valid number");
				return;
			}
			String tempemail = JOptionPane.showInputDialog(null,
					"Update email of channel that posted video (leave blank otherwise)",
					"Input",
					JOptionPane.QUESTION_MESSAGE);
			if (tempemail != null && tempemail.isEmpty()) {
				email = tempemail;
			}
			String tempsponsor = JOptionPane.showInputDialog(null,
					"Update sponsor of video (leave blank otherwise)",
					"Input",
					JOptionPane.QUESTION_MESSAGE);
			if (tempsponsor != null && tempsponsor.isEmpty()) {
				sponsor = tempsponsor;
			}

			try {
				updateVideo(link, title, likes, email, sponsor);
			} catch (SQLException ex) {
				displayErrorMessage("Violates constraints: link may already be in use,\n" +
						"channel specified does not exist, or sponsor does not exist");
				return;
			}
			getVideoInfo();
		}
	}

	private class InsertWatchsAction extends AbstractAction {
		InsertWatchsAction() {
			super("Have an account watch a video");
		}
		@Override
		public void actionPerformed(ActionEvent e) {

			getWatchInfo();
			String link = JOptionPane.showInputDialog(null,
					"Enter link of video to be watched",
					"Input",
					JOptionPane.QUESTION_MESSAGE);
			if (link == null || link.isEmpty()) {
				displayErrorMessage("Video must have link");
				return;
			}
			int watchTime = 0;
			try {
				String l = JOptionPane.showInputDialog(null,
						"Enter how many minutes the video has been watched",
						"Input",
						JOptionPane.QUESTION_MESSAGE);
				watchTime = Integer.parseInt(l);
				if (watchTime < 0) {
					displayErrorMessage("Entry must be natural number (>= 0)");
					return;
				}
			} catch (NumberFormatException err) {
				displayErrorMessage("Entry was not a valid number");
				return;
			}
			String email = JOptionPane.showInputDialog(null,
					"Enter email of account that watched video",
					"Input",
					JOptionPane.QUESTION_MESSAGE);
			if (email == null || email.isEmpty()) {
				displayErrorMessage("Must input the account that watched the video");
				return;
			}

			try {
				insertWatch(link, email, watchTime);
			} catch (SQLException ex) {
				displayErrorMessage("Violates constraints: link may already be in use,\n" +
						"or account specified does not exist");
				return;
			}
			getWatchInfo();
		}
	}

	private class InsertVideoAction extends AbstractAction {
		InsertVideoAction() {
			super("Insert a video");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			getVideoInfo();
			String link = JOptionPane.showInputDialog(null,
					"Enter link of video to insert",
					"Input",
					JOptionPane.QUESTION_MESSAGE);
			if (link == null || link.isEmpty()) {
				displayErrorMessage("Video must have link");
				return;
			}
			String title = JOptionPane.showInputDialog(null,
					"Enter title of video to insert",
					"Input",
					JOptionPane.QUESTION_MESSAGE);
			if (title == null) {
				title = "Untitled";
			}
			int likes = 0;
			try {
				String l = JOptionPane.showInputDialog(null,
						"Enter number of likes video has",
						"Input",
						JOptionPane.QUESTION_MESSAGE);
				likes = Integer.parseInt(l);
				if (likes < 0) {
					displayErrorMessage("Entry must be natural number (>= 0)");
					return;
				}
			} catch (NumberFormatException err) {
				displayErrorMessage("Entry was not a valid number");
				return;
			}
			String email = JOptionPane.showInputDialog(null,
					"Enter email of EXISTING channel that posted video",
					"Input",
					JOptionPane.QUESTION_MESSAGE);
			if (email == null || email.isEmpty()) {
				displayErrorMessage("Video must have a channel");
				return;
			}
			String sponsor = JOptionPane.showInputDialog(null,
					"Enter EXISTING sponsor of video to insert, if there is one",
					"Input",
					JOptionPane.QUESTION_MESSAGE);
			if (sponsor == null) {
				sponsor = "";
			}

            try {
                insertVideo(link, title, likes, email, sponsor);
            } catch (SQLException ex) {
				displayErrorMessage("Violates constraints: link may already be in use,\n" +
						"channel specified does not exist, or sponsor does not exist");
				return;
            }
            getVideoInfo();
		}
	}

	private class DeleteVideoAction extends AbstractAction {
		DeleteVideoAction() {
			super("Delete a video");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			getVideoInfo();
			String sensorLoc = JOptionPane.showInputDialog(null,
					"Enter link of video to delete",
					"Input",
					JOptionPane.QUESTION_MESSAGE);
			if (sensorLoc != null) {
                try {
                    deleteVideo(sensorLoc);
                } catch (SQLException ex) {
					displayErrorMessage("Entered link does not exist or is otherwise invalid");
					return;
                }
            }
			getVideoInfo();
		}
	}

	private class GetWatchsAction extends AbstractAction {
		GetWatchsAction() {
			super("See which accounts have watched what videos");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			getWatchInfo();
		}
	}

	private class IntentWatchersAction extends AbstractAction {
		IntentWatchersAction() {
			super("See viewers who have watched all videos");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			intentWatchers();
		}
	}

	private class SelectionAction extends AbstractAction {
		SelectionAction() {
			super("See videos meeting condition ...");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			getVideoInfo();
			String sql = JOptionPane.showInputDialog(null,
					"Select videos where",
					"Input",
					JOptionPane.QUESTION_MESSAGE);
			if (sql != null && !sql.isEmpty()) {
                try {
                    selection(sql);
                } catch (SQLException ex) {
                    displayErrorMessage("Entered condition invalid, \n" +
							"possibly specified non-existent attributes or incorrect types");
                }
            }
		}
	}
	private class ProjectionAction extends AbstractAction {
		ProjectionAction() {
			super("Project any attributes from any table");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
//			String table = JOptionPane.showInputDialog(null,
//					"Select table",
//					"Input",
//					JOptionPane.QUESTION_MESSAGE);

			String[] tables = {"User1", "Sponsor", "Advertisements_boughtBy1", "Moderator1", "Moderator2", "Account_watches",
					"Channel_owns", "Livestream_streams", "ViewerAccount_watchAs", "ModeratorAccount_uses",
					"Video_postedBy_sponsoredBy", "Advertisements_boughtBy2", "Comment_postedOn_writtenBy",
					"Watch", "Plays", "SubscribesTo", "ApprovedBy"};
			String[][] attributes =
					{{"User1ID", "firstName", "lastName"},
							{"sponsorName", "sponsorEmail", "country"},
							{"length", "compensation"}, {"team", "supervisor"},
							{"employeeID", "firstName", "lastName", "salary", "team"},
							{"email", "alias", "password", "livestreamLink"},
							{"email", "User1ID", "description", "subscribers"},
							{"link", "likes", "views", "email"},
							{"email", "User1ID", "plan"},
							{"email", "rank", "employeeID"},
							{"videoLink", "title", "likes", "email", "sponsorName"},
							{"adID", "length", "sponsorName"},
							{"time", "email", "videoLink", "text"},
							{"videoLink", "email", "watchTime"},
							{"videoLink", "adID"},
							{"accountEmail", "channelEmail"},
							{"email", "videoLink"}};

			JFrame pear = new JFrame("select table");
			JFrame f = new JFrame("select attributes");

			//create a panel
			JPanel apple = new JPanel();

			//create a new label
			JLabel banana = new JLabel("select table");

			//create list
			JList celery = new JList(tables);



			//set a selected index
			celery.setSelectedIndex(2);
			celery.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			JButton confirm = new JButton("Confirm");
			apple.add(celery);
			apple.add(confirm);
			pear.add(apple);
			pear.setSize(200,400);
			pear.setVisible(true);
			confirm.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){

					table = (String) celery.getSelectedValue();
					String[] strings;
					switch (table) {
						case "User1":
							strings = attributes[0];
							break;
						case "Sponsor":
							strings = attributes[1];
							break;
						case "Advertisements_boughtBy1":
							strings = attributes[2];
							break;
						case "Moderator1":
							strings = attributes[3];
							break;
						case "Moderator2":
							strings = attributes[4];
							break;
						case "Account_watches":
							strings = attributes[5];
							break;
						case "Channel_owns":
							strings = attributes[6];
							break;
						case "Livestream_streams":
							strings = attributes[7];
							break;
						case "ViewerAccount_watchAs":
							strings = attributes[8];
							break;
						case "ModeratorAccount_uses":
							strings = attributes[9];
							break;
						case "Video_postedBy_sponsoredBy":
							strings = attributes[10];
							break;
						case "Advertisements_boughtBy2":
							strings = attributes[11];
							break;
						case "Comment_postedOn_writtenBy":
							strings = attributes[12];
							break;
						case "Watch":
							strings = attributes[13];
							break;
						case "Plays":
							strings = attributes[14];
							break;
						case "SubscribesTo":
							strings = attributes[15];
							break;
						case "ApprovedBy":
							strings = attributes[16];
							break;
						default:
							displayErrorMessage("No such table exists");
							return;
					}
					pear.dispose();
					JPanel p =new JPanel();

					//create a new label
					JLabel l= new JLabel("select attributes (cmd/ctrl click)");

					//create list
					JList b= new JList(strings);

					//set a selected index
					b.setSelectedIndex(2);
					b.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					JButton confirmCelery = new JButton("Confirm");
					confirmCelery.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							String[] selection = (String[]) b.getSelectedValuesList().toArray(new String[0]);
							System.out.println(selection);
							project(table, selection);
						}});

					//add list to panel
					p.add(b);
					p.add(confirmCelery);

					f.add(p);

					//set the size of frame
					f.setSize(200,200);
					f.setVisible(true);



				}});


			//create a panel


			//getVideoInfo();
		}
	}

	private class GetVideosAction extends AbstractAction {
		GetVideosAction() {
			super("See all videos posted");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			getVideoInfo();
		}
	}

	private class TotalLikesAction extends AbstractAction {
		TotalLikesAction() {
			super("See total likes of each channel");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			channelsTotalLikes();
		}
	}

	private class SubscriberAction extends AbstractAction {
		SubscriberAction() {
			super("See popular channels' subscriber count");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			manySubscribers();
		}
	}

	private class MostWatchedAction extends AbstractAction {
		MostWatchedAction() {
			super("See watch time of most watched videos");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			mostWatchedWatchTime();
		}
	}

	private class JoinAction extends AbstractAction {
		JoinAction() {
			super("See total watch time of video ...");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			String vlink = JOptionPane.showInputDialog(null,
					"Input video link",
					"Input",
					JOptionPane.QUESTION_MESSAGE);
			if (vlink != null && !vlink.isEmpty()) {
                try {
                    joinVideoAndWatch(vlink);
                } catch (SQLException ex) {
                    displayErrorMessage("Entered link does not exist or is otherwise invalid");
                }
            }
		}
	}

	private void joinVideoAndWatch(String vlink) throws SQLException {
		String[][] data = dbHandler.joinVideo(vlink);
		String[] columnNames =  { "videoLink", "title", "TotalWatchTime"};
		showTable("Joint video and watch", data, columnNames);
	}

	public void project(String table, String[] selected) {
		String[][] data = dbHandler.projectTable(table, selected);
		String[] columnNames = selected;
		showTable(table, data, columnNames);
	}

	public void channelsTotalLikes() {
		String[][] data = dbHandler.channelsTotalLikes();
		String[] columnNames =  { "Channel", "Total Likes"};
		showTable("Channels' total likes", data, columnNames);
	}

	public void manySubscribers() {
		String[][] data = dbHandler.manySubscribers();
		String[] columnNames =  { "Channel", "subscribers"};
		showTable("Channels' subscriber count", data, columnNames);
	}

	public void mostWatchedWatchTime() {
		String[][] data = {{Integer.toString(dbHandler.mostWatchedWatchTime())}};
		String[] columnNames =  { "Time"};
		showTable("Average watch time of most watched videos", data, columnNames);
	}

	public void intentWatchers() {
		String[][] data = {dbHandler.intentWatchers()};
		String[] columnNames =  { "Viewers"};
		showTable("Viewers who have watched every video", data, columnNames);
	}

	public void getWatchInfo() {
		Watch[] watches = dbHandler.getWatchInfo();
		String[][] data = new String[watches.length][5];
		for (int i = 0; i < watches.length; i++) {
			Watch w = watches[i];
			data[i][0] = w.getVideoLink();
			data[i][1] = w.getEmail();
			data[i][2] = Integer.toString(w.getWatchTime());
		}
		String[] cols = {"Video Link", "accounts", "WatchTime"};
		showTable("Watchs", data, cols);
	}

	public void getVideoInfo() {
		Video[] vids = dbHandler.getVideoInfo();
		String[][] data = new String[vids.length][5];
		for (int i = 0; i < vids.length; i++) {
			Video v = vids[i];
			data[i][0] = v.getVideoLink();
			data[i][1] = v.getTitle();
			data[i][2] = Integer.toString(v.getLikes());
			data[i][3] = v.getEmail();
			data[i][4] = v.getSponsor();
		}
		String[] cols = {"Video Link", "Title", "Likes", "Channel", "SponsorName"};
		showTable("Videos", data, cols);
	}

	public void showTable(String title, String[][] data, String[] cols) {
		f = new JFrame();
		f.setTitle(title);
		j = new JTable(data, cols);
		j.setBounds(30, 40, 200, 300);
		j.setBackground(new Color(220, 247, 181));
		j.setForeground(new Color(24, 52, 69));
		JScrollPane sp = new JScrollPane(j);
		sp.setBackground(new Color(220, 247, 181));
		f.add(sp);
		f.setSize(500, 200);
		f.setBackground(new Color(220, 247, 181));

		f.setLocation(500, 500);
		f.setVisible(true);
	}

	public void deleteVideo(String del) throws SQLException {
		dbHandler.deleteVideo(del);
	}

	public void insertVideo(String link, String title, int likes, String email, String sponsor) throws SQLException {
		Video v = new Video(link, title, likes, email, sponsor);
		dbHandler.insertVideo(v);
	}

	public void insertWatch(String link, String email, int watchTime) throws SQLException {
		Watch watch = new Watch(link, watchTime, email);
		dbHandler.insertWatch(watch);
	}

	public void selection(String sql) throws SQLException {
		String[][] data = dbHandler.selectVideoInfo(sql);
		String[] columnNames =  { "videoLink", "title", "likes", "email", "sponsorName"};
		showTable("Select video with requirements", data, columnNames);
	}

	public void updateVideo(String link, String title, int likes, String email, String sponsor) throws SQLException {
		dbHandler.updateVideo(link, title, likes, email, sponsor);
	}

	public Video findVideo(String link) throws SQLException {
		Video v = dbHandler.findVideoGivenLink(link);
		return v;
	}

	public void displayErrorMessage(String text) {
		f = new JFrame();
		f.setTitle("Invalid Input");
		errorMessage.setText(text);
		errorMessage.setHorizontalTextPosition(0);
		JScrollPane sp = new JScrollPane(errorMessage);
		f.add(sp);
		f.setSize(1020, 768);
		f.setVisible(true);
	}


        /**
	 * TerminalTransactionsDelegate Implementation
	 * 
     * The TerminalTransaction instance tells us that the user is fine with dropping any existing table
     * called branch and creating a new one for this project to use
     */ 
	public void databaseSetup() {
		dbHandler.databaseSetup();
	}


	private class DesktopFocusAction extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			GUI.this.requestFocusInWindow();
		}
	}

	/**
	 * Main method called at launch time
	 */
	public static void main(String args[]) {
		GUI bank = new GUI();
		bank.start();
	}
}
