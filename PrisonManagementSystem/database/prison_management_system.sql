-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 22 يناير 2026 الساعة 19:55
-- إصدار الخادم: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `prison_management_system`
--

-- --------------------------------------------------------

--
-- بنية الجدول `crime`
--

CREATE TABLE `crime` (
  `crimeId` int(11) NOT NULL,
  `crimeType` varchar(255) NOT NULL,
  `sentence` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- إرجاع أو استيراد بيانات الجدول `crime`
--

INSERT INTO `crime` (`crimeId`, `crimeType`, `sentence`) VALUES
(1, 'قاتل', 'مؤبد'),
(2, 'ارهاب', '10');

-- --------------------------------------------------------

--
-- بنية الجدول `department`
--

CREATE TABLE `department` (
  `departmentId` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `capacity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- إرجاع أو استيراد بيانات الجدول `department`
--

INSERT INTO `department` (`departmentId`, `name`, `capacity`) VALUES
(1, 'A1', 50),
(2, 'A2', 30);

-- --------------------------------------------------------

--
-- بنية الجدول `incident`
--

CREATE TABLE `incident` (
  `incidentId` int(11) NOT NULL,
  `description` text NOT NULL,
  `incident_date` date NOT NULL,
  `severity` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- بنية الجدول `medical_record`
--

CREATE TABLE `medical_record` (
  `medicalRecordId` int(11) NOT NULL,
  `prisonerId` int(11) NOT NULL,
  `conditions` text DEFAULT NULL,
  `medication` text DEFAULT NULL,
  `recordDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- إرجاع أو استيراد بيانات الجدول `medical_record`
--

INSERT INTO `medical_record` (`medicalRecordId`, `prisonerId`, `conditions`, `medication`, `recordDate`) VALUES
(1, 2, 'مريض نفسي', 'مهدات', '2025-09-09');

-- --------------------------------------------------------

--
-- بنية الجدول `person`
--

CREATE TABLE `person` (
  `id` int(11) NOT NULL,
  `fullName` varchar(255) NOT NULL,
  `age` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- إرجاع أو استيراد بيانات الجدول `person`
--

INSERT INTO `person` (`id`, `fullName`, `age`) VALUES
(1, 'ali', 18),
(2, 'عبدالله فضل', 20),
(3, 'اكرم الحداء', 22),
(4, 'نوال محمد', 40),
(5, 'نوال محمد', 44);

-- --------------------------------------------------------

--
-- بنية الجدول `prisoner`
--

CREATE TABLE `prisoner` (
  `id` int(11) NOT NULL,
  `nationality` varchar(100) DEFAULT NULL,
  `crimeId` int(11) DEFAULT NULL,
  `departmentId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- إرجاع أو استيراد بيانات الجدول `prisoner`
--

INSERT INTO `prisoner` (`id`, `nationality`, `crimeId`, `departmentId`) VALUES
(2, 'يمني', 2, 2);

-- --------------------------------------------------------

--
-- بنية الجدول `room`
--

CREATE TABLE `room` (
  `roomId` int(11) NOT NULL,
  `departmentId` int(11) NOT NULL,
  `typeOfRoom` varchar(100) DEFAULT NULL,
  `capacity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- إرجاع أو استيراد بيانات الجدول `room`
--

INSERT INTO `room` (`roomId`, `departmentId`, `typeOfRoom`, `capacity`) VALUES
(1, 1, 'عادي', 5),
(2, 1, 'فردي', 1);

-- --------------------------------------------------------

--
-- بنية الجدول `staff`
--

CREATE TABLE `staff` (
  `id` int(11) NOT NULL,
  `jobTitle` varchar(100) DEFAULT NULL,
  `rank` varchar(100) DEFAULT NULL,
  `shift` varchar(50) DEFAULT NULL,
  `departmentId` int(11) DEFAULT NULL,
  `accessPermissions` varchar(255) DEFAULT NULL,
  `role` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- بنية الجدول `visit`
--

CREATE TABLE `visit` (
  `visitId` int(11) NOT NULL,
  `prisonerId` int(11) NOT NULL,
  `visitorId` int(11) NOT NULL,
  `visitDate` date NOT NULL,
  `status` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- إرجاع أو استيراد بيانات الجدول `visit`
--

INSERT INTO `visit` (`visitId`, `prisonerId`, `visitorId`, `visitDate`, `status`) VALUES
(1, 2, 5, '2025-09-09', 'مجدولة');

-- --------------------------------------------------------

--
-- بنية الجدول `visitor`
--

CREATE TABLE `visitor` (
  `id` int(11) NOT NULL,
  `idCard` bigint(20) DEFAULT NULL,
  `relationship` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- إرجاع أو استيراد بيانات الجدول `visitor`
--

INSERT INTO `visitor` (`id`, `idCard`, `relationship`) VALUES
(5, 545445465, 'ام');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `crime`
--
ALTER TABLE `crime`
  ADD PRIMARY KEY (`crimeId`);

--
-- Indexes for table `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`departmentId`);

--
-- Indexes for table `incident`
--
ALTER TABLE `incident`
  ADD PRIMARY KEY (`incidentId`);

--
-- Indexes for table `medical_record`
--
ALTER TABLE `medical_record`
  ADD PRIMARY KEY (`medicalRecordId`),
  ADD KEY `prisonerId` (`prisonerId`);

--
-- Indexes for table `person`
--
ALTER TABLE `person`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `prisoner`
--
ALTER TABLE `prisoner`
  ADD PRIMARY KEY (`id`),
  ADD KEY `crimeId` (`crimeId`),
  ADD KEY `departmentId` (`departmentId`);

--
-- Indexes for table `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`roomId`),
  ADD KEY `departmentId` (`departmentId`);

--
-- Indexes for table `staff`
--
ALTER TABLE `staff`
  ADD PRIMARY KEY (`id`),
  ADD KEY `departmentId` (`departmentId`);

--
-- Indexes for table `visit`
--
ALTER TABLE `visit`
  ADD PRIMARY KEY (`visitId`),
  ADD KEY `prisonerId` (`prisonerId`),
  ADD KEY `visitorId` (`visitorId`);

--
-- Indexes for table `visitor`
--
ALTER TABLE `visitor`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `crime`
--
ALTER TABLE `crime`
  MODIFY `crimeId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `department`
--
ALTER TABLE `department`
  MODIFY `departmentId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `incident`
--
ALTER TABLE `incident`
  MODIFY `incidentId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `medical_record`
--
ALTER TABLE `medical_record`
  MODIFY `medicalRecordId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `person`
--
ALTER TABLE `person`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `room`
--
ALTER TABLE `room`
  MODIFY `roomId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `visit`
--
ALTER TABLE `visit`
  MODIFY `visitId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- قيود الجداول المُلقاة.
--

--
-- قيود الجداول `medical_record`
--
ALTER TABLE `medical_record`
  ADD CONSTRAINT `medical_record_ibfk_1` FOREIGN KEY (`prisonerId`) REFERENCES `prisoner` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- قيود الجداول `prisoner`
--
ALTER TABLE `prisoner`
  ADD CONSTRAINT `prisoner_ibfk_1` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `prisoner_ibfk_2` FOREIGN KEY (`crimeId`) REFERENCES `crime` (`crimeId`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `prisoner_ibfk_3` FOREIGN KEY (`departmentId`) REFERENCES `department` (`departmentId`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- قيود الجداول `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `room_ibfk_1` FOREIGN KEY (`departmentId`) REFERENCES `department` (`departmentId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- قيود الجداول `staff`
--
ALTER TABLE `staff`
  ADD CONSTRAINT `staff_ibfk_1` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `staff_ibfk_2` FOREIGN KEY (`departmentId`) REFERENCES `department` (`departmentId`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- قيود الجداول `visit`
--
ALTER TABLE `visit`
  ADD CONSTRAINT `visit_ibfk_1` FOREIGN KEY (`prisonerId`) REFERENCES `prisoner` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `visit_ibfk_2` FOREIGN KEY (`visitorId`) REFERENCES `visitor` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- قيود الجداول `visitor`
--
ALTER TABLE `visitor`
  ADD CONSTRAINT `visitor_ibfk_1` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
