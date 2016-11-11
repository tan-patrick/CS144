#!/bin/bash

ant build
sudo rm -R /var/lib/tomcat7/work/Catalina/localhost/eBay
ant deploy