<!DOCTYPE html>
<html>
	<head>
		<title>Credit Card Input</title>
	</head>
	<body>
		<div align="center">
			<h1>Credit Card Input</h1>
		</div>

		<p>ItemID: <%= request.getAttribute("itemID") %></p>
		<p>ItemName: <%= request.getAttribute("itemName") %></p>
		<p>Buy_Price: <%= request.getAttribute("buyPrice") %></p>

		<form action="https://<%= request.getAttribute("host") %>:8443/eBay/confirmation" method="GET">
			Credit Card:
			<input id="creditCard" type="text" name="creditCard" value=""> <br>
			<input type="submit" value="Submit">
		</form>

	</body>
</html>