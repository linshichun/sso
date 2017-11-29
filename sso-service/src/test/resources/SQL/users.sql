CREATE TABLE users (
  u_id        INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  username    VARCHAR(255) NOT NULL UNIQUE,
  password    VARCHAR(255) NOT NULL,
  salt        VARCHAR(255) NOT NULL,
  create_time DATE         NOT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;