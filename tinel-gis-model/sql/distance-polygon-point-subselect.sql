select *
from (
	select
	ST_distance_sphere(
		GeomFromText('POINT(14.508301 46.048793)', 4326),
		ST_line_interpolate_point(ST_ExteriorRing(b.polygon), 
			ST_line_locate_point(ST_ExteriorRing(b.polygon),
				GeomFromText('POINT(14.508301 46.048793)', 4326)
			)
		)
	)
	as mydist, b.id as td
	from building b
) as t
where t.mydist <= 1000
order by t.mydist
