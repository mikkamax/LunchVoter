DROP ALL OBJECTS;

CREATE TABLE users
(
    id                INTEGER PRIMARY KEY AUTO_INCREMENT,
    name              VARCHAR NOT NULL,
    email             VARCHAR NOT NULL,
    user_password     VARCHAR NOT NULL,
    registration_date DATE    NOT NULL,
    user_role         VARCHAR NOT NULL,
    enabled           BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT user_unique_email_idx UNIQUE (email)
);

CREATE TABLE restaurant
(
    id      INTEGER PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR NOT NULL,
    address VARCHAR NOT NULL,
    CONSTRAINT restaurant_name_address_idx UNIQUE (name, address)
);

CREATE TABLE menu
(
    id            INTEGER PRIMARY KEY AUTO_INCREMENT,
    restaurant_id INTEGER NOT NULL,
    for_date      DATE    NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE,
    CONSTRAINT menu_unique_restaurant_date_idx UNIQUE (restaurant_id, for_date)
);

CREATE TABLE dish
(
    id      INTEGER PRIMARY KEY AUTO_INCREMENT,
    menu_id INTEGER       NOT NULL,
    name    VARCHAR       NOT NULL,
    price   DECIMAL(8, 2) NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE
);

CREATE TABLE vote
(
    user_id       INTEGER NOT NULL,
    restaurant_id INTEGER NOT NULL,
    for_date      DATE    NOT NULL,
    CONSTRAINT vote_user_date_pk PRIMARY KEY (user_id, for_date),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES menu (id) ON DELETE CASCADE
);