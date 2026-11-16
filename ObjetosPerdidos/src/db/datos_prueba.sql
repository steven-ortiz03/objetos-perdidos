-- ============================================================
-- Datos de prueba: objetos de ejemplo y una entrega
-- Las fotografías son imágenes de ejemplo en la carpeta imagenes/
-- ============================================================

INSERT INTO objetos (nombre, descripcion, ruta_foto, fecha_encontrado, estado,
                     id_categoria, id_lugar_encontrado, id_lugar_resguardo, id_registrado_por)
VALUES
('Audífonos inalámbricos',
 'Audífonos negros con estuche de carga, marca sin identificar.',
 'imagenes/demo_1.png', '2026-11-09', 'DISPONIBLE',
 (SELECT id_categoria FROM categorias WHERE nombre = 'Tecnología'),
 (SELECT id_lugar FROM lugares WHERE nombre = 'Biblioteca'),
 (SELECT id_lugar FROM lugares WHERE nombre = 'Coordinación'),
 (SELECT id_usuario FROM usuarios WHERE correo = 'admin@iusai.edu.co')),

('Carné estudiantil',
 'Carné con fotografía, sin cordón. Se entrega solo al titular.',
 'imagenes/demo_2.png', '2026-11-10', 'DISPONIBLE',
 (SELECT id_categoria FROM categorias WHERE nombre = 'Documentos'),
 (SELECT id_lugar FROM lugares WHERE nombre = 'Auditorio'),
 (SELECT id_lugar FROM lugares WHERE nombre = 'Admisiones'),
 (SELECT id_usuario FROM usuarios WHERE correo = 'admin@iusai.edu.co')),

('Termo azul',
 'Termo de acero inoxidable azul, con una calcomanía en la tapa.',
 'imagenes/demo_3.png', '2026-11-05', 'DISPONIBLE',
 (SELECT id_categoria FROM categorias WHERE nombre = 'Accesorios'),
 (SELECT id_lugar FROM lugares WHERE nombre = 'Biblioteca'),
 (SELECT id_lugar FROM lugares WHERE nombre = 'Bienestar Institucional'),
 (SELECT id_usuario FROM usuarios WHERE correo = 'admin@iusai.edu.co')),

('Chaqueta gris',
 'Chaqueta deportiva gris con capucha, talla M.',
 'imagenes/demo_4.png', '2026-11-03', 'DISPONIBLE',
 (SELECT id_categoria FROM categorias WHERE nombre = 'Ropa'),
 (SELECT id_lugar FROM lugares WHERE nombre = 'Auditorio'),
 (SELECT id_lugar FROM lugares WHERE nombre = 'Bienestar Institucional'),
 (SELECT id_usuario FROM usuarios WHERE correo = 'admin@iusai.edu.co')),

('Morral negro',
 'Morral negro con cremallera roja y un llavero de tela.',
 'imagenes/demo_5.png', '2026-11-12', 'DISPONIBLE',
 (SELECT id_categoria FROM categorias WHERE nombre = 'Bolsos y maletas'),
 (SELECT id_lugar FROM lugares WHERE nombre = 'Biblioteca'),
 (SELECT id_lugar FROM lugares WHERE nombre = 'Coordinación'),
 (SELECT id_usuario FROM usuarios WHERE correo = 'admin@iusai.edu.co')),

('Calculadora científica',
 'Calculadora científica gris con las iniciales L.M. en la parte trasera.',
 'imagenes/demo_6.png', '2026-10-28', 'ENTREGADO',
 (SELECT id_categoria FROM categorias WHERE nombre = 'Tecnología'),
 (SELECT id_lugar FROM lugares WHERE nombre = 'Auditorio'),
 (SELECT id_lugar FROM lugares WHERE nombre = 'Coordinación'),
 (SELECT id_usuario FROM usuarios WHERE correo = 'admin@iusai.edu.co'));

INSERT INTO entregas (id_objeto, nombre_receptor, documento_receptor, fecha_entrega,
                      observaciones, id_funcionario)
VALUES
((SELECT id_objeto FROM objetos WHERE nombre = 'Calculadora científica'),
 'Laura Martínez', '1012345678', '2026-11-02',
 'Verificó las iniciales de la calculadora.',
 (SELECT id_usuario FROM usuarios WHERE correo = 'admin@iusai.edu.co'));
