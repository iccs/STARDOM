#!/bin/bash

php5="/Applications/MAMP/bin/php/php5.3.6/bin/php"

for l in {0..21};
do
  from=$(($l * 10))
	$php5 app/console iccs:scm $from 5000;
	sleep 30;
done;

