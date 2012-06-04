BEGIN;

-- Import raw data
create table lines(_id integer primary key, abbr string, ra1 integer, de1 integer, ra2 integer, de2 integer);
.read lines.sql

-- Import constellations symbols from StarryValley
create table constellation(abbr string primary key, name string);
.read constellations.sql

-- Convert integer coordinate to float
create view lines2 AS select _id, C.abbr AS abbr, ra1*24/360./65535. AS ra1, de1/65535. AS de1,ra2*24/360./65535. AS ra2, de2/65535. AS de2 from
lines L, constellation C where L.abbr=upper(C.abbr);

analyze;
COMMIT;
vacuum;
