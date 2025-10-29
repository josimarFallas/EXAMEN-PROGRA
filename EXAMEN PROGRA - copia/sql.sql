CREATE DATABASE IF NOT EXISTS webapp
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE webapp;

DROP TABLE IF EXISTS items;
CREATE TABLE items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(120) NOT NULL,
  descripcion TEXT,
  precio DECIMAL(10,2) DEFAULT 0,
  imagen VARCHAR(255)
);

INSERT INTO items (titulo, descripcion, precio, imagen) VALUES
('Libro: Java Principiante', 'Introducción', 10000.00, 'https://m.media-amazon.com/images/I/71mAXL2SrKL._UF1000,1000_QL80_.jpg'),
('Python para todos', 'Libro de programación en Python', 20000.00, 'https://assets.lectulandia.com/b/ab/Raul%20Gonzalez%20Duque/Python%20para%20todos%20(1)/big.jpg');
