-- CREATE DATABASE `playground` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
-- USE `playground`;

#####################################################################################
--  sequence表
#####################################################################################
CREATE TABLE `SEQUENCE` (
  `name` VARCHAR(50) NOT NULL,
  `current_value` BIGINT NOT NULL,
  `increment` BIGINT NOT NULL DEFAULT 100,
  PRIMARY KEY (`name`));

INSERT INTO `SEQUENCE` (`name`, `current_value`, `increment`) VALUES ('GLOBAL', '100000', '100');

#####################################################################################
-- 函数  seq_currval
#####################################################################################
DROP function IF EXISTS `seq_currval`;

DELIMITER $$
CREATE FUNCTION `seq_currval` (seq_name VARCHAR(50))
RETURNS VARCHAR(64) CHARSET utf8
DETERMINISTIC
BEGIN
	DECLARE retval VARCHAR(64);
    SET retval="-999999999,null";
SELECT 
    CONCAT(CAST(current_value AS CHAR), ',', CAST(increment AS CHAR))
INTO retval FROM
    SEQUENCE
WHERE
    name = seq_name;
	RETURN retval;
END$$

DELIMITER ;

select seq_currval('GLOBAL');

-- 查看函数
help 'function';
-- Error Code: 1289. The 'SHOW PROCEDURE|FUNCTION CODE' feature is disabled; 
-- you need MySQL built with '--with-debug' to have it working
show function code seq_currval;

SHOW FUNCTION STATUS;
SELECT * FROM mysql.func;

#####################################################################################
-- 函数  seq_setval
#####################################################################################
DROP function IF EXISTS `seq_setval`;

DELIMITER $$
CREATE FUNCTION `seq_setval` (seq_name VARCHAR(50), value INTEGER)
RETURNS VARCHAR(64) CHARSET utf8
DETERMINISTIC
BEGIN
	UPDATE SEQUENCE
    SET current_value = value
    WHERE name = seq_name;
	RETURN seq_currval(seq_name);
END$$

DELIMITER ;


#####################################################################################
-- 函数  seq_nextval
#####################################################################################

DROP function IF EXISTS `seq_nextval`;

DELIMITER $$
CREATE FUNCTION `seq_nextval` (seq_name VARCHAR(50))
RETURNS VARCHAR(64) CHARSET utf8
DETERMINISTIC
BEGIN
	UPDATE SEQUENCE
    SET current_value = current_value + increment
    WHERE name = seq_name;
	RETURN seq_currval(seq_name);
END$$

DELIMITER ;

select seq_nextval('GLOBAL');
select * from SEQUENCE;

-- seq_nextvaln
DROP function IF EXISTS `seq_nextvaln`;

DELIMITER $$
CREATE FUNCTION `seq_nextvaln` (seq_name VARCHAR(50), increment INTEGER)
RETURNS VARCHAR(64) CHARSET utf8
DETERMINISTIC
BEGIN
	UPDATE SEQUENCE
    SET current_value = current_value + increment
    WHERE name = seq_name;
	RETURN seq_currval(seq_name);
END$$

DELIMITER ;

select * from SEQUENCE;
select seq_nextvaln('GLOBAL', 1);
select * from SEQUENCE;

