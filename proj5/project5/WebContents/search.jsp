<!DOCTYPE html>
<html>
	<head>
		<title>Search Results</title>
		
        <script type="text/javascript" src="autosuggest.js"></script>
        <script type="text/javascript" src="remotesuggestions.js"></script>
        <link rel="stylesheet" type="text/css" href="autosuggest.css" />        
        <script type="text/javascript">
            window.onload = function () {
                var oTextbox = new AutoSuggestControl(document.getElementById("search"), new RemoteStateSuggestions());        
            }
        </script>
		
	</head>
	<body>
		<div align="center">
			<h1>Search Results</h1>
			
			<form action="/eBay/search" method="GET">
			Enter Another Search Here:<br>
			<input id="search" type="text" name="q" value="">
			<input type="hidden" name="numResultsToSkip" value="0">
			<input type="hidden" name="numResultsToReturn" value="10" >
			<input type="submit" value="Submit">
			</form>
		</div>

		<%= request.getAttribute("results") %>
		
		<a href="search?q=<%= request.getParameter("q") %>&numResultsToSkip=<%= request.getAttribute("prevPageStart")%>&numResultsToReturn=<%= request.getParameter("numResultsToReturn")%>"/>Prev</a>
		<a href="search?q=<%= request.getParameter("q") %>&numResultsToSkip=<%= request.getAttribute("nextPageStart")%>&numResultsToReturn=<%= request.getParameter("numResultsToReturn")%>"/>Next</a>

	</body>
</html>