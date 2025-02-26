-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: talesandtiles
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `gioco`
--

CREATE DATABASE IF NOT EXISTS talesandtiles;
USE talesandtiles;

DROP TABLE IF EXISTS `gioco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gioco` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `eta` varchar(45) NOT NULL,
  `casaProduttrice` varchar(45) NOT NULL,
  `idProdotto` int NOT NULL,
  `idGenere` int NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `idProdotto_idx` (`idProdotto`),
  KEY `idGenereGioco_idx` (`idGenere`),
  CONSTRAINT `idGenereGioco` FOREIGN KEY (`idGenere`) REFERENCES `generegioco` (`ID`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `idProdotto` FOREIGN KEY (`idProdotto`) REFERENCES `prodotto` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gioco`
--

LOCK TABLES `gioco` WRITE;
/*!40000 ALTER TABLE `gioco` DISABLE KEYS */;
INSERT INTO `gioco` VALUES (3,'10','Spin Master Games',164,12),(4,'8','Oh Happy Games',165,12),(5,'8','Monopoly',166,10),(6,'8','GLOP',167,10),(7,'8','DV Giochi',168,10),(8,'13','Hidden Games',169,13),(9,'14','Hasbro Games',170,13);
/*!40000 ALTER TABLE `gioco` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-12 15:27:43
