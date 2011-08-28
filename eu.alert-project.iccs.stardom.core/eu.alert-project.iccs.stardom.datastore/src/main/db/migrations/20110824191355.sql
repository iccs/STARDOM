create table its_ml(
  id int primary key not null unique,
  bug_id integer,
  uuid varchar(256) null,
  status_date timestamp null,
  status varchar(100) null
)engine=innodb;

insert into sequence values('its_ml_sequence',0);
