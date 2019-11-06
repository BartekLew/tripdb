#!/bin/bash -e

gradle download

pushd deps
for aar in *.aar
do
	unzip "$aar" classes.jar
	mv classes.jar "$aar.jar"
	rm "$aar"
done

popd

find deps -type f | awk '{printf $0 ":"}' > .deps-ok
