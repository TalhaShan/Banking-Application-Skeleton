CREATE DATABASE `banking`;


CREATE TABLE `account` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `account_number` varchar(255) NOT NULL,
                           `balance` double NOT NULL,
                           `creation_date` datetime(6) DEFAULT NULL,
                           `is_active` bit(1) DEFAULT NULL,
                           `name` varchar(255) NOT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK_66gkcp94endmotfwb8r4ocxm9` (`account_number`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `databasechangelog` (
                                     `ID` varchar(255) NOT NULL,
                                     `AUTHOR` varchar(255) NOT NULL,
                                     `FILENAME` varchar(255) NOT NULL,
                                     `DATEEXECUTED` datetime NOT NULL,
                                     `ORDEREXECUTED` int NOT NULL,
                                     `EXECTYPE` varchar(10) NOT NULL,
                                     `MD5SUM` varchar(35) DEFAULT NULL,
                                     `DESCRIPTION` varchar(255) DEFAULT NULL,
                                     `COMMENTS` varchar(255) DEFAULT NULL,
                                     `TAG` varchar(255) DEFAULT NULL,
                                     `LIQUIBASE` varchar(20) DEFAULT NULL,
                                     `CONTEXTS` varchar(255) DEFAULT NULL,
                                     `LABELS` varchar(255) DEFAULT NULL,
                                     `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `databasechangeloglock` (
                                         `ID` int NOT NULL,
                                         `LOCKED` bit(1) NOT NULL,
                                         `LOCKGRANTED` datetime DEFAULT NULL,
                                         `LOCKEDBY` varchar(255) DEFAULT NULL,
                                         PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `transaction` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `completion_date` datetime(6) DEFAULT NULL,
                               `amount` double DEFAULT NULL,
                               `currency` varchar(255) NOT NULL,
                               `initiation_date` datetime(6) DEFAULT NULL,
                               `transaction_type` varchar(255) DEFAULT NULL,
                               `source_account_id` bigint DEFAULT NULL,
                               `target_account_id` bigint DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               KEY `FK25e716ukpqahttjt6c487lrer` (`source_account_id`),
                               KEY `FKmj1os7uxmi54buhn6tvro5i6j` (`target_account_id`),
                               CONSTRAINT `FK25e716ukpqahttjt6c487lrer` FOREIGN KEY (`source_account_id`) REFERENCES `account` (`id`),
                               CONSTRAINT `FKmj1os7uxmi54buhn6tvro5i6j` FOREIGN KEY (`target_account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

