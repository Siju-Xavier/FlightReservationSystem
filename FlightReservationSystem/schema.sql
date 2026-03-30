-- SQL Schema for Flight Reservation System

CREATE DATABASE IF NOT EXISTS flight_reservation_db;
USE flight_reservation_db;

-- Table: Airline
CREATE TABLE Airline (
    airline_id VARCHAR(10) PRIMARY KEY,
    airline_name VARCHAR(100) NOT NULL
);

-- Table: Loyalty_points_rules
CREATE TABLE Loyalty_points_rules (
    rule_id INT AUTO_INCREMENT PRIMARY KEY,
    airline_id VARCHAR(10),
    rule_type VARCHAR(50),
    rule TEXT,
    FOREIGN KEY (airline_id) REFERENCES Airline(airline_id)
);

-- Table: Baggage_rules
CREATE TABLE Baggage_rules (
    rule_id INT AUTO_INCREMENT PRIMARY KEY,
    airline_id VARCHAR(10),
    rule_type VARCHAR(50),
    rule TEXT,
    FOREIGN KEY (airline_id) REFERENCES Airline(airline_id)
);

-- Table: Policies
CREATE TABLE Policies (
    rule_id INT AUTO_INCREMENT PRIMARY KEY,
    airline_id VARCHAR(10),
    rule_type VARCHAR(50),
    rule TEXT,
    FOREIGN KEY (airline_id) REFERENCES Airline(airline_id)
);

-- Table: Flight
CREATE TABLE Flight (
    flight_number VARCHAR(20) PRIMARY KEY,
    route_id VARCHAR(20),
    airline_id VARCHAR(10),
    origin VARCHAR(100),
    destination VARCHAR(100),
    departure_time TIME,
    arrival_time TIME,
    flight_date DATE,
    base_price DOUBLE,
    available_seats INT,
    total_seats INT,
    flight_status VARCHAR(50),
    FOREIGN KEY (airline_id) REFERENCES Airline(airline_id)
);

-- Table: Seat
CREATE TABLE Seat (
    seat_id INT AUTO_INCREMENT PRIMARY KEY,
    seat_number VARCHAR(10),
    seat_type VARCHAR(50),
    is_available BOOLEAN DEFAULT TRUE,
    price DOUBLE,
    flight_number VARCHAR(20),
    FOREIGN KEY (flight_number) REFERENCES Flight(flight_number) ON DELETE CASCADE
);

-- Table: User_
CREATE TABLE User_ (
    customer_email VARCHAR(100) PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    customer_password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    customer_role ENUM('Customer', 'FlightAgent', 'SystemAdministrator'),
    employee_id VARCHAR(20),
    airline VARCHAR(10)
);

-- Table: Customer
CREATE TABLE Customer (
    username VARCHAR(100) PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    promotion BOOLEAN DEFAULT FALSE,
    is_guest BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (username) REFERENCES User_(username) ON DELETE CASCADE
);

-- Table: Booking
CREATE TABLE Booking (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    flight_number VARCHAR(20),
    customer_email VARCHAR(100),
    seat_number VARCHAR(10),
    booking_date DATE,
    booking_status VARCHAR(50),
    total_price DOUBLE,
    FOREIGN KEY (flight_number) REFERENCES Flight(flight_number),
    FOREIGN KEY (customer_email) REFERENCES User_(customer_email)
);

-- Table: Booking_history
CREATE TABLE Booking_history (
    booking_history_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_email VARCHAR(100),
    customer_history TEXT,
    FOREIGN KEY (customer_email) REFERENCES User_(customer_email) ON DELETE CASCADE
);
