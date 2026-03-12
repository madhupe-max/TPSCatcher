# FIX Binary Converter REST Service

Spring Boot service that converts FIX binary payloads into ASCII.

## What it does

- Accepts FIX payloads as either hex or base64.
- Returns the raw ASCII FIX message.
- Returns a readable FIX message with SOH (`0x01`) rendered as `|`.

## Build and test

```bash
mvn test
```

## Run

```bash
mvn spring-boot:run
```

The service starts on port `8080` by default.

## Swagger UI

Once the application is running, open:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON is available at:

```text
http://localhost:8080/v3/api-docs
```

## API

Endpoint:

```text
POST /api/fix/convert
POST /api/fix/convert-file
```

Hex request:

```bash
curl -X POST http://localhost:8080/api/fix/convert \
	-H "Content-Type: application/json" \
	-d '{
		"hexData": "383D4649582E342E3401393D31320133353D3001"
	}'
```

Base64 request:

```bash
curl -X POST http://localhost:8080/api/fix/convert \
	-H "Content-Type: application/json" \
	-d '{
		"base64Data": "OD1GSVguNC40ATk9MTIBMzU9MAE="
	}'
```

Multipart file upload:

```bash
curl -X POST http://localhost:8080/api/fix/convert-file \
	-F "file=@/path/to/fix-message.bin;type=application/octet-stream"
```

Swagger UI also exposes this as a file picker on the `POST /api/fix/convert-file` operation.

Example response:

```json
{
	"asciiMessage": "8=FIX.4.4\u00019=12\u000135=0\u0001",
	"readableMessage": "8=FIX.4.4|9=12|35=0|"
}
```