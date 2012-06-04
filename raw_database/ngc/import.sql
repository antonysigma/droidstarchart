BEGIN;

CREATE TABLE ngc_type(_id INTEGER primary key, type STRING);
insert into ngc_type VALUES (1,'Open Cluster');
insert into ngc_type VALUES (2,'Globular Cluster');
insert into ngc_type VALUES (3,'Diffuse Nebula');
insert into ngc_type VALUES (4,'Planetary Nebula');
insert into ngc_type VALUES (5,'Galaxy');
insert into ngc_type VALUES (6,'Cluster associated with nebulosity');
insert into ngc_type VALUES (7,'Non existent');
insert into ngc_type VALUES (8,'Object in Large Magellanic Cloud');
insert into ngc_type VALUES (9,'Object in Small Magellanic Cloud');
insert into ngc_type VALUES (0,'Unverified southern object');

COMMIT;

--BEGIN;

CREATE TABLE ngc(
NGC INTEGER,
n_NGC STRING,
Type INTEGER,
RAh INTEGER, RAm FLOAT, 
DEd INTEGER, DEm INTEGER,
GLON FLOAT, GLAT FLOAT, 
Xpos INTEGER, Ypos INTEGER,
Mag FLOAT, r_Mag INTEGER,
OldDesc STRING,
NewDesc STRING,
Notes STRING,
primary key (NGC,n_NGC),
FOREIGN KEY Type REFERENCES ngc_type(_id));

.separator "\t"
.import ngc.csv ngc

ANALYZE;
--COMMIT;
VACUUM;
