-- MySQL Script generated by MySQL Workbench
-- Wed Dec 13 22:27:03 2017
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema sso
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sso
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sso` DEFAULT CHARACTER SET utf8 ;
USE `sso` ;

-- -----------------------------------------------------
-- Table `sso`.`permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sso`.`permission` ;

CREATE TABLE IF NOT EXISTS `sso`.`permission` (
  `permission_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `permission` VARCHAR(45) NOT NULL COMMENT '权限的具体形式, 使用Shiro的格式, 例如: blog:add',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `last_modified` DATETIME NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`permission_id`),
  UNIQUE INDEX `permission_UNIQUE` (`permission` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '权限表';


-- -----------------------------------------------------
-- Table `sso`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sso`.`role` ;

CREATE TABLE IF NOT EXISTS `sso`.`role` (
  `role_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` VARCHAR(45) NOT NULL COMMENT '角色名称',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `last_modified` DATETIME NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`),
  UNIQUE INDEX `role_name_UNIQUE` (`role_name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '角色表';


-- -----------------------------------------------------
-- Table `sso`.`role_permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sso`.`role_permission` ;

CREATE TABLE IF NOT EXISTS `sso`.`role_permission` (
  `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
  `permission_id` BIGINT(20) NOT NULL COMMENT '权限ID',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `last_modified` DATETIME NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`, `permission_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '角色和权限的映射表';


-- -----------------------------------------------------
-- Table `sso`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sso`.`user` ;

CREATE TABLE IF NOT EXISTS `sso`.`user` (
  `user_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` VARCHAR(45) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `salt` VARCHAR(45) NOT NULL COMMENT '私有盐',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `last_modified` DATETIME NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `username_UNIQUE` (`user_name` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8
COMMENT = '用户信息表';


-- -----------------------------------------------------
-- Table `sso`.`user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sso`.`user_role` ;

CREATE TABLE IF NOT EXISTS `sso`.`user_role` (
  `user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `role_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '角色ID',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `last_modified` DATETIME NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`user_id`, `role_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '用户和角色的映射表';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
