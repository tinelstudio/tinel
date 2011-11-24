select t.td
from (
	select
	ST_distance_sphere(
		GeomFromText('POINT(14.508301 46.048793)', 4326),
		ST_line_interpolate_point(s.lineString,
			ST_line_locate_point(s.lineString, GeomFromText('POINT(14.508301 46.048793)', 4326))
		)
	)
	as mydist, s as td
	from street s
) as t
where t.mydist <= 10000
order by t.mydist
