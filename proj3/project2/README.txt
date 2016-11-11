TEAM: Tan_Lau

Name: Roger Lau, ID: 104115407
Name: Patrick Tan, ID: 204158646


1)
Items (ItemID, Name, Currently, Buy_Price (NULLABLE), First_Bid, Number_of_Bids, Location, Latitude (NULLABLE), Longitude (NULLABLE), Country, Started, Ends, SellerID, Description)
key: ItemID

Categories (ItemID, CategoryName)
key: ItemID, CategoryName

Sellers (UserID, Rating)
key: UserID

Bidders (UserID, Rating, Location, Country)
key: UserID

Bids (UserID, Time, Amount, ItemID)
key: ItemID, Time

2)
None

3)
Yes, because there are no nontrivial functional dependencies left (hence the answer to step two).

4)
Yes, because there are no multivalue dependencies in our table design. 