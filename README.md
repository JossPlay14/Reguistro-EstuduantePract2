# Registro-EstuduantePract2 - Sistema de Registro Universitario

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1-green)
![Maven](https://img.shields.io/badge/Maven-3.8-orange)

Sistema de gestión académica desarrollado como práctica 2 para la asignatura TAW-251. Proyecto Spring Boot que implementa un CRUD completo para estudiantes, materias e inscripciones, con autenticación JWT.

## 📌 Características principales

- **Autenticación segura** con JWT (JSON Web Tokens)
- **Gestión completa** de estudiantes, materias e inscripciones
- **API RESTful** documentada con Swagger
- **Configuración CORS** para integración con frontend
- **Manejo centralizado de excepciones**
- **Validación de datos** en DTOs

## 🛠️ Tecnologías utilizadas

- **Backend**: Java 17, Spring Boot 3.1
- **Base de datos**: (Configurable en application.properties)
- **Autenticación**: JWT
- **Documentación**: Swagger UI
- **Build**: Maven

## 📂 Estructura del proyecto

registro-universitario/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/
│ │ │ └── universidad/
│ │ │ ├── config/ # Configuraciones de la aplicación
│ │ │ ├── controller/ # Controladores REST
│ │ │ ├── dto/ # Objetos de transferencia de datos
│ │ │ ├── exception/ # Manejo de excepciones
│ │ │ ├── model/ # Entidades JPA
│ │ │ ├── repository/ # Interfaces de repositorio
│ │ │ ├── security/ # Configuración de seguridad
│ │ │ ├── service/ # Lógica de negocio
│ │ │ └── validation/ # Validaciones personalizadas
│ │ └── resources/ # Archivos de configuración
│ └── test/ # Pruebas unitarias
├── .gitignore
├── pom.xml # Configuración de Maven
└── README.md

## 🚀 Cómo ejecutar el proyecto

1. **Requisitos previos**:
   - Java JDK 17
   - Maven 3.8+
   - Base de datos configurada (MySQL/PostgreSQL/H2)

2. **Configuración**:
   - Editar `src/main/resources/application.properties` con tus credenciales de BD
   - Configurar puerto y otras propiedades según necesidad

3. **Ejecución**:
   ```bash
   mvn spring-boot:run

