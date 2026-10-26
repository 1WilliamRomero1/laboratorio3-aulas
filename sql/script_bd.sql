CREATE DATABASE IF NOT EXISTS aulas_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE aulas_db;

CREATE TABLE IF NOT EXISTS usuarios (
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    nombre  VARCHAR(100) NOT NULL,
    correo  VARCHAR(120) NOT NULL,
    clave   VARCHAR(64)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_correo UNIQUE (correo)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS plantas (
    id      INT NOT NULL AUTO_INCREMENT,
    numero  INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_plantas_numero UNIQUE (numero)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tipos_aula (
    id      INT         NOT NULL AUTO_INCREMENT,
    nombre  VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_tipos_aula_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS estados_aula (
    id      INT         NOT NULL AUTO_INCREMENT,
    nombre  VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_estados_aula_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS aulas (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    nombre        VARCHAR(50)  NOT NULL,
    edificio      VARCHAR(80)  NOT NULL,
    planta_id     INT          NOT NULL,
    capacidad     INT          NOT NULL,
    tipo_aula_id  INT          NOT NULL,
    responsable   VARCHAR(100) NOT NULL,
    estado_id     INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_aulas_nombre_edificio UNIQUE (nombre, edificio),
    CONSTRAINT fk_aulas_planta    FOREIGN KEY (planta_id)    REFERENCES plantas (id),
    CONSTRAINT fk_aulas_tipo_aula FOREIGN KEY (tipo_aula_id) REFERENCES tipos_aula (id),
    CONSTRAINT fk_aulas_estado    FOREIGN KEY (estado_id)    REFERENCES estados_aula (id)
) ENGINE=InnoDB;

INSERT IGNORE INTO plantas (numero) VALUES (1), (2), (3), (4), (5);

INSERT IGNORE INTO tipos_aula (nombre) VALUES
    ('Normal'), ('Centro de cómputo'), ('Laboratorio'), ('Especial');

INSERT IGNORE INTO estados_aula (nombre) VALUES
    ('Asignada'), ('Asignada temporalmente'), ('En mantenimiento'),
    ('Disponible'), ('Fuera de servicio');

INSERT IGNORE INTO usuarios (nombre, correo, clave)
VALUES ('Administrador', 'admin@ufg.edu.sv', SHA2('admin123', 256));
