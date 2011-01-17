#!/usr/bin/python
import re
import urllib
from pyPgSQL import PgSQL


stations = ['KCODENVE38', 'KCOGOLDE25', 'KCOLAVET1', 'KCOWESTC1', 'KCOCREST2', 'MD0665']

fields = {
"station_id"				:"stationid",
"observation_time_rfc822"   :"DataDate",
"observation_time_rfc822"   :"DataTime", 
""       					:"TempInside",
"temp_f"       				:"TempOutside",
"dewpoint_f"       			:"DewPoint",
""      					:"RelHumInside",
"relative_humidity"      	:"RelHumOutside", 
"wind_mph"       			:"WindSpeed",
"wind_degrees"     			:"WindDir", 
"windchill_f"       		:"WindChill", 
"precip_1hr_in"      		:"RainLastHour",
"precip_today_in"     		:"RainLast24Hour", 
"pressure_string"       	:"RelativePressure",
"" 							:"PressureTendency" 
}

def parseLine(line):
	for k in fields.keys():
		if k != "":
			ptn = "<" + k + ">(.+)<"
			m = re.search(ptn, line)
			if m:
				return [k, m.group(1)];

def splitObservationTime(obsTime):
	# Sat 14 February, 2008 18:26:15 GMT
	ptn = "(\w+, \d+ \w+ \d+) (\d\d:\d\d:\d+ \w+)"
	m = re.search(ptn , obsTime)
	if m:
		return [m.group(1), m.group(2)]

def splitPressureString(s):
	ptn = "(.+)\""
	m = re.search(ptn , s)
	if m:
		return m.group(1)

def createInsertString(stationID):
	sqlString="insert into weather"
	columnsString="(  ";
	valuesString="values ( ";

	URL="http://api.wunderground.com/weatherstation/WXCurrentObXML.asp"
	URL_w_params=URL+"?ID="+stationID
	for line in urllib.urlopen(URL_w_params):
		pair  = parseLine(line)
		if pair:
			if pair[0] == "observation_time_rfc822":
				sot =  splitObservationTime(pair[1])
				if sot:
					columnsString = columnsString + "DataDate, " 
					valuesString = valuesString + "'" + sot[0] + "', "
					columnsString = columnsString + "DataTime," 
					valuesString = valuesString + "'" + sot[1] + "', "

			elif pair[0] == "pressure_string":
				p =  splitPressureString(pair[1])
				if p:
					pair[1] = p
					columnsString = columnsString +  fields[pair[0]] + ", "
					valuesString = valuesString + "'" + p + "', "
			else:
				columnsString = columnsString +  fields[pair[0]] + ", "
				valuesString = valuesString + "'" + pair[1] + "', "

	sqlString = sqlString + columnsString[:-2] + ") " + valuesString[:-2] + ")"
	return sqlString

########### main ##################3

#db = PgSQL.connect(database='weather', user='postgres', password='p0stgr8s')
db = PgSQL.connect(database='weather')
c = db.cursor()

for stationID in stations:

#	try:
#		stationSQL = "insert into station values ('" + stationID + "')";
#		c.execute(stationSQL);
#	except:
#		print "puked on station insert for " + stationID + "(ignore)"
#		print stationSQL

	try:
		sqlString = createInsertString(stationID)
		c.execute(sqlString)
	except:
		print "puked on weather insert for " + stationID
	print

db.commit()
db.close();

