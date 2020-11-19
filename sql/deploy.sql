SET
  SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

SET
  time_zone = "+08:00";

create database if not exists portnet;

use portnet;

drop table if exists schedule;

CREATE TABLE `schedule` (
  `imon` int(20),
  `fullVslM` varchar(50),
  `abbrVslM` varchar(50),
  `fullInVoyN` varchar(50),
  `inVoyN` varchar(50),
  `fullOutVoyN` varchar(50),
  `outVoyN` varchar(50),
  `shiftSeqN` int(50),
  `bthgDt` datetime,
  `unbthgDt` datetime,
  `berthN` varchar(20),
  `status` varchar(20),
  `abbrTerminalM` varchar(20),
  `changeCount` int(5),
  `originalTime` datetime
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

ALTER TABLE
  `schedule`
ADD
  PRIMARY KEY (`abbrVslM`, `inVoyN`, `shiftSeqN`);

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `user` varchar(255) NOT NULL,
  `pass` varchar(255) NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

ALTER TABLE
  `account`
ADD
  PRIMARY KEY (`user`);

DROP TABLE IF EXISTS `favourite`;

CREATE TABLE `favourite` (
  `user` varchar(255) NOT NULL,
  `abbrVslM` varchar(50),
  `inVoyN` varchar(50),
  `shiftSeqN` int(50)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

ALTER TABLE
  `favourite`
ADD
  PRIMARY KEY (`user`, `abbrVslM`, `inVoyN`, `shiftSeqN`);

DROP TABLE IF EXISTS `subscription`;

CREATE TABLE `subscription` (
  `user` varchar(255) NOT NULL,
  `abbrVslM` varchar(50),
  `inVoyN` varchar(50),
  `shiftSeqN` int(50)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

ALTER TABLE
  `subscription`
ADD
  PRIMARY KEY (`user`, `abbrVslM`, `inVoyN`, `shiftSeqN`);