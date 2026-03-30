# ✈️ Flight Reservation System (Web Edition)

A modern, full-stack flight management system built for **ENSF 480**. This project transforms a legacy Java console application into a high-performance web application with a **Java (Javalin) Backend** and a **React (Vite) Frontend**.

---

## 🚀 Architecture Overview

### 🖥️ Backend (Java & Javalin)
The engine of the system, providing a secure REST API for flight searching, booking, and user management.
-   **Server**: Javalin 4.x (Java 8 compatible).
-   **Database**: MySQL 8.0+ for persistent storage.
-   **Structure**: Maven-based directory (`src/main/java`) for clean dependency management.
-   **Entry Point**: `App.java` (Running on port 8080).

### 🎨 Frontend (React & Vite)
A premium, responsive Single Page Application (SPA) designed with a "WOW" factor.
-   **Design**: Modern glassmorphism, smooth animations, and dynamic transitions.
-   **Tech**: React 18+, Vite, Vanilla CSS.
-   **API Integration**: Communication via Fetch API to the Java backend.

---

## 👤 User Roles & Features

*   **Customer**: Browse flights, manage profiles, and book seats (Economy, Business, etc.).
*   **Flight Agent**: Create and manage reservations for customers.
*   **System Administrator**: Complete control over flight schedules and users.
*   **Real-time Availability**: Dynamic seat tracking directly from the MySQL database.

---

## 🛠️ Setup & Installation

### 1. Database Configuration
1.  Create a MySQL database: `CREATE DATABASE flight_reservation_db;`
2.  Initialize the schema using `schema.sql`.
3.  Update connection details in `dbManager.java`.

### 2. Run the Backend
From the root directory, run:
```bash
./mvnw.cmd compile exec:java
```
_The server will start at `http://localhost:8080`._

### 3. Run the Frontend (Coming Soon)
```bash
cd frontend
npm install
npm run dev
```

---

## 📂 Project Structure

```text
├── src/main/java/           # Java Backend Logic
│   ├── App.java             # Entry point (Javalin Server)
│   ├── FlightController.java# Flight business logic
│   └── ...
├── frontend/                # React UI (Vite)
├── pom.xml                  # Maven Dependencies
├── schema.sql               # Database Initialization
└── README.md                # This file!
```

---

## 🛡️ License

Developed as part of the **ENSF 480** Term Project at the University of Calgary.
