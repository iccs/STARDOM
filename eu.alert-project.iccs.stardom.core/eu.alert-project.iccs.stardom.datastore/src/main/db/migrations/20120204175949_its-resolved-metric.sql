create table its_issues_resolved_metric(
  id int primary key not null unique,
  FOREIGN KEY (id) REFERENCES metric_quantitative(id) ON DELETE CASCADE ON UPDATE CASCADE
)engine innodb;

create or replace view its_issues_resolved_metric_view
as
select *
from its_issues_resolved_metric
join metric_quantitative using(id)
join metric using(id);
