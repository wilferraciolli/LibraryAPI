
--PostGre
CREATE TABLE lib_category (
	id      bigserial        NOT NULL PRIMARY KEY,
	name    varchar(25)      NOT NULL UNIQUE
);

--MySQL
CREATE TABLE lib_category (
	id      bigint           NOT NULL PRIMARY KEY AUTO_INCREMENT DEFAULT NULL,
	name    varchar(25)      NOT NULL UNIQUE
);