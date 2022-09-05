INSERT INTO USERS(NAME, EMAIL, USER_PASSWORD, REGISTRATION_DATE, USER_ROLE)
VALUES ('Mike', 'mike@mail.com', '{noop}adminpass', CURRENT_DATE(), 'ADMIN'),
       ('Peter', 'peter@mail.com', '{noop}userpass', CURRENT_DATE(), 'USER');

INSERT INTO RESTAURANT(NAME, ADDRESS)
VALUES ('Temerario', '198 8th Ave'),
       ('Perry St', '176 Perry St'),
       ('Dante West Village', '551 Hudson St');

INSERT INTO MENU(RESTAURANT_ID, FOR_DATE)
VALUES (1, CURRENT_DATE() - 1),
       (1, CURRENT_DATE()),
       (2, CURRENT_DATE() - 2),
       (2, CURRENT_DATE() - 1),
       (3, CURRENT_DATE() - 2),
       (3, CURRENT_DATE() - 1),
       (3, CURRENT_DATE());

INSERT INTO DISH(MENU_ID, NAME, PRICE)
VALUES (1, 'Huarache', 21),
       (1, 'Platanitos', 8),
       (2, 'Guacamole', 15),
       (2, 'Temerario burrito', 18),
       (2, 'Margarita tradicional', 12),
       (3, 'Rice cracker crusted tuna', 28),
       (3, 'Ginger rice bowl', 21),
       (3, 'Strawberry sundae', 15),
       (4, 'Crispy calamari', 22),
       (4, 'Black truffle burger', 30),
       (4, 'Molten chocolate cake', 15),
       (5, 'Breakfast burrata', 18),
       (5, 'Shakshouka', 21),
       (6, 'Wood fire smoked eggplant & heirloom tomato bruschetta', 21),
       (6, 'Green brunch bowl', 17),
       (7, 'Dante’s smoked salmon bagel', 19),
       (7, 'Freshly shucked oysters', 48),
       (7, 'Campari & fluffy orange', 13);