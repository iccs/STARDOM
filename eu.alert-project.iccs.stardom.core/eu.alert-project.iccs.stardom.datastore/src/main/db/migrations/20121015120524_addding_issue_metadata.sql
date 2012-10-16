create table issue_metadata(
  id int primary key not null unique,
  issue_id varchar(256) not null ,
  issue_uri varchar(256) not null ,
  component varchar(256) not null ,
  component_uri varchar(256) not null,
  subject varchar(256) not null,
  issue_created datetime not null
)engine=innodb;

insert into sequence values('issue_metadata_sequence',0);
