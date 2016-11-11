TEAM: Tan_Lau

Name: Roger Lau, ID: 104115407
Name: Patrick Tan, ID: 204158646

Our indexer indexes the ItemID, Name, and the union of name, category, and description. 
We index ItemID and Name separately because we need to return them when we run basicSearch or spatialSearch.
We index the union of name, category, and description together since we want to search through all of them.

Note: We only need to store ItemID and Name since those are the only ones we actually need to know and return later.
	The search string of name, category, and description is just needed for the search to occur and does not need to be returned.
	
We do not need to index longitude or latitude since it will be done in the spatial index.




This example contains a simple utility class to simplify opening database
connections in Java applications, such as the one you will write to build
your Lucene index. 

To build and run the sample code, use the "run" ant target inside
the directory with build.xml by typing "ant run".
