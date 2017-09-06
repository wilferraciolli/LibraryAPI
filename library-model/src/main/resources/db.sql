/*
--PostGre
CREATE TABLE lib_category (
	id      bigserial        NOT NULL PRIMARY KEY,
	name    varchar(25)      NOT NULL UNIQUE
);*/

--MySQL
CREATE TABLE lib_category (
	id      bigint           NOT NULL PRIMARY KEY AUTO_INCREMENT DEFAULT NULL,
	name    varchar(25)      NOT NULL UNIQUE
);

CREATE TABLE lib_author (
	id       bigint         NOT NULL PRIMARY KEY AUTO_INCREMENT DEFAULT NULL,
	name     varchar(40)     NOT NULL
);
--Create an index on the author table for the name column because it is used for search
create index idx_author_name on lib_author(name);