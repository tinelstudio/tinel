select *
from building b
where
ST_distance_sphere(
        GeomFromText('POINT(14.508301 46.048793)', 4326),
        ST_line_interpolate_point(ST_ExteriorRing(b.polygon), 
		ST_line_locate_point(ST_ExteriorRing(b.polygon),
			GeomFromText('POINT(14.508301 46.048793)', 4326)
		)
	)
)
<= 2000
order by
ST_distance_sphere(
        GeomFromText('POINT(14.508301 46.048793)', 4326),
        ST_line_interpolate_point(ST_ExteriorRing(b.polygon), 
		ST_line_locate_point(ST_ExteriorRing(b.polygon),
			GeomFromText('POINT(14.508301 46.048793)', 4326)
		)
	)
)

