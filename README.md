# SalesSavvyBackend

SalesSavvyBackend is a Spring Boot–based backend application designed to support the SalesSavvy system.  
It provides RESTful APIs for managing sales-related operations such as users, products, and orders.

This project demonstrates clean backend architecture and is suitable for real-world backend development.

---

## 🚀 Features

- RESTful API development using Spring Boot
- User management
- Product management
- Order / sales handling
- Layered architecture (Controller, Service, Repository)
- Maven-based project
- Easy to extend and maintain

---

## 🛠️ Tech Stack

- **Language:** Java  
- **Framework:** Spring Boot  
- **Build Tool:** Maven  
- **API Type:** REST  
- **Database:** Configurable via `application.properties`  

---

## 📂 Project Structure

```
SalesSavvyBackend
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── kodnest
│   │   │           └── salessavvy
│   │   │               ├── controller
│   │   │               ├── service
│   │   │               ├── repository
│   │   │               ├── model
│   │   │               └── SalesSavvyApplication.java
│   │   └── resources
│   │       └── application.properties
│   └── test
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

## ⚙️ Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL / PostgreSQL / H2 Database
- Git

---

## ▶️ How to Run the Project

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/punithkodnest/SalesSavvyBackend.git
```

### 2️⃣ Navigate to the Project Directory
```bash
cd SalesSavvyBackend
```

### 3️⃣ Configure Database
Update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/salessavvy
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### 4️⃣ Build the Project
```bash
mvn clean install
```

### 5️⃣ Run the Application
```bash
mvn spring-boot:run
```

Application runs on:
```
http://localhost:8080
```

---

## 📡 Sample API Endpoints

| Method | Endpoint | Description |
|------|---------|------------|
| GET | `/api/products` | Fetch all products |
| POST | `/api/products` | Add a new product |
| GET | `/api/users` | Fetch all users |
| POST | `/api/orders` | Create an order |

> Endpoints may vary based on implementation.

---

## 🧪 Testing

You can test APIs using:
- Postman
- Thunder Client
- curl

---

⭐ If you like this project, please give it a star!
