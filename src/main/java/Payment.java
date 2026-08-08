public class Payment {
    private double amount;
    private Customer customer;
    private PaymentStrategy paymentStrategy;

    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;

    public Payment(double amount, Customer customer) {
        this.amount = amount;
        this.customer = customer;
    }

    public double getAmount() {
        return amount;
    }

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void setCardDetails(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public void checkout() {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set. Set it before calling checkout.");
        }
        // Validate card details using PaymentController validators
        PaymentController.isCardNumberValid(cardNumber);
        PaymentController.isCardHolderNameValid(cardHolderName);
        PaymentController.isExpiryDateValid(expiryDate);
        PaymentController.isCvvValid(cvv);

        // Process payment (no interactive console input in web context)
        paymentStrategy.pay(amount);
    }
}
