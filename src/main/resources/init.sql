DROP ALL OBJECTS;

CREATE TABLE users
(
    id                INTEGER PRIMARY KEY AUTO_INCREMENT,
    name              VARCHAR NOT NULL,
    email             VARCHAR NOT NULL,
    password          VARCHAR NOT NULL,
    registration_date DATE    NOT NULL,
    role              VARCHAR NOT NULL,
    enabled           BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT user_unique_email_idx UNIQUE (email)
);

CREATE TABLE restaurant
(
    id      INTEGER PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR NOT NULL,
    address VARCHAR NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT restaurant_name_address_idx UNIQUE (name, address)
);

CREATE TABLE menu
(
    id            INTEGER PRIMARY KEY AUTO_INCREMENT,
    restaurant_id INTEGER NOT NULL,
    date          DATE    NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE,
    CONSTRAINT menu_unique_restaurant_date_idx UNIQUE (restaurant_id, date)
);

CREATE TABLE dish
(
    id      INTEGER PRIMARY KEY AUTO_INCREMENT,
    menu_id INTEGER       NOT NULL,
    name    VARCHAR       NOT NULL,
    PRICE   DECIMAL(8, 2) NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE
);

CREATE TABLE vote
(
    user_id INTEGER NOT NULL,
    menu_id INTEGER NOT NULL,
    date    DATE    NOT NULL,
    CONSTRAINT vote_user_date_pk PRIMARY KEY (user_id, date),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE
);