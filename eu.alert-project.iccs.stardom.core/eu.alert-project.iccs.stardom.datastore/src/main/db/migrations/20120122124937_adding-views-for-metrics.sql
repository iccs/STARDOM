create or replace view scm_activity_metric_view
as
select *
from scm_activity_metric
join metric_quantitative using(id)
join metric using(id);

create or replace view its_activity_metric_view
as
select *
from its_activity_metric
join metric_quantitative using(id)
join metric using(id);


create or replace view mailing_list_activity_metric_view
as
select *
from mailing_list_activity_metric
join metric_quantitative using(id)
join metric using(id);

create or replace view scm_api_introduced_metric_view
as
select *
from scm_api_introduced_metric
join metric_quantitative using(id)
join metric using(id);

create or replace view scm_api_usage_count_metric_view
as
select *
from scm_api_usage_count_metric
join metric_quantitative using(id)
join metric using(id);

create or replace view scm_temporal_metric_view
as
select *
from scm_temporal_metric
join metric_temporal using(id)
join metric using(id);

create or replace view its_temporal_metric_view
as
select *
from its_temporal_metric
join metric_temporal using(id)
join metric using(id);


create or replace view mailing_list_temporal_metric_view
as
select *
from mailing_list_temporal_metric
join metric_temporal using(id)
join metric using(id);

create or replace view identity_profile_view
as
select i.id, i.uuid,ip.profile_id,p.name, p.lastname, p.username, p.email, p.source, p.source_id from identity i join identity_is_profile ip on i.id=ip.identity_id left outer join profile p on ip.profile_id=p.id;



