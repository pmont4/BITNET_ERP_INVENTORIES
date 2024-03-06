CREATE DATABASE DB_BITNET_ERP;
USE DB_BITNET_ERP;

-- Creacion del usuario especifico par la base de datos

CREATE USER 'user_bitnet_erp'@'%' IDENTIFIED BY 'bitnet123';
GRANT ALL ON  DB_BITNET_ERP.* TO 'user_bitnet_erp'@'%';

-- Creacion de tablas de la base datos

CREATE TABLE TIPO_MENU(
	ID_TIPO_MENU BIGINT NOT NULL,
    NOMBRE VARCHAR(50) NOT NULL,
    CONSTRAINT PK_TIPO_MENU PRIMARY KEY(ID_TIPO_MENU),
    CONSTRAINT IDX_TIPO_MENU_1 UNIQUE (NOMBRE)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

CREATE TABLE MENU(
	ID_MENU BIGINT NOT NULL,
    NOMBRE VARCHAR(50) NOT NULL,
    ID_TIPO_MENU BIGINT NOT NULL,
    ID_MENU_PADRE BIGINT,
    CONSTRAINT PK_MENU PRIMARY KEY(ID_MENU),
    CONSTRAINT FK_MENU_1 FOREIGN KEY(ID_MENU_PADRE) REFERENCES MENU(ID_MENU),
    CONSTRAINT FK_MENU_2 FOREIGN KEY(ID_TIPO_MENU) REFERENCES TIPO_MENU(ID_TIPO_MENU),
    CONSTRAINT IDX_MENU_1 UNIQUE (NOMBRE)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

CREATE TABLE ROL(
	ID_ROL BIGINT NOT NULL,
    NOMBRE VARCHAR(50) NOT NULL,
    CONSTRAINT PK_ROL PRIMARY KEY(ID_ROL),
    CONSTRAINT IDX_ROL_1 UNIQUE (NOMBRE)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

CREATE TABLE ROL_MENU(
	ID_ROL BIGINT NOT NULL,
    ID_MENU BIGINT NOT NULL,
    CONSTRAINT PK_ROL_MENU PRIMARY KEY(ID_ROL, ID_MENU),
    CONSTRAINT FK_ROL_MENU_1 FOREIGN KEY(ID_ROL) REFERENCES ROL(ID_ROL),
    CONSTRAINT FK_ROL_MENU_2 FOREIGN KEY(ID_MENU) REFERENCES MENU(ID_MENU)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

CREATE TABLE USUARIO(
	ID_USUARIO BIGINT NOT NULL,
    NOMBRE_LOGIN VARCHAR(50) NOT NULL,
    CONTRASENA VARCHAR(500) NOT NULL,
    NOMBRE_USUARIO VARCHAR(200) NOT NULL,
    CORREO_ELECTRONICO VARCHAR(200) NOT NULL,
    ID_ROL BIGINT NOT NULL,
    ACTIVO TINYINT NOT NULL,
    CONSTRAINT PK_USUARIO PRIMARY KEY(ID_USUARIO),
    CONSTRAINT IDX_USUARIO_1 UNIQUE(NOMBRE_LOGIN),
    CONSTRAINT FK_USUARIO_1 FOREIGN KEY(ID_ROL) REFERENCES ROL(ID_ROL)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

CREATE TABLE TIPO_EVENTO(
	ID_TIPO_EVENTO BIGINT NOT NULL,
    NOMBRE VARCHAR(50) NOT NULL,
    CONSTRAINT PK_TIPO_EVENTO PRIMARY KEY(ID_TIPO_EVENTO),
    CONSTRAINT IDX_TIPO_EVENTO_1 UNIQUE (NOMBRE)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

CREATE TABLE EVENTO (
	ID_EVENTO BIGINT NOT NULL,
    ID_TIPO_EVENTO BIGINT NOT NULL,
    ID_USUARIO BIGINT NOT NULL,
    FECHA_HORA TIMESTAMP NOT NULL,
    DESCRIPCION TEXT NOT NULL,
    CONSTRAINT PK_EVENTO PRIMARY KEY(ID_EVENTO),
    CONSTRAINT FK_EVENTO_1 FOREIGN KEY(ID_TIPO_EVENTO) REFERENCES TIPO_EVENTO(ID_TIPO_EVENTO),
    CONSTRAINT FK_EVENTO_2 FOREIGN KEY(ID_USUARIO) REFERENCES USUARIO(ID_USUARIO)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

-- Inserts de la base de datos

INSERT INTO TIPO_MENU(ID_TIPO_MENU, NOMBRE) VALUES (1, 'MENU');
INSERT INTO TIPO_MENU(ID_TIPO_MENU, NOMBRE) VALUES (2, 'MENU_ITEM');

INSERT INTO MENU(ID_MENU, NOMBRE, ID_TIPO_MENU, ID_MENU_PADRE) VALUES (1, 'ARCHIVO', 1, NULL);
INSERT INTO MENU(ID_MENU, NOMBRE, ID_TIPO_MENU, ID_MENU_PADRE) VALUES (2, 'MENU', 2, 1);
INSERT INTO MENU(ID_MENU, NOMBRE, ID_TIPO_MENU, ID_MENU_PADRE) VALUES (3, 'ROL', 2, 1);
INSERT INTO MENU(ID_MENU, NOMBRE, ID_TIPO_MENU, ID_MENU_PADRE) VALUES (4, 'ROL-MENU', 2, 1);
INSERT INTO MENU(ID_MENU, NOMBRE, ID_TIPO_MENU, ID_MENU_PADRE) VALUES (5, 'USUARIO', 2, 1);
INSERT INTO MENU(ID_MENU, NOMBRE, ID_TIPO_MENU, ID_MENU_PADRE) VALUES (6, 'CERRAR SESION', 2, 1);

INSERT INTO ROL_MENU(ID_ROL, ID_MENU) VALUES (1, 1);
INSERT INTO ROL_MENU(ID_ROL, ID_MENU) VALUES (1, 2);
INSERT INTO ROL_MENU(ID_ROL, ID_MENU) VALUES (1, 3);
INSERT INTO ROL_MENU(ID_ROL, ID_MENU) VALUES (1, 4);
INSERT INTO ROL_MENU(ID_ROL, ID_MENU) VALUES (1, 5);
INSERT INTO ROL_MENU(ID_ROL, ID_MENU) VALUES (1, 6);

INSERT INTO ROL(ID_ROL, NOMBRE) VALUES (1, 'Administrador');

INSERT INTO USUARIO(ID_USUARIO, NOMBRE_LOGIN, CONTRASENA, NOMBRE_USUARIO, CORREO_ELECTRONICO, ID_ROL, ACTIVO) VALUES (1, 'Admin', 'admin123', 'Administrador', 'pauloamont2005@outlook.com', 1, 1);

INSERT INTO TIPO_EVENTO(ID_TIPO_EVENTO, NOMBRE) VALUES (1, 'LOGIN');

INSERT INTO EVENTO(ID_EVENTO, ID_TIPO_EVENTO, ID_USUARIO, FECHA_HORA, DESCRIPCION) VALUES (1, 1, 1, CURRENT_TIMESTAMP, 'USUARIO ADMIN AUTENTICADO.');

-- Select de la base de datos

-- Vista de informacion de usuarios

CREATE VIEW INFORMACION_USUARIOS
AS
	SELECT 
		U.ID_USUARIO AS "ID DEL USUARIO", 
		U.NOMBRE_LOGIN AS "NOMBRE DE LOGIN DEL USUARIO", 
		U.CONTRASENA AS "CONTRASEÑA DEL USUARIO", 
		U.NOMBRE_USUARIO AS "NOMBRE DEL USUARIO", 
		U.CORREO_ELECTRONICO AS "CORREO ELECTRONICO DEL USUARIO",
		CASE
			WHEN U.ACTIVO = 1 THEN 'ACTIVO'
			ELSE 'INACTIVO'
		END AS "ESTADO DEL USUARIO",
		U.ID_ROL AS "ID ROL",
		R.NOMBRE AS "ROL DEL USUARIO", 
		RM.ID_MENU AS "ID MENU",
		M.NOMBRE AS "NOMBRE DEL MENU"
	FROM USUARIO U 
		 LEFT JOIN ROL_MENU RM ON U.ID_ROL = RM.ID_ROL 
		 LEFT JOIN ROL R ON U.ID_ROL = R.ID_ROL
		 LEFT JOIN MENU M ON RM.ID_MENU = M.ID_MENU;
         
SELECT IU.* FROM INFORMACION_USUARIOS IU;
     
SELECT M.* FROM MENU M ORDER BY M.ID_MENU_PADRE, M.ID_MENU;