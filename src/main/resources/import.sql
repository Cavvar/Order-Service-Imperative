-- Addresses
INSERT INTO addresses(address_id, number, street, city, postcode, country)
VALUES (1, '123', 'ExampleStreet', 'Berlin', '12345', 'Germany');
INSERT INTO addresses(address_id, number, street, city, postcode, country)
VALUES (2, '1', 'Wall Street', 'New York', '67234', 'United States of America');
INSERT INTO addresses(address_id, number, street, city, postcode, country)
VALUES (3, '244', 'Leopoldstraße', 'Munich', '80678', 'Germany');
INSERT INTO addresses(address_id, number, street, city, postcode, country)
VALUES (4, '67', 'Via Roma', 'Rome', '33448', 'Italy');
-- Cards
INSERT INTO cards(card_id, longNum, expires, ccv)
VALUES (1, '1234-5678-9012-3456', '01/99', '123');
INSERT INTO cards(card_id, longNum, expires, ccv)
VALUES (2, '5678-1234-3456-9012', '05/34', '456');
INSERT INTO cards(card_id, longNum, expires, ccv)
VALUES (3, '9012-3456-5678-1234', '12/55', '789');
INSERT INTO cards(card_id, longNum, expires, ccv)
VALUES (4, '3456-9012-1234-5678', '07/17', '187');
-- Customer
INSERT INTO customers(customer_id, firstName, lastName, userName)
VALUES (1, 'Max', 'Mustermann', 'Mustername');
INSERT INTO customers(customer_id, firstName, lastName, userName)
VALUES (2, 'Alberta', 'FamilyName', 'RandomOnlineUsername');
INSERT INTO customers(customer_id, firstName, lastName, userName)
VALUES (3, 'Evelyn', 'Schmidt', 'EvelynSchmidt');
INSERT INTO customers(customer_id, firstName, lastName, userName)
VALUES (4, 'Alex', 'Schneider', 'AlexSchneider');
-- Items
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (1, 'Blue Sock', 4, 1.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (2, 'Red Sock', 2, 2.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (3, 'Green Sock', 2, 9.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (4, 'Yellow Sock', 2, 5.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (5, 'Ash Grey Sock', 8, 0.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (6, 'Black Sock', 10, 2.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (7, 'Orange Sock', 4, 1.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (8, 'Purple Sock', 2, 0.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (9, 'Lime Green Sock', 4, 5.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (10, 'Coral Pink Sock', 6, 2.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (11, 'Emerald Green Sock', 8, 9.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (12, 'Ice Blue Sock', 4, 0.5);