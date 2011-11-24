select 
--distinct on (gn1.name) 
a
from address a
join address_geoname agn1 on a.id=agn1.address_id
join geoname gn1 on agn1.geonames_id=gn1.id
where lower(gn1.name) like '%cank%' and gn1.type='ADDRESS'
limit 1000


/*
select a.*
from address a
where a.id in (
	select min(a.id)
	--, gn1.name
	from address a
	join address_geoname agn1 on a.id=agn1.address_id
	join geoname gn1 on agn1.geonames_id=gn1.id
	where (gn1.name like '%SLOV%' or gn1.name like '%BOV%') and gn1.type='ADDRESS'
	group by gn1.name
)
*/