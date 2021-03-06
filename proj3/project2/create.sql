CREATE TABLE Items(ItemID VARCHAR(40) NOT NULL PRIMARY KEY, Name VARCHAR(256) NOT NULL, Currently DECIMAL(8,2) 
					NOT NULL, Buy_Price DECIMAL(8,2) NULL, First_Bid DECIMAL(8,2) NOT NULL, 
					Number_of_Bids INTEGER NOT NULL, Location VARCHAR(256) NOT NULL, Latitude VARCHAR(40) NULL, 
					Longitude VARCHAR(40) NULL, Country VARCHAR(256) NOT NULL, Started TIMESTAMP NOT NULL, 
					Ends TIMESTAMP NOT NULL, SellerID VARCHAR(40) NOT NULL, Description VARCHAR(4000) NOT NULL);
CREATE TABLE Categories(ItemID VARCHAR(40) NOT NULL, CategoryName VARCHAR(256) NOT NULL, CONSTRAINT pk_categories PRIMARY KEY (ItemID, CategoryName));
CREATE TABLE Sellers(UserID VARCHAR(40) NOT NULL PRIMARY KEY, Rating INTEGER NOT NULL);
CREATE TABLE Bidders(UserID VARCHAR(40) NOT NULL PRIMARY KEY, Rating INTEGER NOT NULL, Location VARCHAR(256), Country VARCHAR(256));
CREATE TABLE Bids(UserID VARCHAR(40) NOT NULL, Time TIMESTAMP NOT NULL, Amount DECIMAL(8,2) NOT NULL, ITEMID VARCHAR(40) NOT NULL, CONSTRAINT pk_bids PRIMARY KEY (UserID, Time));