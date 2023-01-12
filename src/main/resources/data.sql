INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (NAME, ADRESS, ENABLED)
VALUES ('Claude Monet', '25/20 building 1 Spiridonovka street 25/20b1, Moscow', TRUE),
       ('Primavera', '17 Shabolovka Street, Moscow', TRUE),
       ('Praga', '2/1 Arbat St, Moscow', TRUE);

INSERT INTO DISH (DATE, NAME, PRICE, REST_ID, CURRENCY_CODE_ISO)
VALUES ('2022-01-10', 'Bouillabaisse', 55000, 1, 'RUB'),
       ('2022-01-10', 'Ratatouille', 35000, 1, 'RUB'),
       ('2022-01-10', 'Salade nisoise', 35000, 1, 'RUB'),
       ('2022-01-10', 'La creme brulee', 20000, 1, 'RUB'),
       ('2022-01-10', 'Coffee', 10000, 1, 'RUB'),
       ('2022-01-10', 'Pasta', 30000, 2, 'RUB'),
       ('2022-01-10', 'Pizza', 40000, 2, 'RUB'),
       ('2022-01-10', 'Panna cotta', 20000, 2, 'RUB'),
       ('2022-01-10', 'Juice', 8000, 2, 'RUB'),
       ('2022-01-10', 'Tartar', 30000, 3, 'RUB'),
       ('2022-01-10', 'Dumplings', 25000, 3, 'RUB'),
       ('2022-01-10', 'Tea', 6000, 3, 'RUB');

INSERT INTO VOTE (DATE, REST_ID, USER_ID)
VALUES ('2022-01-10', 1, 1),
       ('2022-01-10', 2, 2),
       ('2022-01-11', 1, 2);


