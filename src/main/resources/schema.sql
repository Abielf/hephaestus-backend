

CREATE TABLE post(
                     id BIGSERIAL NOT NULL PRIMARY KEY,
                     title VARCHAR(100) NOT NULL,
                     body VARCHAR(6000) NOT NULL,
                     author VARCHAR(30) NOT NULL,
                     published_on DATE NOT NULL);


CREATE TABLE tag(
                    id BIGSERIAL NOT NULL PRIMARY KEY,
                    label VARCHAR(50) NOT NULL,
                    post_id BIGINT NOT NULL);

CREATE TABLE comment(
                        id BIGSERIAL NOT NULL PRIMARY KEY,
                        body VARCHAR(500) NOT NULL,
                        author VARCHAR(30) NOT NULL,
                        written_on DATE DEFAULT NULL,
                        post_id BIGINT DEFAULT NULL);