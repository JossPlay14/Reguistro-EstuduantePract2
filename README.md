# Registro-EstudiantePract2 - Sistema de Registro Universitario

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-green)
![Maven](https://img.shields.io/badge/Maven-3.8-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![JWT](https://img.shields.io/badge/JWT-0.11.5-yellow)

Sistema de gestión académica desarrollado como práctica 2 para la asignatura TAW-251. Proyecto Spring Boot que implementa un CRUD completo para estudiantes, materias e inscripciones, con autenticación JWT.

## 📌 Características principales

- **Autenticación segura** con JWT (JSON Web Tokens)
- **Gestión completa** de estudiantes, materias e inscripciones
- **API RESTful** documentada con OpenAPI 3.0 (Swagger UI)
- **Configuración CORS** para integración con frontend
- **Manejo centralizado de excepciones**
- **Validación de datos** con Jakarta Validation
- **Caching** con Redis
- **Documentación automática** con SpringDoc OpenAPI

## 🛠️ Stack Tecnológico

### Backend
- Java 21
- Spring Boot 3.5
- Spring Security
- Spring Data JPA
- Spring Validation

### Base de Datos
- PostgreSQL (configurable)

### Seguridad
- JWT (jjwt 0.11.5)
- Spring Security

### Herramientas
- Lombok
- SpringDoc OpenAPI 2.2.0
- Jackson Datatype JSR310

## 📂 Estructura del Proyecto

```text
registro-universitario/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── universidad/
│   │   │           ├── config/                 # Configuraciones (Security, Redis, Swagger)
│   │   │           ├── controller/             # Controladores REST
│   │   │           ├── dto/                    # Objetos de transferencia
│   │   │           ├── exception/              # Manejo de excepciones
│   │   │           ├── model/                  # Entidades JPA
│   │   │           ├── repository/             # Repositorios Spring Data
│   │   │           ├── security/               # Configuración JWT
│   │   │           ├── service/                # Lógica de negocio
│   │   │           └── validation/             # Validaciones personalizadas
│   │   └── resources/
│   │       └── application.properties          # Configuración de la app
│   └── test/                                   # Pruebas unitarias
├── .gitignore
├── pom.xml                                     # Dependencias Maven
└── README.md
