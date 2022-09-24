DROP TABLE IF EXISTS PRODUCT;

CREATE TABLE PRODUCT (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR NOT NULL,
    name VARCHAR NOT NULL,
    price DOUBLE NOT NULL,
    qty INT NOT NULL
);

INSERT INTO PRODUCT(sku, name, price, qty) VALUES ('120P90', 'Google Home', 49.99, 10);
INSERT INTO PRODUCT(sku, name, price, qty) VALUES ('43N23P', 'MacBook Pro', 5399.99, 5);
INSERT INTO PRODUCT(sku, name, price, qty) VALUES ('A304SD', 'Alexa Speaker', 109.50, 10);
INSERT INTO PRODUCT(sku, name, price, qty) VALUES ('234234', 'Raspberry Pi B', 30.00, 2);
