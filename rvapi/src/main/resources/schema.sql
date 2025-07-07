CREATE TABLE
    IF NOT EXISTS usermap (
        id VARCHAR(255) PRIMARY KEY,
        username VARCHAR(255) NOT NULL UNIQUE,
        fullname VARCHAR(255),
        email VARCHAR(255),
        hashed_password VARCHAR(255),
        auth_token VARCHAR(255),
        acc_active BOOLEAN,
        acc_created VARCHAR(255),
        acc_updated VARCHAR(255)
    );

---combo must be unique, each specific doesn't have to be
CREATE TABLE
    IF NOT EXISTS repotags (
        tag_id VARCHAR(255) NOT NULL,
        user_id VARCHAR(255) NOT NULL,
        tagname VARCHAR(255),
        reponame VARCHAR(255),
        latest_tag VARCHAR(25),
        tag_created VARCHAR(255),
        tag_updated VARCHAR(255),
        is_show BOOLEAN,
        PRIMARY KEY (user_id, reponame)
    );