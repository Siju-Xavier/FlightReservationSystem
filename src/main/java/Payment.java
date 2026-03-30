import java.util.Scanner;

public class Payment {
    private double amount;
    private Customer customer;
    private PaymentStrategy paymentStrategy;

    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;

    private Scanner scanner = new Scanner(System.in);

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

            int choice = 0;
            while (true) {
                System.out.println("Select payment method:");
                System.out.println("1. Debit Card");
                System.out.println("2. Credit Card");
                System.out.print("Enter choice (1 or 2): ");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice == 1 || choice == 2) break;
                    else System.out.println("Invalid choice. Enter 1 or 2.");
                } catch (Exception e) {
                    System.out.println("Invalid input. Enter 1 or 2.");
                }
            }

            collectCardInfo();

            if (choice == 1) {
                paymentStrategy = new DebitCardPayment(cardNumber, cvv, expiryDate, cardHolderName);
            } else {
                paymentStrategy = new CreditCardPayment(cardNumber, cvv, expiryDate, cardHolderName);
            }
        }

        paymentStrategy.pay(amount);
    }

    private void collectCardInfo() {
        while (true) {
            System.out.println("Enter Card Number:");
            cardNumber = scanner.nextLine();
            try {
                PaymentController.isCardNumberValid(cardNumber);
                break;
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage() + " Please try again.");
            }
        }

        while (true) {
            System.out.println("Enter Card Holder Name:");
            cardHolderName = scanner.nextLine();
            try {
                PaymentController.isCardHolderNameValid(cardHolderName);
                break;
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage() + " Please try again.");
            }
        }

        while (true) {
            System.out.println("Enter Expiry Date (MM/YY):");
            expiryDate = scanner.nextLine();
            try {
                PaymentController.isExpiryDateValid(expiryDate);
                break;
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage() + " Please try again.");
            }
        }

        while (true) {
            System.out.println("Enter CVV:");
            cvv = scanner.nextLine();
            try {
                PaymentController.isCvvValid(cvv);
                break;
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage() + " Please try again.");
            }
        }
    }
}