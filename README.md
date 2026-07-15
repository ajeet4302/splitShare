# 💸 SplitShare - Expense Sharing & Bill Splitting Application

SplitShare is a **Java Full Stack Expense Sharing Application** inspired by Splitwise. It allows users to create groups, add members, split expenses equally, track balances, and settle payments with ease.

🔗 **Live Demo:** https://splitshare-vfh9.onrender.com/

📂 **GitHub Repository:** https://github.com/ajeet4302/splitShare

---

# 📖 Overview

Managing shared expenses with friends, roommates, colleagues, or family can be difficult. SplitShare simplifies expense management by automatically calculating balances, tracking who owes whom, and recording settlements.

The application is built using **Spring Boot**, **MySQL**, **HTML**, **CSS**, and **JavaScript**, with secure **JWT Authentication**.

---

# ✨ Features

### 👤 User Authentication
- User Registration
- Secure Login
- JWT Authentication
- Password Encryption using BCrypt
- Profile Management

### 👥 Group Management
- Create Expense Groups
- View All Groups
- Add Members to Groups
- Manage Group Members

### 💰 Expense Management
- Add Expenses
- Equal Expense Splitting
- Track Individual Shares
- Expense History

### 📊 Balance Tracking
- View Outstanding Balances
- Check Who Owes Money
- Calculate Amounts Automatically

### 🤝 Settlement
- Record Settlements
- Reduce Outstanding Balances
- Settlement History

---

# 🛠️ Tech Stack

## Backend
- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Hibernate
- JWT Authentication
- Maven

## Frontend
- HTML5
- CSS3
- JavaScript

## Database
- MySQL (Aiven Cloud)

## Deployment
- Docker
- Render

## Version Control
- Git
- GitHub

---

# 📂 Project Structure

```
SplitShare
│
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── controller
│   │   │   ├── service
│   │   │   ├── repository
│   │   │   ├── entity
│   │   │   ├── dto
│   │   │   ├── security
│   │   │   └── config
│   │   │
│   │   └── resources
│   │       ├── static
│   │       └── application.properties
│   │
│   └── test
│
├── Dockerfile
├── pom.xml
└── README.md
```

---

# 🚀 Live Application

### 🌐 Application

https://splitshare-vfh9.onrender.com/

### 📂 GitHub Repository

https://github.com/ajeet4302/splitShare

---

# 🔐 Authentication

The application uses:

- JWT Authentication
- Spring Security
- BCrypt Password Encoding
- Stateless Session Management

---

# ⚙️ Installation

## Clone Repository

```bash
git clone https://github.com/ajeet4302/splitShare.git
```

Move into the project

```bash
cd splitShare
```

---

## Configure Database

Update your `application.properties`

```properties
spring.datasource.url=YOUR_DATABASE_URL
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

---

## Build Project

```bash
./mvnw clean package
```

---

## Run Application

```bash
java -jar target/splitwise-0.0.1-SNAPSHOT.jar
```

Application runs on

```
http://localhost:8080
```

---

# 📸 Application Modules

- Login
- Register
- Dashboard
- Create Group
- Group Members
- Add Expense
- Expenses
- Balance
- Settlement
- Profile

---

# 📌 Future Enhancements

- Expense Categories
- Monthly Reports
- Pie Charts & Analytics
- Notifications
- Email Verification
- Password Reset
- Custom Expense Split
- Mobile Responsive UI
- Dark Mode

---

# 👨‍💻 Developer

**Ajeet Malviya**

Java Full Stack Developer

### Skills

- Java
- Spring Boot
- Spring Security
- Hibernate
- MySQL
- HTML
- CSS
- JavaScript
- Docker
- Git
- GitHub

---

# ⭐ If you like this project

Give this repository a ⭐ on GitHub if you found it useful.

---

## 📄 License

This project is developed for learning and portfolio purposes.
