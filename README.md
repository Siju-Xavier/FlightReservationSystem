# ✈️ Flight Reservation System (Web Edition)

A full-stack flight management system built for **ENSF 480** at the University of Calgary. This project transforms a legacy Java console application into a web application with a **Java (Javalin) backend** and a **React (Vite) frontend**.


## Screenshots

<!--
Add screenshots below. Suggested shots: flight search/results page, booking/checkout flow, and admin/agent dashboard if applicable.
Create a `/screenshots` folder in the repo root and drop your images there.
-->

![Home / Flight Search](screenshots/home.png)
![Checkout / Booking Flow](screenshots/checkout.png)

---

## 🚀 Architecture Overview

### 🖥️ Backend (Java & Javalin)

The engine of the system, providing a REST API for flight searching, booking, and user management.

- **Server**: Javalin 4.x (Java 8 compatible)
- **Database**: MySQL 8.0+ for persistent storage
- **Structure**: Maven-based directory (`src/main/java`)
- **Entry Point**: `App.java` (runs on port 8080)

### 🎨 Frontend (React & Vite)

A responsive Single Page Application (SPA).

- **Design**: Glassmorphism styling with smooth transitions
- **Tech**: React 18+, Vite, vanilla CSS
- **API Integration**: Fetch API calls to the Java backend

---

## 👤 User Roles & Features

- **Customer**: Browse flights, manage profile, and book seats (Economy, Business, etc.)
- **Flight Agent**: Create and manage reservations for customers
- **System Administrator**: Manage flight schedules and users
- **Real-time Availability**: Dynamic seat tracking from the MySQL database

---

## 🛠️ Setup & Installation

### 1. Database Configuration

1. Create a MySQL database: `CREATE DATABASE flight_reservation_db;`
2. Initialize the schema using `schema.sql`.
3. Update connection details in `dbManager.java`.

### 2. Run the Backend

From the root directory:

```bash
./mvnw.cmd compile exec:java
```

The server starts at `http://localhost:8080`.

### 3. Run the Frontend

```bash
cd frontend
npm install
npm run dev
```

Then open `http://localhost:5173` (default Vite port).

### Testing the API

You can test endpoints directly with `curl`, for example:

```bash
curl -X POST http://localhost:8080/api/checkout \
  -H "Content-Type: application/json" \
  -d '{"amount":10,"paymentMethod":"debit","cardNumber":"4111111111111111","cardHolderName":"John Doe","expiryDate":"12/25","cvv":"123"}'
```

A successful response returns `{"message":"Payment successful"}`.

---

## 📂 Project Structure

```
├── src/main/java/            # Java backend logic
│   ├── App.java               # Entry point (Javalin server)
│   ├── FlightController.java  # Flight business logic
│   └── ...
├── frontend/                  # React UI (Vite)
├── pom.xml                    # Maven dependencies
├── schema.sql                 # Database initialization
└── README.md
```

---

## 🛡️ Context

Developed as part of the **ENSF 480** Term Project at the University of Calgary. Built as a team project — Java controller logic and most backend classes, then extended with the React/Vite frontend.
