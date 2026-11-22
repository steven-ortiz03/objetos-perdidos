# Sistema de Objetos Perdidos y Encontrados (beta 0.2)

Aplicación de escritorio en **Java Swing** que centraliza el registro, la consulta y la entrega de los
objetos encontrados en el Instituto Universitario de San Andrés Islas. Proyecto final de Desarrollo de
Software Orientado a Objetos (Parcial 3).

- **Administrador (funcionario):** registra objetos con fotografía, los edita, los elimina, registra
  entregas y consulta el historial.
- **Estudiante:** consulta el catálogo, busca, filtra y ve en qué dependencia puede reclamar su objeto.

**No necesita instalar nada más:** la base de datos es SQLite (un archivo local) y su controlador ya viene
en `ObjetosPerdidos/lib/`. La base se crea sola la primera vez que se abre la aplicación.

## Editar el diseño de las ventanas en NetBeans

Todas las ventanas y pantallas están hechas con el diseñador visual (cada una tiene su `.java` y su `.form`
en `src/objetosperdidos/view`):

| Archivo | Tipo | Qué es |
|---|---|---|
| `FrmLogin` | JFrame | Inicio de sesión |
| `FrmRegistroEstudiante` | JFrame | Registro de estudiante |
| `FrmPrincipal` | JFrame | Ventana principal: menú (`pnlMenu`) y área central (`pnlContenido`) |
| `FrmObjeto` | JFrame | Registrar o editar un objeto |
| `FrmEntrega` | JFrame | Registrar la entrega de un objeto |
| `FrmDetalleObjeto` | JFrame | Ficha de un objeto |
| `PnlInicio`, `PnlCatalogo`, `PnlGestionObjetos`, `PnlHistorial` | JPanel | Pantallas que se muestran dentro de `FrmPrincipal` |

1. Abre el archivo en NetBeans y pulsa **Design** en la parte superior del editor.
2. Arrastra componentes desde la paleta, muévelos o cambia sus propiedades (texto, color, fuente).
   Para programar un botón: clic derecho > *Events* > *Action* > *actionPerformed*.
3. Para ver solo una ventana (JFrame): clic derecho en el archivo > *Run File* (Shift+F6).

No edites la sección gris "Generated Code": la escribe NetBeans. La lógica va fuera de ella.
No cambies el nombre de variable de los componentes existentes (`txtNombre`, `btnGuardar`...), porque el código los usa.
Las tarjetas del catálogo (`TarjetaObjeto`) y el contenido de las tablas se llenan por código.

Nota: el registro de estudiantes usa la **cédula** (6 a 10 dígitos, solo números) como identificador único.
Si ya tenías una base `data/objetos_perdidos.db` creada antes, la aplicación la actualiza sola al abrirse.

## Requisitos

| Herramienta | Versión |
|---|---|
| JDK | 17 o superior |
| Apache NetBeans | 17 o superior (proyecto "Java with Ant") |

## Cómo ejecutarlo

1. En NetBeans: **File > Open Project...** y seleccione la carpeta `ObjetosPerdidos` (la de dentro, la que tiene el ícono de la taza de café).
2. Clic derecho sobre el proyecto > **Clean and Build**.
3. Presione **Run (F6)**. La clase principal es `objetosperdidos.ObjetosPerdidos`.

La primera ejecución crea `ObjetosPerdidos/data/objetos_perdidos.db` con las categorías, los lugares, las
cuentas de demostración y seis objetos de ejemplo (con las fotos `imagenes/demo_*.png`).

**Para volver a empezar de cero:** cierre la aplicación y borre la carpeta `ObjetosPerdidos/data`.

Sin NetBeans, con el `.jar` que deja **Clean and Build**, desde la carpeta `ObjetosPerdidos`:

```bash
java -jar dist/ObjetosPerdidos.jar
```

> Ejecútelo siempre desde la carpeta `ObjetosPerdidos`: ahí busca `data/` e `imagenes/`.

## Cuentas de demostración

| Rol | Correo | Contraseña |
|---|---|---|
| Administrador | admin@iusai.edu.co | Admin123* |
| Estudiante | estudiante@iusai.edu.co | Estudiante123* |

Las contraseñas se guardan como hash PBKDF2 (SHA-256, 65536 iteraciones) con sal aleatoria.
Los estudiantes pueden crear su propia cuenta desde el inicio de sesión (**Regístrate**).

## Cómo probar las funciones principales

| # | Prueba | Pasos | Resultado esperado |
|---|---|---|---|
| 1 | Inicio de sesión incorrecto | Escribir una contraseña errada | "Correo o contraseña incorrectos" |
| 2 | Administrador | Entrar con la cuenta de administrador | Menú: Inicio, Catálogo, Gestión de objetos, Historial |
| 3 | Estudiante | Entrar con la cuenta de estudiante | El menú solo muestra Inicio y Catálogo |
| 4 | Registrar objeto | Gestión de objetos > **+ Nuevo objeto**, llenar, **Seleccionar fotografía**, Guardar | Confirmación; aparece en la tabla y en el catálogo |
| 5 | Validaciones | Guardar con el nombre vacío o sin fotografía | Mensaje del dato que falta; no guarda |
| 6 | Duplicado | Registrar otro objeto con el mismo nombre, lugar y fecha | Advertencia con opción de continuar |
| 7 | Editar | Seleccionar una fila > **Editar**, cambiar un dato, Guardar | La tabla muestra el cambio |
| 8 | Registrar entrega | Seleccionar un objeto disponible > **Registrar entrega** | Pasa a Entregado y aparece en el historial |
| 9 | Eliminar entregado | Seleccionar un objeto Entregado > **Eliminar** | Mensaje: no se puede eliminar |
| 10 | Eliminar disponible | Seleccionar uno Disponible > **Eliminar** > Sí | Desaparece y se borra su fotografía |
| 11 | Catálogo y detalle | Catálogo > clic en una tarjeta | Ficha con foto ampliada y "Reclámalo en..." |
| 12 | Búsqueda y filtros | Escribir "termo", o elegir categoría, lugar, estado o fechas > **Buscar** | Solo las tarjetas que coinciden |
| 13 | Rango de fechas inválido | "Desde" posterior a "hasta" | Mensaje de error; no busca |
| 14 | Registro de estudiante | **Regístrate**, llenar y crear | Se puede iniciar sesión con la cuenta nueva |
| 15 | Persistencia | Cerrar la aplicación y abrirla de nuevo | Los datos siguen ahí |
| 16 | Base no disponible | Hacer de solo lectura la carpeta `data` o abrir la app donde no pueda escribir | Mensaje claro; la app no se cierra |

## Estructura del proyecto

```
objetos-perdidos-main/
├── README.md
└── ObjetosPerdidos/                 Proyecto de NetBeans (Ant)
    ├── build.xml, manifest.mf, nbproject/
    ├── lib/sqlite-jdbc-3.53.2.1.jar  Controlador de la base de datos
    ├── imagenes/                     Fotografías (demo_*.png son de ejemplo)
    ├── data/                         Base de datos SQLite (se crea sola, no se sube a Git)
    └── src/
        ├── db/                       schema.sql, datos_iniciales.sql, datos_prueba.sql
        └── objetosperdidos/
            ├── ObjetosPerdidos.java  Punto de entrada
            ├── model/                Clases del dominio (Usuario, ObjetoPerdido, Entrega...)
            ├── database/             ConexionBD: abre SQLite y crea la base la primera vez
            ├── dao/                  Consultas SQL (PreparedStatement)
            ├── controller/           Validaciones, permisos y reglas del negocio
            ├── view/                 Ventanas y paneles Swing
            └── util/                 Contraseñas, imágenes, validaciones, tema visual, sesión
```

### Arquitectura por capas

`view` → `controller` → `dao` → `database` → SQLite. Las vistas no contienen SQL y los controladores
verifican el rol antes de cada operación de escritura.

### Programación orientada a objetos aplicada

| Concepto | Dónde se aplica |
|---|---|
| Abstracción y herencia | `Usuario` (abstracta) con las subclases `Administrador` y `Estudiante` |
| Polimorfismo | `puedeGestionarObjetos()` responde distinto según el tipo de usuario; el menú y los controladores lo usan sin preguntar por el rol |
| Encapsulamiento | Atributos privados con getters y setters en todo el paquete `model` |
| Interfaz | `Recargable`, implementada por todos los paneles de la ventana principal |
| Excepción propia | `ValidacionException` para los errores que el usuario puede corregir |
| Separación de responsabilidades | Un paquete por capa; cada clase tiene una sola tarea |

## Modelo de datos

Cinco tablas (`usuarios`, `categorias`, `lugares`, `objetos`, `entregas`) definidas en `src/db/schema.sql`,
con claves foráneas `ON DELETE RESTRICT` (un objeto con entrega no se puede borrar) y restricciones `CHECK`
para el rol, el estado y el formato de las fechas. La aplicación activa `PRAGMA foreign_keys = ON` en cada
conexión, porque SQLite trae las claves foráneas apagadas por defecto.

Las fechas se guardan como texto `AAAA-MM-DD`; la fecha de entrega es la del día en que se registra.

## Requisitos cubiertos

Implementados: RF-01 a RF-20 y RNF-01 a RNF-12 del documento de requisitos.

Pendientes (prioridad Baja): RF-21 (administrar categorías y lugares desde la aplicación; por ahora se
agregan en `src/db/datos_iniciales.sql` antes de crear la base) y RF-22 (crear administradores desde la
aplicación; por ahora se agregan en el mismo script).

## Problemas frecuentes

| Síntoma | Causa | Solución |
|---|---|---|
| "No suitable driver found" | NetBeans no ve `sqlite-jdbc` | Clic derecho en **Libraries > Add JAR/Folder...** y elegir `lib/sqlite-jdbc-3.53.2.1.jar` |
| Mensaje "No se encontró el script /db/schema.sql" | Falta compilar | **Clean and Build** (copia `src/db` a la aplicación) |
| Cambié los scripts SQL y no se nota | La base ya existía | Borrar la carpeta `data` y ejecutar de nuevo |
| "database is locked" | Otro programa tiene abierto el `.db` | Cerrar DB Browser u otras herramientas |
| Advertencia `--release 17 is recommended` | El JDK es más nuevo que 17 | Es solo un aviso; se puede ignorar |
| Tildes y eñes dañadas | Codificación distinta | Propiedades del proyecto > Sources > Encoding: UTF-8 |
