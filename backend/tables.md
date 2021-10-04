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
    user_email varchar(45) NOT NULL,
    user_birthdate date NOT NULL,
    user_sexe varchar(1) NOT NULL,
    user_country varchar(45) NOT NULL,
    user_city varchar(45) NOT NULL,
    user_address varchar(45) NOT NULL,
    user_house_number integer NOT NULL,
    user_zipcode integer NOT NULL
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

### Table products

```sql
CREATE TABLE products (
    product_name varchar(45) NOT NULL PRIMARY KEY,
    product_price integer NOT NULL,
    product_description varchar(255) NOT NULL,
    product_stock integer NOT NULL
);
```

### Table settings

```sql
CREATE TABLE settings (
    user_username varchar(45) NOT NULL REFERENCES users,
    camera_id integer NOT NULL REFERENCES cameras,
    settings_temperature_outside decimal,
    settings_temperature_ground decimal,
    settings_humidity decimal,
    CONSTRAINT pk_settings PRIMARY KEY (user_username, camera_id),
    CONSTRAINT fk_settings_users FOREIGN KEY (user_username) REFERENCES users (user_username),
    CONSTRAINT fk_settings_cameras FOREIGN KEY (camera_id) REFERENCES cameras (camera_id)
);
```

### Table cameras

```sql
CREATE TABLE cameras (
    camera_id integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
    camera_status varchar(45) NOT NULL,
    camera_extra_info varchar(45) NOT NULL
);
```

# Insertion d'un utilisateur placeholder et de son matériel
```sql
INSERT INTO users (user_username, user_password, user_money, user_firstname, user_lastname, user_email, user_birthdate, user_sexe, user_country, user_city, user_address, user_house_number, user_zipcode) VALUES ('potagenial', 'p0t4g3ni4l', 250, 'potagenial', 'pwdgenial', 'potagenial@students.ephec.be', '2021-10-02', 'X', 'Belgique', 'Louvain-la-Neuve', 'Avenue du Ciseau', 15, 1348);
INSERT INTO products (product_name, product_price, product_description, product_stock) VALUES ('graines de tournesol', 1, 'graines pour planter des tournesols', 25);
INSERT INTO cameras (camera_status, camera_extra_info) VALUES ('RUNNING', 'IPv4=X.Y.X.Z');
```

```sql
INSERT INTO orders (user_username, product_name, product_quantity) VALUES ('potagenial', 'graines de tournesol', 5);
INSERT INTO settings (user_username, camera_id) VALUES ('potagenial', 1);
```