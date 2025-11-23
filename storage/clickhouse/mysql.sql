DROP DATABASE IF EXISTS mysql_test

CREATE DATABASE IF NOT EXISTS mysql_test -- [ON CLUSTER cluster]
ENGINE = MySQL('172.25.16.1:3306', 'test', 'root', 'admin')

