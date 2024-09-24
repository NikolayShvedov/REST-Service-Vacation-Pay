# REST-Service-Vacation-Pay-Calculator
REST service implementing API for calculating and receiving vacation pay.

```shell
curl -X POST http://localhost:8081/calculacte \
-H "Content-Type: application/json" \
-d '{
  "averageSalaryPerYear": 30500.00,
  "vacationDays": 30,
  "startVacationDate": "2024-02-25"
}'
```

Response body: 
```json
{
  "vacationPay": 18113.20
}
```

# Technology stack
- SpringBoot v 2.7.5
- Java 11.0.4
- Stream API
- Advice
- MockMvc
- Docker