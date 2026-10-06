# Proyecto Final Framework GT

Proyecto multi-módulo que implementa un framework corporativo reutilizable y una aplicación final que lo consume a través de dependencias publicadas en Nexus.

La solución usa Java 21, Spring Boot 4.1.1, Maven multi-module y una estrategia contract-first con OpenAPI.

## Objetivo

- Desarrollar un framework modular para uso corporativo.
- Centralizar dependencias y versiones con un BOM.
- Publicar librerías y módulos en Nexus.
- Consumir el framework desde una app final sin depender del código fuente del framework.
- Generar el backend REST desde contratos OpenAPI y dejar la lógica de negocio en delegates.

## Arquitectura

El proyecto está dividido en dos grandes bloques:

1. Framework corporativo
   - Módulos reutilizables con responsabilidad específica.
   - Publicados como artefactos Maven.
   - Consumidos por la app final desde Nexus.

2. POC de integración
   - Implementación de negocio real bajo arquitectura hexagonal.
   - La capa de dominio mantiene las entidades y puertos.
   - La capa de aplicación orquesta casos de uso y comandos/consultas.
   - Los adaptadores de entrada/salida conectan la API REST, persistencia JPA y consumo del gateway anti-fraude.
   - Genera su API desde `contracts/openapi-creditcard.yaml`.
   - La lógica no se escribe en un controller explícito, sino en un delegate generado por OpenAPI.

### Estructura hexagonal de `poc-integracion`

```text
pe.edu.galaxy.pocintegracion
├── domain
│   ├── model/        CreditCard, CreditCardStatus (POJOs sin Spring/JPA)
│   ├── exception/    CreditCardNotFoundException, FraudRejectedException
│   └── port/out/     CreditCardRepositoryPort, FraudCheckPort (@OutboundPort)
├── application
│   ├── port/in/      comandos, consultas y resultado (@InboundPort)
│   └── service/      handlers CQRS (@ApplicationService)
└── adapter
    ├── in/web/       delegate OpenAPI + error/ AndesExceptionMapper (@InboundAdapter)
    └── out/
        ├── persistence/  entidad JPA, repositorio Spring Data, mapper, adaptador
        └── fraud/        FraudCheckAdapter (@OutboundAdapter)
```

Las reglas de dependencia (dominio puro, aplicacion sin adaptadores, adaptadores aislados entre si) se verifican con ArchUnit en `HexagonalArchitectureTest`.

### Enfoque API-first

- El contrato de entrada/salida del server está en `contracts/openapi-creditcard.yaml`.
- El controller REST se genera con `delegatePattern=true`.
- La logica especifica vive en el adaptador de entrada `poc-integracion/.../adapter/in/web/CreditCardsApiDelegateImpl`.
- El cliente para fraude se genera desde `contracts/openapi-fraudcheck.yaml`.
- La integración con la API de fraude e ID queda encapsulada en `framework-id-fraud`.

## Módulos del framework

- `framework-bom`: BOM central del framework.
- `framework-core`: abstracciones y utilitarios base.
- `framework-cqrs-core`: contratos CQRS.
- `framework-bus-spring`: implementación Spring del command/query bus.
- `framework-jpa-exception-core`: manejo de persistencia y excepciones.
- `framework-architecture-hexagonal`: anotaciones de arquitectura hexagonal (puertos y adaptadores).
- `framework-openapi`: modelos OpenAPI compartidos.
- `framework-id-fraud`: integración de fraude + id-generator basada en Andes API.
- `poc-integracion`: aplicación de integración consumidora del framework bajo arquitectura hexagonal.

## Dependencias externas

El proyecto consume librerías y starters ya existentes en Nexus, sin recrearlos dentro del repositorio:

- `pe.andes.api:andes-api-bom:1.0.10` (los starters no estan en el BOM: se declaran con version explicita y classifier `plain`)
- `pe.andes.api:andes-api-server-spring-boot-starter`
- `pe.andes.api:andes-api-client-spring-boot-starter`
- `pe.andes.api:andes-id-generator-spring-boot-starter`
- `pe.andes.api:andes-text-utils`
- `pe.edu.galaxy.training.java.bom:oms-starter-bom-core:3.0.3`
- `pe.edu.galaxy.training.java:oms-starter-logs-core`
- `pe.edu.galaxy.training.java:oms-starter-audit-core`
- `pe.edu.galaxy.training.java:oms-starter-security-core`
- `pe.edu.galaxy.training.java:oms-starter-observability-core`

## Requisitos

- Java 21
- Maven 3.9+
- Spring Boot 4.1.1
- Nexus disponible en `http://localhost:8089`
- Credenciales configuradas en `~/.m2/settings.xml` con los ids:
  - `nexus-releases`
  - `nexus-snapshots`

## Compilar el framework

Desde la raíz del proyecto:

```bash
mvn clean install
```

Si se desea incluir además los starters Galaxy en la compilación:

```bash
mvn -DwithGalaxyStarters=true clean install
```

## Publicar a Nexus

Para desplegar los módulos del framework en release:

```bash
mvn -DskipTests -Prelease clean deploy
```

Esto publica los artefactos en el repositorio Maven de releases configurado en el `pom.xml` principal.

## Ejecutar la aplicación final

### 1) Levantar el mock del backend anti-fraude

```bash
python3 scripts/mock_fraudcheck_server.py --port 9090
```

### 2) Levantar la app final

```bash
mvn -pl poc-integracion spring-boot:run
```

La aplicación queda disponible en:

```text
http://localhost:8080
```

## Validación de endpoints

### Crear tarjeta aprobada

```bash
curl -s -i -X POST 'http://localhost:8080/api/v1/credit-cards' \
  -H 'Content-Type: application/json' \
  -d '{
    "holderName": "Ada Lovelace",
    "cardNumber": "4111111111111111",
    "documentNumber": "12345678"
  }'
```

Respuesta esperada: `HTTP/1.1 201 Created`

### Consultar tarjeta

```bash
curl -s -i 'http://localhost:8080/api/v1/credit-cards/{externalId}'
```

Respuesta esperada: `HTTP/1.1 200 OK`

### Caso rechazado por fraude

```bash
curl -s -i -X POST 'http://localhost:8080/api/v1/credit-cards' \
  -H 'Content-Type: application/json' \
  -d '{
    "holderName": "Grace Hopper",
    "cardNumber": "4111111111110000",
    "documentNumber": "87654321"
  }'
```

Respuesta esperada: `HTTP/1.1 422 Unprocessable Entity` con error de fraude.

## Contratos OpenAPI

- `contracts/openapi-creditcard.yaml`: contrato del servicio REST principal.
- `contracts/openapi-fraudcheck.yaml`: contrato del cliente externo anti-fraude.

## Estructura principal del repositorio

```text
.
├── contracts/
│   ├── openapi-creditcard.yaml
│   └── openapi-fraudcheck.yaml
├── poc-integracion/
├── framework-architecture-hexagonal/
├── framework-bom/
├── framework-bus-spring/
├── framework-core/
├── framework-cqrs-core/
├── framework-jpa-exception-core/
├── framework-openapi/
├── framework-id-fraud/
├── scripts/
│   ├── mock_fraudcheck_server.py
│   ├── publish-local.sh
│   └── publish-nexus.sh
├── pom.xml
├── README.md
├── guia.md
└── req-tecnicos.md
```

## Estado del proyecto

El proyecto ya quedó validado en entorno local con:

- compilación del framework y de la app final,
- publicación en Nexus,
- consumo del framework desde la app final,
- arranque de la aplicación,
- validación de flujo de aprobación y rechazo,
- consumo del mock anti-fraude.

## Recomendación de uso

Si se quiere reutilizar este framework en otra aplicación, la ruta recomendada es:

1. publicar los módulos del framework a Nexus,
2. importar el BOM corporativo,
3. declarar las dependencias de los módulos necesarios,
4. construir la app con contratos OpenAPI y delegates para mantener la lógica desacoplada.
