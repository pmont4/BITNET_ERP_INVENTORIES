CREATE DATABASE DB_BITNET_ERP;
USE DB_BITNET_ERP;

-- Creation of the responsible user for database DB_BITNET_ERP management
CREATE USER 'user_bitnet_erp'@'%' IDENTIFIED BY 'bitnet123';
GRANT ALL ON  DB_BITNET_ERP.* TO 'user_bitnet_erp'@'%';

-- Tables creation

CREATE TABLE IF NOT EXISTS MENU_TYPE(
	ID_MENU_TYPE BIGINT NOT NULL,
    NAME VARCHAR(50) NOT NULL,
    CONSTRAINT PK_MENU_TYPE PRIMARY KEY(ID_MENU_TYPE),
    CONSTRAINT IDX_MENU_TYPE_1 UNIQUE (NAME)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

CREATE TABLE IF NOT EXISTS MENU(
	ID_MENU BIGINT NOT NULL,
    NAME VARCHAR(50) NOT NULL,
    ID_MENU_TYPE BIGINT NOT NULL,
    ID_PARENT_MENU BIGINT,
    CONSTRAINT PK_MENU PRIMARY KEY(ID_MENU),
    CONSTRAINT FK_MENU_1 FOREIGN KEY(ID_PARENT_MENU) REFERENCES MENU(ID_MENU),
    CONSTRAINT FK_MENU_2 FOREIGN KEY(ID_MENU_TYPE) REFERENCES MENU_TYPE(ID_MENU_TYPE),
    CONSTRAINT IDX_MENU_1 UNIQUE (NAME)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

CREATE TABLE IF NOT EXISTS ROLE(
	ID_ROLE BIGINT NOT NULL,
    NAME VARCHAR(50) NOT NULL,
    CONSTRAINT PK_ROL PRIMARY KEY(ID_ROLE),
    CONSTRAINT IDX_ROL_1 UNIQUE (NAME)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

CREATE TABLE IF NOT EXISTS MENU_ROLE(
	ID_ROLE BIGINT NOT NULL,
    ID_MENU BIGINT NOT NULL,
    CONSTRAINT PK_MENU_ROLE PRIMARY KEY(ID_ROLE, ID_MENU),
    CONSTRAINT FK_MENU_ROLE_1 FOREIGN KEY(ID_ROLE) REFERENCES ROLE(ID_ROLE),
    CONSTRAINT FK_MENU_ROLE_2 FOREIGN KEY(ID_MENU) REFERENCES MENU(ID_MENU)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

CREATE TABLE IF NOT EXISTS USER(
	ID_USER BIGINT NOT NULL,
    LOGIN_NAME VARCHAR(50) NOT NULL,
    PASSWORD VARCHAR(500) NOT NULL,
    USERNAME VARCHAR(200) NOT NULL,
    EMAIL VARCHAR(200) NOT NULL,
    ID_ROLE BIGINT NOT NULL,
    STATUS TINYINT NOT NULL,
    CONSTRAINT PK_USER PRIMARY KEY(ID_USER),
    CONSTRAINT IDX_USER_1 UNIQUE(LOGIN_NAME),
    CONSTRAINT FK_USER_1 FOREIGN KEY(ID_ROLE) REFERENCES ROLE(ID_ROLE)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

CREATE TABLE IF NOT EXISTS EVENT_TYPE(
	ID_EVENT_TYPE BIGINT NOT NULL,
    NAME VARCHAR(50) NOT NULL,
    CONSTRAINT PK_EVENT_TYPE PRIMARY KEY(ID_EVENT_TYPE),
    CONSTRAINT IDX_EVENT_TYPE_1 UNIQUE (NAME)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

CREATE TABLE IF NOT EXISTS EVENT (
	ID_EVENT BIGINT NOT NULL,
    ID_EVENT_TYPE BIGINT NOT NULL,
    ID_USER BIGINT NOT NULL,
    LOG_DATE TIMESTAMP NOT NULL,
    DESCRIPTION TEXT NOT NULL,
    CONSTRAINT PK_EVENT PRIMARY KEY(ID_EVENT),
    CONSTRAINT FK_EVENT_1 FOREIGN KEY(ID_EVENT_TYPE) REFERENCES EVENT_TYPE(ID_EVENT_TYPE),
    CONSTRAINT FK_EVENT_2 FOREIGN KEY(ID_USER) REFERENCES USER(ID_USER)
) ENGINE = INNODB DEFAULT CHARACTER SET = UTF8MB4;

-- Data inserts into tables

INSERT INTO MENU_TYPE(ID_MENU_TYPE, NAME) VALUES (1, 'MENU');
INSERT INTO MENU_TYPE(ID_MENU_TYPE, NAME) VALUES (2, 'MENU_ITEM');

INSERT INTO MENU(ID_MENU, NAME, ID_MENU_TYPE, ID_PARENT_MENU) VALUES (1, 'ARCHIVO', 1, NULL);
INSERT INTO MENU(ID_MENU, NAME, ID_MENU_TYPE, ID_PARENT_MENU) VALUES (2, 'MENU', 2, 1);
INSERT INTO MENU(ID_MENU, NAME, ID_MENU_TYPE, ID_PARENT_MENU) VALUES (3, 'ROL', 2, 1);
INSERT INTO MENU(ID_MENU, NAME, ID_MENU_TYPE, ID_PARENT_MENU) VALUES (4, 'ROL-MENU', 2, 1);
INSERT INTO MENU(ID_MENU, NAME, ID_MENU_TYPE, ID_PARENT_MENU) VALUES (5, 'USUARIO', 2, 1);
INSERT INTO MENU(ID_MENU, NAME, ID_MENU_TYPE, ID_PARENT_MENU) VALUES (6, 'CERRAR SESION', 2, 1);

INSERT INTO MENU_ROLE(ID_ROLE, ID_MENU) VALUES (1, 1);
INSERT INTO MENU_ROLE(ID_ROLE, ID_MENU) VALUES (1, 2);
INSERT INTO MENU_ROLE(ID_ROLE, ID_MENU) VALUES (1, 3);
INSERT INTO MENU_ROLE(ID_ROLE, ID_MENU) VALUES (1, 4);
INSERT INTO MENU_ROLE(ID_ROLE, ID_MENU) VALUES (1, 5);
INSERT INTO MENU_ROLE(ID_ROLE, ID_MENU) VALUES (1, 6);

INSERT INTO ROLE(ID_ROLE, NAME) VALUES (1, 'Administrador');

INSERT INTO USER(ID_USER, LOGIN_NAME, PASSWORD, USERNAME, EMAIL, ID_ROLE, STATUS) VALUES (1, 'Admin', 'admin123', 'Administrador', 'pauloamont2005@outlook.com', 1, 1);

INSERT INTO EVENT_TYPE(ID_EVENT_TYPE, NAME) VALUES (1, 'LOGIN');

INSERT INTO EVENT(ID_EVENT, ID_EVENT_TYPE, ID_USER, LOG_DATE, DESCRIPTION) VALUES (1, 1, 1, CURRENT_TIMESTAMP, 'USUARIO ADMIN AUTENTICADO.');

-- Show table date

-- User information view

CREATE VIEW USER_INFORMATION
AS
	SELECT 
		U.ID_USER AS "ID DEL USUARIO", 
		U.LOGIN_NAME AS "NOMBRE DE LOGIN DEL USUARIO", 
		U.PASSWORD AS "CONTRASEÑA DEL USUARIO", 
		U.USERNAME AS "NOMBRE DEL USUARIO", 
		U.EMAIL AS "CORREO ELECTRONICO DEL USUARIO",
		CASE
			WHEN U.STATUS = 1 THEN 'ACTIVO'
			ELSE 'INACTIVO'
		END AS "ESTADO DEL USUARIO",
		U.ID_ROLE AS "ID ROL",
		R.NAME AS "ROL DEL USUARIO", 
		MR.ID_MENU AS "ID MENU",
		M.NAME AS "NOMBRE DEL MENU"
	FROM USER U 
		 LEFT JOIN MENU_ROLE MR ON U.ID_ROLE = MR.ID_ROLE
		 LEFT JOIN ROLE R ON U.ID_ROLE = R.ID_ROLE
		 LEFT JOIN MENU M ON MR.ID_MENU = M.ID_MENU;
         
SELECT UI.* FROM USER_INFORMATION UI;
     
SELECT M.* FROM MENU M ORDER BY M.ID_PARENT_MENU, M.ID_MENU;

SELECT M.ID_MENU, M.NAME, M.ID_MENU_TYPE, MT.NAME, M.ID_PARENT_MENU
FROM MENU M
LEFT JOIN MENU_TYPE MT ON M.ID_MENU_TYPE = MT.ID_MENU_TYPE;

SELECT
    U.ID_USER,
    U.LOGIN_NAME,
    REPEAT('*', LENGTH(U.PASSWORD)) AS "Password",
    U.USERNAME,
    U.EMAIL,
    U.ID_ROLE,
    CASE
        WHEN U.STATUS = 1 THEN 'ONLINE'
        ELSE 'OFFLINE'
    END AS Status
FROM USER U;
