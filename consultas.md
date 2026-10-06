# Consultas cURL - poc-integracion

Este documento contiene un ejemplo `curl` por endpoint definido para la POC, mas casos de error (400, 404, 422).

> Prerrequisito para los casos 422: el mock de fraude debe estar corriendo (`python3 mock_fraudcheck_server.py --port 9090`).

## Variables sugeridas

```bash
API_BASE="http://localhost:8080"
```

## 1) Crear tarjeta de credito

**Endpoint:** `POST /api/v1/credit-cards`

```bash
curl -X POST "$API_BASE/api/v1/credit-cards" \
  -H "Content-Type: application/json" \
  -d '{
    "holderName": "Juan Perez",
    "cardNumber": "4111111111111111",
    "documentNumber": "12345678"
  }'
```

## 2) Obtener tarjeta por externalId

**Endpoint:** `GET /api/v1/credit-cards/{externalId}`

El `externalId` tiene formato `CC-<uuid>` y se obtiene de la respuesta del punto 1 (`data.externalId`).

**ExternalId valido** (existente, devuelve 200):

```bash
curl -X GET "$API_BASE/api/v1/credit-cards/CC-3f2b8c1e-9a47-4d6e-b1a2-5c7d8e9f0a1b"
```

**ExternalId invalido** (inexistente, devuelve 404 `CREDIT_CARD_NOT_FOUND`):

```bash
curl -i -X GET "$API_BASE/api/v1/credit-cards/CC-00000000-0000-0000-0000-000000000000"
```

Tip: crear y consultar en un solo paso (requiere `jq`):

```bash
EXTERNAL_ID=$(curl -s -X POST "$API_BASE/api/v1/credit-cards" \
  -H "Content-Type: application/json" \
  -d '{"holderName":"Juan Perez","cardNumber":"4111111111111111","documentNumber":"12345678"}' \
  | jq -r '.data.externalId')

curl -X GET "$API_BASE/api/v1/credit-cards/$EXTERNAL_ID"
```

---

## Casos de error

### A) Validacion del request (`POST /api/v1/credit-cards`)

Reglas del contrato OpenAPI: `holderName` (3-150), `cardNumber` (13-19), `documentNumber` (8-20), todos obligatorios.

- A1 a A6 (campos faltantes o longitudes invalidas): el starter Andes responde **422** con `code: VALIDATION_ERROR` y el detalle por campo en `error.details`.
- A7 (JSON mal formado): responde **400** con `code: MALFORMED_REQUEST` (`MalformedRequestExceptionMapper`).

Todos los errores usan el mismo envelope `{success:false, data:null, error:{code,message,httpStatus,traceId,timestamp,details[]}, metadata:null}`.

**A1. `holderName` demasiado corto (< 3):**

```bash
curl -i -X POST "$API_BASE/api/v1/credit-cards" \
  -H "Content-Type: application/json" \
  -d '{
    "holderName": "Jo",
    "cardNumber": "4111111111111111",
    "documentNumber": "12345678"
  }'
```

**A2. `cardNumber` demasiado corto (< 13):**

```bash
curl -i -X POST "$API_BASE/api/v1/credit-cards" \
  -H "Content-Type: application/json" \
  -d '{
    "holderName": "Juan Perez",
    "cardNumber": "411111",
    "documentNumber": "12345678"
  }'
```

**A3. `cardNumber` demasiado largo (> 19):**

```bash
curl -i -X POST "$API_BASE/api/v1/credit-cards" \
  -H "Content-Type: application/json" \
  -d '{
    "holderName": "Juan Perez",
    "cardNumber": "41111111111111111111",
    "documentNumber": "12345678"
  }'
```

**A4. `documentNumber` demasiado corto (< 8):**

```bash
curl -i -X POST "$API_BASE/api/v1/credit-cards" \
  -H "Content-Type: application/json" \
  -d '{
    "holderName": "Juan Perez",
    "cardNumber": "4111111111111111",
    "documentNumber": "1234"
  }'
```

**A5. Campo obligatorio faltante (`documentNumber`):**

```bash
curl -i -X POST "$API_BASE/api/v1/credit-cards" \
  -H "Content-Type: application/json" \
  -d '{
    "holderName": "Juan Perez",
    "cardNumber": "4111111111111111"
  }'
```

**A6. Body vacio:**

```bash
curl -i -X POST "$API_BASE/api/v1/credit-cards" \
  -H "Content-Type: application/json" \
  -d '{}'
```

**A7. JSON mal formado:**

```bash
curl -i -X POST "$API_BASE/api/v1/credit-cards" \
  -H "Content-Type: application/json" \
  -d '{ "holderName": "Juan Perez", '
```

**A8. Tipo de dato invalido (`cardNumber` numerico en vez de string):**

```bash
curl -i -X POST "$API_BASE/api/v1/credit-cards" \
  -H "Content-Type: application/json" \
  -d '{
    "holderName": "Juan Perez",
    "cardNumber": 4111111111111111,
    "documentNumber": "12345678"
  }'
```

### B) 422 Unprocessable Entity - Rechazo por fraude (`FRAUD_REJECTED`)

Regla del mock: se rechaza si `cardNumber` termina en `0000` **o** `documentNumber` termina en `9999`.

**B1. Tarjeta que termina en `0000`:**

```bash
curl -i -X POST "$API_BASE/api/v1/credit-cards" \
  -H "Content-Type: application/json" \
  -d '{
    "holderName": "Juan Perez",
    "cardNumber": "4111111111110000",
    "documentNumber": "12345678"
  }'
```

**B2. Documento que termina en `9999`:**

```bash
curl -i -X POST "$API_BASE/api/v1/credit-cards" \
  -H "Content-Type: application/json" \
  -d '{
    "holderName": "Juan Perez",
    "cardNumber": "4111111111111111",
    "documentNumber": "12349999"
  }'
```

**B3. Ambas condiciones a la vez:**

```bash
curl -i -X POST "$API_BASE/api/v1/credit-cards" \
  -H "Content-Type: application/json" \
  -d '{
    "holderName": "Juan Perez",
    "cardNumber": "4111111111110000",
    "documentNumber": "12349999"
  }'
```

Respuesta esperada (envelope Andes; el codigo de dominio lo aporta `FraudRejectedExceptionMapper`):

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "FRAUD_REJECTED",
    "message": "Credit card rejected by fraud-check: REJECTED_BY_MOCK_RULE",
    "httpStatus": 422,
    "traceId": "19f8a806-12ce-4fd7-9bb8-169d33f2de58",
    "timestamp": "2026-10-06T21:01:29.074Z",
    "details": []
  },
  "metadata": null
}
```

### C) 404 Not Found - Tarjeta inexistente (`GET /api/v1/credit-cards/{externalId}`)

```bash
curl -i -X GET "$API_BASE/api/v1/credit-cards/CC-no-existe"
```

Respuesta esperada (envelope Andes; `CreditCardNotFoundExceptionMapper`):

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "CREDIT_CARD_NOT_FOUND",
    "message": "Credit card not found",
    "httpStatus": 404,
    "traceId": "b7baa10d-c7cb-4258-ae4e-76329bcf4e32",
    "timestamp": "2026-10-06T21:01:28.867Z",
    "details": []
  },
  "metadata": null
}
```

## Resumen de codigos

| Codigo | Endpoint | Causa |
|--------|----------|-------|
| 201 | `POST /api/v1/credit-cards` | Tarjeta creada y aprobada por fraude |
| 200 | `GET /api/v1/credit-cards/{externalId}` | Tarjeta encontrada |
| 400 | `POST /api/v1/credit-cards` | `MALFORMED_REQUEST` (JSON ilegible) |
| 404 | `GET /api/v1/credit-cards/{externalId}` | `CREDIT_CARD_NOT_FOUND` |
| 422 | `POST /api/v1/credit-cards` | `VALIDATION_ERROR` (campos faltantes o longitudes invalidas) |
| 422 | `POST /api/v1/credit-cards` | `FRAUD_REJECTED` (`cardNumber` termina en `0000` o `documentNumber` en `9999`) |

