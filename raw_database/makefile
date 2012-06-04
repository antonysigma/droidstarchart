database=constellation/constellation.sqlite ppm/ppm.sqlite

all:star_lite.db

split:s00.db s01.db

s00.db s01.db:.SUCCESS
	@

.PHONY:.SUCCESS

.SUCCESS:star_lite.db
	split -b1M -d -a2 $< s &&\
	mv s00 s00.db &&\
	mv s01 s01.db &&\
	touch $@

star_lite.db:convert.sql $(database)
	rm -f $@ &&\
	sqlite3 $@ < convert.sql
