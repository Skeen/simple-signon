USE `services`;

-- Add the supported service types
INSERT INTO `services`.`service_type` (`idservice_type`, `service_type_name`) VALUES ('1', 'WifiServiceType');
INSERT INTO `services`.`service_type` (`idservice_type`, `service_type_name`) VALUES ('2', 'PingServiceType');
INSERT INTO `services`.`service_type` (`idservice_type`, `service_type_name`) VALUES ('3', 'CiscoVPNServiceType');
INSERT INTO `services`.`service_type` (`idservice_type`, `service_type_name`) VALUES ('4', 'WebServiceType');
INSERT INTO `services`.`service_type` (`idservice_type`, `service_type_name`) VALUES ('5', 'DefaultServiceType');

-- Create the supported services
INSERT INTO `services`.`service` (`idservice`, `service_type_id`, `service_logo`, `service_name`) VALUES ('1', '1', 'resource\\Wifi.png', 'WIFI');
INSERT INTO `services`.`service` (`idservice`, `service_type_id`, `service_logo`, `service_name`) VALUES ('2', '2', 'resource\\DNS.png', 'DNS');
INSERT INTO `services`.`service` (`idservice`, `service_type_id`, `service_logo`, `service_name`) VALUES ('3', '3', 'resource\\vpn.png', 'VPN');
INSERT INTO `services`.`service` (`idservice`, `service_type_id`, `service_logo`, `service_name`) VALUES ('4', '4', 'resource\\Elevplan.gif', 'Elevplan');
INSERT INTO `services`.`service` (`idservice`, `service_type_id`, `service_logo`, `service_name`) VALUES ('5', '5', 'resource\\akis.png', 'Akis');
INSERT INTO `services`.`service` (`idservice`, `service_type_id`, `service_logo`, `service_name`) VALUES ('6', '5', 'resource\\akis_green.png', 'Akis Green');
INSERT INTO `services`.`service` (`idservice`, `service_type_id`, `service_logo`, `service_name`) VALUES ('7', '5', 'resource\\bug.png', 'Bug');
INSERT INTO `services`.`service` (`idservice`, `service_type_id`, `service_logo`, `service_name`) VALUES ('8', '5', 'resource\\Bulb.gif', 'Bulb');
INSERT INTO `services`.`service` (`idservice`, `service_type_id`, `service_logo`, `service_name`) VALUES ('9', '5', 'resource\\Bus.png', 'Bus');
INSERT INTO `services`.`service` (`idservice`, `service_type_id`, `service_logo`, `service_name`) VALUES ('10', '5', 'resource\\Car.png', 'Car');
INSERT INTO `services`.`service` (`idservice`, `service_type_id`, `service_logo`, `service_name`) VALUES ('11', '5', 'resource\\Clock.png', 'Clock');
INSERT INTO `services`.`service` (`idservice`, `service_type_id`, `service_logo`, `service_name`) VALUES ('12', '5', 'resource\\dragon.png', 'Dragon');
INSERT INTO `services`.`service` (`idservice`, `service_type_id`, `service_logo`, `service_name`) VALUES ('13', '4', 'resource\\its_learning.png', 'It\'s Learning');

-- Create the user services for skeen
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`) VALUES ('1', 'skeen', '1');
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`) VALUES ('2', 'skeen', '2');
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`) VALUES ('3', 'skeen', '3');
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`) VALUES ('4', 'skeen', '4');
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`, `in_use`) VALUES ('5', 'skeen', '5', false);
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`, `in_use`) VALUES ('6', 'skeen', '6', false);
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`, `in_use`) VALUES ('7', 'skeen', '7', false);
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`, `in_use`) VALUES ('8', 'skeen', '8', false);
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`, `in_use`) VALUES ('9', 'skeen', '9', false);
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`, `in_use`) VALUES ('10', 'skeen', '10', false);
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`, `in_use`) VALUES ('11', 'skeen', '11', false);
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`, `in_use`) VALUES ('12', 'skeen', '12', false);
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`) VALUES ('18', 'skeen', '13');

-- Create the user services for emray
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`) VALUES ('13', 'emray', '1');
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`) VALUES ('14', 'emray', '2');
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`) VALUES ('15', 'emray', '3');
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`) VALUES ('16', 'emray', '4');
INSERT INTO `services`.`user_service` (`user_service_id`, `username`, `service_id`) VALUES ('17', 'emray', '12');

-- Create the indirection tables
-- Wifi check address
INSERT INTO `services`.`service_indirection` (`idservice_indirection`, `service_id`) VALUES ('1', '1');
INSERT INTO `services`.`key_value` (`service_indirection_id`, `key_entry`, `value_entry`) VALUES ('1', 'host', 'www.google.com');
-- DNS check address
INSERT INTO `services`.`service_indirection` (`idservice_indirection`, `service_id`) VALUES ('2', '2');
INSERT INTO `services`.`key_value` (`service_indirection_id`, `key_entry`, `value_entry`) VALUES ('2', 'host', '8.8.8.8');
-- VPN service address
INSERT INTO `services`.`service_indirection` (`idservice_indirection`, `service_id`) VALUES ('3', '3');
INSERT INTO `services`.`key_value` (`service_indirection_id`, `key_entry`, `value_entry`) VALUES ('3', 'host', 'vpn.cs.au.dk');
-- VPN service profile (for Skeen)
INSERT INTO `services`.`service_indirection` (`idservice_indirection`, `user_service_id`) VALUES ('4', '15');
INSERT INTO `services`.`key_value` (`service_indirection_id`, `key_entry`, `value_entry`) VALUES ('4', 'profile', 'AU-Gadget');
-- VPN service profile (for Emray)
INSERT INTO `services`.`service_indirection` (`idservice_indirection`, `user_service_id`) VALUES ('5', '3');
INSERT INTO `services`.`key_value` (`service_indirection_id`, `key_entry`, `value_entry`) VALUES ('5', 'profile', 'AUWLAN');
-- Login URL for elevplan
INSERT INTO `services`.`service_indirection` (`idservice_indirection`, `service_id`) VALUES ('6', '4');
INSERT INTO `services`.`key_value` (`service_indirection_id`, `key_entry`, `value_entry`) VALUES ('6', 'URL', 'https://www.elevplan.dk/SSO_AUTO_LOGIN');
INSERT INTO `services`.`key_value` (`service_indirection_id`, `key_entry`, `value_entry`) VALUES ('6', 'PROXY_FILTER_CLASS', 'ElevPlan');
-- Login Info for elevplan (for Skeen)
INSERT INTO `services`.`service_indirection` (`idservice_indirection`, `user_service_id`) VALUES ('7', '4');
INSERT INTO `services`.`key_value` (`service_indirection_id`, `key_entry`, `value_entry`) VALUES ('7', 'USERNAME', 'lanie962');
INSERT INTO `services`.`key_value` (`service_indirection_id`, `key_entry`, `value_entry`) VALUES ('7', 'PASSWORD', 'onsdag01Maj');
-- Login URL for it's learning
INSERT INTO `services`.`service_indirection` (`idservice_indirection`, `service_id`) VALUES ('8', '13');
INSERT INTO `services`.`key_value` (`service_indirection_id`, `key_entry`, `value_entry`) VALUES ('8', 'URL', 'https://aarhustech.itslearning.com/SSO_AUTO_LOGIN');
INSERT INTO `services`.`key_value` (`service_indirection_id`, `key_entry`, `value_entry`) VALUES ('8', 'PROXY_FILTER_CLASS', 'ItsLearning');
-- Login Info for it's learning (for Skeen)
INSERT INTO `services`.`service_indirection` (`idservice_indirection`, `user_service_id`) VALUES ('9', '18');
INSERT INTO `services`.`key_value` (`service_indirection_id`, `key_entry`, `value_entry`) VALUES ('9', 'USERNAME', 'llntest');
INSERT INTO `services`.`key_value` (`service_indirection_id`, `key_entry`, `value_entry`) VALUES ('9', 'PASSWORD', 'Svc1sommer');
