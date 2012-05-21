--BEGIN;
create table `wds`(
`WDS` char(10),
`Disc` char(7), `Comp` char(5),
`Obs1` int(4), `Obs2` int(4),
`Nobs` int(4),
`pa1` int(3), `pa2` int(3),
`sep1` float, `sep2` float,
`mag1` float, `mag2` float,
`SpType` char(10),
`pmRA1` int(4), `pmDE1` int(4), `pmRA2` int(4), `pmDE2` int(4),
`DM` char(9),
`Notes` char(4),
`n_RAh` char(1),
`RAh` int(2), `RAm` int(2), `RAs` float,
`DEd` int(2), `DEm` int(2), `DEs` float,
primary key (WDS,Disc,Comp,Obs2));

.separator "\t"
.import wds.csv wds

ANALYZE;
--COMMIT;
VACUUM;
