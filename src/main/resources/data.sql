INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (NAME, ADRESS)
VALUES ('Claude Monet', '25/20 building 1 Spiridonovka street 25/20b1, Moscow'),
       ('Primavera', '17 Shabolovka Street, Moscow'),
       ('Praga', '2/1 Arbat St, Moscow');

INSERT INTO DISH (NAME, PRICE, REST_ID)
VALUES ('Bouillabaisse', 55000, 1),
       ('Ratatouille', 35000, 1),
       ('Salade nisoise', 35000, 1),
       ('La creme brulee', 20000, 1),
       ('Coffee', 10000, 1),
       ('Pasta', 30000, 2),
       ('Pizza', 40000, 2),
       ('Panna cotta', 20000, 2),
       ('Juice', 8000, 2),
       ('Tartar', 30000, 3),
       ('Dumplings', 25000, 3),
       ('Tea', 6000, 3);

