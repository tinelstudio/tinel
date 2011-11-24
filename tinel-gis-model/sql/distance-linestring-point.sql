select s
from street s
where
ST_distance_sphere(
	GeomFromText('POINT(14.508301 46.048793)', 4326),
	ST_line_interpolate_point(s.lineString, ST_line_locate_point(s.lineString, GeomFromText('POINT(14.508301 46.048793)', 4326)))
)
<= 10000
order by 
ST_distance_sphere(
	GeomFromText('POINT(14.508301 46.048793)', 4326),
	ST_line_interpolate_point(s.lineString, ST_line_locate_point(s.lineString, GeomFromText('POINT(14.508301 46.048793)', 4326)))
)
