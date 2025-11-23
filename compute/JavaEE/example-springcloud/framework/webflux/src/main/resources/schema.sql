CREATE TABLE `orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `book_isbn` VARCHAR(255) NOT NULL,
  `book_name` VARCHAR(255) NULL,
  `book_price` DECIMAL(8,2) NULL,
  `quantity` INT NOT NULL,
  `status` VARCHAR(255) NOT NULL,
  `created_date` DATETIME NOT NULL,
  `last_modified_date` DATETIME NOT NULL,
  `version` INT NOT NULL,
  PRIMARY KEY (`id`));
