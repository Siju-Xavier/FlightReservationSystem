# Payment Testing Instructions

This document provides step‑by‑step guidance for testing the **checkout** API of the Flight Reservation System.

## Endpoint
```
POST http://localhost:8080/api/checkout
```

## Required JSON Payload
All fields are **required** unless noted otherwise. The payload must be sent with the `Content-Type: application/json` header.

```json
{
    "amount": 123.45,
    "paymentMethod": "debit",   // "debit" or "credit"
    "cardNumber": "4111111111111111",
    "cardHolderName": "John Doe",
    "expiryDate": "12/25",      // format MM/YY
    "cvv": "123"
}
```
- **amount** – Total amount to be charged (decimal).
- **paymentMethod** – Either `"debit"` or `"credit"`.
- **cardNumber** – 16‑digit card number (no spaces).
- **cardHolderName** – Name on the card.
- **expiryDate** – Expiration date in `MM/YY` format.
- **cvv** – 3‑digit security code.

## Example cURL Command (Windows CMD)
```cmd
curl -X POST http://localhost:8080/api/checkout ^
    -H "Content-Type: application/json" ^
    -d "{\"amount\":123.45,\"paymentMethod\":\"debit\",\"cardNumber\":\"4111111111111111\",\"cardHolderName\":\"John Doe\",\"expiryDate\":\"12/25\",\"cvv\":\"123\"}"
```

## Expected Responses
- **200 OK** – Payment processed successfully. Body: `{ "message": "Payment successful" }`
- **400 Bad Request** – Missing or invalid fields. Body contains an error description.
- **500 Internal Server Error** – Unexpected server error.

## Testing Tips
- Use the **debit** method for a successful flow (the mock `DebitCardPayment` always returns `true`).
- The **credit** method also succeeds via `CreditCardPayment` (also mocked). In a real system you would replace these strategies with real gateway integrations.
- Ensure the backend server (`App`) is running (`mvnw.cmd exec:java -Dexec.mainClass=App`).

Feel free to adjust the values for further testing.
