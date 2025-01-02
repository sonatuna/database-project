# CPSC 304 Project Milestone 2
Milestone #: 2
Date: Feb 9 2024
Group Number: 30
### Group Member:
Dennis Zhu, 47936935, v9l1a, dennis58@student.ubc.ca

Kathryn Wu, 19598275, t9o2s, kwu10@student.ubc.ca

Tina Yu, 23193998, w1c0r, tincantuna03@gmail.com

By typing our names and student numbers in the above table, we certify that the work in the attached assignment was performed solely by those whose names and student IDs are included above. (In the case of Project Milestone 0, the main purpose of this page is for you to let us know your e-mail address, and then let us assign you to a TA for your project supervisor.) In addition, we indicate that we are fully aware of the rules and consequences of plagiarism, as set forth by the Department of Computer Science and the University of British Columbia

### 2. Summary
	The project models a digital video platform where users can use one or more accounts to watch, post, stream live, and comment on content. Moderators are people hired by the platform to use special accounts to review and approve videos. Sponsors can purchase ad slots and sponsor individual videos.

### 3. ER Diagram

Changes after Milestone 1 feedback: 
Moderator has additional attributes that differentiate it from ModeratorAccount, including firstName, lastName, salary, team, and supervisor
Account has lost the attributes “name” and “ID”, replaced with “alias”, the nickname used to refer to the account by users, to make its distinction from User more obvious
Alias is not necessarily similar to or the same as the associated User’s firstName, lastName
Additional changes:
Livestreams are now total on streams relationship and Channels is not: a livestream must be streamed by one channel, a channel can stream at most one livestream but does not have to
The boughtBy relationship is now one to many: an advertisement slot is bought by one sponsor
Meaningful attributes added to Sponsors, Moderators

 - ![fail to load image](./ImageResources/ER.png "ER diagram")

### 4. Schema
** Notation:  Underline is primary key, bold is foreign key
Abbreviations: primary key (PK), candidate key (CK), foreign key (FK)
Names differ from ER diagram to indicate a relationship set was combined with an entity set

```
Account_watches(email: VARCHAR, alias: VARCHAR, password: VARCHAR, livestreamLink: VARCHAR)
PK: email
CK: alias, email
FK: livestreamLink
alias is unique, not null; password is not null 
Channel_owns(email: VARCHAR, userID: INTEGER, description: VARCHAR, subscribers: INTEGER) 
PK/CK: email
FK: userID, email
ViewerAccount_watchAs(email: VARCHAR, userID: INTEGER, plan:VARCHAR)
PK/CK: email
FK: userID, email
ModeratorAccount_uses(email: VARCHAR, rank: INTEGER, employeeID)
PK/CK: email
FK: email, employeeID
Video_postedBy_sponsoredBy(videoLink: VARCHAR, title: VARCHAR, likes: INTEGER, email: VARCHAR, sponsorName: VARCHAR)
PK: videolink
CK: videolink, (title, email)
FK: email, sponsorName
email is not null
Sponsor(sponsorName: VARCHAR, sponsorEmail: VARCHAR, country: VARCHAR)
PK: sponsorName
CK: sponsorName, sponsorEmail
sponsorEmail is unique
Advertisement_boughtBy(adID: INTEGER, compensation: REAL, length: REAL, sponsorName: VARCHAR)
PK/CK: adID
FK: sponsorName
User(userId: INTEGER, firstName: VARCHAR, lastName: VARCHAR)
PK/CK: userId
Moderator(employeID: INTEGER, firstName: VARCHAR, lastName: VARCHAR, salary: REAL, team: VARCHAR, supervisor: VARCHAR)
PK/CK: employeeID
Livestream_streams(link: VARCHAR, likes: INTEGER, views: INTEGER, email: VARCHAR) 
PK/CK: link
FK: email
email is not null and is unique
Comment_postedOn_writtenBy(time: CHAR(16), email: VARCHAR, videoLink: VARCHAR, text: VARCHAR)
PK/CK: (time, email, videoLink)
FK: email, videoLink
Watch(videoLink: VARCHAR, email: VARCHAR, watchTime: INTEGER)
PK/CK: (videoLink, email)
FK: videoLink, email
Plays(videoLink: VARCHAR, adID: INTEGER)
PK/CK: (videoLink, adID)
FK: videoLink, adID
SubscribesTo(accountEmail: INTEGER, channelEmail: INTEGER)
PK/CK: (accountEmail, channelEmail)
FK: accountEmail, channelEmail
accountEmail, channelEmail are both ‘email’ in their parent tables, renamed for clarity
ApprovedBy(email: VARCHAR, videoLink: VARCHAR)
PK/CK: (email, videoLink)
FK: email, videoLink
```

### 5. Functional Dependencies
```
Account_watches(email, alias, password, livestreamLink):
email → alias, password, livestreamLink
alias  → email, password, livestreamLink
Channel_owns(email, userID, description, subscribers):
email → userID, description, subscribers
ViewerAccount_watchAs(email, userID, plan):
email → userID, plan
ModeratorAccount_uses(email, rank, employeeID):
email → employeeID, rank
Video_postedBy_sponsoredBy(videoLink, title, likes, email, sponsorName):
videoLink → title, email, likes, sponsorName
Sponsor(sponsorName, sponsorEmail, country):
sponsorName → email, country
email → sponsorName, country
Advertisement_boughtBy(adID, length, compensation, sponsorName):
adID → compensation, length, sponsorName
length → compensation
User(userID, firstName, lastName):
userId → firstName, lastName
Moderator(employeeID, firstName, lastName, salary, team, supervisor): 
employeeID → firstName, lastName, salary, supervisor, team
team → supervisor
Livestream_streams(link, likes, views, email):
link → likes, views, email
Comment_postedOn_writtenBy(time, videoLink, email, text):
email, videoLink, time → text
Watch(videoLink, email, watchTime):
videoLink, email → watchTime
* only non-trivial functional dependencies included, i.e. relations with only key attributes not listed
```

### 6. Normalization
All of the above Relations contain only primary or candidate key dependencies, and are therefore already in BCNF, with the exception of the following: 
** Note: Underline is Primary Key (PK), bold is Foreign Key (FK)
Advertisement_boughtBy(adID, compensation, length, sponsorName)
PK/CK: adID
length → compensation
Moderator(employeeID, firstName, lastName, salary, team, supervisor)
PK/CK: employeeID
team → supervisor

Advertisement_boughtBy
Advertisement_boughtBy(adID, compensation, length, sponsorName) is not in BCNF because the dependency length → compensation violates BCNF. length is not a superkey for the relation (since the closure of length length+ = {length, compensation}). 
We decompose Advertisements on length → compensation and split it into the relations: 
Advertisements_boughtBy1(length, compensation)
PK/CK: length
Advertisements_boughtBy2(length, adID, sponsorName)
PK/CK: adID
FK: sponsorName
Checking that Advertisements1 is in BCNF:
This is a relation of 2 attributes, so it is in BCNF.
Checking that Advertisements2 is in BCNF:
compensation is not in this relation, so there are no violating functional dependencies and it is in BCNF.

Moderator
Moderator is not in BCNF because the dependency team → supervisor violates BCNF. team is not a superkey for the relation (since closure of team team+ = {team, supervisor}). We split it into the relations
Moderator1(team, supervisor)
PK/CK: team
Moderator2(team, employeeID, firstName, lastName, salary)
PK/CK: employeeId
Check that Moderator1 is in BCNF:
This is a relation of 2 attributes, so it is in BCNF.
Check that Moderator2 is in BCNF:
No violating FD’s exist, so it is in BCNF.


After Normalization:
** Notation:  Underline is primary key, bold is foreign key
Abbreviations: primary key (PK), candidate key (CK), foreign key (FK)
```
Account_watches(email: VARCHAR, alias: VARCHAR, password: VARCHAR, livestreamLink: VARCHAR)
PK: email
CK: alias, email
FK: livestreamLink
alias is unique, not null; password is not null 
Channel_owns(email: VARCHAR, userID: INTEGER, description: VARCHAR, subscribers: INTEGER) 
PK/CK: email
FK: userID, email
ViewerAccount_watchAs(email: VARCHAR, userID: INTEGER, plan: VARCHAR)
PK/CK: email
FK: userID, email
ModeratorAccount_uses(email: VARCHAR, rank: INTEGER, employeeID)
PK/CK: email
FK: email, employeeID
Video_postedBy_sponsoredBy(videoLink: VARCHAR, title: VARCHAR, likes: INTEGER, email: VARCHAR, sponsorName: VARCHAR)
PK: videolink
CK: videolink, (title, email)
FK: email, sponsorName
email is not null
Sponsor(sponsorName: VARCHAR, sponsorEmail: VARCHAR, country: VARCHAR)
PK: sponsorName
CK: sponsorName, sponsorEmail
sponsorEmail is unique
Advertisements_boughtBy1(length:REAL, compensation:REAL)
PK/CK: length
Advertisements_boughtBy2(length:REAL, adID:INTEGER, sponsorName: VARCHAR)
PK/CK: adID
FK: sponsorName
User(userId: INTEGER, firstName: VARCHAR, lastName: VARCHAR)
PK/CK: userId
Moderator1(team: VARCHAR, supervisor:VARCHAR)
PK/CK: team
Moderator2(employeeID:INTEGER, firstName:VARCHAR, lastName:VARCHAR, salary:REAL, team: VARCHAR)
PK/CK: employeeID
Livestream_streams(link: VARCHAR, likes: INTEGER, views: INTEGER, email: VARCHAR) 
PK/CK: link
FK: email
email is not null and is unique
Comment_postedOn_writtenBy(time: CHAR(16), email: VARCHAR, videoLink: VARCHAR, text: VARCHAR)
PK/CK: (time, email, videoLink)
FK: email, videoLink
Watch(videoLink: VARCHAR, email: VARCHAR, watchTime: INTEGER)
PK/CK: (videoLink, email)
FK: videoLink, email
Plays(videoLink: VARCHAR, adID: INTEGER)
PK/CK: (videoLink, adID)
FK: videoLink, adID
SubscribesTo(accountEmail: INTEGER, channelEmail: INTEGER)
PK/CK: (accountEmail, channelEmail)
FK: accountEmail, channelEmail
accountEmail, channelEmail are both ‘email’ in their parent tables, renamed for clarity
ApprovedBy(email: VARCHAR, videoLink: VARCHAR)
PK/CK: (email, videoLink)
FK: email, videoLink
```

### 7. SQL DDL:
```sql
CREATE TABLE Account_watches (
	email VARCHAR,
	alias VARCHAR UNIQUE NOT NULL,
	password VARCHAR NOT NULL, 
	livestreamLink: VARCHAR,
	PRIMARY KEY (email),
	FOREIGN KEY (livestreamLink) REFERENCES Livestream_streams(link))

CREATE TABLE Channel_owns (
   	email VARCHAR,
   	userID INTEGER,
   	description VARCHAR,
   	subscribers INTEGER,
PRIMARY KEY (email),
FOREIGN KEY (email) REFERENCES Account_watches(email),
FOREIGN KEY (userID) REFERENCES User(userID))

CREATE TABLE ViewerAccount_watchAs (
   	email VARCHAR,
    	userID INTEGER,
    	plan VARCHAR,
	PRIMARY KEY (email),
	FOREIGN KEY (email) REFERENCES Account_watches(email),
    	FOREIGN KEY (userID) REFERENCES User(userID))

CREATE TABLE ModeratorAccount_uses (
    	email VARCHAR,
    	rank INTEGER,
    	employeeID INTEGER,
	PRIMARY KEY (email),
FOREIGN KEY (email) REFERENCES Account_watches(email),
    	FOREIGN KEY (employeeID) REFERENCES Moderator(employeeID))

CREATE TABLE Video_postedBy_sponsoredBy (
    	videoLink VARCHAR,
    	title VARCHAR,
    	likes INTEGER,
    	email VARCHAR NOT NULL,
    	sponsorName VARCHAR,
	PRIMARY KEY (videoLink), 
    	FOREIGN KEY (email) REFERENCES Channel_owns(email),
    	FOREIGN KEY (sponsorName) REFERENCES Sponsor(sponsorName))

CREATE TABLE Sponsor (
    	sponsorName VARCHAR,
    	sponsorEmail VARCHAR UNIQUE,
    	country VARCHAR,
	PRIMARY KEY (sponsorName))

CREATE TABLE Advertisements_boughtBy1 (
	length REAL,
	compensation REAL,
	PRIMARY KEY (length))

CREATE TABLE Advertisements_boughtBy2 (
	adID INTEGER,
	length REAL,
sponsorName VARCHAR,
	PRIMARY KEY (adID),
FOREIGN KEY (sponsorName) REFERENCES Sponsor(sponsorName))

CREATE TABLE User (
    	userID INTEGER,
    	firstName VARCHAR,
    	lastName VARCHAR,
PRIMARY KEY (userID))

CREATE TABLE Moderator1 (
    	team VARCHAR,
    	supervisor VARCHAR,
	PRIMARY KEY (team))

CREATE TABLE Moderator2 (
    	employeID INTEGER,
    	firstName VARCHAR,
   	lastName VARCHAR,
    	salary REAL,
    	team VARCHAR,
	PRIMARY KEY (employeeID))

CREATE TABLE Livestream_streams (
    	link VARCHAR,
    	likes INTEGER,
    	views INTEGER,
    	email VARCHAR UNIQUE NOT NULL,
PRIMARY KEY (link),
    	FOREIGN KEY (email) REFERENCES Channel_owns (email))

CREATE TABLE Comment_postedOn_writtenBy (
    	time CHAR(16),
    	email INTEGER,
    	videoLink VARCHAR,
    	text VARCHAR,
    	PRIMARY KEY (time, email, videoLink),
    	FOREIGN KEY (email) REFERENCES Account_watches(email),
    	FOREIGN KEY (videoLink) REFERENCES Video_postedBy_sponsoredBy (videoLink))

CREATE TABLE Watch (
    	videoLink VARCHAR,
    	email VARCHAR,
    	watchTime INTEGER,
    	PRIMARY KEY (videoLink, email),
    	FOREIGN KEY (videoLink) REFERENCES Video_postedBy_sponsoredBy(videoLink),
    	FOREIGN KEY (email) REFERENCES Account_watches(email))

CREATE TABLE Plays (
    	videoLink VARCHAR,
    	adID INTEGER NOT NULL,
    	PRIMARY KEY (videoLink, adID),
    	FOREIGN KEY (videoLink) REFERENCES Video_postedBy_sponsoredBy(videoLink),
    	FOREIGN KEY (adID) REFERENCES Advertisement_boughtBy2(adID))

CREATE TABLE SubscribesTo (
    	accountEmail INTEGER,
    	channelEmail INTEGER,
    	PRIMARY KEY (accountEmail, channelEmail),
    	FOREIGN KEY (accountEmail) REFERENCES Account_watches(email),
    	FOREIGN KEY (channelEmail) REFERENCES Channel_owns(email))

CREATE TABLE ApprovedBy (
    	email VARCHAR,
    	videoLink VARCHAR,
    	PRIMARY KEY (email, videoLink),
   	FOREIGN KEY (email) REFERENCES ModeratorAccount_uses(email),
    	FOREIGN KEY (videoLink) REFERENCES Video_postedBy_sponsoredBy(videoLink))

```
8. INSERT Statements:
```sql
INSERT INTO Account_watches (email, alias, password, livestreamLink) VALUES
('user1@test.com', 'user1', 'pass123', 'examplelink.com/1'),
('user2@test.com', 'user2', 'pass123', 'examplelink.com/2'),
('user3@test.com', 'user3', 'pass123', 'examplelink.com/3'),
('user4@test.com', 'user4', 'pass123', 'examplelink.com/4'),
('user5@test.com', 'user5', 'pass123', 'examplelink.com/5');

INSERT INTO Channel_owns (email, userID, description, subscribers) VALUES
('channel1@test.com', 00001, 'Tech', 1000),
('channel2@test.com', 00002, 'Gaming', 2000),
('channel3@test.com', 00003, 'Cooking', 3000),
('channel4@test.com', 00004, 'Travel', 4000),
('channel5@test.com', 00005, 'Education', 5000);

INSERT INTO ViewerAccount_watchAs (email, userID, plan) VALUES
('viewer1@test.com', 00001, 'Free'),
('viewer2@test.com', 00002, 'Premium'),
('viewer3@test.com', 00003, 'Free'),
('viewer4@test.com', 00004, 'Premium'),
('viewer5@test.com', 00005, 'Free');

INSERT INTO ModeratorAccount_uses (email, rank, employeeID) VALUES
('moderator1@test.com', 1, 1001),
('moderator2@test.com', 2, 1002),
('moderator3@test.com', 3, 1003),
('moderator4@test.com', 4, 1004),
('moderator5@test.com', 5, 1005);

INSERT INTO Video_postedBy_sponsoredBy(videoLink, title, likes, email, sponsorName) VALUES
	('videoLink1', ‘Top 10 Treacherous Tales’, 1000, 'channel1@test.com', NULL),
('videoLink2', ‘DIY Makeup Tutorial’, 2000, 'channel2@test.com', ‘Sponsor1’),
(videoLink3', ‘Unboxing a TI-84’, 3000, 'channel3@test.com', ‘Sponsor2’),
('videoLink4', ‘Pirate Movie’, 4000, 'channel4@test.com', NULL),
('videoLink5', ‘disappointment’, 5000, 'channel5@test.com', NULL);

INSERT INTO Sponsor (sponsorName, sponsorEmail, country) VALUES
('Sponsor1', 'sponsor1@test.com', 'Canada'),
('Sponsor2', 'sponsor2@test.com', 'USA'),
('Sponsor3', 'sponsor3@test.com', 'Japan'),
('Sponsor4', 'sponsor4@test.com', 'Canada'),
('Sponsor5', 'sponsor5@test.com', 'UK');

INSERT INTO Advertisements_boughtBy1 (length, compensation) VALUES
(30.5, 100.00),
(45.0, 150.00),
(60.0, 200.00),
(15.5, 50.00),
(90.5, 300.00);

INSERT INTO Advertisements_boughtBy2 (adID, length, sponsorName) VALUES
(1, 30.5, 'Sponsor1'),
(2, 45.0, 'Sponsor2'),
(3, 60.0, 'Sponsor3'),
(4, 15.5, 'Sponsor4'),
(5, 90.5, 'Sponsor5');

INSERT INTO User (userID, firstName, lastName) VALUES
(00001, 'John', 'Smith'),
(00002, 'Jane', 'Jackson'),
(00003, 'Mike', 'Smith'),
(00004, 'Sara', 'Lee'),
(99999, 'Chris', 'Brown');

INSERT INTO Moderator1 (team, supervisor) VALUES
('TeamA', 'Supervisor1'),
('TeamB', 'Supervisor2'),
('TeamC', 'Supervisor3'),
('TeamA', 'Supervisor1'),
('TeamB', 'Supervisor2');

INSERT INTO Moderator2 (employeID, firstName, lastName, salary, team) VALUES
(1001, 'John', 'Finn', 50000, 'TeamA'),
(1002, 'Jack', 'Owen', 52000, 'TeamB'),
(1003, 'Jay', 'Smith', 53000, 'TeamC'),
(1004, 'Anna', 'Jackson', 54000, 'TeamA'),
(1005, 'Chris', 'Davis', 55000, 'TeamB');

INSERT INTO Livestream_streams (link, likes, views, email) VALUES
('examplelink.com/1', 1001, 1000, 'channel1@test.com'),
('examplelink.com/2', 2006, 2000, 'channel2@test.com'),
('examplelink.com/3', 3002, 3000, 'channel3@test.com'),
('examplelink.com/4', 4200, 4000, 'channel4@test.com'),
('examplelink.com/5', 5030, 5000, 'channel5@test.com');

INSERT INTO Comment_postedOn_writtenBy (time, email, videoLink, text) VALUES
('03/04/2024 21:10', 'user1@test.com', 'videoLink1', 'Great video!'),
('02/29/2024 21:10', 'user2@test.com', 'videoLink2', 'Very Good.'),
('03/04/2024 21:11', 'user3@test.com', 'videoLink3', 'Love This!'),
('02/27/2024 11:10', 'user4@test.com', 'videoLink4', 'Can't wait for the next one.'),
('03/04/2024 21:19', 'user5@test.com', 'videoLink5', 'Really helpful!');

INSERT INTO Watch (videoLink, email, watchTime) VALUES
('videoLink1', 'email1@test.com', 10),
('videoLink2', 'email2@test.com', 3),
('videoLink3', 'email3@test.com', 24),
('videoLink4', 'email4@test.com', 11),
('videoLink5', 'email5@test.com', 5);

INSERT INTO Plays (videoLink, adID) VALUES
('videoLink1', 101),
('videoLink2', 102),
('videoLink3', 103),
('videoLink4', 104),
('videoLink5', 105);

INSERT INTO SubscribesTo (accountEmail, channelEmail) VALUES
('account1@test.com', 'channel1@test.com'),
('account2@test.com', 'channel2@test.com'),
('account3@test.com', 'channel3@test.com'),
('account4@test.com', 'channel4@test.com'),
('account5@test.com', 'channel5@test.com');

INSERT INTO ApprovedBy (email, videoLink) VALUES
('moderator1@test.com', 'videoLink1'),
('moderator2@test.com', 'videoLink2'),
('moderator3@test.com', 'videoLink3'),
('moderator4@test.com', 'videoLink4'),
('moderator5@test.com', 'videoLink5');
```

