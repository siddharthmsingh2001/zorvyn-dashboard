CREATE TABLE IF NOT EXISTS authorities(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS role_authorities(
    role_id INT NOT NULL,
    authority_id INT NOT NULL,
    PRIMARY KEY (role_id, authority_id),
    CONSTRAINT fk_role_map FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    CONSTRAINT fk_auth_map FOREIGN KEY (authority_id) REFERENCES authorities(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users(
    id BINARY(16) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    role_id INT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    enabled BOOLEAN DEFAULT TRUE,
    locked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    parent_id INT DEFAULT NULL,
    user_id BINARY(16) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT uq_category_user UNIQUE(name, user_id),
    CONSTRAINT fk_cat_parent FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL,
    CONSTRAINT fk_cat_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS finance_records (
    id BINARY(16) PRIMARY KEY,
    amount DECIMAL(19,2) NOT NULL,
    type ENUM('INCOME', 'EXPENSE') NOT NULL,
    category_id INT DEFAULT NULL,
    user_id BINARY(16) NOT NULL,
    description VARCHAR(255) DEFAULT NULL,
    transaction_date DATE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_rec_cat FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE SET NULL,
    CONSTRAINT fk_rec_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_records_user ON finance_records(user_id);
CREATE INDEX idx_records_date ON finance_records(transaction_date);
CREATE INDEX idx_category_user ON categories(user_id);