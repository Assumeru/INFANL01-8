CREATE TABLE producten(
	naam varchar(200) PRIMARY KEY,
	aantal int
);
CREATE TABLE veranderingen(
	product varchar(200) REFERENCES producten(naam),
	verandering int,
	PRIMARY KEY(product, tijd)
);