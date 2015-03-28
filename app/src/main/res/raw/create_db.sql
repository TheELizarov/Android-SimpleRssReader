CREATE TABLE rss (
    _id INTEGER PRIMARY KEY,
    rss_id TEXT,
    chanel_id INTEGER,
    title TEXT,
    description TEXT,
    link TEXT,
    created INTEGER,
    pub_date INTEGER,
    viewed INTEGER
);

CREATE TABLE chanel (
    _id INTEGER PRIMARY KEY,
    name TEXT,
    type TEXT,
    link TEXT,
    checked INTEGER
);