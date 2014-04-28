
create table weather (
stationid  varchar(20) references station,
DataDate  Date not null,
DataTime  Time not null,
TempInside Real,
TempOutside Real,
DewPoint Real,
RelHumInside  Integer,
RelHumOutside  Integer,
WindSpeed Real,
WindDir Real,
WindChill Real,
RainLastHour Real,
RainLast24Hour Real,
RelativePressure Real,
PressureTendency varchar(20)
);
