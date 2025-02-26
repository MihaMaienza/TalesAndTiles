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
-- Table structure for table `libro`
--

CREATE DATABASE IF NOT EXISTS talesandtiles;
USE talesandtiles;

DROP TABLE IF EXISTS `libro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libro` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `scrittore` varchar(45) NOT NULL,
  `editore` varchar(45) NOT NULL,
  `isbn` varchar(45) NOT NULL,
  `idGenere` int NOT NULL,
  `idProdotto` int NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `isbn_UNIQUE` (`isbn`),
  KEY `id_prodotto_idx` (`idProdotto`),
  KEY `id_genereLibro_idx` (`idGenere`),
  CONSTRAINT `id_genereLibro` FOREIGN KEY (`idGenere`) REFERENCES `generelibro` (`ID`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `id_prodotto` FOREIGN KEY (`idProdotto`) REFERENCES `prodotto` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libro`
--

LOCK TABLES `libro` WRITE;
/*!40000 ALTER TABLE `libro` DISABLE KEYS */;
INSERT INTO `libro` VALUES (6,'TJ Klune','Mondadori','978-8804735144',9,148),(7,'Agatha Christie','Mondadori','8804732520',10,149),(8,'Stuart Turton','Neri Pozza','8854518115',10,150),(9,'TJ Klune','Mondadori','8804750014',9,151),(10,'C. S. Pacat','Mondadori','978-8804745624',9,152),(11,'Mo Xiang Tong Xiu','Mondadori','8804775173',9,153),(12,'Agatha Christie','Mondadori','8804732539',10,154),(13,'Arthur Conan Doyle','Mondadori','8804726784',10,155),(14,'Maurice Leblanc','Salani','979-1259570192',10,156),(15,'Geronimo Stilton','PIEMME','8856683210',11,157),(16,'Geronimo Stilton','PIEMME','8856645858',11,158),(17,'Tea Stilton','PIEMME','8856678470',11,159),(18,'Tea Stilton','PIEMME','8856689057',11,160),(19,'Ali Hazelwood','Sperling & Kupfer','8820077604',8,161),(20,'Felicia Kingsley',' Newton Compton Editori','882276241X',8,162),(21,'Felicia Kingsley',' Newton Compton Editori','8822783921',8,163);
/*!40000 ALTER TABLE `libro` ENABLE KEYS */;
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
