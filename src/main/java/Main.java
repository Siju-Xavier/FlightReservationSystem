import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LoginController loginController = new LoginController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Flight Application System!!");

        while (true) {
            loginController.startLoginProcess();

            System.out.print("\nWould you like to return to main menu? (yes/no): ");
            String response = scanner.nextLine();
            if (!response.equalsIgnoreCase("yes")) {
                System.out.println("Thank you for using our Application. Goodbye!");
                break;
            }
        }

        scanner.close();
    }
}