public class CheckoutRequest {
    public double amount;
    public String paymentMethod; // "debit" or "credit"
    public String cardNumber;
    public String cardHolderName;
    public String expiryDate; // MM/YY
    public String cvv;
}
