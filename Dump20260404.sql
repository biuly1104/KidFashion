-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: kidsfashion
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Áo'),(2,'Quần'),(3,'Giày'),(4,'Balo'),(6,'Váy');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discounts`
--

DROP TABLE IF EXISTS `discounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discounts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `code` varchar(255) NOT NULL,
  `percent` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKbc29q3wh0lqhy0k84bx3afk08` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discounts`
--

LOCK TABLES `discounts` WRITE;
/*!40000 ALTER TABLE `discounts` DISABLE KEYS */;
INSERT INTO `discounts` VALUES (2,_binary '','sale1100',20),(3,_binary '','sale2200',10),(4,_binary '','kid1223',5);
/*!40000 ALTER TABLE `discounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` double NOT NULL,
  `product_id` bigint DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `quantity` int NOT NULL,
  `subtotal` double NOT NULL,
  `order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,123456,3,'Quần',1,123456,1),(2,456789,4,'Quần Dior',1,456789,1),(3,123123123,2,'Áo LV',1,123123123,2);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(500) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `total_amount` double NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'ádasdasd','2026-03-27 17:16:13.105231','Phước Nguyễn Đức','COD','0123456789','Đã hủy',580245,'biuly104'),(2,'11122 ádasdasd','2026-03-27 17:16:36.635012','Phước Nguyễn Đức','COD','0123456789','Đã hủy',123123123,'biuly104');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `best_seller` bit(1) NOT NULL,
  `newest` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1mtsbur82frn64de7balymq9s` (`category_id`),
  CONSTRAINT `FK1mtsbur82frn64de7balymq9s` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (14,NULL,'Chất liệu cotton thoải mái, thoáng khí giúp trẻ dễ dàng vận động cả ngày.','Áo thun trơn trẻ em',80000,'/uploads/1775218247142_ao1.webp',1,_binary '\0',_binary '\0'),(15,NULL,'Chất liệu cotton thoáng khí, giúp trẻ dễ dàng vận động','Áo thun trơn trẻ em',80000,'/uploads/1775218290073_ao2.webp',1,_binary '\0',_binary '\0'),(16,NULL,'Chất liệu dày dặn, bảo vệ khỏi tia UV mà vẫn thoáng khí.','Áo khoác thể thao cho trẻ',150000,'/uploads/1775218348299_ao3.avif',1,_binary '',_binary '\0'),(17,NULL,'Áo thể thao với họa tiết bắt mắt và chất liệu thoáng khí dễ dàng vận động','Áo thể thao cho bé trai',75000,'/uploads/1775218399261_ao6.avif',1,_binary '\0',_binary '\0'),(18,NULL,'Với họa tiết đơn giản mà vẫn dễ thương cho các bé nữ','Váy lửng Xanh Navy ',80000,'/uploads/1775218466251_ao4.webp',1,_binary '\0',_binary ''),(19,NULL,'Váy ngắn dễ thương cho bé','Váy ngắn cho bé nhỏ',76000,'/uploads/1775218503826_ao5.webp',1,_binary '\0',_binary '\0'),(20,NULL,'Chất liệu dày dặn với form thời thượng','Quần Jean xanh nhạt',95000,'/uploads/1775218550570_ao7.avif',2,_binary '\0',_binary '\0'),(21,NULL,'Chất liệu cotton thoáng khí với họa tiết đơn giản','Áo thun họa tiết Nike',82000,'/uploads/1775218593905_ao8.webp',1,_binary '\0',_binary '\0'),(22,NULL,'Balo dày dặn với họa tiết dễ thương','Balo Zoo cho trẻ',75000,'/uploads/1775218629787_b2.png',4,_binary '',_binary '\0'),(23,NULL,'Chất liệu cotton thoáng khí','Áo thun xanh Mint',75000,'/uploads/1775219744150_ao9.webp',1,_binary '\0',_binary '\0'),(24,NULL,'Váy đẹp cho trẻ','Váy xanh nhạt cho bé nữ',89000,'/uploads/1775219811502_v5.jpg',6,_binary '\0',_binary ''),(26,NULL,'Balo hoạt hình và đa dạng màu sắc','Balo hình mèo',97000,'/uploads/1775219898966_b5.jpg',4,_binary '\0',_binary ''),(27,NULL,'Quần yểm dễ thương cho bé','Quần yếm cho bé trai',87000,'/uploads/1775219948702_q1.png',2,_binary '',_binary '\0'),(28,NULL,'Giày em ái, giúp trẻ dê dàng vận động','Giày đá bóng họa tiết đơn giản',96000,'/uploads/1775219998276_g5.webp',3,_binary '\0',_binary ''),(29,NULL,'Váy xòe, thơ mộng cho bé gái','Váy nàng thơ cho bé',99000,'/uploads/1775220043709_v1.webp',6,_binary '',_binary '\0'),(30,NULL,'Họa tiết dễ thương, màu sắc bắt mắt','Giày hồng cute cho bé gái',65000,'/uploads/1775220116963_g4.jpg',3,_binary '\0',_binary '\0'),(31,NULL,'Balo họa tiết hình con bọ dễ thương','Balo hình con bọ',67000,'/uploads/1775220147376_b4.jpg',4,_binary '\0',_binary '\0'),(32,NULL,'Quần đơn giản với form thoải mái','Quần kaki màu Navy',56000,'/uploads/1775220178518_q5.webp',2,_binary '\0',_binary '\0'),(33,NULL,'Giày sandal cho cả bé trai lẫn gái, hoạt tiết đơn giản','Giày sandal thoải mái vận động',78000,'/uploads/1775220218211_g6.webp',3,_binary '\0',_binary '\0'),(34,NULL,'Họa tiết bắt mắt, form dáng thoải mái','Giày hoạt tiết trái cây cho bé gái',78000,'/uploads/1775220256370_g3.webp',3,_binary '\0',_binary '\0'),(35,NULL,'Chiếc váy đơn giản mà bắt mắt','Váy hồng cute cho bé gái',67000,'/uploads/1775220290133_v3.webp',6,_binary '\0',_binary '\0'),(36,NULL,'Quần model thời thượng','Quần short jean cho bé trai',87000,'/uploads/1775220325872_q4.jpg',2,_binary '\0',_binary '\0');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(20) NOT NULL,
  `username` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'$2a$10$HS0sJ3DfMbGQK5laD.INpOfD6LZmp6kJt8OKFOKfKuDFCd8uL7Uw2','USER','Phuoc'),(3,'b1556dea32e9d0cdbfed038fd7787275775ea40939c146a64e205bcb349ad02f','USER','phuoc112'),(4,'e5409a7d8cac0c5b29f104b0026c047d737e846dca25204b3e8c3b5bcf53d5b2','USER','biuly104'),(5,'$2a$10$1.gBzYB6WALG4P9hdOqfiO3s0qkTUkD47P84gQedhqdgsJWYkd9hG','ADMIN','admin1104'),(6,'$2a$10$KvrY5aWOSKO7pCwhJWinTObVrgvDurT.M7o1jp4zn4F0ZT/sJmgGW','USER','biuly1104');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-04 22:27:01
