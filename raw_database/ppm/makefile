all:ppm.sqlite

ppm.sqlite:import.sql ppm1.csv ppm2.csv
	rm -f $@ &&\
	sqlite3 $@ < $<

ppm%.csv:ppm%.dat.gz
	zcat $< | awk 'BEGIN {FIELDWIDTHS = "7 11 5 3 3 3 7 5 3 6 9 7 3 3 3 5 5 6 6 7 7 11 6"} {print $$1"\t"$$2"\t"$$3"\t"$$4"\t"$$5"\t"$$6"\t"$$7"\t"$$8"\t"$$9"\t"$$10"\t"$$11"\t"$$12"\t"$$13"\t"$$14"\t"$$15"\t"$$16"\t"$$17"\t"$$18"\t"$$19"\t"$$21"\t"$$22"\t"$$23"\t"$$24}' | sed -Ee 's/^ +//' -e 's/\t +/\t/g' >$@

