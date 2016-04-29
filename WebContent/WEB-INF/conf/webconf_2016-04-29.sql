# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 192.168.21.216 (MySQL 5.5.47)
# Database: webconf
# Generation Time: 2016-04-29 07:45:09 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table gameconfig
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gameconfig`;

CREATE TABLE `gameconfig` (
  `id` smallint(3) unsigned NOT NULL AUTO_INCREMENT,
  `gamename` varchar(50) NOT NULL,
  `gameflag` varchar(32) NOT NULL,
  `secretkey` varchar(32) NOT NULL,
  `alarm` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `mails` varchar(512) NOT NULL DEFAULT '',
  `open` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `gametype` (`gameflag`),
  KEY `gametype_2` (`gameflag`),
  KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `gameconfig` WRITE;
/*!40000 ALTER TABLE `gameconfig` DISABLE KEYS */;

INSERT INTO `gameconfig` (`id`, `gamename`, `gameflag`, `secretkey`, `alarm`, `mails`, `open`)
VALUES
	(1,'西游破坏神','xyphs','3sr+fvHmHbPOCiwEsZlD4g==',0,'chmzh@cndw.com',1),
	(2,'斗侠','dxqq','XHmvpMRYkMc=',0,'',0);

/*!40000 ALTER TABLE `gameconfig` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table logconfig
# ------------------------------------------------------------

DROP TABLE IF EXISTS `logconfig`;

CREATE TABLE `logconfig` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `selector` varchar(10) NOT NULL,
  `logtypename` varchar(64) NOT NULL,
  `logtypeflag` varchar(64) NOT NULL,
  `logfields` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `logtypeflag` (`logtypeflag`),
  KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `logconfig` WRITE;
/*!40000 ALTER TABLE `logconfig` DISABLE KEYS */;

INSERT INTO `logconfig` (`id`, `selector`, `logtypename`, `logtypeflag`, `logfields`)
VALUES
	(1,'HBASE','用户注册信息','userreginfo','userType:string,gender:string,age:int,gameServer:string,serverId:string,areaId:string,roleId:string,playerId:string,roleName:string,userName:string,ts:int'),
	(2,'HBASE','用户登陆信息','userlogininfo','gameServer:string,serverId:string,areaId:string,roleId:string,playerId:string,roleName:string,userName:string,ts:int'),
	(3,'HBASE','选择服务器信息','serverselinfo','gameServer:string,serverId:string,areaId:string,roleId:string,playerId:string,roleName:string,userName:string,ts:int'),
	(4,'HBASE','角色创建统计信息','rolecreateinfo','serverId:string,roleType:string,roleGender:string,roleAge:int,roleLevel:int,roleId:string,playerId:string,roleName:string,userName:string,ts:int'),
	(5,'HBASE','角色登陆统计信息','rolelogininfo','serverId:string,roleLevel:int,roleId:string,playerId:string,roleName:string,userName:string,ts:int'),
	(6,'HBASE','角色升级统计信息','rolelvupinfo','serverId:string,oldlevel:int,newlevel:int,sucess:int,params:string,roleId:string,playerId:string,roleName:string,userName:string,ts:int'),
	(7,'HBASE','角色注销统计信息','rolecancelinfo','serverId:string,roleLevel:int,roleId:string,playerId:string,roleName:string,userName:string,ts:int'),
	(8,'HBASE','充值请求统计信息','payrequestinfo','serverId:string,orderId:string,roleLevel:int,IngotAmount:int,IngotTotal:int,iapId:string,currencyAmount:double,currencyType:string,virtualCurrencyAmount:double,paymentType:string,payPlatform:string,roleId:string,playerId:string,roleName:string,userName:string,ts:int'),
	(9,'HDHB','充值成功统计信息','paysucinfo','serverId:string,orderId:string,roleLevel:int,IngotAmount:int,IngotTotal:int,iapId:string,currencyAmount:double,currencyType:string,virtualCurrencyAmount:double,paymentType:string,payPlatform:string,roleId:string,playerId:string,roleName:string,userName:string,ts:int'),
	(10,'HBASE','跟踪获赠元宝和虚拟货币信息','rewardinfo','serverId:string,IngotAmount:int,IngotTotal:int,virtualCurrencyAmount:double,reason:string,roleId:string,playerId:string,roleName:string,userName:string,ts:int'),
	(11,'HBASE','跟踪游戏消费点统计信息','consumeinfo','serverId:string,item:string,number:string,IngotTotal:string,IngotPrice:string,virtualCurrencyPrice:double,missionId:string,roleLevel:int,roleId:string,playerId:string,roleName:string,userName:string,ts:int'),
	(12,'HBASE','任务、关卡或副本信息','missioninfo','serverId:string,missionId:string,type:int,status:int,cause:string,roleId:string,playerId:string,roleName:string,userName:string,playerStatus:string,Ingot:int,virtualCurrency:double,scenceId:string,roleLevel:int,ts:int');

/*!40000 ALTER TABLE `logconfig` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table test
# ------------------------------------------------------------

DROP TABLE IF EXISTS `test`;

CREATE TABLE `test` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(12) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;

INSERT INTO `test` (`id`, `name`)
VALUES
	(1,'abc'),
	(2,'edg');

/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user
# ------------------------------------------------------------

CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uname` char(128) NOT NULL DEFAULT '',
  `pwd` char(32) NOT NULL DEFAULT '',
  `enabled` tinyint(1) NOT NULL,
  `qq` char(64) NOT NULL DEFAULT '',
  `lastTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `power` tinyint(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `iuname` (`uname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `uname`, `pwd`, `enabled`, `qq`, `lastTime`, `power`)
VALUES
	(1,'cndw','f73f9c019afc657b8b822f706a212e41',1,'','2016-03-23 18:55:04',1),
	(9,'','e10adc3949ba59abbe56e057f20f883e',1,'','2016-04-08 10:23:49',0);

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Dumping routines (PROCEDURE) for database 'webconf'
--
DELIMITER ;;

# Dump of PROCEDURE looppc
# ------------------------------------------------------------

/*!50003 DROP PROCEDURE IF EXISTS `looppc` */;;
/*!50003 SET SESSION SQL_MODE="NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`%`*/ /*!50003 PROCEDURE `looppc`()
begin
declare i int;
set i = 1;
lp1 : LOOP
set i=i+1;
insert into user_test(uname,pwd,enabled,qq) values(concat('abc',i),concat('abc',i),1,'qq');
if i>10000 then
leave lp1;
end if;
end LOOP;
end */;;

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
DELIMITER ;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
