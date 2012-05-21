attach database 'ppm/ppm.sqlite' as PPM;
BEGIN;

-- Assume constellations have already been imported.

-- Import PPM database
create table ppm(_id integer primary key, ra float, de float, mag float);

insert into main.ppm select PPM,RAh+RAm/60.+RAs/3600.,DEd+DEm/60.+DEs/3600.,Mag from PPM.ppm where DEd >=0 and mag <=7;
insert into main.ppm select PPM,RAh+RAm/60.+RAs/3600.,DEd-DEm/60.-DEs/3600.,Mag from PPM.ppm where DEd <0 and mag <= 7;

ANALYZE;
COMMIT;
VACUUM;
