-- Creamos la base de datos y la seleccionamos
CREATE DATABASE IF NOT EXISTS get_today;
USE get_today;

-- 1. Tabla de Usuarios 
CREATE TABLE Usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre_completo VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    telefono VARCHAR(15),
    rol ENUM('cliente', 'negocio') NOT NULL
);

-- 2. Tabla de Negocios 
CREATE TABLE Negocios (
    id_negocio INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT UNIQUE NOT NULL, 
    nombre_local VARCHAR(100) NOT NULL,
    direccion VARCHAR(255),
    descripcion TEXT,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE
);

-- 3. Tabla de Horarios 
CREATE TABLE Horarios (
    id_horario INT PRIMARY KEY AUTO_INCREMENT,
    id_negocio INT NOT NULL,
    dia_semana TINYINT NOT NULL COMMENT 'Del 1 (Lunes) al 7 (Domingo)',
    hora_apertura TIME NOT NULL,
    hora_cierre TIME NOT NULL,
    FOREIGN KEY (id_negocio) REFERENCES Negocios(id_negocio) ON DELETE CASCADE
);

-- 4. Tabla de Servicios 
CREATE TABLE Servicios (
    id_servicio INT PRIMARY KEY AUTO_INCREMENT,
    id_negocio INT NOT NULL,
    nombre_servicio VARCHAR(100) NOT NULL,
    precio INT NOT NULL, 
    FOREIGN KEY (id_negocio) REFERENCES Negocios(id_negocio) ON DELETE CASCADE
);

-- 5. Tabla de Citas 
CREATE TABLE Citas (
    id_cita INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,  
    id_servicio INT NOT NULL, 
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    estado ENUM('confirmada', 'completada', 'cancelada') DEFAULT 'confirmada',
    FOREIGN KEY (id_cliente) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_servicio) REFERENCES Servicios(id_servicio) ON DELETE CASCADE
);