select *
from address a
where intersects(a.point, expand(GeomFromText('POINT(15.073435 45.5132341)', 4326), 0.2))=true
order by distance(a.point, GeomFromText('POINT(15.073435 45.5132341)', 4326))
--limit 1