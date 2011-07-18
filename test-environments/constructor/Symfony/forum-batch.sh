#!/bin/bash

php5="/Applications/MAMP/bin/php/php5.3.6/bin/php"

for l in {0..100};
do
	
  from=$(($l * 10))
	$php5 app/console iccs:forums $from 10;

	sleep 60;
done;

