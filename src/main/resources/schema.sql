/*
DROP DATABASE "task_cache";
CREATE DATABASE "task_cache";

DROP TABLE IF EXISTS courses;
*/

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create TABLE IF NOT EXISTS  courses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR (50) NOT NULL,
    info VARCHAR (300) NOT NULL,
    cost DECIMAL NOT NULL,
    discount DECIMAL NOT NULL,
    start DATE NOT NULL,
    duration BIGINT NOT NULL,
	deleted BOOLEAN DEFAULT FALSE
);