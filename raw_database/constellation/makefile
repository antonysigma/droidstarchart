output=constellation.sqlite

all:$(output)

$(output):import.sql lines.sql constellations.sql stars.sql
	rm -f $@ &&\
	sqlite3 $@ < $<

lines.sql:DataConstellationLine.java
	sed -nE -e 's/^ +new Line\(([0-9]+),(-?[0-9]+),([0-9]+),(-?[0-9]+)\),  \/\/([A-Z]+)  Line:([0-9]+).+$$/INSERT INTO lines(abbr,ra1,de1,ra2,de2) values ("\5",\1,\2,\3,\4);/p' < $< > $@

stars.sql:DataStar.java
	sed -nE -e 's/^ +new Star\(([0-9]+),(-?[0-9]+),([0-9]+)\).+$$/INSERT INTO star(ra,de,mag) values (\1,\2,\3);/p' < $< > $@

constellations.sql:DataConstellation.java
	sed -nE -e 's/^ +new Constellation\(([^,]+),([^,]+),.+$$/INSERT INTO constellation values (\2,\1);/p' < $< > $@

