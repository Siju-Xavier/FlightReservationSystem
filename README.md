# ✈️ Flight Reservation System

A comprehensive Java-based command-line application for managing flight bookings, user profiles, and airline schedules. This system provides a robust backend integrated with a MySQL database to handle complex reservation logic for customers, flight agents, and system administrators.

---

## 🚀 Features

### 👤 User Roles
*   **Customer**: Search for flights, book seats (Economy, Business, etc.), manage personal profile, and view booking history.
*   **Flight Agent**: Create, modify, and cancel reservations for customers, and manage customer data.
*   **System Administrator**: Full control over flight schedules (add/remove flights), user management, and system configuration.
*   **Guest**: Browse available flights without an account.

### 📅 Flight & Booking Management
*   **Real-time Availability**: Dynamic seat tracking based on database state.
*   **Multi-tiered Pricing**: Automatic pricing based on seat type (Economy, Premium Economy, Business, First Class).
*   **Search Engine**: Filter flights by origin and destination.
*   **Reservation Lifecycle**: Secure booking, modification, and cancellation with automatic seat updates.

### 🛠️ Technical Highlights
*   **Database Integration**: Robust persistence using MySQL and JDBC.
*   **MVC Pattern**: Clean separation of concerns with dedicated controllers for logic and models for data.
*   **Transactional Integrity**: Handled via `dbManager` to ensure data consistency during bookings and cancellations.

---

## 🛠️ Technologies Used

-   **Language**: Java 17+
-   **Database**: MySQL 8.0+
-   **Driver**: MySQL Connector/J (JDBC)
-   - **Tooling**: VS Code / IntelliJ IDEA / Eclipse

---

## 📋 Prerequisites

Before running the application, ensure you have the following installed:
-   [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) (Version 17 or higher)
-   [MySQL Server](https://dev.mysql.com/downloads/mysql/)
-   [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) (Included in the `FlightReservation.jar` or as a dependency)

---

## ⚙️ Setup & Installation

### 1. Database Configuration
1.  Open your MySQL terminal or a tool like MySQL Workbench.
2.  Create the database:
    ```sql
    CREATE DATABASE flight_reservation_db;
    ```
3.  Execute the schema script (see `Database Schema` section below) to create the necessary tables.
4.  Update the connection details in `dbManager.java` if your local MySQL settings differ:
    ```java
    private static final String URL = "jdbc:mysql://localhost:3306/flight_reservation_db";
    private static final String USER = "root";
    private static final String PASS = "your_password";
    ```

### 2. Compilation
If you are using the command line:
```bash
javac *.java
```

---

## 🏃 How to Run

1.  Navigate to the project root directory.
2.  Run the application:
    ```bash
    java Main
    ```
    *Alternatively, if using the provided JAR:*
    ```bash
    java -jar FlightReservation.jar
    ```

---

## 📂 Project Structure

```text
├── Main.java                # Application entry point
├── dbManager.java           # Database connection and SQL operations
├── controllers/             # Logic handlers
│   ├── LoginController.java
│   ├── FlightController.java
│   ├── BookingController.java
│   └── ...
└── models/                  # Data objects
    ├── User.java
    ├── Flight.java
    ├── Booking.java
    └── ...
```

---

## 🛡️ License

This project is developed as part of the **ENSF 480** course at the University of Calgary.
