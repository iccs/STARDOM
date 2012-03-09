-- create table scm_api_introduced_metric(
--   id int primary key not null unique,
--   FOREIGN KEY (id) REFERENCES metric_quantitative(id) ON DELETE CASCADE ON UPDATE CASCADE
-- )engine innodb;

alter table scm_api_introduced_metric add column amount integer;
