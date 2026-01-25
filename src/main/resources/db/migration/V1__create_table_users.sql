CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(40) NOT NULL,
                       last_name VARCHAR(40) NOT NULL,
                       email VARCHAR(70) NOT NULL UNIQUE,
                       password VARCHAR(128) NOT NULL
);

CREATE TABLE users_roles (
                             user_id BIGINT NOT NULL,
                             roles VARCHAR(50) NOT NULL,
                             CONSTRAINT fk_users_roles_user
                                 FOREIGN KEY (user_id)
                                     REFERENCES users (id)
                                     ON DELETE CASCADE
);
