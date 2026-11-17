-- ============================================================
-- Sistema de Objetos Perdidos y Encontrados
-- Modelo físico para SQLite
-- Nota: la aplicación debe ejecutar PRAGMA foreign_keys = ON
-- en cada conexión para que se apliquen las claves foráneas.
-- ============================================================

PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario           INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre               TEXT    NOT NULL,
    correo               TEXT    NOT NULL UNIQUE,
    cedula TEXT    NOT NULL UNIQUE,
    password_hash        TEXT    NOT NULL,
    salt                 TEXT    NOT NULL,
    rol                  TEXT    NOT NULL
                         CHECK (rol IN ('ADMINISTRADOR', 'ESTUDIANTE')),
    activo               INTEGER NOT NULL DEFAULT 1
                         CHECK (activo IN (0, 1)),
    fecha_registro       TEXT    NOT NULL DEFAULT (date('now'))
);

CREATE TABLE IF NOT EXISTS categorias (
    id_categoria INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre       TEXT    NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS lugares (
    id_lugar INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre   TEXT    NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS objetos (
    id_objeto           INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre              TEXT    NOT NULL,
    descripcion         TEXT    NOT NULL,
    ruta_foto           TEXT    NOT NULL,
    fecha_encontrado    TEXT    NOT NULL
                        CHECK (fecha_encontrado GLOB '[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]'),
    estado              TEXT    NOT NULL DEFAULT 'DISPONIBLE'
                        CHECK (estado IN ('DISPONIBLE', 'ENTREGADO')),
    id_categoria        INTEGER NOT NULL,
    id_lugar_encontrado INTEGER NOT NULL,
    id_lugar_resguardo  INTEGER NOT NULL,
    id_registrado_por   INTEGER NOT NULL,
    FOREIGN KEY (id_categoria)        REFERENCES categorias (id_categoria) ON DELETE RESTRICT,
    FOREIGN KEY (id_lugar_encontrado) REFERENCES lugares (id_lugar)        ON DELETE RESTRICT,
    FOREIGN KEY (id_lugar_resguardo)  REFERENCES lugares (id_lugar)        ON DELETE RESTRICT,
    FOREIGN KEY (id_registrado_por)   REFERENCES usuarios (id_usuario)     ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS entregas (
    id_entrega          INTEGER PRIMARY KEY AUTOINCREMENT,
    id_objeto           INTEGER NOT NULL UNIQUE,
    nombre_receptor     TEXT    NOT NULL,
    documento_receptor  TEXT    NOT NULL,
    fecha_entrega       TEXT    NOT NULL
                        CHECK (fecha_entrega GLOB '[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]'),
    observaciones       TEXT,
    id_funcionario      INTEGER NOT NULL,
    FOREIGN KEY (id_objeto)      REFERENCES objetos (id_objeto)    ON DELETE RESTRICT,
    FOREIGN KEY (id_funcionario) REFERENCES usuarios (id_usuario)  ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_objetos_estado    ON objetos (estado);
CREATE INDEX IF NOT EXISTS idx_objetos_fecha     ON objetos (fecha_encontrado);
CREATE INDEX IF NOT EXISTS idx_objetos_categoria ON objetos (id_categoria);
