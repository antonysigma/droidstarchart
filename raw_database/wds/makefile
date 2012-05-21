all:wds.sqlite

wds.sqlite:import.sql wds.csv
	rm -f $@ &&\
	sqlite3 $@ < $<

wds.csv:wds.dat.gz
	zcat $< | awk 'BEGIN {FIELDWIDTHS = "10 7 5 5 5 5 4 4 6 7 5 6 11 4 4 5 4 10 4 1 2 2 5 3 2 4"} {print $$1"\t"$$2"\t"$$3"\t"$$4"\t"$$5"\t"$$6"\t"$$7"\t"$$8"\t"$$9"\t"$$10"\t"$$11"\t"$$12"\t"$$13"\t"$$14"\t"$$15"\t"$$16"\t"$$17"\t"$$18"\t"$$19"\t"$$20"\t"$$21"\t"$$22"\t"$$23"\t"$$24"\t"$$25"\t"$$26}' | sed -Ee 's/^ +//' -e 's/\t +/\t/g' > $@

