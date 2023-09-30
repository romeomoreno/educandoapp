CREATE SCHEMA IF NOT EXISTS `EducandoApp` DEFAULT CHARACTER SET utf8;

USE `EducandoApp`;

-- -----------------------------------------------------
-- Table `Usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Usuario` (
  `id_Usuario` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  `Apellido` VARCHAR(45) NOT NULL,
  `Email` VARCHAR(45) NOT NULL,
  `Contraseña` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_Usuario`)
) ENGINE = InnoDB;

-- INSERT INTO Usuario (id_Usuario, Nombre, Apellido, Email, Contraseña)
-- VALUES (1, 'Dario', 'Lopez', 'prueba@hotmail.com', '12345');

-- -----------------------------------------------------
-- Table `Curso`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Curso` (
  `id_Curso` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  `Usuario_id_Usuario` INT NOT NULL,
  `Descripcion` VARCHAR(500) NOT NULL, -- Se corrigió aquí
  `Profesor` VARCHAR(45) NOT NULL, -- Se corrigió aquí
  `Duracion` VARCHAR(20) NOT NULL, -- Se corrigió aquí
  PRIMARY KEY (`id_Curso`)
) ENGINE = InnoDB;

-- INSERT INTO Curso (id_Curso, Nombre, Usuario_id_Usuario)
-- VALUES (1, 'Matematica', 1);

-- -----------------------------------------------------
-- Table `Categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Categoria` (
  `id_Categoria` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  `Descripcion` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_Categoria`)
) ENGINE = InnoDB;

-- INSERT INTO Categoria (id_Categoria, Nombre, Descripcion)
-- VALUES (1, 'Educando', 'Prueba');
