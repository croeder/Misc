#!/usr/bin/python

from pyPgSQL import PgSQL

db = PgSQL.connect(database='weather')
c = db.cursor()
c.execute("insert into station values('TEST1')");
c.execute("insert into station values('KCODENV38')");
db.close();

#stationid  varchar(20) references station,
#DataDate  Date not null,
#DataTime  Time not null,
#TempInside Real,
#TempOutside Real,
#DewPoint Real,
#RHi  Real,
#RHo  Real,
#WindSpeed Real,
#WindDir Real,
#WC Real,
#RainLastHour Real,
#RainLast24Hour Real,
#RelativePressure Real,
#PressureTendency Real
