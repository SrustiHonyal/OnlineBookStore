-- ============================================
--  ONLINE BOOK STORE - MySQL Database Script
--  Tool: MySQL Workbench
-- ============================================

CREATE DATABASE IF NOT EXISTS bookstore_db;
USE bookstore_db;

-- -----------------------------------------------
-- Table: users
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(15),
    address TEXT,
    role ENUM('customer', 'admin') DEFAULT 'customer',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------
-- Table: categories
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

-- -----------------------------------------------
-- Table: books
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    category_id INT,
    price DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0,
    description TEXT,
    image_url VARCHAR(255),
    publisher VARCHAR(100),
    publish_year INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- -----------------------------------------------
-- Table: orders
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('pending', 'confirmed', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',
    shipping_address TEXT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- -----------------------------------------------
-- Table: order_items
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- -----------------------------------------------
-- Table: cart
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS cart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    UNIQUE KEY unique_cart_item (user_id, book_id)
);

-- -----------------------------------------------
-- Sample Data: categories
-- -----------------------------------------------
INSERT INTO categories (name, description) VALUES
('Fiction', 'Novels, short stories, and literary fiction'),
('Science & Technology', 'Books on science, engineering, and technology'),
('Self Help', 'Personal development and motivational books'),
('History', 'Historical accounts and biographies'),
('Children', 'Books for young readers');

-- -----------------------------------------------
-- Sample Data: books
-- -----------------------------------------------
INSERT INTO books (title, author, isbn, category_id, price, stock, description, publisher, publish_year) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', '978-0743273565', 1, 299.00, 50, 'A classic American novel set in the Jazz Age.', 'Scribner', 1925),
('To Kill a Mockingbird', 'Harper Lee', '978-0061935466', 1, 349.00, 40, 'A story of racial injustice and moral growth.', 'HarperCollins', 1960),
('Clean Code', 'Robert C. Martin', '978-0132350884', 2, 699.00, 30, 'A handbook of agile software craftsmanship.', 'Prentice Hall', 2008),
('The Pragmatic Programmer', 'Andrew Hunt', '978-0135957059', 2, 799.00, 25, 'Your journey to mastery.', 'Addison-Wesley', 2019),
('Atomic Habits', 'James Clear', '978-0735211292', 3, 499.00, 60, 'Tiny changes, remarkable results.', 'Avery', 2018),
('Think and Grow Rich', 'Napoleon Hill', '978-1585424337', 3, 299.00, 45, 'The landmark bestseller in success literature.', 'TarcherPerigee', 1937),
('Sapiens', 'Yuval Noah Harari', '978-0062316097', 4, 599.00, 35, 'A brief history of humankind.', 'Harper', 2015),
('The Diary of a Young Girl', 'Anne Frank', '978-0553296983', 4, 249.00, 55, 'The diary of a Jewish girl in WW2.', 'Bantam', 1947),
('Harry Potter and the Sorcerer Stone', 'J.K. Rowling', '978-0590353427', 5, 449.00, 70, 'The first book in the Harry Potter series.', 'Scholastic', 1997),
('The Little Prince', 'Antoine de Saint-Exupery', '978-0156012195', 5, 199.00, 80, 'A timeless childrens classic.', 'Mariner Books', 1943);

-- -----------------------------------------------
-- Sample Admin User (password: admin123)
-- -----------------------------------------------
INSERT INTO users (username, email, password, full_name, role) VALUES
('admin', 'admin@bookstore.com', 'admin123', 'Admin User', 'admin');
  
  
  select * from users;