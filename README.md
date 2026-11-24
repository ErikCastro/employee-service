# Employee Service API

API REST para la gestión de empleados, desarrollada con Java 17 y Spring
Boot 2.7.18. Utiliza Spring Data JPA, Hibernate y MySQL para la
persistencia de datos.

## 1. Tecnologías

-   Java 17
-   Spring Boot 2.7.18
-   Maven
-   Spring Web
-   Spring Data JPA / Hibernate
-   MySQL
-   Bean Validation
-   Jackson
-   Springdoc OpenAPI / Swagger
-   JUnit 5 y Mockito

## 2. Funcionalidades

La API implementa las siguientes operaciones:

-   Consultar todos los empleados.
-   Consultar un empleado por su identificador.
-   Buscar empleados por coincidencia parcial en nombres y apellidos.
-   Registrar un empleado.
-   Registrar varios empleados mediante una sola petición.
-   Actualizar un empleado.
-   Eliminar un empleado.

También incluye:

-   Validación de datos al registrar empleados.
-   Manejo centralizado de excepciones.
-   Respuestas HTTP según el resultado de cada operación.
-   Registro de eventos de la aplicación y consultas SQL.
-   Documentación interactiva con Swagger.
-   Pruebas automatizadas con JUnit 5, Mockito y MockMvc.

## 3. Modelo de empleado

El recurso Employee contiene los siguientes campos:

  Campo              		Tipo            Descripción
  ------------------------------------------------------------------------------
  `id`               		Long            Identificador generado por la base de datos
  `firstName`        	String          Nombre
  `middleName`       	String          Segundo nombre (opcional)
  `lastName`         	 	String          Apellido
  `secondLastName`  	String          Segundo apellido (opcional)
  `age`              		Integer         Edad
  `gender`           		String          Sexo
  `birthDate`        	LocalDate       Fecha de nacimiento
  `position`         		String          Puesto
  `createdAt`        	LocalDateTime   Fecha y hora de registro
  `active`           		boolean         Estado del empleado

Al crear un empleado, el servicio asigna automáticamente `createdAt` y
establece `active` en `true`. El identificador se genera mediante la
estrategia `IDENTITY` de JPA.

La fecha de nacimiento se recibe en formato `dd-MM-yyyy`.

## 4. Configuración de MySQL

### Crear la base de datos

Ejecutar en MySQL:

``` sql
CREATE DATABASE employee_db;
```

### Configurar la conexión

Editar `src/main/resources/application.properties` y configurar la URL,
el usuario y la contraseña correspondientes a la instalación local de
MySQL:

``` properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

`spring.jpa.hibernate.ddl-auto=update` permite que Hibernate actualice
el esquema de la base de datos durante el desarrollo.

## 5. Requisitos y ejecución

### Requisitos previos

-   JDK 17.
-   MySQL en ejecución.
-   Base de datos `employee_db` creada.
-   Maven instalado.

### Compilar y ejecutar pruebas

Desde la carpeta raíz del proyecto:

``` bash
mvn clean test
```

### Iniciar la aplicación

``` bash
mvn spring-boot:run
```

También se puede importar el proyecto como **Existing Maven Project**
desde Eclipse y ejecutar la clase `EmployeeServiceApplication`.

La API queda disponible en:

``` text
http://localhost:8080
```

## 6. Endpoints

La ruta base es `/employees`.

  ---------------------------------------------------------------------------------
  Método                  Endpoint                          Descripción
  ----------------------- --------------------------------- -----------------------
  `POST`                  `/employees`                      Crea un empleado o una
                                                            lista de empleados

  `GET`                   `/employees`                      Consulta todos los
                                                            empleados

  `GET`                   `/employees/{id}`                 Consulta un empleado
                                                            por ID

  `GET`                   `/employees/search?name={name}`   Busca por coincidencia
                                                            parcial en nombres y
                                                            apellidos

  `PUT`                   `/employees/{id}`                 Actualiza los datos de
                                                            un empleado

  `DELETE`                `/employees/{id}`                 Elimina un empleado
  ---------------------------------------------------------------------------------

### Crear un empleado

`POST /employees`

``` json
{
  "firstName": "Juan",
  "middleName": "Carlos",
  "lastName": "Perez",
  "secondLastName": "Lopez",
  "age": 30,
  "gender": "Male",
  "birthDate": "15-06-1996",
  "position": "Java Developer"
}
```

### Crear varios empleados

`POST /employees`

Enviar un arreglo JSON con objetos que tengan la misma estructura que el
ejemplo anterior:

``` json
[
  {
    "firstName": "Juan",
    "lastName": "Perez",
    "age": 30,
    "gender": "Male",
    "birthDate": "15-06-1996",
    "position": "Java Developer"
  },
  {
    "firstName": "Maria",
    "lastName": "Lopez",
    "age": 28,
    "gender": "Female",
    "birthDate": "20-09-1998",
    "position": "QA Engineer"
  }
]
```

La petición de creación acepta un objeto o un arreglo no vacío. Los
datos se validan antes de guardar el conjunto.

### Consultar y buscar

-   `GET /employees`
-   `GET /employees/1`
-   `GET /employees/search?name=Car`

La búsqueda no distingue entre mayúsculas y minúsculas y revisa nombre,
segundo nombre, apellido y segundo apellido.

### Actualizar un empleado

`PUT /employees/{id}`

El controlador recibe un `EmployeeRequest` con validaciones de campos
obligatorios. Por lo tanto, la implementación actual requiere enviar los
campos obligatorios del empleado; no debe interpretarse como una
actualización parcial.

Ejemplo:

``` json
{
  "firstName": "Juan",
  "middleName": "Carlos",
  "lastName": "Perez",
  "secondLastName": "Lopez",
  "age": 31,
  "gender": "Male",
  "birthDate": "15-06-1996",
  "position": "Senior Java Developer"
}
```

### Eliminar un empleado

`DELETE /employees/{id}`

Cuando la eliminación se realiza correctamente, la API responde con HTTP
`204 No Content`.

## 7. Swagger / OpenAPI

Con la aplicación en ejecución, la documentación interactiva está
disponible en:

``` text
http://localhost:8080/swagger-ui.html
```

El documento OpenAPI se puede consultar en:

``` text
http://localhost:8080/v3/api-docs
```

Swagger permite revisar los endpoints, sus parámetros y respuestas, y
ejecutar solicitudes de prueba.

## 8. Validaciones y manejo de errores

La validación de los datos de entrada utiliza Bean Validation. El manejo
centralizado de excepciones se implementa mediante
`GlobalExceptionHandler`, anotado con `@RestControllerAdvice`.

Respuestas principales:

  Código HTTP                   			Significado
  ----------------------------- ----------------------------------
  `200 OK`                      		Consulta o actualización exitosa
  `201 Created`                 		Empleado(s) creado(s)
  `204 No Content`              	Empleado eliminado
  `400 Bad Request`             	Solicitud o datos inválidos
  `404 Not Found`               	Empleado no encontrado
  `500 Internal Server Error`   Error inesperado del servidor

## 9. Pruebas automatizadas

Las pruebas están desarrolladas con JUnit 5 y Mockito. Para las pruebas
del controlador se utiliza MockMvc.

Ejecutar:

``` bash
mvn test
```

Los archivos de prueba se encuentran en `src/test/java` e incluyen
pruebas del servicio y del controlador.

## 10. Logging

La configuración de logging se encuentra en
`src/main/resources/application.properties`.

Se configura:

-   Nivel `INFO` para el registro general.
-   Nivel `DEBUG` para los paquetes de la aplicación.
-   Nivel `DEBUG` para las consultas SQL de Hibernate.
-   Nivel `TRACE` para los parámetros SQL enlazados por Hibernate.

El nivel `TRACE` puede mostrar valores enviados a la base de datos. Debe
revisarse o deshabilitarse en ambientes productivos para evitar exponer
información sensible.


## Monitoreo con Spring Boot Actuator

El proyecto incorpora Spring Boot Actuator para consultar el estado de salud de la aplicación y algunos de sus componentes.

Configuración en `src/main/resources/application.properties`:

```properties
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
```

Con la aplicación en ejecución, consultar:

```http
GET http://localhost:8080/actuator/health
```

Un estado `UP` indica que el componente revisado está disponible. La respuesta puede incluir el estado de la aplicación, la conexión a MySQL (`db`) y el espacio en disco (`diskSpace`).

## Colección de Postman

En la raíz del proyecto se incluye `Employee Service API.postman_collection.json`.

Para utilizarla:

1. Abrir Postman.
2. Seleccionar **Import**.
3. Elegir el archivo `Employee Service API.postman_collection.json`.
4. Verificar que la variable `baseUrl` tenga el valor `http://localhost:8080`.
5. Iniciar la aplicación y ejecutar las solicitudes.

La colección incluye:

| Nombre | Método | Endpoint |
|---|---|---|
| Create Employee | POST | `/employees` |
| Create Multiple Employees | POST | `/employees` |
| Get All Employees | GET | `/employees` |
| Get Employee By ID | GET | `/employees/5` |
| Search Employees By Name | GET | `/employees/search?name=Car` |
| Update Employee | PUT | `/employees/5` |
| Delete Employee | DELETE | `/employees/5` |
| Health Check | GET | `/actuator/health` |

Las solicitudes que utilizan el ID `5` son ejemplos. Debe existir un empleado con ese identificador para consultar, actualizar o eliminarlo. Ejecutar `Delete Employee` después de las operaciones que necesiten ese registro.
