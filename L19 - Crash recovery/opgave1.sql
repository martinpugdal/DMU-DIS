use databaseA;

drop table if exists Kunde;
create table Kunde (
	kundeid int primary key,
	navn varchar(25),
	postnr char(4)
)
drop table if exists Bestilling;
create table Bestilling (
	kundeid int primary key,
	varenavn varchar(25),
	antalVarer int
)

insert into Kunde values 
	(0, 'Per', '1111'),
	(1, 'Torben', '1234'),
	(2, 'Martin', '2344'),
	(3, 'Thomas', '4444');

insert into Bestilling values
	(0, 'Pepsi', 2), -- db a
	(1, 'Øl', 33),
	(12, 'Papir', 45), -- db b
	(13, 'Chips', 2);


use databaseB;

drop table if exists Kunde;
create table Kunde (
	kundeid int primary key,
	navn varchar(25),
	postnr char(4)
)
drop table if exists Postering;
create table Postering (
	kundeid int primary key,
	beloeb int
)

insert into Kunde values 
	(12, 'Kasper', '1211'),
	(13, 'Magnus', '1134'),
	(14, 'Rasmus', '3412'),
	(15, 'Lone', '3444');

insert into Postering values
	(0, 500), -- db a
	(1, 2000),
	(12, 600), -- db b
	(13, 25);