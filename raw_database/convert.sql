
attach database 'ppm/ppm.sqlite' as PPM;
attach database 'boundary/boundary.sqlite' as boundary;
attach database 'constellation/constellation.sqlite' as conste;
attach database 'ngc/ngc.sqlite' as NGC;
attach database 'wds/wds.sqlite' as WDS;

BEGIN;

--------------------------------------------------------------------------------
-- Import PPM database
create table ppm(_id integer primary key, ra float, de float, mag float);

insert into main.ppm select PPM,RAh+RAm/60.+RAs/3600.,DEd+DEm/60.+DEs/3600.,Mag from PPM.ppm where DEd >=0 and mag <=7;
insert into main.ppm select PPM,RAh+RAm/60.+RAs/3600.,DEd-DEm/60.-DEs/3600.,Mag from PPM.ppm where DEd <0 and mag <= 7;


--------------------------------------------------------------------------------
-- Import constellations symbols from StarryValley
create table constellation(abbr string primary key, name string);
insert into main.constellation select * from conste.constellation;

-- Import constellation line database
create table lines(_id integer primary key, abbr string, ra1 integer, de1 integer, ra2 integer, de2 integer);
insert into main.lines select * from conste.lines2;

-- Match Starryvalley's constellation line with PPM star catalog
create view closest_star as 
SELECT L._id as id, abbr, A._id as star1, B._id as star2, A.mag as mag1, B.mag as mag2
FROM lines L, ppm A, ppm B
WHERE
abs(ra1 - A.ra)*360/24. < 1 and abs(de1 - A.de) < 1 and abs(ra2 - B.ra)*360/24. < 1 and abs(de2 - B.de) < 1
and A._id < B._id;

create table constellation_line(abbr string, star1 integer, star2 integer, primary key(abbr,star1,star2),
foreign key(star1) references ppm(_id), foreign key(star2) references ppm(_id));

insert into constellation_line select distinct abbr, star1, star2 
from (select id,min(mag1) as m1,min(mag2) as m2 from closest_star group by id) as X, closest_star C
where X.id=C.id and X.m1 = C.mag1 and X.m2 = C.mag2;

drop view closest_star;
drop table lines;

--------------------------------------------------------------------------------
-- Import Boundary database
create table constellation_boundary (_id integer primary key,
ra float, de float, abbr text, foreign key (abbr) references constellation(abbr));

insert into main.constellation_boundary select _id,ra,de,C.abbr 
from boundary.constellation_boundary B, constellation C
where lower(C.abbr)=lower(B.abbr);

--------------------------------------------------------------------------------
-- Import NGC catalog
CREATE TABLE ngc_type(_id INTEGER primary key, type STRING);
insert into ngc_type select * from NGC.ngc_type;

CREATE TABLE ngc(
NGC INTEGER,
n_NGC STRING,
Type INTEGER,
ra FLOAT, de FLOAT,
mag FLOAT,
primary key (NGC,n_NGC));
insert into main.ngc select NGC,n_NGC,Type,RAh+RAm/60.,DEd+DEm/60.,Mag from NGC.ngc where DEd >=0;
insert into main.ngc select NGC,n_NGC,Type,RAh-RAm/60.,DEd-DEm/60.,Mag from NGC.ngc where DEd <0;

--------------------------------------------------------------------------------
-- Import WDS catalog
CREATE TABLE wds(
WDS INTEGER, 
n_WDS STRING,
ra FLOAT,
de FLOAT,
mag1 FLOAT,
mag2 FLOAT,
sep FLOAT,
primary key (WDS,n_WDS));
insert into main.wds select WDS,Comp,RAh+RAm/60.+RAs/3600.,DEd+DEm/60.+DEs/3600.,mag1,mag2,sep2 from WDS.wds where DEd >=0 and max(mag1,mag2)<=7;
insert into main.wds select WDS,Comp,RAh+RAm/60.+RAs/3600.,DEd+DEm/60.+DEs/3600.,mag1,mag2,sep2 from WDS.wds where DEd <0 and max(mag1,mag2)<=7;

ANALYZE;
COMMIT;
VACUUM;
