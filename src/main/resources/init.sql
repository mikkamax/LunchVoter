DROP ALL OBJECTS;

CREATE TABLE users
(
    id                INTEGER PRIMARY KEY AUTO_INCREMENT,
    name              VARCHAR NOT NULL,
    email             VARCHAR NOT NULL,
    password          VARCHAR NOT NULL,
    registration_date DATE    NOT NULL,
    is_active         BOOLEAN DEFAULT TRUE,
    CONSTRAINT user_unique_email_idx UNIQUE (email)
);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurant
(
    id        INTEGER PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR NOT NULL,
    address   VARCHAR,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT restaurant_name_address_idx UNIQUE (name, address)
);

CREATE TABLE menu
(
    id            INTEGER PRIMARY KEY AUTO_INCREMENT,
    restaurant_id INTEGER NOT NULL,
    date          DATE    NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id),
    CONSTRAINT menu_unique_restaurant_date_idx UNIQUE (restaurant_id, date)
);

CREATE TABLE dish
(
    menu_id INTEGER NOT NULL,
    name    VARCHAR NOT NULL,
    PRICE   DECIMAL(8, 2),
    CONSTRAINT dish_menu_name_pk PRIMARY KEY (menu_id, name),
    FOREIGN KEY (menu_id) REFERENCES menu (id)
);

CREATE TABLE vote
(
    user_id INTEGER NOT NULL,
    menu_id INTEGER NOT NULL,
    date    DATE    NOT NULL,
    CONSTRAINT vote_user_date_pk PRIMARY KEY (user_id, date),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (menu_id) REFERENCES menu (id)
);