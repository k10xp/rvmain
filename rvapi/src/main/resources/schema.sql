CREATE TABLE
    IF NOT EXISTS usermap (
        id TEXT PRIMARY KEY,
        username VARCHAR(255) NOT NULL UNIQUE,
        fullname VARCHAR(255),
        email VARCHAR(255),
        hashed_password VARCHAR(255),
        auth_token TEXT,
        acc_active BOOLEAN
    );

CREATE TABLE
    IF NOT EXISTS repotags (
        id TEXT PRIMARY KEY,
        username VARCHAR (255) NOT NULL,
        name VARCHAR(255),
        repo_name VARCHAR(255)
    );