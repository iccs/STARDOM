create table sequence(
  sequence_name varchar(100) primary key unique,
  sequence_index INT NOT NULL
) type=innodb;

create table schema_history(
  history_date timestamp default now(),
  description text not null
) type=innodb;


create table `identity` (
  id int primary key not null unique,
  uuid varchar(256) not null
)type innodb;
insert into sequence values('identity_sequence',0);


create table person(
  id int primary key not null unique,
  `name` varchar(255) not null,
  `lastname` varchar(255) not null,
  `username` varchar(50) not null,
  `email` varchar(255) not null
)type innodb;
insert into sequence values('person_sequence',0);


create table identity_is_person(
  identity_id int not null,
  person_id int not null,
  FOREIGN KEY(identity_id) REFERENCES `identity`(id) ON DELETE CASCADE,
  FOREIGN KEY(person_id) REFERENCES person(id) ON DELETE CASCADE
) type=innodb;

create table identity_not_person(
  identity_id int not null,
  person_id int not null,
  FOREIGN KEY(identity_id) REFERENCES `identity`(id) ON DELETE CASCADE,
  FOREIGN KEY(person_id) REFERENCES person(id) ON DELETE CASCADE
) type=innodb;


