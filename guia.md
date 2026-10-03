01 Arquitectura del
Framework

Diseñar y documentar la arquitectura general del Custom Framework
corporativo, diferenciando claramente Custom Libraries, Custom Starters,
Frameworks y aplicaciones consumidoras, así como sus responsabilidades,
dependencias, convenciones y mecanismos de integración.

02 Custom Libraries
Integration

Integrar y reutilizar las Custom Libraries desarrolladas en el Curso 1 –
Library Development, tales como utilitarios, componentes de dominio,
mappers, logging base u otras librerías corporativas. Las librerías deberán
consumirse como dependencias reutilizables y mantenerse desacopladas de
un microservicio específico.

03 Custom Starters
Integration

Integrar y reutilizar los Custom Starters desarrollados en el Curso 2 – Starter
Development, incluyendo capacidades como logging, auditoría,

seguridad/cifrado y observabilidad. Los starters deberán utilizar auto-
configuración y permitir parametrización externa cuando corresponda.

04 Framework Core

Implementar un Framework Core genérico, reutilizable e independiente de
los dominios de negocio, definiendo contratos, abstracciones, componentes
comunes y mecanismos de extensión que puedan ser utilizados por
diferentes aplicaciones o microservicios.

05 Framework JPA &
Exception Core

Implementar componentes reutilizables para persistencia y manejo
estandarizado de excepciones, mediante módulos como Framework JPA
Core y Exception Starter/Core, o equivalentes, demostrando su consumo
desde una aplicación o microservicio.

06 CQRS Core

Implementar los contratos y abstracciones principales del patrón CQRS,
incluyendo Command, CommandHandler, Query y QueryHandler,
manteniendo desacoplada la infraestructura del framework respecto de la
lógica específica de negocio.

www.galaxy.edu.pe
Pattern Consideraciones

Cumple Comentarios de
revisión

Si No

07 Command Bus &
Query Bus

Implementar mecanismos de Command Bus y Query Bus para
recibir, resolver y enrutar Commands y Queries hacia sus
respectivos Handlers, permitiendo incorporar comportamientos
transversales como validación, autorización, auditoría, logging o
manejo de transacciones.

08
Application
Architecture
Framework

Implementar al menos un framework especializado que facilite la
aplicación de un estilo arquitectónico como Layered, Clean, Hexagonal
o DDD, definiendo su estructura, contratos, convenciones y
componentes reutilizables.

09

BOM &
Dependency
Management

Integrar Custom Libraries, Custom Starters y módulos del Framework
mediante un BOM corporativo, aplicando gestión centralizada de
dependencias, versionamiento semántico (SemVer) y compatibilidad
entre componentes.

10
Integración, Testing
y Documentación

Integrar el Custom Framework en al menos una aplicación o
microservicio Spring Boot, evidenciando el consumo conjunto de
Custom Libraries, Custom Starters y módulos del Framework.
Implementar pruebas, README.md, diagramas, documentación
técnica e instrucciones de compilación, publicación y consumo.