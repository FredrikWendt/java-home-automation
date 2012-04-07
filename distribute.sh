#!/bin/sh

for i in film filserver ; do 
	scp src/main/scripts/station.sh ceda@$i:lib/
	scp target/bluetooth-mon-0.0.1-SNAPSHOT-jar-with-dependencies.jar ceda@$i:lib/home-automation.jar
done
