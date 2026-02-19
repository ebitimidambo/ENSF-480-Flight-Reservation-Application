DROP DATABASE IF EXISTS flight_reservation;
CREATE DATABASE IF NOT EXISTS flight_reservation;
USE flight_reservation;

-- ======================================
-- 1. ROLE TABLE
-- ======================================
CREATE TABLE `Role` (
    roleID INT AUTO_INCREMENT PRIMARY KEY,
    roleName VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO `Role` (roleName)
VALUES ('CUSTOMER'), ('FLIGHT_AGENT'), ('SYSTEM_ADMIN');

-- ======================================
-- 2. USER TABLE (KEEP YOUR INSERTS)
-- ======================================
CREATE TABLE `User` (
    userID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    emailAddress VARCHAR(100) UNIQUE NOT NULL,
    dateOfBirth DATE,
    address VARCHAR(255),
    roleID INT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (roleID) REFERENCES `Role`(roleID)
        ON UPDATE CASCADE ON DELETE SET NULL
);

INSERT INTO `User` (username, password, firstName, lastName, emailAddress, dateOfBirth, address, roleID)
VALUES 
('john_doe', 'password123', 'John', 'Doe', 'john.doe@email.com', '1990-01-01', '123 Main St, Toronto', 1),
('agent_smith', 'agentpass', 'Sarah', 'Smith', 'sarah.smith@airline.com', '1985-02-14', '456 Agent Rd, Calgary', 2),
('sys_admin', 'adminpass', 'Alex', 'Johnson', 'alex.admin@flightcorp.com', '1980-06-22', '789 Control Ave, Vancouver', 3);

-- ======================================
-- 3. ROUTE TABLE (2 pairs = 4 routes)
-- ======================================
CREATE TABLE `Route` (
    routeID INT AUTO_INCREMENT PRIMARY KEY,
    origin VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    distance DECIMAL(8,2),
    status VARCHAR(30) DEFAULT 'Active'
);

INSERT INTO `Route` (routeID, origin, destination, distance, status)
VALUES
(1, 'Calgary', 'Toronto', 2710.00, 'Active'),
(2, 'Toronto', 'Calgary', 2710.00, 'Active'),
(3, 'Nigeria', 'Calgary', 10000.00, 'Active'),
(4, 'Calgary', 'Nigeria', 10000.00, 'Active');

-- ======================================
-- 4. AIRCRAFT TABLE (keep same insertion values)
-- ======================================
CREATE TABLE `Aircraft` (
    aircraftID INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    haulType VARCHAR(30)
);

INSERT INTO `Aircraft` (aircraftID, model, capacity, haulType)
VALUES 
(1, 'Boeing 737', 180, 'Medium-haul'),
(2, 'Airbus A320', 160, 'Short-haul'),
(3, 'Boeing 787', 250, 'Long-haul'),
(4, 'Embraer E175', 88, 'Regional');

-- ======================================
-- 5. FLIGHT TABLE (8 flights total)
-- 2 flights per route on Dec 4 and Dec 5
-- ======================================
CREATE TABLE `Flight` (
    flightID INT AUTO_INCREMENT PRIMARY KEY,
    flightName VARCHAR(50) NOT NULL,
    routeID INT,
    aircraftID INT,
    departureTime DATETIME NOT NULL,
    arrivalTime DATETIME NOT NULL,
    status VARCHAR(30) DEFAULT 'Scheduled',
    FOREIGN KEY (routeID) REFERENCES `Route`(routeID)
        ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (aircraftID) REFERENCES `Aircraft`(aircraftID)
        ON UPDATE CASCADE ON DELETE SET NULL
);

INSERT INTO `Flight` (flightID, flightName, routeID, aircraftID, departureTime, arrivalTime, status)
VALUES
-- Calgary -> Toronto (route 1) ~3h30
(1, 'ACK101', 1, 1, '2025-12-04 08:00:00', '2025-12-04 11:30:00', 'Scheduled'),
(2, 'ACK102', 1, 1, '2025-12-05 14:00:00', '2025-12-05 17:30:00', 'Scheduled'),

-- Toronto -> Calgary (route 2) ~3h30
(3, 'ACK201', 2, 1, '2025-12-04 09:00:00', '2025-12-04 12:30:00', 'Scheduled'),
(4, 'ACK202', 2, 1, '2025-12-05 15:00:00', '2025-12-05 18:30:00', 'Scheduled'),

-- Nigeria -> Calgary (route 3) long-haul ~12h
(5, 'ACK301', 3, 3, '2025-12-04 07:00:00', '2025-12-04 19:00:00', 'Scheduled'),
(6, 'ACK302', 3, 3, '2025-12-05 13:00:00', '2025-12-06 01:00:00', 'Scheduled'),

-- Calgary -> Nigeria (route 4) long-haul ~12h
(7, 'ACK401', 4, 3, '2025-12-04 10:00:00', '2025-12-04 22:00:00', 'Scheduled'),
(8, 'ACK402', 4, 3, '2025-12-05 16:00:00', '2025-12-06 04:00:00', 'Scheduled');

-- ======================================
-- 6. SEAT TABLE
-- NOTE: To allow 48 seats total (unique per flight), we must NOT have UNIQUE(seatNumber, class).
-- Column names unchanged.
-- ======================================
CREATE TABLE `Seat` (
    seatID INT AUTO_INCREMENT PRIMARY KEY,
    seatNumber VARCHAR(10) NOT NULL,
    class VARCHAR(20) DEFAULT 'Economy',
    price DECIMAL(10,2) NOT NULL,
    isAvailable BOOLEAN DEFAULT TRUE
);

-- 48 seats total = 6 seats per flight * 8 flights
-- Seat layout per flight:
-- 1A/1B Business, 2A/2B Premium Economy, 3A/3B Economy
INSERT INTO `Seat` (seatID, seatNumber, class, price, isAvailable)
VALUES
-- Flight 1 seats (IDs 1..6)
(1,'1A','Business',450.00,TRUE),
(2,'1B','Business',450.00,TRUE),
(3,'2A','Premium Economy',300.00,TRUE),
(4,'2B','Premium Economy',300.00,TRUE),
(5,'3A','Economy',200.00,TRUE),
(6,'3B','Economy',200.00,TRUE),

-- Flight 2 seats (IDs 7..12)
(7,'1A','Business',450.00,TRUE),
(8,'1B','Business',450.00,TRUE),
(9,'2A','Premium Economy',300.00,TRUE),
(10,'2B','Premium Economy',300.00,TRUE),
(11,'3A','Economy',200.00,TRUE),
(12,'3B','Economy',200.00,TRUE),

-- Flight 3 seats (IDs 13..18)
(13,'1A','Business',450.00,TRUE),
(14,'1B','Business',450.00,TRUE),
(15,'2A','Premium Economy',300.00,TRUE),
(16,'2B','Premium Economy',300.00,TRUE),
(17,'3A','Economy',200.00,TRUE),
(18,'3B','Economy',200.00,TRUE),

-- Flight 4 seats (IDs 19..24)
(19,'1A','Business',450.00,TRUE),
(20,'1B','Business',450.00,TRUE),
(21,'2A','Premium Economy',300.00,TRUE),
(22,'2B','Premium Economy',300.00,TRUE),
(23,'3A','Economy',200.00,TRUE),
(24,'3B','Economy',200.00,TRUE),

-- Flight 5 seats (IDs 25..30)
(25,'1A','Business',650.00,TRUE),
(26,'1B','Business',650.00,TRUE),
(27,'2A','Premium Economy',450.00,TRUE),
(28,'2B','Premium Economy',450.00,TRUE),
(29,'3A','Economy',300.00,TRUE),
(30,'3B','Economy',300.00,TRUE),

-- Flight 6 seats (IDs 31..36)
(31,'1A','Business',650.00,TRUE),
(32,'1B','Business',650.00,TRUE),
(33,'2A','Premium Economy',450.00,TRUE),
(34,'2B','Premium Economy',450.00,TRUE),
(35,'3A','Economy',300.00,TRUE),
(36,'3B','Economy',300.00,TRUE),

-- Flight 7 seats (IDs 37..42)
(37,'1A','Business',650.00,TRUE),
(38,'1B','Business',650.00,TRUE),
(39,'2A','Premium Economy',450.00,TRUE),
(40,'2B','Premium Economy',450.00,TRUE),
(41,'3A','Economy',300.00,TRUE),
(42,'3B','Economy',300.00,TRUE),

-- Flight 8 seats (IDs 43..48)
(43,'1A','Business',650.00,TRUE),
(44,'1B','Business',650.00,TRUE),
(45,'2A','Premium Economy',450.00,TRUE),
(46,'2B','Premium Economy',450.00,TRUE),
(47,'3A','Economy',300.00,TRUE),
(48,'3B','Economy',300.00,TRUE);

-- ======================================
-- 7. FLIGHT-SEAT MAPPING TABLE
-- ======================================
CREATE TABLE `FlightSeat` (
    flightID INT,
    seatID INT,
    PRIMARY KEY (flightID, seatID),
    FOREIGN KEY (flightID) REFERENCES `Flight`(flightID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (seatID) REFERENCES `Seat`(seatID)
        ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO `FlightSeat` (flightID, seatID)
VALUES
-- Flight 1 -> seats 1..6
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),
-- Flight 2 -> seats 7..12
(2,7),(2,8),(2,9),(2,10),(2,11),(2,12),
-- Flight 3 -> seats 13..18
(3,13),(3,14),(3,15),(3,16),(3,17),(3,18),
-- Flight 4 -> seats 19..24
(4,19),(4,20),(4,21),(4,22),(4,23),(4,24),
-- Flight 5 -> seats 25..30
(5,25),(5,26),(5,27),(5,28),(5,29),(5,30),
-- Flight 6 -> seats 31..36
(6,31),(6,32),(6,33),(6,34),(6,35),(6,36),
-- Flight 7 -> seats 37..42
(7,37),(7,38),(7,39),(7,40),(7,41),(7,42),
-- Flight 8 -> seats 43..48
(8,43),(8,44),(8,45),(8,46),(8,47),(8,48);

-- ======================================
-- 8. RESERVATION TABLE (unchanged)
-- ======================================
CREATE TABLE `Reservation` (
    reservationID INT AUTO_INCREMENT PRIMARY KEY,
    isReturn BOOLEAN DEFAULT FALSE,
    userID INT,
    passengerFirstName VARCHAR(30),
    passengerLastName VARCHAR (30),
    passengerDateOfBirth VARCHAR (30),
    flightID INT,
    departureSeatID INT,
    returnSeatID INT,
    checkedIn BOOLEAN DEFAULT FALSE,
    status VARCHAR(20) DEFAULT 'Pending',
    dateBooked TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userID) REFERENCES `User`(userID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (flightID) REFERENCES `Flight`(flightID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (departureSeatID) REFERENCES `Seat`(seatID)
        ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (returnSeatID) REFERENCES `Seat`(seatID)
        ON UPDATE CASCADE ON DELETE SET NULL
);

-- ======================================
-- 9. PAYMENT TABLE (unchanged)
-- ======================================
CREATE TABLE `Payment` (
    paymentID INT AUTO_INCREMENT PRIMARY KEY,
    reservationID INT,
    amount DECIMAL(10,2) NOT NULL,
    method VARCHAR(50) NOT NULL,
    status VARCHAR(30) DEFAULT 'Pending',
    `timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reservationID) REFERENCES `Reservation`(reservationID)
        ON UPDATE CASCADE ON DELETE CASCADE
);
