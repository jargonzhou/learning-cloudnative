CREATE TABLE `spend_report` (
  `account_id` BIGINT NOT NULL,
  `log_ts` TIMESTAMP NOT NULL,
  `amount` BIGINT NULL,
  PRIMARY KEY (`account_id`, `log_ts`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;
