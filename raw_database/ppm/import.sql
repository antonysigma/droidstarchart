--BEGIN;
create table ppm(
PPM integer, PM STRING, Mag float, Sp STRING,
RAh integer, RAm integer, RAs float,
DEd integer, DEm integer, DEs float,
pmRA float, pmDE float,
Npos integer,
e_RAs integer, e_DEs integer,
e_pmRA float, e_omDE float,
EpRA float, EpDE float,
SAO integer, HD integer,
AGK3 STRING,
Flag STRING,
primary key (PPM));

.separator "\t"
.import ppm1.csv ppm
.import ppm2.csv ppm

ANALYZE;
--COMMIT;
VACUUM;
