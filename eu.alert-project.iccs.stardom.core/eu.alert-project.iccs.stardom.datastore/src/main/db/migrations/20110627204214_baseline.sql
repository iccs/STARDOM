create table sequence(
  sequence_name varchar(100) primary key unique,
  sequence_index INT NOT NULL
) engine=innodb;

create table schema_history(
  history_date timestamp default now(),
  description text not null
) engine=innodb;


create table `identity` (
  id int primary key not null unique,
  uuid varchar(256) not null
)engine innodb;
insert into sequence values('identity_sequence',0);


create table profile(
  id int primary key not null unique,
  `name` varchar(255) null,
  `lastname` varchar(255) null,
  `username` varchar(50) null,
  `email` varchar(255) null
)engine innodb;
insert into sequence values('profile_sequence',0);


create table identity_is_profile(
  identity_id int not null,
  profile_id int not null,
  FOREIGN KEY(identity_id) REFERENCES `identity`(id) ON DELETE CASCADE,
  FOREIGN KEY(profile_id) REFERENCES profile(id) ON DELETE CASCADE
) engine=innodb;

create table identity_not_profile(
  identity_id int not null,
  profile_id int not null,
  FOREIGN KEY(identity_id) REFERENCES `identity`(id) ON DELETE CASCADE,
  FOREIGN KEY(profile_id) REFERENCES profile(id) ON DELETE CASCADE
) engine=innodb;


