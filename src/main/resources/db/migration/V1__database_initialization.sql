CREATE TABLE users
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    phone_number VARCHAR(15),
    avatar_url VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT FALSE,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    verification_code VARCHAR(10),
    verification_expiration TIMESTAMP,
    reset_password_code VARCHAR(10),
    reset_password_expiration TIMESTAMP
);

CREATE TABLE chicken_race
(
    id SERIAL PRIMARY KEY,
    race VARCHAR(255),
    description VARCHAR(255),
    image_url VARCHAR(255)

);

CREATE TABLE post
(
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    text VARCHAR(255),
    creation_date DATE,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE chicken_race_in_post
(
    id_chicken_race INT,
    id_post INT,
    PRIMARY KEY (id_chicken_race, id_post),
    FOREIGN KEY (id_chicken_race) REFERENCES chicken_race (id) ON DELETE CASCADE,
    FOREIGN KEY (id_post) REFERENCES post (id) ON DELETE CASCADE
);



