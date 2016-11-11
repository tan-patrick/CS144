<!DOCTYPE html>
<html>
	<head>
		<title>Credit Card Input</title>
	</head>
	<body>
		<div align="center">
			<h1>Credit Card Input</h1>
		</div>

		<a href="http://<%= request.getAttribute("host") %>:1448/eBay/keywordSearch.html">Search for another Item</a>

		<br>
		<p>ItemID: <%= request.getAttribute("itemID") %></p>
		<p>ItemName: <%= request.getAttribute("itemName") %></p>
		<p>Buy_Price: <%= request.getAttribute("buyPrice") %></p>
		<p>Credit Card: <%= request.getAttribute("creditCard") %></p>
		<p>Time: <%= request.getAttribute("time") %></p>

	</body>
</html>