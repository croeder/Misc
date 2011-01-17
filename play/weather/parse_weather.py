#!/usr/bin/python

from pyPgSQL import PgSQL



fields = {
"stationid":"stationid",
"Date"     :"DataDate",
"Time"     :"DataTime", 
"Ti"       :"TempInside",
"To"       :"TempOutside",
"DP"       :"DewPoint",
"RHi"      :"RelHumInside",
"RHo"      :"RelHumOutside", 
"WS"       :"WindSpeed",
"DIR0"     :"WindDir", 
"WC"       :"WindChill", 
"R1h"      :"RainLastHour",
"R24h"     :"RainLast24Hour", 
"RP"       :"RelativePressure",
#"Tendency" :"PressureTendency" 
}

def createInsertString(stationID):
	sqlString="insert into weather"
	columnsString="( stationid, ";
	valuesString="values ( " + stationID + ", ";
	for r in open("/home/croeder/weather"):
		k =  r.split()[0]
		if fields.has_key(k):
			columnsString = columnsString + fields[k] + ", "
			valuesString = valuesString + "'" + r.split()[1] + "', "
	sqlString = sqlString + "\n" + columnsString[:-2]  + ") \n"  + valuesString[:-2] + ") ;\n"
	return sqlString

db = PgSQL.connect(database='weather')
c = db.cursor()
s=createInsertString("'TEST1'")
c.execute(s)
s=createInsertString("'TEST2'")
c.execute(s)
s=createInsertString("'TEST3'")
c.execute(s)
s=createInsertString("'TEST4'")
c.execute(s)
s=createInsertString("'TEST5'")
c.execute(s)
s=createInsertString("'TEST6'")
c.execute(s)
s=createInsertString("'TEST7'")
c.execute(s)
s=createInsertString("'TEST8'")
c.execute(s)
s=createInsertString("'TEST9'")
c.execute(s)
s=createInsertString("'TEST10'")
c.execute(s)

#print "------------------------------"
#c.execute("select * from weather");
#resultset = c.fetchall();
#for r in resultset:
#	print r

db.commit()
db.close();
