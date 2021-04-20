 --create database wines;

drop table if exists non_fruit_wine;
drop table if exists fruit_wine;
drop table if exists wines;
drop table if exists non_fruit;
drop table if exists fruit;
drop table if exists region;
drop table if exists style;
drop table if exists acidity; 
drop table if exists body;
drop table if exists tannin;
drop table if exists varietal;
drop table if exists wine_types;

create table wine_types (
        type_id serial primary key,
        wine_type varchar(32) not null
);

create table varietal (
        varietal_id serial primary key,
        varietal varchar(32) not null
);

create table tannin (
        tannin_id serial primary key,
        tannin_level varchar(32) not null
);

create table body (
        body_id serial primary key,
        body_level varchar(32) not null
);

create table acidity (
        acidity_id serial primary key,
        acidity_level varchar(32) not null
);

create table style (
        style_id serial primary key,
        style_type varchar(32) not null
);

create table region (
        region_id serial primary key,
        region_name varchar(32) not null,
        style_id int not null,
        
        constraint fk_style_id_region foreign key (style_id) references style(style_id)
       
);

create table fruit (
        fruit_id serial primary key,
        fruit_character varchar(32) not null
);

create table non_fruit (
        non_fruit_id serial primary key,
        non_fruit_character varchar(32) not null
);

create table wines (
        wine_id serial primary key,
        name varchar(100) not null,
        vintage int,
        price numeric not null,
        type_id int not null,
        varietal_id int not null,
        tannin_id int not null,
        body_id int not null,
        acidity_id int not null,
        region_id int not null,
        
        constraint fk_type_id_wines foreign key (type_id) references wine_types(type_id),
        constraint fk_varietal_id_wines foreign key (varietal_id) references varietal(varietal_id),
        constraint fk_tannin_id_wines foreign key (tannin_id) references tannin(tannin_id),
        constraint fk_body_id_wines foreign key (body_id) references body(body_id),
        constraint fk_acidity_id_wines foreign key (acidity_id) references acidity(acidity_id),
        constraint fk_region_id_wines foreign key (region_id) references region(region_id)
      
);

create table fruit_wine (
        wine_id int,
        fruit_id int,
        
        primary key (wine_id, fruit_id),
        constraint fk_fruit_wine_wine_id foreign key (wine_id) references wines(wine_id), 
        constraint fk_fruit_wine_fruit_id foreign key (fruit_id) references fruit(fruit_id) 
);

create table non_fruit_wine (
        wine_id int,
        non_fruit_id int,
        
        primary key (wine_id, non_fruit_id),
        constraint fk_non_fruit_wine_wine_id foreign key (wine_id) references wines(wine_id), 
        constraint fk_non_fruit_wine_fruit_id foreign key (non_fruit_id) references non_fruit(non_fruit_id) 
);

SELECT * FROM wine_types;

INSERT INTO wine_types (type_id, wine_type) VALUES (default, 'Sparkling');
INSERT INTO wine_types (type_id, wine_type) VALUES (default, 'White');
INSERT INTO wine_types (type_id, wine_type) VALUES (default, 'Rose');
INSERT INTO wine_types (type_id, wine_type) VALUES (default, 'Red');


SELECT * FROM varietal;

INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'Riesling');
INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'Pinot Grigio');
INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'Sauvignon Blanc');
INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'Chardonnay');
INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'Pinot Noir');
INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'Merlot');
INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'Malbec');
INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'Cabernet Sauvignon');
INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'Syrah');
INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'Sangiovese');
INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'Grenanche');
INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'Tempranillo');
INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'White Blend');
INSERT INTO varietal (varietal_id, varietal) VALUES (default, 'Red Blend');


SELECT * FROM tannin;

INSERT INTO tannin (tannin_id, tannin_level) VALUES (default, 'Very Sweet');
INSERT INTO tannin (tannin_id, tannin_level) VALUES (default, 'Sweet');
INSERT INTO tannin (tannin_id, tannin_level) VALUES (default, 'Off Dry');
INSERT INTO tannin (tannin_id, tannin_level) VALUES (default, 'Dry');
INSERT INTO tannin (tannin_id, tannin_level) VALUES (default, 'Very Dry');



SELECT * FROM body;

INSERT INTO body (body_id, body_level) VALUES (default, 'Light Body');
INSERT INTO body (body_id, body_level) VALUES (default, 'Medium Body');
INSERT INTO body (body_id, body_level) VALUES (default, 'Full Body');


SELECT * FROM acidity;

INSERT INTO acidity (acidity_id, acidity_level) VALUES (default, 'Less Lively');
INSERT INTO acidity (acidity_id, acidity_level) VALUES (default, 'Lively');
INSERT INTO acidity (acidity_id, acidity_level) VALUES (default, 'Very Lively');


SELECT * FROM style;
INSERT INTO style (style_id, style_type) VALUES (default, 'Old World');
INSERT INTO style (style_id, style_type) VALUES (default, 'New World');


SELECT * FROM region;

INSERT INTO region (region_id, region_name, style_id) VALUES (default, 'France', 1);
INSERT INTO region (region_id, region_name, style_id) VALUES (default, 'Italy', 1);
INSERT INTO region (region_id, region_name, style_id) VALUES (default, 'Germany', 1);
INSERT INTO region (region_id, region_name, style_id) VALUES (default, 'Austria', 1);
INSERT INTO region (region_id, region_name, style_id) VALUES (default, 'Spain', 1);

INSERT INTO region (region_id, region_name, style_id) VALUES (default, 'California', 2);
INSERT INTO region (region_id, region_name, style_id) VALUES (default, 'Oregon', 2);
INSERT INTO region (region_id, region_name, style_id) VALUES (default, 'New Zealand', 2);
INSERT INTO region (region_id, region_name, style_id) VALUES (default, 'Australia', 2);
INSERT INTO region (region_id, region_name, style_id) VALUES (default, 'Argentina', 2);


SELECT * FROM fruit;

INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Tropical');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Citrus');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Grapefruit');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Apple');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Red Cherry');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Strawberry');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Black Cherry');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Plum');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Rasberry');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Blackberry');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Black Currant');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Blueberry');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Pear');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Orange');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Peach');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Red Currant');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Watermelon');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Apricot');
INSERT INTO fruit (fruit_id, fruit_character) VALUES (default, 'Melon');


SELECT * FROM non_fruit;

INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Mineral');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Petrol');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Honey');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Cut Grass');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Black Pepper');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Leather');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Jalepeno');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Vanilla');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Mushroom');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Earth');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Olive');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Coffee');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Cedar');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Tobacco');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Milk Chocolate');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Dark Chocolate');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Cured Meat');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Cinnamon');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Toasted Cracker');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Wild Flowers');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Wet Stone');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Ocean Air');
INSERT INTO non_fruit (non_fruit_id, non_fruit_character) VALUES (default, 'Herbs');



SELECT * FROM wine_types;
SELECT * FROM varietal;
SELECT * FROM tannin;
SELECT * FROM body;
SELECT * FROM acidity;
SELECT * FROM region;

SELECT * FROM wines;

--SPARKLING

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id) 
VALUES (default, 'Bisol Jeio, Prosecco', null, 36.00, 1, 13, 3, 1, 3, 1);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Juve Y Camps, Cava', 2016, 45.00, 1, 13, 3, 2, 3, 5);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Nicolas Feuillatte Brut Rose, Champagne', null, 48.00, 1, 14, 3, 1, 3, 1);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Pierre Sparr, Brut Rose', null, 52.00, 1, 5, 3, 2, 2, 1);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Pierre Gimonnet, Champagne', null, 85.00, 1, 4, 3, 3, 2, 1);

--ROSE

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Whispering Angel', 2019, 32.00, 3, 14, 3, 2, 2, 1);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'La Spinetta', 2019, 40.00, 3, 14, 4, 2, 2, 2);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Daou', 2019, 38.00, 3, 11, 3, 1, 2, 6);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Scribe', 2019, 40.00, 3, 5, 3, 1, 2, 6);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Chateau DeAquaria', 2018, 52.00, 3, 14, 3, 3, 3, 1);

--WHITE

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'J&H Selbach', 2018, 32.00, 2, 1, 2, 2, 2, 3);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Scarpetta', 2018, 42.00, 2, 2, 4, 1, 2, 2);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'A to Z', 2019, 25.00, 2, 2, 3, 3, 2, 7);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Alan Scott', 2019, 30.00, 2, 3, 4, 2, 3, 8);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Domain Ferret', 2017, 45.00, 2, 4, 4, 3, 3, 1);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Chateau Monetlena', 2016, 52.00, 2, 4, 4, 3, 2, 6);

--RED

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Lioco', 2018, 28.00, 4, 5, 4, 2, 3, 6);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Catena', 2017, 28.00, 4, 7, 5, 3, 2, 10);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Antinori', 2018, 32.00, 4, 14, 5, 3, 2, 2);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Marques', 2015, 35.00, 4, 12, 4, 2, 3, 5);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Silver Oak', 2015, 75.00, 4, 8, 5, 3, 2, 6);

INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id)
VALUES (default, 'Caymus', 2014, 105.00, 4, 9, 5, 3, 3, 6);

SELECT * FROM wine_types;
SELECT * FROM varietal;
SELECT * FROM tannin;
SELECT * FROM body;
SELECT * FROM acidity;
SELECT * FROM region;


SELECT * FROM wines;


SELECT * FROM fruit_wine;
--bisol
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (1, 4);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (1, 2);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (1, 15);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (1, 1);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (1, 20);

--juve y camps
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (2, 14);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (2, 13);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (2, 19);

--nicolas
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (3, 16);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (3, 12);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (3, 9);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (3, 20);

--pierre sparr
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (4, 6);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (4, 9);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (4, 15);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (4, 21);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (4, 22);

--pierre gimonnnet
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (5, 4);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (5, 13);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (5, 20);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (5, 19);

--whispering angel
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (6, 5);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (6, 1);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (6, 3);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (6, 21);

--la spinetta
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (7, 1);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (7, 15);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (7, 6);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (7, 20);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (7, 1);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (7, 10);

--daou
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (8, 6);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (8, 1);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (8, 20);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (8, 3);

--scribe
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (9, 6);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (9, 2);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (9, 15);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (9, 1);

--chateau deaquaria
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (10, 17);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (10, 6);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (10, 15);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (10, 20);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (10, 1);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (10, 23);

--j&h
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (11, 4);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (11, 15);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (11, 1);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (11, 3);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (11, 1);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (11, 21);

--scarpetta
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (12, 2);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (12, 18);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (12, 19);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (12, 20);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (12, 3);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (12, 1);

--scarpetta
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (13, 1);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (13, 14);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (13, 3);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (13, 9);

--alan scott
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (14, 1);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (14, 3);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (14, 4);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (14, 23);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (14, 7);

--domain ferret
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (15, 4);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (15, 2);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (15, 13);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (15, 21);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (15, 8);

--Chateau Monetlena
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (16, 2);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (16, 18);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (16, 19);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (16, 20);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (16, 18);

--Lioco
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (17, 9);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (17, 7);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (17, 5);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (17, 20);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (17, 23);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (17, 13);

--Cantena
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (18, 12);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (18, 10);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (18, 11);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (18, 20);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (18, 16);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (18, 6);

--Antinori
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (19, 9);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (19, 10);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (19, 14);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (19, 5);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (19, 6);

--Marques
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (20, 5);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (20, 7);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (20, 8);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (20, 20);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (20, 8);

--Silver Oak
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (21, 5);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (21, 7);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (21, 10);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (21, 17);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (21, 23);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (21, 9);

--caymus
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (22, 12);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (22, 10);
INSERT INTO fruit_wine (wine_id, fruit_id) VALUES (22, 8);

INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (22, 20);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (22, 23);
INSERT INTO non_fruit_wine (wine_id, non_fruit_id) VALUES (22, 5);

SELECT * FROM wines;
SELECT * FROM fruit;
SELECT * FROM non_fruit;

SELECT * FROM non_fruit_wine;
SELECT * FROM fruit_wine;

--user accounts
drop table if exists accounts;
drop table if exists users;

create table users (
        user_id serial primary key,
        user_name varchar(50) not null,
        password_hash varchar(200) not null
);

create table accounts (
        account_id serial primary key,
        user_id int not null,
        balance numeric(13 ,2),
        
        constraint fk_user_id_accounts foreign key (user_id) references users(user_id) 
);


drop table if exists user_history;
drop table if exists recommendations;
drop table if exists friends_list;

create table user_history (
        order_number serial primary key,
        order_date date not null,
        wine_id int not null,
        account_id_from int not null,
        account_id_to int not null,
        favorite boolean not null,
        gift boolean not null,
        message varchar(250),

        constraint fk_account_id_to_user_history foreign key (account_id_to) references accounts(account_id),
        constraint fk_account_id_from_user_history foreign key (account_id_from) references accounts(account_id),
        constraint fk_wine_id_user_history foreign key (wine_id) references wines(wine_id)

);

create table recommendations (
        recommendation_number serial primary key,
        recommendation_date date not null,
        wine_id int not null,
        account_id_to int not null,
        account_id_from int not null,
        message varchar(250),

        constraint fk_account_id_to_user_history foreign key (account_id_to) references accounts(account_id),
        constraint fk_account_id_from_user_history foreign key (account_id_from) references accounts(account_id),
        constraint fk_wine_id_user_history foreign key (wine_id) references wines(wine_id)
);

create table friends_list(
        account_id int not null,
        friend_account_id int not null,
        
        primary key (account_id, friend_account_id),
        constraint fk_friends_list_account_id foreign key (account_id) references accounts(account_id), 
        constraint fk_friends_list_friend_account_id foreign key (friend_account_id) references accounts(account_id) 
);


