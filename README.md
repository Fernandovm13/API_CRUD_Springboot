# Investigación e Implementación de Gestión de Usuarios y Autenticación Básica con Spring Boot

## Parte 1: Investigación Teórica

### 1. Arquitectura en Capas
¿Cuál es la responsabilidad concreta de las capas Controller, Service, Repository y Entity en un flujo de usuarios?

- **Entity (Entidad)**: Representa el modelo de dominio y la estructura de datos que se mapea con la tabla correspondiente en la base de datos (mediante JPA/Hibernate). Contiene los atributos del usuario (id, username, email, password, roles, etc.) y anotaciones de mapeo relacional.
- **Repository (Repositorio)**: Interfaz que extiende de `JpaRepository` o `CrudRepository`. Se encarga de la abstracción y el acceso a los datos, ejecutando operaciones de persistencia en la base de datos (SQL queries, `save`, `findById`, `delete`, etc.) sin contener lógica de negocio.
- **Service (Servicio)**: Contiene la lógica de negocio central de la aplicación. Es responsable de validar datos de entrada, aplicar hashing a las contraseñas, verificar credenciales en el inicio de sesión, coordinar transacciones de base de datos y transformar objetos entre DTOs y Entidades.
- **Controller (Controlador)**: Maneja la capa de interacción HTTP/REST. Expone los endpoints de la API, recibe y valida las solicitudes HTTP (`@RequestBody`, `@PathVariable`), invoca a la capa Service para procesar la petición y retorna una respuesta HTTP (`ResponseEntity`) con el código de estado adecuado y el cuerpo de respuesta JSON.

---

### 2. Manejo de Contraseñas

#### ¿Por qué nunca se deben guardar contraseñas en texto plano en la base de datos?
Guardar contraseñas en texto plano vulnera críticamente la seguridad del sistema. En caso de un incidente de seguridad (filtración de base de datos, inyección SQL, backups expuestos o acceso no autorizado por personal interno), los atacantes obtendrían acceso inmediato a las credenciales de todos los usuarios. Además, debido a que muchos usuarios reutilizan contraseñas en diferentes servicios web, una filtración pondría en riesgo sus cuentas en otras plataformas externas.

#### ¿Qué función cumple un algoritmo de hashing como BCrypt?
- **Función Unidireccional (One-Way Hash)**: Transforma una contraseña de texto plano en un resumen criptográfico (hash) de forma irreversible, imposibilitando reconstruir la contraseña original a partir del hash.
- **Inclusión de Sal Aleatoria (Salt)**: BCrypt genera e incorpora automáticamente una cadena aleatoria única (sal) para cada hash. Esto garantiza que dos usuarios con la misma contraseña tengan hashes completamente diferentes, previniendo ataques mediante Tablas Arcoíris (Rainbow Tables).
- **Factor de Trabajo Ajustable (Cost Factor)**: Permite aumentar o disminuir deliberadamente el costo computacional necesario para calcular el hash, ralentizando significativamente los ataques de fuerza bruta y diccionario.

---

### 3. Lógica de Autenticación Básica

#### ¿Cuál es la diferencia conceptual entre el proceso de Registro (crear usuario) y el de Login (verificar credenciales)?

- **Proceso de Registro (Creación de Identidad)**:
  - **Objetivo**: Crear una nueva cuenta de usuario en el sistema.
  - **Flujo**: Recibe los datos del usuario, valida que el nombre de usuario/email no existan previamente, aplica el algoritmo de hashing (BCrypt) a la contraseña recibida y persiste la nueva entidad de usuario en la base de datos.
- **Proceso de Login (Verificación de Credenciales)**:
  - **Objetivo**: Autenticar y confirmar que la persona que intenta ingresar posee las credenciales correctas de una cuenta ya existente.
  - **Flujo**: Recibe las credenciales (nombre de usuario/email y contraseña en texto plano), busca al usuario en la base de datos, compara la contraseña enviada contra el hash almacenado utilizando `PasswordEncoder.matches()`, y concede el acceso (o genera la sesión/token) si coinciden, o devuelve un error de autenticación si no coinciden.

---

### 4. Buenas Prácticas REST

Definición de verbos HTTP y códigos de estado recomendados para el módulo de gestión de usuarios y autenticación:

| Endpoint | Verbo HTTP | Descripción del Endpoint | Código Éxito | Códigos de Error / Excepción |
| :--- | :---: | :--- | :---: | :--- |
| `/api/auth/register` | `POST` | Registrar un nuevo usuario en la plataforma. | `201 Created` | `400 Bad Request` (Datos inválidos o usuario ya existente) |
| `/api/auth/login` | `POST` | Autenticar usuario y verificar credenciales. | `200 OK` | `401 Unauthorized` (Credenciales inválidas) <br> `400 Bad Request` |
| `/api/users` | `GET` | Obtener el listado general de usuarios. | `200 OK` | `401 Unauthorized` |
| `/api/users/{id}` | `GET` | Consultar la información de un usuario específico. | `200 OK` | `404 Not Found` (Usuario no existe) <br> `401 Unauthorized` |
| `/api/users/{id}` | `PUT` | Actualizar la información de un usuario existente. | `200 OK` | `400 Bad Request` <br> `404 Not Found` <br> `401 Unauthorized` |
| `/api/users/{id}` | `DELETE` | Eliminar un usuario del sistema. | `200 OK` / `204 No Content` | `404 Not Found` <br> `401 Unauthorized` |
