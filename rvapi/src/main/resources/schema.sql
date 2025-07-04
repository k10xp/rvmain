CREATE TABLE
    IF NOT EXISTS usermap (
        id TEXT PRIMARY KEY,
        username VARCHAR(255) NOT NULL UNIQUE,
        full_name VARCHAR(255),
        email VARCHAR(255),
        hashed_password VARCHAR(255),
        acc_disabled BOOL
    );

CREATE TABLE
    IF NOT EXISTS repotags (
        id TEXT PRIMARY KEY,
        username VARCHAR (255) NOT NULL,
        name VARCHAR(255),
        repo_name VARCHAR(255)
    );