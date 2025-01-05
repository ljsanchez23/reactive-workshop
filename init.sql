CREATE SCHEMA IF NOT EXISTS onboarding;

SET search_path TO onboarding;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255) NOT NULL
);