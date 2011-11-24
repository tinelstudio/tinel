select *
from address a
where ST_distance_sphere(a.point, GeomFromText('POINT(15.073435 45.5132341)', 4326)) <= 2000
order by ST_distance_sphere(a.point, GeomFromText('POINT(15.073435 45.5132341)', 4326))
--limit 1