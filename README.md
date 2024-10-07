# Project Scaffolding Template

Este proyecto proporciona un scaffolding básico para un microservicio.

Incluye una estructura de carpetas predefinida, configuración inicial y
dependencias esenciales para empezar rápidamente.

## Estructura del proyecto

Este proyecto sigue los principios de la **arquitectura Hexagonal**
(también conocida como **Arquitectura de Puertos y Adaptadores**),
que organiza la aplicación en capas bien definidas:
**Domain**, **Application** e **Infrastructure**. La principal ventaja
de esta arquitectura es que separa claramente la lógica de negocio del
manejo de dependencias externas.


```plaintext
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.invima.scaffolding
│   │   │       ├── application
│   │   │       ├── domain
│   │   │       ├── infrastructure
│   │   └── resources
│   │       └── application.yml
│   └── test
│       └── java
├── pom.xml
├── Dockerfile
├── README.md
└── .gitignore
```

### Capas principales

1. **Domain**:
    - Contiene la lógica de negocio pura y las entidades del sistema.
    - Esta capa es completamente independiente de frameworks, bases de datos o tecnologías externas.
    - Solo define reglas y comportamientos del negocio, y no debería verse afectada por cambios en la infraestructura.

2. **Application**:
    - Orquesta los casos de uso y se encarga de gestionar las interacciones entre el **domain** y las capas externas.
    - Define los **puertos** (interfaces) que serán implementados por adaptadores externos.
    - Esta capa conoce el dominio y los flujos de negocio, pero no depende directamente de implementaciones específicas.

3. **Infrastructure**:
    - Contiene los **adaptadores** que conectan la aplicación con el mundo exterior, como bases de datos, APIs, sistemas de mensajería, frameworks, etc.
    - Implementa los puertos definidos en la capa de aplicación y permite que la lógica central del sistema funcione sin depender directamente de estas tecnologías externas.

### Ejemplo visual

```plaintext
                 +--------------------------+
                 |      Infrastructure      |
                 +--------------------------+
                            |
                 +--------------------------+
                 |       Application        |
                 +--------------------------+
                            |
                 +--------------------------+
                 |          Domain          |
                 +--------------------------+
