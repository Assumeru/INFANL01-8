CREATE DATABASE `analyse 8`;
USE `analyse 8`;

CREATE TABLE IF NOT EXISTS `producten`(
	naam varchar(200) PRIMARY KEY,
	aantal int
) ENGINE=InnoDB;

CREATE TABLE `veranderingen`(
	product varchar(200) REFERENCES producten(naam),
	verandering int
) ENGINE=InnoDB;

INSERT INTO `producten` VALUES
('Mac Book', 0);