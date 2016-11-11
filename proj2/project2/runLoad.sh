#!/bin/bash

# Run the drop.sql batch file to drop existing tables
# Inside the drop.sql, you sould check whether the table exists. Drop them ONLY if they exists.
mysql CS144 < drop.sql

# Run the create.sql batch file to create the database and tables
mysql CS144 < create.sql

# Compile and run the parser to generate the appropriate load files
ant
ant run-all

# If the Java code does not handle duplicate removal, do this now
sort ./Items.dat | uniq > ./Items2.dat
sort ./Categories.dat | uniq > ./Categories2.dat
sort ./Sellers.dat | uniq > ./Sellers2.dat
sort ./Bidders.dat | uniq > ./Bidders2.dat
sort ./Bids.dat | uniq > ./Bids2.dat
echo 'Duplicates being removed...'

# Run the load.sql batch file to load the data
mysql CS144 < load.sql
echo 'Tables being loaded...'

# Remove all temporary files
rm 'Items.dat'
rm 'Categories.dat'
rm 'Sellers.dat'
rm 'Bidders.dat'
rm 'Bids.dat'

# rm 'Items2.dat'
rm 'Categories2.dat'
rm 'Sellers2.dat'
rm 'Bidders2.dat'
rm 'Bids2.dat'
echo 'Temporary files being removed...'
