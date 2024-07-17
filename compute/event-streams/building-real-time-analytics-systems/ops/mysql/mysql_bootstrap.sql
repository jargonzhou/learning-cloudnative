CREATE TABLE IF NOT EXISTS rta.users
(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    residence VARCHAR(500),
    lat DECIMAL(10, 8),
    lon DECIMAL(10, 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rta.products (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  description VARCHAR(500),
  category VARCHAR(100),
  price FLOAT,
  image VARCHAR(200),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- https://github.com/mneedham/real-time-analytics-book/blob/main/mysql/data/users.csv
LOAD DATA INFILE '/var/lib/mysql-files/users.csv' 
INTO TABLE rta.users 
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
IGNORE 1 LINES
(first_name,last_name,email,residence,lat,lon);

-- https://github.com/mneedham/real-time-analytics-book/blob/main/mysql/data/products.csv
LOAD DATA INFILE '/var/lib/mysql-files/products.csv'
INTO TABLE rta.products
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
IGNORE 1 LINES
(name,description,price,category,image);