# 📚 BookNest - Online Book Store

## Technology Stack
- **Frontend**: HTML, CSS, JavaScript (Vanilla)
- **Backend**: Java (Servlets) — deployed on Apache Tomcat
- **Database**: MySQL (via MySQL Workbench)
- **Build Tool**: Maven

---

## 🗂️ Project Structure
```
OnlineBookStore/
├── frontend/                   ← Open directly in browser (after Tomcat setup)
│   ├── index.html              ← Home page
│   ├── books.html              ← Book listing + filter
│   ├── cart.html               ← Shopping cart
│   ├── login.html              ← Login
│   ├── register.html           ← Registration
│   ├── orders.html             ← My Orders
│   ├── admin.html              ← Admin dashboard
│   ├── css/style.css           ← All styles
│   └── js/main.js              ← Shared JS utilities
│
├── backend/                    ← Maven Java project
│   ├── pom.xml
│   └── src/main/java/com/bookstore/
│       ├── model/              ← Book, User, Order, OrderItem
│       ├── dao/                ← BookDAO, UserDAO, OrderDAO
│       ├── controller/         ← BookServlet, UserServlet, OrderServlet
│       └── util/               ← DBConnection, CORSFilter
│
└── database/
    └── bookstore.sql           ← Full schema + sample data
```

---

## ✅ STEP-BY-STEP SETUP

### STEP 1 — Install Required Software
1. **Java JDK 11+** → https://adoptium.net
2. **Apache Tomcat 9** → https://tomcat.apache.org/download-90.cgi
3. **MySQL Server** → https://dev.mysql.com/downloads/mysql/
4. **MySQL Workbench** → https://dev.mysql.com/downloads/workbench/
5. **Apache Maven** → https://maven.apache.org/download.cgi
6. **IDE** (Eclipse / IntelliJ IDEA) → recommended

---

### STEP 2 — Set Up MySQL Database

1. Open **MySQL Workbench**
2. Connect to your local MySQL server
3. Click **File → Open SQL Script**
4. Open `database/bookstore.sql`
5. Click ⚡ **Execute** (or press Ctrl+Shift+Enter)
6. You should see `bookstore_db` database created with tables and sample data

---

### STEP 3 — Configure Database Connection

Open `backend/src/main/java/com/bookstore/util/DBConnection.java`

Edit these lines:
```java
private static final String URL = "jdbc:mysql://localhost:3306/bookstore_db";
private static final String USER = "root";
private static final String PASSWORD = "your_password_here"; // ← Change this
```

Replace `your_password_here` with your MySQL root password.

---

### STEP 4 — Build the Backend with Maven

Open terminal/command prompt in the `backend/` folder:

```bash
cd backend
mvn clean package
```

This generates `target/bookstore.war`

---

### STEP 5 — Deploy to Apache Tomcat

**Option A: Copy WAR file**
1. Copy `backend/target/bookstore.war` to `<TOMCAT_HOME>/webapps/`
2. Start Tomcat:
   - Windows: Run `<TOMCAT_HOME>/bin/startup.bat`
   - Mac/Linux: Run `<TOMCAT_HOME>/bin/startup.sh`
3. Tomcat auto-deploys the WAR

**Option B: Use Eclipse IDE**
1. File → Import → Existing Maven Projects → select `backend/`
2. Right-click project → Run As → Run on Server
3. Select Apache Tomcat 9

---

### STEP 6 — Set Up Frontend

**Option A: Serve via Tomcat (Recommended)**
1. Copy the entire `frontend/` folder into `<TOMCAT_HOME>/webapps/bookstore/`
2. Access at: `http://localhost:8080/bookstore/index.html`

**Option B: Use VS Code Live Server**
1. Install VS Code extension "Live Server"
2. Open `frontend/` folder in VS Code
3. Right-click `index.html` → Open with Live Server
4. Make sure backend Tomcat is also running at port 8080

---

### STEP 7 — Verify Everything Works

1. Open browser → `http://localhost:8080/bookstore/index.html`
2. You should see the BookNest home page with books loaded
3. Test API directly: `http://localhost:8080/bookstore/api/books`

---

## 🔑 Default Admin Login
| Field    | Value              |
|----------|--------------------|
| Username | `admin`            |
| Password | `admin123`         |
| Role     | Admin (Full Access)|

---

## 📋 API Endpoints

| Method | Endpoint                         | Description         |
|--------|----------------------------------|---------------------|
| GET    | /api/books                       | Get all books       |
| GET    | /api/books/{id}                  | Get book by ID      |
| GET    | /api/books?search=keyword        | Search books        |
| GET    | /api/books?category=1            | Filter by category  |
| POST   | /api/books                       | Add book (admin)    |
| PUT    | /api/books                       | Update book (admin) |
| DELETE | /api/books/{id}                  | Delete book (admin) |
| POST   | /api/users/register              | Register user       |
| POST   | /api/users/login                 | Login user          |
| GET    | /api/orders?userId=1             | Get user orders     |
| GET    | /api/orders/all                  | All orders (admin)  |
| POST   | /api/orders                      | Place order         |
| PUT    | /api/orders/{id}/status?status=  | Update status       |

---

## 🌟 Features
- Browse and search books by title, author, description
- Filter books by category (Fiction, Science, Self Help, History, Children)
- Sort books by price or title
- Book detail modal popup
- Add to cart / update quantity / remove
- User registration and login
- Place orders with shipping address
- View order history with status tracking
- Admin dashboard: manage books, update order statuses

---

## ⚠️ Troubleshooting

**Books not loading?**
- Check Tomcat is running on port 8080
- Verify API: `http://localhost:8080/bookstore/api/books`
- Check browser console for CORS or network errors

**Database connection failed?**
- Verify MySQL is running
- Check password in `DBConnection.java`
- Ensure `bookstore_db` database was created from SQL script

**Build errors?**
- Ensure JDK 11+ is installed: `java -version`
- Ensure Maven is installed: `mvn -version`
- Run `mvn clean package -U` to force dependency refresh
