INSERT INTO `servies`.`service_type` (`idservice_type`, `service_type_name`) VALUES ('1', '\"EXECUTE_PROGRAM\"');
INSERT INTO `servies`.`service_type` (`idservice_type`, `service_type_name`) VALUES ('2', '\"HTML_FORM\"');

INSERT INTO `servies`.`service` (`idservice`, `service_type_id`, `service_name`) VALUES ('1', '1', '\"CISCO_VPN\"');
INSERT INTO `servies`.`service` (`idservice`, `dependency_id`, `service_type_id`, `service_name`) VALUES ('2', '1', '2', '\"IT\'S LEARNING\"');

INSERT INTO `servies`.`user_service` (`user_service_id`, `username`, `service_id`) VALUES ('1', 'skeen', '1');
INSERT INTO `servies`.`user_service` (`user_service_id`, `username`, `service_id`) VALUES ('2', 'skeen', '2');

INSERT INTO `servies`.`service_indirection` (`idservice_indirection`, `service_id`) VALUES ('1', '1');
INSERT INTO `servies`.`service_indirection` (`idservice_indirection`, `service_id`) VALUES ('2', '2');
INSERT INTO `servies`.`service_indirection` (`idservice_indirection`, `user_service_id`) VALUES ('3', '1');
INSERT INTO `servies`.`service_indirection` (`idservice_indirection`, `user_service_id`) VALUES ('4', '2');

INSERT INTO `servies`.`key_value` (`service_indirection_id`, `key`, `value`) VALUES ('1', 'PATH', 'C:\Program Files (x86)\Cisco\Cisco AnyConnect Secure Mobility Client\vpncli.exe connect user ${USERNAME} pwd ${PASSWORD}');
INSERT INTO `servies`.`key_value` (`service_indirection_id`, `key`, `value`) VALUES ('2', 'WEB_PAGE', 'www.login.itslearning.dk');
INSERT INTO `servies`.`key_value` (`service_indirection_id`, `key`, `value`) VALUES ('3', 'USERNAME', 'skeen');
INSERT INTO `servies`.`key_value` (`service_indirection_id`, `key`, `value`) VALUES ('3', 'PASSWORD', '12345');
