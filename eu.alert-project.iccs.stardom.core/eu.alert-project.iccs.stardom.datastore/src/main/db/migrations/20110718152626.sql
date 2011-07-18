create table mailing_list_activity_metric(
  id int primary key not null unique,
  FOREIGN KEY (id) REFERENCES metric_quantitative(id) ON DELETE CASCADE ON UPDATE CASCADE
)engine=innodb;


create table mailing_list_temporal_metric(
  id int primary key not null unique,
  FOREIGN KEY (id) REFERENCES metric_temporal(id) ON DELETE CASCADE ON UPDATE CASCADE
)engine=innodb;
