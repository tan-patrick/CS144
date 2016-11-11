CREATE TABLE ItemLocations (ItemID VARCHAR(40), Locations POINT NOT NULL, SPATIAL INDEX(locations)) ENGINE=MyISAM;

INSERT INTO ItemLocations (ItemID, Locations) 
SELECT ItemID, Point(CAST(Latitude AS DECIMAL(12, 9)), CAST(Longitude AS DECIMAL(12, 9))) FROM Items WHERE Latitude IS NOT NULL AND Longitude IS NOT NULL;
