# UTN - Programación III
## Trabajo Práctico Integrador (TPI): Food Store
### Sistema de Gestión de Pedidos

### Datos del Alumno
* **Alumna:** Giardini Silvia
* **Comisión:** 07
* **Materia:** Programación III
* **Carrera:** Tecnicatura Universitaria en Programación a Distancia
* **Link al video de YouTube:** 

### Descripción del Proyecto

### 📝 Descripción del Proyecto

Este proyecto constituye la capa de backend y persistencia del Sistema de Gestión de Pedidos Food Store.
Desarrollado en Java 21 bajo el entorno de Gradle, utiliza JPA (Java Persistence API) junto con Hibernate y una base de datos relacional H2 en modo archivo local.

El objetivo principal es implementar una arquitectura de datos robusta a través de repositorios genéricos y específicos para la administración completa del ciclo de vida de las entidades
(Categoria, Producto, Usuario y Pedido). La interacción con el sistema se realiza mediante un menú interactivo por consola que ejecuta la lógica de negocio,
valida las reglas comerciales y controla los procesos transaccionales del establecimiento.

---

### 📂 Estructura del Proyecto
Los componentes nuevos y modificados se organizan bajo la siguiente estructura de paquetes dentro del directorio raíz `src/main/java/`:

* `com.tp.jpa/`
    * `model/entities/` -> Entidades de dominio gestionadas por JPA.
        * `Base.java` -> Superclase anotada con `@MappedSuperclass` para las herencias.
        * `Categoria.java` -> Entidad para la clasificación del catálogo.
        * `Producto.java` -> Entidad con atributos de precio, stock y disponibilidad.
        * `Usuario.java` -> Entidad de clientes y administradores con validación de mail único.
        * `Pedido.java` -> Clase que implementa `Calculable` para orquestar la suma de costes.
        * `DetallePedido.java` -> Línea de compra unidireccional dependiente del pedido.
    * `model/enums/` -> Enumeradores de control tipado.
        * `Rol.java` -> Roles de acceso integrados (`ADMIN`, `USUARIO`).
        * `Estado.java` -> Ciclo de vida del pedido (`PENDIENTE`, `CONFIRMADO`, `TERMINADO`, `CANCELADO`).
        * `FormaPago.java` -> `TARJETA`, `TRANSFERENCIA`, `EFECTIVO`.
    * `model/interfaces/` -> Contratos funcionales.
        * `Calculable.java` -> Interfaz para el cálculo de totales sobre colecciones.
    * `repository/` 
        * `BaseRepository.java` -> Operaciones ABM genéricas y comunes para todas las entidades.
        * `CategoriaRepository.java` -> Búsqueda de productos activos por categoría usando JOIN
        * `ProductoRepository.java` -> Gestión y almacenamiento del catálogo de productos.
        * `UsuarioRepository.java` -> Validación de mails únicos y consulta de sus pedidos asociados.
        * `PedidoRepository.java` -> Filtrado y consulta de pedidos según su estado.
    * `util/` -> Herramientas compartidas.
        * `JPAUtil.java` -> Gestión centralizada de las conexiones (EntityManagerFactory).
    * `Main.java` -> Entrada del programa. Coordina la inicialización de repositorios y renderiza las pantallas de la consola.

---

###  Instrucciones de Ejecución

#### Requisitos Previos
* Java Development Kit (JDK) 21 instalado y configurado.
* **IDE:** Compatible con Gradle, de preferencia *IntelliJ IDEA* o *Eclipse*.
* 
#### Pasos para ejecutar la aplicación desde el IDE:

1. **Importar el proyecto:** Luego de descomprimir el archivo del proyecto, abra su IDE y seleccione la opción de abrir o importar un proyecto existente.
    * Seleccione la carpeta raíz (donde se encuentra el archivo `build.gradle`) para que el entorno lo reconozca y configure automáticamente como un proyecto Gradle válido.
2. **Sincronizar dependencias:** Permita que el IDE finalice la carga, indexación y sincronización inicial de Gradle. Este proceso descargará de forma automática todas las dependencias necesarias.
3. **Ejecutar la aplicación:** * Navegue en el árbol de directorios del IDE hasta la ruta `src/main/java/com/gestion/pedidos/Main.java`.
    * Abra el archivo y ejecute la clase utilizando el **ícono de ejecución nativo (Run)** ubicado en el margen izquierdo de la declaración del método principal, o haga clic derecho sobre el archivo y seleccione la opción **Run 'Main.main()'**.
    * El menú interactivo se desplegará en la consola integrada del IDE.