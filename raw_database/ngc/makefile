all:ngc.sqlite

ngc.sqlite:import.sql ngc.csv
	rm -f $@ &&\
	sqlite3 $@ < $<

ngc.csv:catalog.dat
	awk 'BEGIN {FIELDWIDTHS = "5 1 3 3 5 4 3 7 7 5 4  5 2 41 57 41"} {print $$1"\t"$$2"\t"$$3"\t"$$4"\t"$$5"\t"$$6"\t"$$7"\t"$$8"\t"$$9"\t"$$10"\t"$$11"\t"$$12"\t"$$13"\t"$$14"\t"$$15"\t"$$16}' < $< | sed -Ee 's/^ +//' -e 's/\t +/\t/g' > $@

