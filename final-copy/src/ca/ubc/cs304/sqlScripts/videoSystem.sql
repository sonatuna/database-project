-- Drop tables in reverse order to avoid foreign key constraint issues
Drop TABLE ApprovedBy;
DROP TABLE SubscribesTo;
DROP TABLE Plays;
DROP TABLE Watch;
DROP TABLE Comment_postedOn_writtenBy;
DROP TABLE Video_postedBy_sponsoredBy;
DROP TABLE ModeratorAccount_uses;
DROP TABLE ViewerAccount_watchAs;
DROP TABLE Livestream_streams;
DROP TABLE Channel_owns;
DROP TABLE Account_watches;
DROP TABLE Advertisements_boughtBy2;
DROP TABLE Advertisements_boughtBy1;
DROP TABLE Sponsor;
DROP TABLE Moderator2;
DROP TABLE Moderator1;
DROP TABLE User1;

-- Create tables, ensuring that referenced tables are created first
CREATE TABLE User1 (User1ID INTEGER PRIMARY KEY,firstName VARCHAR(255),lastName VARCHAR(255));

CREATE TABLE Sponsor (sponsorName VARCHAR(255) PRIMARY KEY,sponsorEmail VARCHAR(255) UNIQUE,country VARCHAR(255));

CREATE TABLE Advertisements_boughtBy1 (length REAL PRIMARY KEY,compensation REAL);

CREATE TABLE Moderator1 (team VARCHAR(255) PRIMARY KEY,supervisor VARCHAR(255));

CREATE TABLE Moderator2 (employeeID INTEGER PRIMARY KEY,firstName VARCHAR(255),lastName VARCHAR(255),salary REAL,team VARCHAR(255),FOREIGN KEY (team) REFERENCES Moderator1(team));

CREATE TABLE Account_watches (email VARCHAR(255) PRIMARY KEY,alias VARCHAR(255) UNIQUE NOT NULL,password VARCHAR(255) NOT NULL, livestreamLink VARCHAR(255));

CREATE TABLE Channel_owns (email VARCHAR(255) PRIMARY KEY,User1ID INTEGER,description VARCHAR(255),subscribers INTEGER,FOREIGN KEY (email) REFERENCES Account_watches(email),FOREIGN KEY (User1ID) REFERENCES User1(User1ID));

CREATE TABLE Livestream_streams (link VARCHAR(255) PRIMARY KEY,likes INTEGER,views INTEGER,email VARCHAR(255) UNIQUE NOT NULL,FOREIGN KEY (email) REFERENCES Channel_owns(email));

CREATE TABLE ViewerAccount_watchAs (email VARCHAR(255) PRIMARY KEY,User1ID INTEGER,plan VARCHAR(255),FOREIGN KEY (email) REFERENCES Account_watches(email),FOREIGN KEY (User1ID) REFERENCES User1(User1ID));

CREATE TABLE ModeratorAccount_uses (email VARCHAR(255) PRIMARY KEY,rank INTEGER,employeeID INTEGER,FOREIGN KEY (email) REFERENCES Account_watches(email),FOREIGN KEY (employeeID) REFERENCES Moderator2(employeeID));

CREATE TABLE Video_postedBy_sponsoredBy (videoLink VARCHAR(255) PRIMARY KEY,title VARCHAR(255),likes INTEGER,email VARCHAR(255) NOT NULL,sponsorName VARCHAR(255),FOREIGN KEY (email) REFERENCES Channel_owns(email),FOREIGN KEY (sponsorName) REFERENCES Sponsor(sponsorName));

CREATE TABLE Advertisements_boughtBy2 (adID INTEGER PRIMARY KEY,length REAL,sponsorName VARCHAR(255),FOREIGN KEY (sponsorName) REFERENCES Sponsor(sponsorName));

CREATE TABLE Comment_postedOn_writtenBy (time CHAR(16),email VARCHAR(255),videoLink VARCHAR(255),text VARCHAR(255),PRIMARY KEY (time, email, videoLink),FOREIGN KEY (email) REFERENCES Account_watches(email),FOREIGN KEY (videoLink) REFERENCES Video_postedBy_sponsoredBy(videoLink) ON DELETE CASCADE);

CREATE TABLE Watch (videoLink VARCHAR(255),email VARCHAR(255),watchTime INTEGER,PRIMARY KEY (videoLink, email),FOREIGN KEY (videoLink) REFERENCES Video_postedBy_sponsoredBy(videoLink) ON DELETE CASCADE,FOREIGN KEY (email) REFERENCES Account_watches(email));

CREATE TABLE Plays (videoLink VARCHAR(255),adID INTEGER NOT NULL,PRIMARY KEY (videoLink, adID),FOREIGN KEY (videoLink) REFERENCES Video_postedBy_sponsoredBy(videoLink) ON DELETE CASCADE,FOREIGN KEY (adID) REFERENCES Advertisements_boughtBy2(adID));

CREATE TABLE SubscribesTo (accountEmail VARCHAR(255),channelEmail VARCHAR(255),PRIMARY KEY (accountEmail, channelEmail),FOREIGN KEY (accountEmail) REFERENCES Account_watches(email),FOREIGN KEY (channelEmail) REFERENCES Channel_owns(email));


CREATE TABLE ApprovedBy (email varchar(255),videoLink varchar(255),primary KEY (email, videoLink),FOREIGN KEY (email) REFERENCES ModeratorAccount_uses(email),FOREIGN KEY (videoLink) REFERENCES Video_postedBy_sponsoredBy(videoLink) ON DELETE CASCADE);

INSERT INTO User1 (User1ID, firstName, lastName) VALUES (00001, 'John', 'Smith');
INSERT INTO User1 (User1ID, firstName, lastName) VALUES (00002, 'Jane', 'Jackson');
INSERT INTO User1 (User1ID, firstName, lastName) VALUES (00003, 'Mike', 'Smith');
INSERT INTO User1 (User1ID, firstName, lastName) VALUES (00004, 'Sara', 'Lee');
INSERT INTO User1 (User1ID, firstName, lastName) VALUES (00005, 'Chris', 'Brown');

INSERT INTO Sponsor (sponsorName, sponsorEmail, country) VALUES ('Sponsor1', 'sponsor1@test.com', 'Canada');
INSERT INTO Sponsor (sponsorName, sponsorEmail, country) VALUES ('Sponsor2', 'sponsor2@test.com', 'USA');
INSERT INTO Sponsor (sponsorName, sponsorEmail, country) VALUES ('Sponsor3', 'sponsor3@test.com', 'Japan');
INSERT INTO Sponsor (sponsorName, sponsorEmail, country) VALUES ('Sponsor4', 'sponsor4@test.com', 'Canada');
INSERT INTO Sponsor (sponsorName, sponsorEmail, country) VALUES ('Sponsor5', 'sponsor5@test.com', 'UK');

INSERT INTO Advertisements_boughtBy1 (length, compensation) VALUES (30.5, 100.00);
INSERT INTO Advertisements_boughtBy1 (length, compensation) VALUES (45.0, 150.00);
INSERT INTO Advertisements_boughtBy1 (length, compensation) VALUES (60.0, 200.00);
INSERT INTO Advertisements_boughtBy1 (length, compensation) VALUES (15.5, 50.00);
INSERT INTO Advertisements_boughtBy1 (length, compensation) VALUES (90.5, 300.00);

INSERT INTO Moderator1 (team, supervisor) VALUES ('TeamA', 'Supervisor1');
INSERT INTO Moderator1 (team, supervisor) VALUES ('TeamB', 'Supervisor2');
INSERT INTO Moderator1 (team, supervisor) VALUES ('TeamC', 'Supervisor3');

INSERT INTO Moderator2 (employeeID, firstName, lastName, salary, team) VALUES (1001, 'John', 'Finn', 50000, 'TeamA');
INSERT INTO Moderator2 (employeeID, firstName, lastName, salary, team) VALUES (1002, 'Jack', 'Owen', 52000, 'TeamB');
INSERT INTO Moderator2 (employeeID, firstName, lastName, salary, team) VALUES (1003, 'Jay', 'Smith', 53000, 'TeamC');
INSERT INTO Moderator2 (employeeID, firstName, lastName, salary, team) VALUES (1004, 'Anna', 'Jackson', 54000, 'TeamA');
INSERT INTO Moderator2 (employeeID, firstName, lastName, salary, team) VALUES (1005, 'Chris', 'Davis', 55000, 'TeamB');

INSERT INTO Account_watches (email, alias, password, livestreamLink) VALUES ('User111@test.com', 'User111', 'pass123', 'examplelink.com/1');
INSERT INTO Account_watches (email, alias, password, livestreamLink) VALUES ('User112@test.com', 'User112', 'pass123', 'examplelink.com/2');
INSERT INTO Account_watches (email, alias, password, livestreamLink) VALUES ('User113@test.com', 'User113', 'pass123', 'examplelink.com/3');
INSERT INTO Account_watches (email, alias, password, livestreamLink) VALUES ('User114@test.com', 'User114', 'pass123', 'examplelink.com/4');
INSERT INTO Account_watches (email, alias, password, livestreamLink) VALUES ('User115@test.com', 'User115', 'pass123', 'examplelink.com/5');

INSERT INTO Channel_owns (email, User1ID, description, subscribers) VALUES ('User111@test.com', 00001, 'Tech', 1000);
INSERT INTO Channel_owns (email, User1ID, description, subscribers) VALUES ('User112@test.com', 00002, 'Gaming', 2000);
INSERT INTO Channel_owns (email, User1ID, description, subscribers) VALUES ('User113@test.com', 00003, 'Cooking', 3000);
INSERT INTO Channel_owns (email, User1ID, description, subscribers) VALUES ('User114@test.com', 00004, 'Travel', 4000);
INSERT INTO Channel_owns (email, User1ID, description, subscribers) VALUES ('User115@test.com', 00005, 'Education', 5000);

INSERT INTO Livestream_streams (link, likes, views, email) VALUES ('examplelink.com/1', 1001, 1000, 'User111@test.com');
INSERT INTO Livestream_streams (link, likes, views, email) VALUES ('examplelink.com/2', 2006, 2000, 'User112@test.com');
INSERT INTO Livestream_streams (link, likes, views, email) VALUES ('examplelink.com/3', 3002, 3000, 'User113@test.com');
INSERT INTO Livestream_streams (link, likes, views, email) VALUES ('examplelink.com/4', 4200, 4000, 'User114@test.com');
INSERT INTO Livestream_streams (link, likes, views, email) VALUES ('examplelink.com/5', 5030, 5000, 'User115@test.com');

INSERT INTO ViewerAccount_watchAs (email, User1ID, plan) VALUES ('User111@test.com', 00001, 'Free');
INSERT INTO ViewerAccount_watchAs (email, User1ID, plan) VALUES ('User112@test.com', 00002, 'Premium');
INSERT INTO ViewerAccount_watchAs (email, User1ID, plan) VALUES ('User113@test.com', 00003, 'Free');
INSERT INTO ViewerAccount_watchAs (email, User1ID, plan) VALUES ('User114@test.com', 00004, 'Premium');
INSERT INTO ViewerAccount_watchAs (email, User1ID, plan) VALUES ('User115@test.com', 00005, 'Free');

INSERT INTO ModeratorAccount_uses (email, rank, employeeID) VALUES ('User111@test.com', 1, 1001);
INSERT INTO ModeratorAccount_uses (email, rank, employeeID) VALUES ('User112@test.com', 2, 1002);
INSERT INTO ModeratorAccount_uses (email, rank, employeeID) VALUES ('User113@test.com', 3, 1003);
INSERT INTO ModeratorAccount_uses (email, rank, employeeID) VALUES ('User114@test.com', 4, 1004);
INSERT INTO ModeratorAccount_uses (email, rank, employeeID) VALUES ('User115@test.com', 5, 1005);

INSERT INTO Video_postedBy_sponsoredBy(videoLink, title, likes, email, sponsorName) VALUES ('videoLink1', 'Top 10 Treacherous Tales', 1000, 'User112@test.com', NULL);
INSERT INTO Video_postedBy_sponsoredBy(videoLink, title, likes, email, sponsorName) VALUES ('videoLink2', 'DIY Makeup Tutorial', 2000, 'User113@test.com', 'Sponsor1');
INSERT INTO Video_postedBy_sponsoredBy(videoLink, title, likes, email, sponsorName) VALUES ('videoLink3', 'Unboxing a TI-84', 3000, 'User114@test.com', 'Sponsor2');
INSERT INTO Video_postedBy_sponsoredBy(videoLink, title, likes, email, sponsorName) VALUES ('videoLink4', 'Pirate Movie', 4000, 'User115@test.com', NULL);
INSERT INTO Video_postedBy_sponsoredBy(videoLink, title, likes, email, sponsorName) VALUES ('videoLink5', 'disappointment', 5000, 'User111@test.com', NULL);
INSERT INTO Video_postedBy_sponsoredBy(videoLink, title, likes, email, sponsorName) VALUES ('videoLink6', 'disappointment', 5000, 'User111@test.com', NULL);

INSERT INTO Advertisements_boughtBy2 (adID, length, sponsorName) VALUES (1, 30.5, 'Sponsor1');
INSERT INTO Advertisements_boughtBy2 (adID, length, sponsorName) VALUES (2, 45.0, 'Sponsor2');
INSERT INTO Advertisements_boughtBy2 (adID, length, sponsorName) VALUES (3, 60.0, 'Sponsor3');
INSERT INTO Advertisements_boughtBy2 (adID, length, sponsorName) VALUES (4, 15.5, 'Sponsor4');
INSERT INTO Advertisements_boughtBy2 (adID, length, sponsorName) VALUES (5, 90.5, 'Sponsor5');

INSERT INTO Comment_postedOn_writtenBy (time, email, videoLink, text) VALUES ('03/04/2024 21:10', 'User111@test.com', 'videoLink1', 'Great video!');
INSERT INTO Comment_postedOn_writtenBy (time, email, videoLink, text) VALUES ('02/29/2024 21:10', 'User112@test.com', 'videoLink2', 'Very Good.');
INSERT INTO Comment_postedOn_writtenBy (time, email, videoLink, text) VALUES ('03/04/2024 21:11', 'User113@test.com', 'videoLink3', 'Love This!');
INSERT INTO Comment_postedOn_writtenBy (time, email, videoLink, text) VALUES ('02/27/2024 11:10', 'User114@test.com', 'videoLink4', 'Like it');
INSERT INTO Comment_postedOn_writtenBy (time, email, videoLink, text) VALUES ('03/04/2024 21:19', 'User115@test.com', 'videoLink5', 'Really helpful!');

INSERT INTO Watch (videoLink, email, watchTime) VALUES ('videoLink1', 'User111@test.com', 10);
INSERT INTO Watch (videoLink, email, watchTime) VALUES ('videoLink2', 'User112@test.com', 3);
INSERT INTO Watch (videoLink, email, watchTime) VALUES ('videoLink3', 'User113@test.com', 24);
INSERT INTO Watch (videoLink, email, watchTime) VALUES ('videoLink4', 'User114@test.com', 11);
INSERT INTO Watch (videoLink, email, watchTime) VALUES ('videoLink5', 'User115@test.com', 5);
INSERT INTO Watch (videoLink, email, watchTime) VALUES ('videoLink2', 'User111@test.com', 3);
INSERT INTO Watch (videoLink, email, watchTime) VALUES ('videoLink3', 'User111@test.com', 24);
INSERT INTO Watch (videoLink, email, watchTime) VALUES ('videoLink4', 'User111@test.com', 11);
INSERT INTO Watch (videoLink, email, watchTime) VALUES ('videoLink5', 'User111@test.com', 5);
INSERT INTO Watch (videoLink, email, watchTime) VALUES ('videoLink6', 'User111@test.com', 90);

INSERT INTO Plays (videoLink, adID) VALUES ('videoLink1', 1);
INSERT INTO Plays (videoLink, adID) VALUES ('videoLink2', 2);
INSERT INTO Plays (videoLink, adID) VALUES ('videoLink3', 3);
INSERT INTO Plays (videoLink, adID) VALUES ('videoLink4', 4);
INSERT INTO Plays (videoLink, adID) VALUES ('videoLink5', 5);

INSERT INTO SubscribesTo (accountEmail, channelEmail) VALUES ('User112@test.com', 'User111@test.com');
INSERT INTO SubscribesTo (accountEmail, channelEmail) VALUES ('User113@test.com', 'User111@test.com');
INSERT INTO SubscribesTo (accountEmail, channelEmail) VALUES ('User114@test.com', 'User111@test.com');
INSERT INTO SubscribesTo (accountEmail, channelEmail) VALUES ('User113@test.com', 'User112@test.com');
INSERT INTO SubscribesTo (accountEmail, channelEmail) VALUES ('User114@test.com', 'User112@test.com');
INSERT INTO SubscribesTo (accountEmail, channelEmail) VALUES ('User114@test.com', 'User115@test.com');

INSERT INTO ApprovedBy (email, videoLink) VALUES ('User112@test.com', 'videoLink1');