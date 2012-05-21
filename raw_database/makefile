database=constellation/constellation.sqlite ppm/ppm.sqlite

all:star_lite.db

star_lite.db:convert.sql $(database)
	cp constellation/constellation.sqlite $@ &&\
	sqlite3 $@ < convert.sql
