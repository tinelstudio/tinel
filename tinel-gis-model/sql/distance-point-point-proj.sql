select *
from address a
where ST_DWithin(ST_Transform(a.point, 3395), ST_Transform(GeomFromText('POINT(14.508301 46.048793)', 4326), 3395), 500) = true
order by ST_Distance(ST_Transform(a.point, 3395), ST_Transform(GeomFromText('POINT(14.508301 46.048793)', 4326), 3395))

/*
select *
from address a
where ST_DWithin(ST_Transform(a.point, 32633), ST_Transform(GeomFromText('POINT(14.508301 46.048793)', 4326), 32633), 500) = true
order by ST_Distance(ST_Transform(a.point, 32633), ST_Transform(GeomFromText('POINT(14.508301 46.048793)', 4326), 32633))
*/