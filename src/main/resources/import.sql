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
VALUES (1, 'Blue Sock', 5, 5.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (2, 'Red Sock', 3, 2.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (3, 'Green Sock', 2, 9.5);
INSERT INTO items(item_id, name, quantity, unitPrice)
VALUES (4, 'Yellow Sock', 1, 0.5);