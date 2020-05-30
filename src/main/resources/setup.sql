CREATE TABLE IF NOT EXISTS users(
    id VARCHAR(32) NOT NULL UNIQUE,
    email VARCHAR(256) NOT NULL UNIQUE,
    name VARCHAR(256) NOT NULL,
    passwordHash VARCHAR(128),
    role int,
    PRIMARY KEY(id));


INSERT INTO users VALUES(
    '1a6c81bc90694223a1377f1f0fc71f03',
    'admin@com',
    'admin',
    '$2a$10$EA4jB.W3yCPXb7SODL0Y1eReZl/ln.vR/mD6rhYfIGcRglXKX77La',
    1);

CREATE TABLE IF NOT EXISTS trainings(
    id VARCHAR(32) NOT NULL UNIQUE,
    status INT NOT NULL,
    trainerId VARCHAR(32) NOT NULL,
    name VARCHAR(256) NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    location VARCHAR(256) NOT NULL,
    trainees INT NOT NULL,
    maxTrainees INT NOT NULL,
    minTrainees INT NOT NULL,
    describe VARCHAR(1028) NOT NULL,
    PRIMARY KEY(id));

INSERT INTO trainings VALUES(
    'ee2b37630da64853aac18e068d7afd2f',
    0,
    '1a6c81bc90694223a1377f1f0fc71f03',
    'サンプルトレーニング',
    '2020-04-01',
    '2020-04-02',
    'osaka',
    3,
    10,
    5,
    '説明がき'
);

CREATE TABLE IF NOT EXISTS reservations(
    id VARCHAR(32) NOT NULL UNIQUE,
    trainingId VARCHAR(32) NOT NULL,
    traineeId VARCHAR(32) NOT NULL,
    PRIMARY KEY(id));

CREATE INDEX idxReservations ON reservations(trainingId,traineeId);