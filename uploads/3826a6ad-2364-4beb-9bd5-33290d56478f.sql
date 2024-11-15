-- --------------------------------------------------------
-- 호스트:                          54.180.127.132
-- 서버 버전:                        8.4.3 - MySQL Community Server - GPL
-- 서버 OS:                        Linux
-- HeidiSQL 버전:                  12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- devProject 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `devProject` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `devProject`;

-- 테이블 devProject.Banner 구조 내보내기
CREATE TABLE IF NOT EXISTS `Banner` (
  `ban_id` int NOT NULL AUTO_INCREMENT,
  `ban_edate` varchar(255) DEFAULT NULL,
  `ban_etime` varchar(255) DEFAULT NULL,
  `ban_image` varchar(255) DEFAULT NULL,
  `ban_link` varchar(255) DEFAULT NULL,
  `ban_location` varchar(255) DEFAULT NULL,
  `ban_name` varchar(255) DEFAULT NULL,
  `ban_sdate` varchar(255) DEFAULT NULL,
  `ban_size` varchar(255) DEFAULT NULL,
  `ban_stime` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ban_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.cart 구조 내보내기
CREATE TABLE IF NOT EXISTS `cart` (
  `cartId` int NOT NULL,
  `price` int NOT NULL,
  `productId` int NOT NULL,
  `rdate` datetime(6) DEFAULT NULL,
  `stock` int NOT NULL,
  `uid` int NOT NULL,
  PRIMARY KEY (`cartId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.cartItem 구조 내보내기
CREATE TABLE IF NOT EXISTS `cartItem` (
  `cartItemId` int NOT NULL,
  `price` int NOT NULL,
  `stock` int NOT NULL,
  `cartId` int DEFAULT NULL,
  `productId` int DEFAULT NULL,
  PRIMARY KEY (`cartItemId`),
  KEY `FKt60x11ksdby2xwx4cxf2uyr8e` (`cartId`),
  KEY `FK3fawoydq6eb7bieyxffekk1a8` (`productId`),
  CONSTRAINT `FK3fawoydq6eb7bieyxffekk1a8` FOREIGN KEY (`productId`) REFERENCES `product` (`productId`),
  CONSTRAINT `FKt60x11ksdby2xwx4cxf2uyr8e` FOREIGN KEY (`cartId`) REFERENCES `cart` (`cartId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.cartItem_SEQ 구조 내보내기
CREATE TABLE IF NOT EXISTS `cartItem_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.cart_SEQ 구조 내보내기
CREATE TABLE IF NOT EXISTS `cart_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.file 구조 내보내기
CREATE TABLE IF NOT EXISTS `file` (
  `fno` int NOT NULL AUTO_INCREMENT,
  `banner` int NOT NULL,
  `download` int NOT NULL,
  `oName` varchar(255) DEFAULT NULL,
  `rdate` datetime(6) DEFAULT NULL,
  `sName` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `bno` int NOT NULL,
  PRIMARY KEY (`fno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.footerinfo 구조 내보내기
CREATE TABLE IF NOT EXISTS `footerinfo` (
  `ft_id` int NOT NULL,
  `ft_addr1` varchar(255) DEFAULT NULL,
  `ft_addr2` varchar(255) DEFAULT NULL,
  `ft_bo` varchar(255) DEFAULT NULL,
  `ft_ceo` varchar(255) DEFAULT NULL,
  `ft_company` varchar(255) DEFAULT NULL,
  `ft_copyright` varchar(255) DEFAULT NULL,
  `ft_email` varchar(255) DEFAULT NULL,
  `ft_hp` varchar(255) DEFAULT NULL,
  `ft_mo` varchar(255) DEFAULT NULL,
  `ft_time` varchar(255) DEFAULT NULL,
  `ft_troublehp` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ft_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.Member 구조 내보내기
CREATE TABLE IF NOT EXISTS `Member` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addr` varchar(255) DEFAULT NULL,
  `addr2` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `grade` varchar(255) DEFAULT NULL,
  `hp` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `point` decimal(38,2) DEFAULT NULL,
  `postcode` varchar(255) DEFAULT NULL,
  `regDate` datetime(6) DEFAULT NULL,
  `userinfocol` varchar(255) DEFAULT NULL,
  `user_uid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKqd6ikoywkfpvded5ov2exyo7u` (`user_uid`),
  CONSTRAINT `FK8n9immcjubt7162c5l4gnucpp` FOREIGN KEY (`user_uid`) REFERENCES `USER` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.prodCategory 구조 내보내기
CREATE TABLE IF NOT EXISTS `prodCategory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `level` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `subcategory` varchar(255) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.product 구조 내보내기
CREATE TABLE IF NOT EXISTS `product` (
  `productId` int NOT NULL,
  `ProductDesc` varchar(255) DEFAULT NULL,
  `categoryId` int NOT NULL,
  `discount` int NOT NULL,
  `isCoupon` bit(1) DEFAULT NULL,
  `isSaled` bit(1) DEFAULT NULL,
  `point` int NOT NULL,
  `price` int NOT NULL,
  `productName` varchar(255) DEFAULT NULL,
  `rdate` varchar(255) DEFAULT NULL,
  `sellerId` varchar(255) DEFAULT NULL,
  `shippingFee` int NOT NULL,
  `shippingTerms` int NOT NULL,
  `stock` int NOT NULL,
  PRIMARY KEY (`productId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.ProductCategory 구조 내보내기
CREATE TABLE IF NOT EXISTS `ProductCategory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `level` int DEFAULT NULL,
  `subcategory` varchar(255) DEFAULT NULL,
  `disp_yn` varchar(255) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKikp6ecs7hmfxcs5ix4u5gfkhb` (`parent_id`),
  CONSTRAINT `FKikp6ecs7hmfxcs5ix4u5gfkhb` FOREIGN KEY (`parent_id`) REFERENCES `ProductCategory` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.QnA 구조 내보내기
CREATE TABLE IF NOT EXISTS `QnA` (
  `qna_id` int NOT NULL AUTO_INCREMENT,
  `iscompleted` varchar(255) DEFAULT NULL,
  `productid` int NOT NULL,
  `qna_title` varchar(255) DEFAULT NULL,
  `qna_type1` varchar(255) DEFAULT NULL,
  `qna_type2` varchar(255) DEFAULT NULL,
  `qna_writer` varchar(255) DEFAULT NULL,
  `rdate` datetime(6) DEFAULT NULL,
  `sellerid` int NOT NULL,
  PRIMARY KEY (`qna_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.Seller 구조 내보내기
CREATE TABLE IF NOT EXISTS `Seller` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addr` varchar(255) DEFAULT NULL,
  `addr2` varchar(255) DEFAULT NULL,
  `bno` varchar(255) DEFAULT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `ceo` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `fax` varchar(255) DEFAULT NULL,
  `grade` varchar(255) DEFAULT NULL,
  `hp` varchar(255) DEFAULT NULL,
  `mo` varchar(255) DEFAULT NULL,
  `postcode` varchar(255) DEFAULT NULL,
  `regDate` datetime(6) DEFAULT NULL,
  `user_uid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKgtjde76b9byt3mb001jn0bwa8` (`user_uid`),
  CONSTRAINT `FKmt2st8ggh6gm8pf0mfc5w3nd8` FOREIGN KEY (`user_uid`) REFERENCES `USER` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.USER 구조 내보내기
CREATE TABLE IF NOT EXISTS `USER` (
  `uid` varchar(255) NOT NULL,
  `pass` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 devProject.version 구조 내보내기
CREATE TABLE IF NOT EXISTS `version` (
  `verId` int NOT NULL AUTO_INCREMENT,
  `rdate` datetime(6) DEFAULT NULL,
  `verContent` varchar(255) DEFAULT NULL,
  `verName` varchar(255) DEFAULT NULL,
  `verWriter` varchar(255) DEFAULT NULL,
  `ver_content` varchar(255) DEFAULT NULL,
  `ver_name` varchar(255) DEFAULT NULL,
  `ver_writer` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`verId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.


-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기

-- information_schema 데이터베이스 구조 내보내기
/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
