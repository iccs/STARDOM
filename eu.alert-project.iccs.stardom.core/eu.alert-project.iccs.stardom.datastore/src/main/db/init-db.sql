drop database if exists alert_dev;
drop database if exists alert_test;

create database alert_dev;
create database alert_test;

grant all privileges on alert_dev.* to 'alert'@'localhost' identified by '1234';
grant all privileges on alert_test.* to 'alert'@'localhost' identified by '1234';

