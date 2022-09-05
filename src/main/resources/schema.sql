DROP ALL OBJECTS;

CREATE TABLE users
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    name              VARCHAR(255) NOT NULL,
    email             VARCHAR(255) NOT NULL,
    user_password     VARCHAR(255) NOT NULL,
    registration_date DATE         NOT NULL,
    user_role         VARCHAR(100) NOT NULL,
    enabled           BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT user_unique_email_idx UNIQUE (email)
);

CREATE TABLE restaurant
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    CONSTRAINT restaurant_name_address_idx UNIQUE (name, address)
);

CREATE TABLE menu
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    restaurant_id BIGINT NOT NULL,
    for_date      DATE   NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE,
    CONSTRAINT menu_unique_restaurant_date_idx UNIQUE (restaurant_id, for_date)
);

CREATE TABLE dish
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    menu_id BIGINT        NOT NULL,
    name    VARCHAR(255)  NOT NULL,
    price   DECIMAL(8, 2) NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE
);

CREATE TABLE vote
(
    user_id       BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    for_date      DATE   NOT NULL,
    CONSTRAINT vote_user_date_pk PRIMARY KEY (user_id, for_date),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES menu (id) ON DELETE CASCADE
);