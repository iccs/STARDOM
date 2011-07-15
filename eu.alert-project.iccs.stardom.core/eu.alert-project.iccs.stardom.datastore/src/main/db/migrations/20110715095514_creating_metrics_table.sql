create table metric(
  id int primary key not null unique,
  created_at timestamp not null default CURRENT_TIMESTAMP
)type innodb;
insert into sequence values('metric_sequence',0);


create table metric_temporal(
  id int primary key not null unique,
  `when` timestamp not null,
  FOREIGN KEY (id) REFERENCES metric(id) ON DELETE CASCADE ON UPDATE CASCADE
)type innodb;


create table metric_quantitative(
  id int primary key not null unique,
  `quantity` integer not null default 0,
  FOREIGN KEY (id) REFERENCES metric(id) ON DELETE CASCADE ON UPDATE CASCADE
)type innodb;









--
--
-- create table forum_activity_metric(
--   id int primary key not null unique,
--   activity int not null default 0,
--
-- )type innodb;
--
-- create table forum_timestamp_metric(
--   id int primary key not null unique,
--   `when` not null,
--   FOREIGN KEY (id) REFERENCES metric(id) ON DELETE CASCADE ON UPDATE CASCADE
-- )type innodb;
--
-- create table its_activity_metric(
--   id int primary key not null unique,
--   activity int not null default 0,
--   FOREIGN KEY (id) REFERENCES metric(id) ON DELETE CASCADE ON UPDATE CASCADE
-- )type innodb;
--
-- create table scm_activity_metric(
--   id int primary key not null unique,
--   activity int not null default 0,
--   FOREIGN KEY (id) REFERENCES metric(id) ON DELETE CASCADE ON UPDATE CASCADE
-- )type innodb;
--
-- create table scm_api_introduced_metric(
--   id int primary key not null unique,
--   `count` int not null default 0,
--   FOREIGN KEY (id) REFERENCES metric(id) ON DELETE CASCADE ON UPDATE CASCADE
-- )type innodb;
--
--
-- create table scm_api_usage_metric(
--   id int primary key not null unique,
--   `use` int not null default 0,
--   FOREIGN KEY (id) REFERENCES metric(id) ON DELETE CASCADE ON UPDATE CASCADE
-- )type innodb;
--
-- create table scm_timestamp_metric(
--   id int primary key not null unique,
--   `when` not null,
--   FOREIGN KEY (id) REFERENCES metric(id) ON DELETE CASCADE ON UPDATE CASCADE
-- )type innodb;
--
--
