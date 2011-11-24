select *
from address a
where ST_DWithin(a.point, GeomFromText('POINT(15.073435 45.5132341)', 4326), 0.09)=true
order by ST_Distance(a.point, GeomFromText('POINT(15.073435 45.5132341)', 4326))
--limit 1