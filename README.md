# UTN - Programación III
## Evaluación Parcial 2: Java Persistence API (JPA)

### Datos del Alumno
* **Alumna:** Giardini Silvia
* **Comisión:** 07
* **Materia:** Programación III
* **Carrera:** Tecnicatura Universitaria en Programación a Distancia
* **Link al video de YouTube:** https://youtu.be/vi78eNGTAU0

### Descripción del Proyecto
Este proyecto consiste en una extensión de la aplicación de gestión de pedidos desarrollada sobre un entorno automatizado con Gradle.
El objetivo principal es implementar una arquitectura de persistencia robusta utilizando **JPA (Java Persistence API)**  a través de repositorios genéricos y específicos.
Esto permite la administración completa del ciclo de vida de las entidades `Categoria` y `Producto` mediante operaciones ABM y consultas personalizadas optimizadas en JPQL.

El sistema cuenta con validaciones de negocio (control de precios mayores a cero y stocks no negativos) junto a un mecanismo de **baja lógica**,
resguardando la integridad histórica y referencial de los datos.

---

### 📂 Estructura del Proyecto
Los componentes nuevos y modificados se organizan bajo la siguiente estructura de paquetes dentro del directorio raíz `src/main/java/`:

* `com.gestion.pedidos/`
    * `model/entities/` -> Entidades de negocio autogestionadas por JPA (`Base`, `Categoria`, `Producto`, `Pedido`, `DetallePedido`, `Usuario`).
    * `model/enums/` -> Enumeradores de control de estado (`Estado`, `FormaPago`, `Rol`).
    * `model/interfaces/` -> Interfaces de comportamiento del modelo (`Calculable`).
    * `repository/` -> Capa de acceso a datos y abstracción de persistencia.
        * `BaseRepository.java` -> Clase abstracta genérica con operaciones CRUD seguras y manejo transaccional.
        * `CategoriaRepository.java` -> Repositorio específico para la entidad Categoría.
        * `ProductoRepository.java` -> Repositorio específico para Producto con consultas JPQL tipadas y parámetros nombrados.
    * `util/` -> Contiene `JPAUtil.java` para la gestión centralizada del ciclo de vida del `EntityManagerFactory`.
    * `Main.java` -> Clase principal con el flujo de control, menús interactivos y el módulo de reportes.

---

###  Instrucciones de Ejecución

#### Requisitos Previos
* Java Development Kit (JDK) 21 instalado y configurado.
* Entorno de Desarrollo Integrado (IDE) como *IntelliJ IDEA* o *Eclipse*.

#### Pasos para ejecutar la aplicación desde el IDE:

1. **Importar el proyecto:** Luego de descomprimir el archivo del proyecto, abra su IDE y seleccione la opción de abrir o importar un proyecto existente.
    * Seleccione la carpeta raíz (donde se encuentra el archivo `build.gradle`) para que el entorno lo reconozca y configure automáticamente como un proyecto Gradle válido.
2. **Sincronizar dependencias:** Permita que el IDE finalice la carga, indexación y sincronización inicial de Gradle. Este proceso descargará de forma automática todas las dependencias necesarias.
3. **Ejecutar la aplicación:** * Navegue en el árbol de directorios del IDE hasta la ruta `src/main/java/com/gestion/pedidos/Main.java`.
    * Abra el archivo y ejecute la clase utilizando el **ícono de ejecución nativo (Run)** ubicado en el margen izquierdo de la declaración del método principal, o haga clic derecho sobre el archivo y seleccione la opción **Run 'Main.main()'**.
    * El menú interactivo se desplegará en la consola integrada del IDE.