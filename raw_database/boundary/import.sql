--BEGIN;
create temp table bound ( ra float,de float,abbr text,type text);

.separator "\t"
.import boundary.csv bound

create table constellation_boundary (_id integer primary key,
ra float, de float, abbr text, type text);

insert into constellation_boundary(ra,de,abbr,type) select * from bound;

ANALYZE;
--COMMIT;
VACUUM;
