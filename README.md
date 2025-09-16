# RETO TRANSFERENCIA

## Funcionalidades
1. Iniciar una transacción de transferencia.
2. Consultar el estado de una transacción.
3. Listar transacciones aprobadas de un usuario.
4. Consultar el historial de transacciones y el estado de cada una en tiempo real.

## Requisitos
- JAVA VERSION 21

### Dependencias extras
- spring-boot-devtools -> Ante cambios de codigo es bueno siempre no tener que estar corriendo el main denuevo
- lombok -> bueno para ahorrar tiempo, evitamos ser repetitivos
- *Pueden existir otras, que no eran necesarias que se me puede haber escapado.*

## Cómo correr el proyecto
1. Clonar repo
   ```
   git clone https://github.com/mathiuSpv/redlink-challenge
   ```
2. Abrir el proyecto (Si es con IntelliJ mejor) el pom.xml, automaticamente estan se instalan las dependencias del proyecto
   (Caso de no ser con IntelliJ descargar Maven 4.0.0).
3. En ese instante Ya podes correr ChallengeApplication

## Recursos disponibles
### Swagger
- URL: http://localhost:8080/swagger-ui.html

### H2
- URL: http://localhost:8080/h2-console/
- Credenciales que debes cambiar:
  - JDBC URL: jdbc:h2:mem:testdb
  - User Name: sa
  - Password: admin
- Si no puedes entrar es chequear application.properties dentro del  src/main

## Diagrama
- El diagrama se encuentra en docs en un [pdf](https://github.com/mathiuSpv/redlink-challenge/blob/main/docs/Diagrama%20de%20Transferencia.pdf). 

## Postman
- Las peticiones se pueden exportar en Postman con el archivo dentro de redlink.postman_collection.json<br><br>
- Ejemplo de peticiones:
    - ```html 
        POST http://localhost:8080/transferencia
        Content-Type: application/json
        Raw example:
          {
           "userId": "1234",
           "amount": 1400.00,
           "currency": "ARS",
           "bankCode": "BANK123",
           "recipientAccount": "DE89370400440532013000"
         }
        ```
        - response:
          ```json
            {
              "transactionId": "550e8400-e29b-41d4-a716-446655440001",
              "userId": "1234",
              "amount": 1400.00,
              "currency": "ARS",
              "status": "APPROVED",
              "createdAt": "2025-09-15T10:30:00Z",
              "bankCode": "BANK123",
              "recipientAccount": "DE89370400440532013000"
            }
          ```
    - ```html
      GET http://localhost:8080/transferencia/550e8400-e29b-41d4-a716-446655440001?moneda=USD&tasa=1.3
      ```
      - response:
        ```json
            {
              "transactionId": "550e8400-e29b-41d4-a716-446655440001",
              "userId": "1234",
              "amount": 1820.00,
              "currency": "USD",
              "status": "APPROVED",
              "createdAt": "2025-09-15T10:30:00Z",
              "bankCode": "BANK123",
              "recipientAccount": "DE89370400440532013000"
            }
        ```
  
