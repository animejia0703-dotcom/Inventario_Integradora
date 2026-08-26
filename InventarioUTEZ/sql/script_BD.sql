-- Script para crear la base de datos desde cero
-- Si ya existe la borra primero

DROP DATABASE IF EXISTS inventarios_equipos_computo;
CREATE DATABASE inventarios_equipos_computo;
USE inventarios_equipos_computo;

-- Tipos de equipo (computadora, monitor, proyector, etc.)
CREATE TABLE tipos_equipo (
    id_tipo INT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(50) NOT NULL UNIQUE
);

-- Ubicaciones (edificio y aula)
CREATE TABLE ubicaciones (
    id_ubicacion INT AUTO_INCREMENT PRIMARY KEY,
    edificio     VARCHAR(50) NOT NULL,
    aula         VARCHAR(50) NOT NULL,
    activa       TINYINT(1) NOT NULL DEFAULT 1,
    UNIQUE KEY uk_edificio_aula (edificio, aula)
);

-- Estados en los que puede estar un equipo
CREATE TABLE estados_fisicos (
    id_estado INT AUTO_INCREMENT PRIMARY KEY,
    nombre    VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla principal: los equipos
CREATE TABLE equipos (
    id_equipo      INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    id_tipo        INT NOT NULL,
    marca          VARCHAR(50),
    modelo         VARCHAR(50),
    numero_serie   VARCHAR(80) UNIQUE,
    id_estado      INT NOT NULL,
    notas          VARCHAR(255),          -- notas del equipo
    id_ubicacion   INT NOT NULL,          -- ubicacion actual del equipo
    responsable    VARCHAR(100),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    eliminado      TINYINT(1) NOT NULL DEFAULT 0,
    fecha_baja     DATETIME NULL,

    CONSTRAINT fk_equipo_tipo      FOREIGN KEY (id_tipo)      REFERENCES tipos_equipo(id_tipo),
    CONSTRAINT fk_equipo_estado    FOREIGN KEY (id_estado)    REFERENCES estados_fisicos(id_estado),
    CONSTRAINT fk_equipo_ubicacion FOREIGN KEY (id_ubicacion) REFERENCES ubicaciones(id_ubicacion)
);

-- Guarda cada cambio de ubicacion de un equipo
CREATE TABLE historial_ubicaciones (
    id_historial   INT AUTO_INCREMENT PRIMARY KEY,
    id_equipo      INT NOT NULL,
    id_ubicacion   INT NOT NULL,
    fecha_cambio   DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_historial_equipo    FOREIGN KEY (id_equipo)    REFERENCES equipos(id_equipo),
    CONSTRAINT fk_historial_ubicacion FOREIGN KEY (id_ubicacion) REFERENCES ubicaciones(id_ubicacion)
);

-- Guarda cada cambio de estado fisico de un equipo
CREATE TABLE historial_estados (
    id_historial   INT AUTO_INCREMENT PRIMARY KEY,
    id_equipo      INT NOT NULL,
    id_estado      INT NOT NULL,
    fecha_cambio   DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_historial_estado_equipo FOREIGN KEY (id_equipo) REFERENCES equipos(id_equipo),
    CONSTRAINT fk_historial_estado_estado FOREIGN KEY (id_estado) REFERENCES estados_fisicos(id_estado)
);

-- Datos iniciales
INSERT INTO tipos_equipo (nombre) VALUES
('Computadora'), ('Monitor'), ('Proyector'), ('Impresora'), ('Otro');

INSERT INTO estados_fisicos (nombre) VALUES
('Buenas condiciones'),
('Malas condiciones, pero usable'),
('En mantenimiento'),
('Dado de baja'),
('Registro erroneo');

INSERT INTO ubicaciones (edificio, aula) VALUES
('Docencia 1', 'Laboratorio 1'),
('Docencia 2', 'Laboratorio 1'),
('Docencia 3', 'Laboratorio 1'),
('Docencia 4', 'Laboratorio 1'),
('Docencia 5', 'Laboratorio 1'),
('Cecadec', 'Laboratorio 1'),
('Ceviset', 'Laboratorio 1'),
('Taller Pesado 1', 'Taller 1'),
('Taller Pesado 2', 'Taller 1'),
('Rectoria', 'Sala 1'),
('Cedim', 'Laboratorio 1');
