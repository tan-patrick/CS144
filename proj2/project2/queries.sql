
SELECT COUNT(DISTINCT UserID) 
FROM (SELECT UserID FROM Sellers 
		UNION ALL
	SELECT UserID FROM Bidders) AS Users;


SELECT COUNT(*)
FROM Items
WHERE BINARY location="New York";



SELECT COUNT(*) 
FROM (SELECT ItemID, COUNT(CategoryName) as numCats 
		FROM Categories
		GROUP BY ItemID) AS ItemCatNums
WHERE numCats=4;


SELECT ItemID
FROM Items
WHERE Ends >'2001-12-20 00:00:01'
	AND Number_of_Bids > 0
	AND Currently = 
		(SELECT MAX(Currently) 
		FROM Items
		WHERE Ends > '2001-12-20 00:00:01'
			AND Number_of_Bids > 0);
			

SELECT COUNT(*) 
FROM Sellers
WHERE Rating > 1000;



SELECT COUNT(*)
FROM Sellers, Bidders
WHERE Sellers.userID = Bidders.userID;


SELECT COUNT(DISTINCT CategoryName) 
FROM Categories,
	( SELECT DISTINCT ItemID 
	 FROM Bids
	 WHERE Amount>100 ) AS ITEM100
WHERE Categories.ItemID = ITEM100.ItemID; 

