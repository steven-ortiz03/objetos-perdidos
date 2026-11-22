-- ============================================================
-- Datos iniciales del sistema
-- Contraseñas de demostración (hash PBKDF2WithHmacSHA256,
-- 65536 iteraciones, clave de 256 bits, sal de 16 bytes, Base64):
--   admin@iusai.edu.co       -> Admin123*
--   estudiante@iusai.edu.co  -> Estudiante123*
-- ============================================================

INSERT INTO categorias (nombre) VALUES
    ('Tecnología'), ('Documentos'), ('Accesorios'),
    ('Ropa'), ('Bolsos y maletas'), ('Otros');

INSERT INTO lugares (nombre) VALUES
    ('Biblioteca'), ('Auditorio'), ('Coordinación'),
    ('Bienestar Institucional'), ('Admisiones');

INSERT INTO usuarios (nombre, correo, cedula, password_hash, salt, rol) VALUES
    ('Administrador', 'admin@iusai.edu.co', '1000000001',
     'X8vUiWtHHx+d990eTCSkxvZTNwbouYuIIqOA90x80FQ=', 'SnHmHB5aj4WC5YAAo/Hhhg==', 'ADMINISTRADOR'),
    ('Estudiante', 'estudiante@iusai.edu.co', '1000000002',
     'deuV6ZFZ0wffUh0XWDZOrTF9JNt0KrB7jYZ+iyGr10k=', '1avraQvTx4g/McozDne3BQ==', 'ESTUDIANTE');
