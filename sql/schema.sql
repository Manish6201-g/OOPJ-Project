CREATE DATABASE IF NOT EXISTS campus_lost_found;
USE campus_lost_found;

CREATE TABLE IF NOT EXISTS users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('STUDENT','ADMIN') NOT NULL DEFAULT 'STUDENT',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS items (
  item_id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(150) NOT NULL,
  category VARCHAR(100),
  description TEXT,
  location VARCHAR(150),
  item_type ENUM('LOST','FOUND') NOT NULL,
  status ENUM('OPEN','CLAIMED','RETURNED') NOT NULL DEFAULT 'OPEN',
  reported_by INT,
  item_date DATE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_items_reporter FOREIGN KEY (reported_by) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS claims (
  claim_id INT AUTO_INCREMENT PRIMARY KEY,
  item_id INT NOT NULL,
  user_id INT NOT NULL,
  claim_description TEXT,
  status ENUM('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
  claim_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_claim_item FOREIGN KEY (item_id) REFERENCES items(item_id),
  CONSTRAINT fk_claim_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE INDEX idx_items_type_status ON items(item_type,status);
CREATE INDEX idx_claims_user ON claims(user_id);
