# Poo_U2._Proyecto
# Sistema Gestor de Estudiantes y Calificaciones de Unidades
## Descripción del Proyecto

Este proyecto es un sistema de gestión de estudiantes y sus calificaciones por unidades, desarrollado en Java utilizando la librería Swing para la interfaz gráfica de usuario. Permite registrar nuevos estudiantes con sus respectivas materias y calificaciones por unidad, consultar la información detallada de cada estudiante, actualizar calificaciones de unidades específicas y visualizar un listado general de todos los estudiantes registrados.

El sistema calcula promedios por materia y un promedio general del estudiante, determinando automáticamente si aplica a algún tipo de beca (Excelencia, Mérito Académico, Estímulo) o si su promedio es reprobatorio. Incluye validaciones en tiempo real para la entrada de datos, asegurando la calidad y coherencia de la información.

---

## Integrantes del Equipo

* **Joshua André Alvarado Tovar** - 2330242
* **Luis Manuel Anaya Maldonado** - 2330267
* **Gibran Emmanuel García Cervantes** - 2330219

---

## Características Principales

* **Registro de Estudiantes:** Permite añadir nuevos estudiantes con su nombre y matrícula única.
* **Gestión de Materias y Unidades:** Asigna hasta 5 materias por estudiante, y cada materia puede tener múltiples unidades con sus respectivas calificaciones.
* **Validación de Entrada:** Campos de texto con filtros que restringen la entrada a caracteres válidos (letras/espacios para nombres, números para matrículas y calificaciones).
* **Consulta Detallada:** Busca estudiantes por matrícula y muestra un desglose completo de sus materias, unidades, calificaciones y promedios.
* **Actualización de Calificaciones:** Posibilidad de modificar las calificaciones de unidades específicas de un estudiante.
* **Cálculo de Promedios:** Calcula el promedio de cada materia y el promedio general del estudiante.
* **Asignación de Becas:** Determina automáticamente el tipo de beca según el promedio general del estudiante (Excelencia, Mérito Académico, Estímulo) o indica si no aplica o si el promedio es reprobatorio.
* **Listado General:** Genera un informe con la información de todos los estudiantes registrados, sus promedios y estado (aprobado/reprobado).
* **Interfaz Gráfica Intuitiva:** Desarrollado con Java Swing, utilizando el Look and Feel "Nimbus" para una experiencia de usuario moderna y agradable.

---

## Estructura del Proyecto

El proyecto sigue un diseño basado en clases que modelan las entidades principales del sistema:

* `Unidad`: Representa una unidad de una materia con su nombre y calificación.
* `Materia`: Contiene un nombre y una lista de `Unidad`es. Calcula el promedio de sus unidades.
* `Estudiante`: Almacena el nombre, matrícula y una lista de `Materia`s. Gestiona la adición y actualización de materias/calificaciones, calcula el promedio general y determina el tipo de beca.
* `Proyecto`: Contiene el método `main` y toda la lógica de la interfaz gráfica (Swing), manejando las interacciones del usuario y la manipulación de los datos.

---

## Tecnologías Utilizadas

* **Lenguaje de Programación:** Java
* **Interfaz de Usuario:** Java Swing
* **Entorno de Desarrollo (IDE):** IntelliJ IDEA

---

## Instalación y Ejecución

Para compilar y ejecutar este proyecto, necesitas tener instalado el Kit de Desarrollo de Java (JDK). Se recomienda JDK 8 o superior.

1.  **Clona el repositorio (o descarga el archivo `Proyecto.java`):**

    ```bash
    git clone [https://github.com/tu_usuario/tu_repositorio.git](https://github.com/tu_usuario/tu_repositorio.git)
    cd tu_repositorio
    ```
    (Reemplaza `tu_usuario/tu_repositorio` con la URL real de tu repositorio si lo subes a GitHub).

2.  **Compila el código fuente:**
    Abre una terminal en el directorio donde se encuentra `Proyecto.java` y ejecuta:

    ```bash
    javac Proyecto.java
    ```

3.  **Ejecuta la aplicación:**
    Después de la compilación exitosa, ejecuta la aplicación con:

    ```bash
    java Proyecto
    ```

### Ejecución desde un IDE

Si utilizas un IDE como IntelliJ IDEA, Eclipse o NetBeans:
1.  Importa el proyecto como un nuevo proyecto Java existente.
2.  Asegúrate de que el JDK esté configurado correctamente.
3.  Ejecuta la clase `Proyecto` (contiene el método `main`).

---

## Uso del Sistema

Al iniciar la aplicación, verás una ventana con tres pestañas principales:

### 1. Pestaña "Registro"

* **Nombre del Estudiante:** Ingresa el nombre completo del estudiante.
* **Matrícula:** Ingresa la matrícula del estudiante (solo números).
* **Agregar materia:** Haz clic en este botón para añadir un nuevo bloque de materia.
    * Dentro de cada bloque de materia, ingresa el nombre de la materia.
    * **Agregar unidad:** Dentro de cada materia, haz clic para añadir una unidad. Ingresa el nombre de la unidad y su calificación (0-100).
* **Registrar Estudiante:** Una vez completados los datos, haz clic para guardar el estudiante en el sistema. Se mostrará un resumen del registro en la pestaña de consultas.

### 2. Pestaña "Consultas y actualizaciones"

* **Búsqueda de estudiantes:**
    * **Matrícula:** Ingresa la matrícula del estudiante que deseas buscar.
    * **Buscar por Matrícula:** Haz clic para mostrar la información detallada del estudiante en el área de resultados.
* **Actualizaciones de calificaciones:**
    * Si se encontró un estudiante, los `ComboBox` de "Materia" y "Unidad" se poblarán con sus datos.
    * Selecciona la **Materia** y la **Unidad** que deseas actualizar.
    * **Nueva calificación:** Ingresa la nueva calificación (0-100).
    * **Actualizar Calificación:** Haz clic para aplicar la nueva calificación. El área de resultados se actualizará con los nuevos promedios.

### 3. Pestaña "Listado general"

* **Actualizar listado de estudiantes:** Haz clic en este botón para ver un listado completo de todos los estudiantes registrados en el sistema, incluyendo sus promedios y becas.
