TEAM: Tan_Lau

Name: Roger Lau, ID: 104115407
Name: Patrick Tan, ID: 204158646

Question 1)
	We have a secure connection from (4)->(5) and from (5)->(6). Ie, the credit card number is always secure when in transit. This is accomplished by making a secure request to the
	confirmation page, which results in a secure request and response. 

Question 2) 
	We use sessions. The buy price is taken from the database while processing the request for the item page; while processing the item page, the buy price is then placed into a 
	session. This is the buy price that is used, and so the user never touches it. 