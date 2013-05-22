DROP DATABASE IF EXISTS `services`; 

CREATE DATABASE `services`;
USE `services`;

CREATE TABLE `service_type` (
  `idservice_type` int(11) NOT NULL,
  `service_type_name` varchar(64) NOT NULL,
  #`implementation` varchar(45) NOT NULL,
  PRIMARY KEY (`idservice_type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `service` (
  `idservice` int(11) NOT NULL AUTO_INCREMENT,
  `dependency_id` int(11) DEFAULT NULL,
  `service_type_id` int(11) NOT NULL,
  `service_logo` binary(1) DEFAULT NULL,
  `service_name` varchar(128) NOT NULL,
  PRIMARY KEY (`idservice`),
  UNIQUE KEY `idservice_UNIQUE` (`idservice`),
  KEY `dependency_must_exist_idx` (`dependency_id`),
  KEY `service_type_must_exist_idx` (`service_type_id`),
  CONSTRAINT `service_type_must_exist` FOREIGN KEY (`service_type_id`) REFERENCES `service_type` (`idservice_type`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dependency_must_exist` FOREIGN KEY (`dependency_id`) REFERENCES `service` (`idservice`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `user_service` (
  `user_service_id` int(11) NOT NULL,
  `username` varchar(32) NOT NULL,
  `service_id` int(11) NOT NULL,
  PRIMARY KEY (`user_service_id`),
  KEY `service_must_exist_idx` (`service_id`),
  KEY `username_index` (`username`),
  CONSTRAINT `service_must_exist` FOREIGN KEY (`service_id`) REFERENCES `service` (`idservice`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

# TODO: Contraint, such that one of (service_id and user_service_id) is null, and the otherone isn't.
CREATE TABLE `service_indirection` (
  `idservice_indirection` int(11) NOT NULL,
  `service_id` int(11) DEFAULT NULL,
  `user_service_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`idservice_indirection`),
  KEY `service_id_must_exist_idx` (`service_id`),
  KEY `user_service_id_must_exist_idx` (`user_service_id`),
  CONSTRAINT `user_service_id_must_exist` FOREIGN KEY (`user_service_id`) REFERENCES `user_service` (`user_service_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `service_id_must_exist` FOREIGN KEY (`service_id`) REFERENCES `service` (`idservice`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `key_value` (
  `service_indirection_id` int(11) NOT NULL,
  `key` varchar(32) NOT NULL,
  `value` varchar(256) NOT NULL,
  PRIMARY KEY (`service_indirection_id`, `key`),
  CONSTRAINT `1service_id_must_exist` FOREIGN KEY (`service_indirection_id`) REFERENCES `service_indirection` (`idservice_indirection`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

