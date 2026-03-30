import java.sql.SQLException;

public class ProfileController {
    private dbManager dbManager;

    public ProfileController() {
        this.dbManager = dbManager.getInstance();
    }

    public boolean updatePersonalInfo(Customer customer, String newFirstName, String newLastName,
                                      String newEmail, String newPhoneNumber, String newPassword) {

        String oldEmail = customer.getEmail();

        if (customer == null) {
            System.out.println("Error: Customer not found!");
            return false;
        }

        if (customer.isGuestUser()) {
            System.out.println("Error: You need to create an account first!");
            return false;
        }

        if (newFirstName == null || newFirstName.trim().isEmpty()) {
            System.out.println("Error: First name cannot be empty");
            return false;
        }

        if (newLastName == null || newLastName.trim().isEmpty()) {
            System.out.println("Error: Last name cannot be empty");
            return false;
        }

        if (newEmail == null || !newEmail.contains("@") || newEmail.trim().isEmpty()) {
            System.out.println("Error: Invalid email format");
            return false;
        }

        if (newPassword == null || newPassword.length() < 6) {
            System.out.println("Error: Password must be at least 6 characters");
            return false;
        }

        String originalUsername = customer.getUsername();

        customer.setFirstName(newFirstName);
        customer.setLastName(newLastName);
        customer.updateUserInfo(originalUsername, newEmail, newPhoneNumber, newPassword);

        saveProfile(customer, oldEmail, originalUsername);
        return true;
    }
    private void saveProfile(Customer customer, String oldEmail, String oldUsername) {
        try {
            boolean success = dbManager.updateUserProfile(customer, oldEmail, oldUsername);
            if (success) {
                System.out.println("Profile saved to database: " + customer.getUsername());
            } else {
                System.out.println("Error: Failed to save profile to database");
            }
        } catch (SQLException e) {
            System.out.println("Database error while saving profile: " + e.getMessage());
        }
    }

        public String getProfileInfo(Customer customer) {
        if (customer == null) {
            return "Error! Try again!";
        }

        StringBuilder profile = new StringBuilder();
        profile.append("Username: ").append(customer.getUsername()).append("\n");
        profile.append("Name: ").append(customer.getFirstName()).append(" ").append(customer.getLastName()).append("\n");
        profile.append("Email: ").append(customer.getEmail()).append("\n");
        profile.append("Phone: ").append(customer.getPhoneNumber()).append("\n");
        profile.append("Promotion Subscriber: ").append(customer.isPromotionNewsSubscriber()).append("\n");

        return profile.toString();
    }


    public boolean promotionNewsSubscription(Customer customer) {
        if (customer == null) {
            System.out.println("Error: Try again");
            return false;
        }

        if (customer.isPromotionNewsSubscriber()) {
            customer.unsubscribe();
            System.out.println("Unsubscribed from promotions");
        } else {
            customer.subscribe();
            System.out.println("Subscribed to promotions");
        }

        saveProfile(customer,customer.getEmail(),customer.getUsername());
        return true;
    }


}