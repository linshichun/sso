CREATE TABLE role_permissions (
  role_id       INT(10) UNSIGNED NOT NULL,
  permission_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (role_id, permission_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;