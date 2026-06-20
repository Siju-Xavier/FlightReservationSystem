import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;

public class CheckoutController {
    public void handleCheckout(Context ctx) {
        // Parse JSON body into CheckoutRequest DTO
        CheckoutRequest req = ctx.bodyAsClass(CheckoutRequest.class);
        // Create Payment object (customer handling omitted for demo)
        Payment payment = new Payment(req.amount, null);
        // Set card details
        payment.setCardDetails(req.cardNumber, req.cardHolderName, req.expiryDate, req.cvv);
        // Choose strategy based on requested method
        if ("debit".equalsIgnoreCase(req.paymentMethod)) {
            payment.setPaymentStrategy(new DebitCardPayment(req.cardNumber, req.cvv, req.expiryDate, req.cardHolderName));
        } else {
            payment.setPaymentStrategy(new CreditCardPayment(req.cardNumber, req.cvv, req.expiryDate, req.cardHolderName));
        }
        // Perform checkout (non‑interactive)
        try {
            payment.checkout();
            Map<String, String> resp = new HashMap<>();
            resp.put("message", "Payment successful");
            ctx.json(resp);
        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", e.getMessage()));
        }
    }
}
