-- MySQL dump 10.13  Distrib 8.0.39, for Linux (x86_64)
--
-- Host: localhost    Database: vetclinic_lapka
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
  `appointment_id` int NOT NULL AUTO_INCREMENT,
  `time` datetime NOT NULL,
  `complaint` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `pet_id` int NOT NULL,
  `vet_id` int NOT NULL,
  PRIMARY KEY (`appointment_id`),
  KEY `vet_id` (`vet_id`),
  KEY `appointment_ibfk_1` (`pet_id`),
  CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`pet_id`) REFERENCES `pet` (`pet_id`) ON UPDATE CASCADE,
  CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`vet_id`) REFERENCES `vet` (`vet_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES (1,'2024-09-30 10:00:00','Чухання, почервоніння на шкірі',1,1),(2,'2024-09-30 12:00:00','Сильний свербіж в області вух, неприємний запах.',2,2),(3,'2024-09-30 14:00:00','Часте чухання, випадіння шерсті',3,1),(4,'2024-10-01 16:30:00','Кашель, чхання, виділення з носа',5,2),(5,'2024-10-01 10:00:00','Розпатлане пір\'я, втрата апетиту, млявість',7,4),(6,'2024-10-02 12:00:00','Втрата апетиту, млявість, діарея',9,3),(7,'2024-10-02 14:30:00','Кульгавість, опухлість лапки',10,3),(8,'2024-10-02 16:00:00','Кульгавість, біль у лапі',15,2),(9,'2024-10-03 10:00:00','Сильна втрата ваги, випадіння шерсті',12,3),(10,'2024-10-04 12:00:00','Часта відмова від води, втрата ваги',3,2),(11,'2024-10-04 14:00:00','Кашель, чхання, виділення з носа',11,1),(12,'2024-10-04 16:00:00','Свербіж, випадання шерсті, поранення на шкірі',9,3),(13,'2024-10-04 10:30:00','Собака млява, відмова від їжі, блювота',6,2),(14,'2024-10-07 12:00:00','Поганий стан зубів, відмова від твердої їжі',14,3),(15,'2024-10-07 14:00:00','Труднощі при диханні, виділення з носа',14,3),(16,'2024-10-08 16:00:00','Часте дихання, кашель, відсутність активності',1,1),(17,'2024-10-08 10:00:00','Утруднене сечовипускання, вокалізація',4,2),(18,'2024-10-08 12:30:00','Постійне чухання, зниження активності',8,4),(19,'2024-10-09 14:00:00','Підвищена спрага, часте сечовипускання',13,2),(20,'2024-10-09 16:00:00','Блювота, втрата апетиту, апатія',3,1);
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `breed`
--

DROP TABLE IF EXISTS `breed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `breed` (
  `breed_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `specie_id` int NOT NULL,
  PRIMARY KEY (`breed_id`),
  KEY `specie_id` (`specie_id`),
  CONSTRAINT `breed_ibfk_1` FOREIGN KEY (`specie_id`) REFERENCES `specie` (`specie_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `breed`
--

LOCK TABLES `breed` WRITE;
/*!40000 ALTER TABLE `breed` DISABLE KEYS */;
INSERT INTO `breed` VALUES (1,'Мала собака',1),(2,'Середня собака',1),(3,'Велика собака',1),(4,'Короткошерстна',2),(5,'Довгошерстна',2),(6,'Східна порода',2),(7,'Гібридна',2),(8,'Хвилястий папугай',3),(9,'Велика птиця',3),(10,'Інша птиця',3),(11,'Хомяк',4),(12,'Заєць',4),(13,'Малий гризун',4),(14,'Великий гризун',4);
/*!40000 ALTER TABLE `breed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medical_card`
--

DROP TABLE IF EXISTS `medical_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medical_card` (
  `card_id` int NOT NULL AUTO_INCREMENT,
  `appointment_id` int NOT NULL,
  `diagnosis` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `treatment` varchar(1000) NOT NULL,
  PRIMARY KEY (`card_id`),
  KEY `fk_appointment` (`appointment_id`),
  CONSTRAINT `fk_appointment` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`appointment_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_card`
--

LOCK TABLES `medical_card` WRITE;
/*!40000 ALTER TABLE `medical_card` DISABLE KEYS */;
INSERT INTO `medical_card` VALUES (3,1,'Алергічний дерматит','Антигістамінні, корекція харчування'),(4,2,'Отит','Промивання вух, антибіотики, протизапальні засоби'),(5,3,'Блошиний дерматит','Обробка від бліх, антигістамінні препарати'),(6,4,'Респіраторна інфекція','Антибіотики, противірусні препарати, догляд за носом'),(7,5,'Пташиний грип','Ізоляція, противірусні препарати, підтримуюча терапія.'),(8,6,'Інтоксикація','Промивання шлунка, сорбенти, підтримуюча терапія'),(9,7,'Перелом кінцівки.','Фіксація лапки, спокій'),(10,8,'Перелом','Рентген, фіксація кінцівки, можливе хірургічне втручання'),(11,9,'Нестача вітамінів','Введення вітамінів, корекція харчування'),(12,10,'Хронічна ниркова недостатність','Дієта, підтримуюча терапія, інфузії'),(13,11,'Респіраторна інфекція','Антибіотики, противірусні препарати, догляд за носом'),(14,12,'Мікози','Протигрибкові мазі, підтримка імунітету'),(15,13,'Гастрит','Дієта, гастропротектори, пробіотики'),(16,14,'Перерослі зуби','Обрізання зубів, корекція дієти.'),(17,15,'Респіраторна інфекція','Антибіотики, догляд, підтримуюча терапія'),(18,16,'Серцева недостатність','Серцеві препарати, сечогінні'),(19,17,'Сечокам\'яна хвороба','Спеціальна дієта, катетеризація, препарати для розчинення каменів'),(20,18,'Пухо-пероїдний паразит','Обробка інсектицидними засобами, очищення клітки'),(21,19,'Цукровий діабет','Інсулінова терапія, контроль дієти'),(22,20,'Харчове отруєння','Сорбенти, дієта, рідини для підтримки електролітів');
/*!40000 ALTER TABLE `medical_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `owner`
--

DROP TABLE IF EXISTS `owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `owner` (
  `owner_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `phone` varchar(25) NOT NULL,
  PRIMARY KEY (`owner_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `owner`
--

LOCK TABLES `owner` WRITE;
/*!40000 ALTER TABLE `owner` DISABLE KEYS */;
INSERT INTO `owner` VALUES (1,'Коваленко Олена','050-123-45-67'),(2,'Шевченко Андрій','067-987-65-43'),(3,'Ковальчук Марія','063-456-78-90'),(4,'Сидоренко Іван','073-234-56-78'),(5,'Мельник Тетяна','098-765-43-21'),(6,'Маляр Сергій','091-234-56-78'),(7,'Ковальчук Наталія','095-876-54-32'),(8,'Тимошенко Павло','097-543-21-09'),(9,'Ткач Катерина','092-345-67-89'),(10,'Ткач Олексій','066-678-90-12');
/*!40000 ALTER TABLE `owner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pet`
--

DROP TABLE IF EXISTS `pet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pet` (
  `pet_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `date_of_birth` date NOT NULL,
  `gender` int NOT NULL,
  `features` varchar(1000) DEFAULT NULL,
  `breed_id` int NOT NULL,
  `owner_id` int NOT NULL,
  PRIMARY KEY (`pet_id`),
  KEY `breed_id` (`breed_id`),
  KEY `pet_ibfk_2` (`owner_id`),
  CONSTRAINT `pet_ibfk_1` FOREIGN KEY (`breed_id`) REFERENCES `breed` (`breed_id`) ON UPDATE CASCADE,
  CONSTRAINT `pet_ibfk_2` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`owner_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pet`
--

LOCK TABLES `pet` WRITE;
/*!40000 ALTER TABLE `pet` DISABLE KEYS */;
INSERT INTO `pet` VALUES (1,'Барсік','2022-01-25',1,'Проблеми зі серцем',2,1),(2,'Рекс','2023-02-15',1,'Агресивний',3,2),(3,'Мурка','2022-08-11',2,NULL,5,3),(4,'Тиша','2021-04-18',2,NULL,4,4),(5,'Граф','2024-03-01',1,NULL,6,5),(6,'Сірко','2023-08-05',1,'Погано переносить наркоз',1,6),(7,'Белла','2023-10-10',2,NULL,9,6),(8,'Арчі','2021-08-07',1,NULL,8,8),(9,'Фіфі','2024-03-03',2,'Проблема з рефлексами',12,7),(10,'Чіп','2022-09-12',1,NULL,11,9),(11,'Соня','2023-04-05',2,NULL,7,10),(12,'Чарлі','2023-12-07',2,NULL,14,8),(13,'Комбі','2024-03-15',1,NULL,2,1),(14,'Зубік','2023-07-07',1,NULL,11,2),(15,'Мурчик','2023-11-11',2,'Агресивний',4,4),(17,'1','2024-03-15',2,NULL,2,2);
/*!40000 ALTER TABLE `pet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specie`
--

DROP TABLE IF EXISTS `specie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specie` (
  `specie_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`specie_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specie`
--

LOCK TABLES `specie` WRITE;
/*!40000 ALTER TABLE `specie` DISABLE KEYS */;
INSERT INTO `specie` VALUES (1,'Собака'),(2,'Кіт'),(3,'Птиця'),(4,'Гризун');
/*!40000 ALTER TABLE `specie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vet`
--

DROP TABLE IF EXISTS `vet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vet` (
  `vet_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `phone` varchar(25) NOT NULL,
  `is_available` tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (`vet_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vet`
--

LOCK TABLES `vet` WRITE;
/*!40000 ALTER TABLE `vet` DISABLE KEYS */;
INSERT INTO `vet` VALUES (1,'Маляр Іван','063-988-82-97',1),(2,'Чайковська Марія','091-399-84-12',1),(3,'Копач Мар\'ян','096-855-78-20',1),(4,'Пахуч Василь','098-625-87-33',1);
/*!40000 ALTER TABLE `vet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vet_speciality`
--

DROP TABLE IF EXISTS `vet_speciality`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vet_speciality` (
  `speciality_id` int NOT NULL AUTO_INCREMENT,
  `vet_id` int NOT NULL,
  `specie_id` int NOT NULL,
  PRIMARY KEY (`speciality_id`),
  KEY `vet_id` (`vet_id`),
  KEY `specie_id` (`specie_id`),
  CONSTRAINT `vet_speciality_ibfk_1` FOREIGN KEY (`vet_id`) REFERENCES `vet` (`vet_id`) ON UPDATE CASCADE,
  CONSTRAINT `vet_speciality_ibfk_2` FOREIGN KEY (`specie_id`) REFERENCES `specie` (`specie_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vet_speciality`
--

LOCK TABLES `vet_speciality` WRITE;
/*!40000 ALTER TABLE `vet_speciality` DISABLE KEYS */;
INSERT INTO `vet_speciality` VALUES (1,1,1),(2,1,2),(3,2,1),(4,2,2),(5,3,4),(6,4,3);
/*!40000 ALTER TABLE `vet_speciality` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'vetclinic_lapka'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-24 22:29:36
