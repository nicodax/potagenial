# Commandes de création et de l'initialisation des tables
### Table users

```sql
CREATE TABLE users (
    user_username varchar(45) NOT NULL PRIMARY KEY,
    user_password varchar(45) NOT NULL,
    user_role integer NOT NULL DEFAULT 0,
    user_money integer NOT NULL DEFAULT 0,
    user_firstname varchar(45) NOT NULL,
    user_lastname varchar(45) NOT NULL,
    user_email varchar(45) NOT NULL UNIQUE,
    user_birthdate date NOT NULL,
    user_sexe varchar(1) NOT NULL,
    user_country varchar(45) NOT NULL,
    user_city varchar(45) NOT NULL,
    user_address varchar(45) NOT NULL,
    user_house_number integer NOT NULL,
    user_zipcode integer NOT NULL
);
```

### Table products

```sql
CREATE TABLE products (
    product_name varchar(45) NOT NULL PRIMARY KEY,
    product_price integer NOT NULL,
    product_description varchar(255) NOT NULL,
    product_stock integer NOT NULL
);
```

### Table orders

```sql
CREATE TABLE orders (
    order_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_username varchar(45) NOT NULL REFERENCES users,
    product_name varchar(45) NOT NULL REFERENCES products,
    product_quantity integer NOT NULL,
    check_product binary,
    CONSTRAINT fk_orders_users FOREIGN KEY (user_username) REFERENCES users (user_username),
    CONSTRAINT fk_orders_products FOREIGN KEY (product_name) REFERENCES products (product_name)
);
```

### Table sondes

```sql
CREATE TABLE sondes (
    sonde_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
    sonde_status varchar(45) NOT NULL,
    sonde_extra_info varchar(45) NOT NULL
);
```
### Table cameras

```sql
CREATE TABLE cameras (
    camera_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
    camera_status varchar(45) NOT NULL,
    camera_ip varchar(45) NOT NULL,
    camera_username varchar(45) NOT NULL,
    camera_password varchar(45) NOT NULL
);
```

### Table settings

```sql
CREATE TABLE settings (
    user_username varchar(45) NOT NULL REFERENCES users,
    camera_id integer NOT NULL REFERENCES cameras,
    sonde_id integer NOT NULL REFERENCES sondes,
    settings_temperature_outside decimal,
    settings_temperature_ground decimal,
    settings_humidity decimal,
    settings_last_sprinkling date,
    settings_last_sprinkling_quantity decimal,
    settings_automatic_sprinkling boolean NOT NULL DEFAULT false,
    settings_automatic_sprinkling_frequency decimal NOT NULL DEFAULT 6,
    CONSTRAINT pk_settings PRIMARY KEY (user_username, camera_id, sonde_id),
    CONSTRAINT fk_settings_users FOREIGN KEY (user_username) REFERENCES users (user_username),
    CONSTRAINT fk_settings_cameras FOREIGN KEY (camera_id) REFERENCES cameras (camera_id),
    CONSTRAINT fk_settings_sondes FOREIGN KEY (sonde_id) REFERENCES sondes (sonde_id)
);
```
### Table emails

```sql
CREATE TABLE emails (
    email_user varchar(45) NOT NULL,
    email_client varchar(45) NOT NULL,
    password_client varchar(45) NOT NULL,
    CONSTRAINT pk_emails PRIMARY KEY (email_user, email_client, password_client)
);
```

### Table tokens

```sql
CREATE TABLE tokens (
    token varchar(256) NOT NULL PRIMARY KEY
);
```

# Insertion des données relatives à un utilisateur placeholder et de son matériel
```sql
INSERT INTO emails (email_user, email_client, password_client) VALUES ("potagenial@gmail.com", "potagenial@gmail.com", "pot4geni4l**");
INSERT INTO products (product_name, product_price, product_description, product_stock) VALUES ('graines de tournesol', 1, 'graines pour planter des tournesols', 25);
INSERT INTO cameras (camera_status, camera_ip, camera_username, camera_password) 
VALUES ('RUNNING', '109.131.244.183:554', 'root', 'ipcam');
INSERT INTO sondes (sonde_status, sonde_extra_info) VALUES ('RUNNING', 'IPv4=X.Y.X.B');
```

```sql
INSERT INTO orders (user_username, product_name, product_quantity) VALUES ('potagenial', 'graines de tournesol', 5);
INSERT INTO settings (user_username, camera_id, sonde_id, settings_temperature_outside, settings_temperature_ground, settings_humidity, settings_last_sprinkling, settings_last_sprinkling_quantity, settings_automatic_sprinkling, settings_automatic_sprinkling_frequency) VALUES ('potagenial', 1, 1, 15, 25, 55, (SELECT STR_TO_DATE('10-12-2021', '%d-%m-%Y')), 15, 1, 6);
```
