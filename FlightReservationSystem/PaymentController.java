
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

interface PaymentStrategy {
    void pay(double amount);
}

public class PaymentController {
    public static boolean isCardNumberValid(String cardNumber) {
        if (!cardNumber.matches("^[0-9]{13,16}$")) {
            throw new IllegalArgumentException("Card number format is invalid");
        }
        return true;
    }

    public static boolean isCvvValid(String cvv) {
        if (!cvv.matches("^[0-9]{3}$")) {
            throw new IllegalArgumentException("CVV format is invalid");
        }
        return true;
    }

    public static boolean isExpiryDateValid(String expiryDate) {
        if (!expiryDate.matches("^(0[1-9]|1[0-2])/([0-9]{2})$")) {
            throw new IllegalArgumentException("Expiry date must be in MM/YY format (e.g., 01/26 or 12/25)");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth cardExpiry = YearMonth.parse(expiryDate, formatter);
            YearMonth currentMonth = YearMonth.now();

            if (cardExpiry.isBefore(currentMonth)) {
                throw new IllegalArgumentException("Card has expired");
            }
        }
        catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Expiry date format is invalid");
        }
        return true;
    }

    public static boolean isCardHolderNameValid(String cardHolderName) {
        if (cardHolderName == null || cardHolderName.trim().isEmpty() || !cardHolderName.matches("^[a-zA-Z ]+$")) {
            throw new IllegalArgumentException("Card holder name cannot be empty");
        }
        return true;
    }

    public static void validatePayment(Payment payment) {
        if (payment == null) throw new IllegalArgumentException("Payment cannot be null");

    }
}

class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String cardHolderName;

    public CreditCardPayment(String cardNumber, String cvv, String expiryDate, String cardHolderName) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.cardHolderName = cardHolderName;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Credit Card.");
        System.out.println("Card Info:- \n Card Number: " + cardNumber + "\n Holder Name: " + cardHolderName + "\n Expiry Date: " + expiryDate + "\n CVV: " + cvv);
    }
}

class DebitCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String cardHolderName;

    public DebitCardPayment(String cardNumber, String cvv, String expiryDate, String cardHolderName) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.cardHolderName = cardHolderName;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Debit Card.");
        System.out.println("Card Info:- \n Card Number: " + cardNumber + "\n Holder Name: " + cardHolderName + "\n Expiry Date: " + expiryDate + "\n CVV: " + cvv);
    }
}