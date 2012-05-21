all:boundary.sqlite

boundary.sqlite:import.sql boundary.csv
	rm -f $@ &&\
	sqlite3 $@ <$<

boundary.csv:bound_20.dat
	awk 'BEGIN {FIELDWIDTHS = "10 12 4 3"} {print $$1"\t"$$2"\t"$$3"\t"$$4}' < $< | sed -Ee 's/^ +//' -e 's/\t +/\t/g' > $@
