-- public.product definition

-- Drop table

-- DROP TABLE public.product;

CREATE TABLE public.product (
                                id int8 NOT NULL,
                                "name" varchar(255) NULL,
                                price float8 NOT NULL,
                                quantity int4 NOT NULL,
                                sku varchar(255) NULL,
                                CONSTRAINT product_pkey PRIMARY KEY (id)
);

-- public.cart definition

-- Drop table

-- DROP TABLE public.cart;

CREATE TABLE public.cart (
                             id int8 NOT NULL,
                             CONSTRAINT cart_pkey PRIMARY KEY (id)
);

-- public.cart_item definition

-- Drop table

-- DROP TABLE public.cart_item;

CREATE TABLE public.cart_item (
                                  id int8 NOT NULL,
                                  quantity int4 NOT NULL,
                                  cart_id int8 NULL,
                                  product_id int8 NULL,
                                  CONSTRAINT cart_item_pkey PRIMARY KEY (id)
);

-- public.cart_item foreign keys

ALTER TABLE public.cart_item ADD CONSTRAINT cart_id_fk FOREIGN KEY (cart_id) REFERENCES public.cart(id);
ALTER TABLE public.cart_item ADD CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES public.product(id);

-- seed data

INSERT INTO public.product (id,"name",price,quantity,sku) VALUES
                                                              (1,'Google Home',49.99,10,'120P90'),
                                                              (2,'MacBook Pro',5399.99,5,'43N23P'),
                                                              (3,'Alexa Speaker',109.5,10,'A304SD'),
                                                              (4,'Raspberry Pi B',30.0,2,'234234');
