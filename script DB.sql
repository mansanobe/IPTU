drop table if exists imovel;

create table imovel (
	id integer primary key autoincrement,
	inscricao integer not null unique,
	dt_liberacao text not null,
	valor decimal(10,2) not null,
	area integer not null,
	categoria char not null
);

insert into imovel values(1, 12345678, "20200507", 1500000, 500, "A");
insert into imovel values(2, 23456789, "20151221", 1000000, 200, "B");
insert into imovel values(3, 34567890, "20030210",  800000, 150, "C");
insert into imovel values(4, 45678901, "19750915",  500000,  80, "D");
insert into imovel values(5, 56789012, "19670427",  230000,  40, "E");
insert into imovel values(6, 67890123, "19950617",  120000,  60, "Z");