BEGIN;

-- Import raw data
create table lines(_id integer primary key, abbr string, ra1 integer, de1 integer, ra2 integer, de2 integer);
.read lines.sql

create table star(_id integer primary key, ra integer, de integer, mag integer);
.read stars.sql

-- Import major stars from StarryValley
create table major_star(_id integer primary key, ra float, de float, mag float, unique(ra,de,mag));
insert into major_star select min(_id),ra*24/360./65535.,de/65535.,mag/65535. from star group by ra,de,mag;

-- Import constellations stars from StarryValley
create table constellation(abbr string primary key, name string);
.read constellations.sql

-- Match Starryvalley's constellation line with major star catalog
create view closest_star as 
SELECT L._id as id, abbr, A._id as star1, B._id as star2, A.mag as mag1, B.mag as mag2
FROM lines L, star A, star B
WHERE
abs(ra1 - A.ra) < 32768 and abs(de1 - A.de) < 32768 and abs(ra2 - B.ra) < 32768 and abs(de2 - B.de) < 32768
and A._id <> B._id;

create table constellation_line(abbr string, star1 integer, star2 integer, primary key(abbr,star1,star2),
foreign key(star1) references major_star(_id), foreign key(star2) references major_star(_id));

insert into constellation_line select abbr, star1, star2 
from (select id,min(mag1) as m1,min(mag2) as m2 from closest_star group by id) as X, closest_star C
where X.id=C.id and X.m1 = C.mag1 and X.m2 = C.mag2;

drop view closest_star;
drop table lines;
drop table star;

analyze;
COMMIT;
vacuum;
