import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    private List<User> users;
    private dbManager dbManager;

    public UserController() {
        this.users = new ArrayList<>();
        this.dbManager = dbManager.getInstance();
        loadUsersFromDatabase();

    }
    private void loadUsersFromDatabase() {
        loadCustomersFromDatabase();
        loadFlightAgentsFromDatabase();
        loadSystemAdminsFromDatabase();

    }
    private void loadCustomersFromDatabase() {
        try {
            List<Customer> customers = dbManager.getCustomers();
            this.users.addAll(customers);
            System.out.println("Loaded " + customers.size() + " customers from database");
        } catch (SQLException e) {
            System.out.println("Error loading customers from database: " + e.getMessage());
        }
    }

    private void loadFlightAgentsFromDatabase() {
        try {
            List<FlightAgent> agents = dbManager.getFlightAgents();
            this.users.addAll(agents);
            System.out.println("Loaded " + agents.size() + " flight agents from database");
        } catch (SQLException e) {
            System.out.println("Error loading flight agents from database: " + e.getMessage());
        }
    }

    private void loadSystemAdminsFromDatabase() {
        try {
            List<SystemAdministrator> admins = dbManager.getSystemAdministrators();
            this.users.addAll(admins);
            System.out.println("Loaded " + admins.size() + " system administrators from database");
        } catch (SQLException e) {
            System.out.println("Error loading system administrators from database: " + e.getMessage());
        }
    }

    public boolean addUser(User user) {
        if (user == null) {
            System.out.println("Error: User cannot be null");
            return false;
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            System.out.println("Error: Username cannot be null or empty");
            return false;
        }

        if (user.getUsername().length() < 6) {
            System.out.println("Error: Username must be at least 6 characters");
            return false;
        }

        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            System.out.println("Error: Invalid email format");
            return false;
        }

        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                System.out.println("Error: Username already exists, try another username");
                return false;
            }
        }

        users.add(user);
        saveUser(user);
        System.out.println("User added successfully: " + user.getUsername());
        return true;
    }

    public boolean updateUser(String username, String newEmail, String newPhoneNumber) {
        User user = getUser(username);
        if (user == null) {
            System.out.println("Error: User not found");
            return false;
        }

        user.setEmail(newEmail);
        user.setPhoneNumber(newPhoneNumber);
        saveUser(user);
        System.out.println("User updated successfully: " + username);
        return true;
    }

    public boolean resetPassword(String username, String newPassword) {
        User user = getUser(username);
        if (user == null) {
            System.out.println("Error: User not found");
            return false;
        }

        user.setPassword(newPassword);
        saveUser(user);
        System.out.println("Password is successfully reset");
        return true;
    }

    public boolean deleteUser(String username) {
        User user = getUser(username);
        if (user == null) {
            System.out.println("Error: User not found");
            return false;
        }

        users.remove(user);
        System.out.println("User deleted successfully: " + username);
        return true;
    }

    public User getUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Error! Username is empty");
            return null;
        }
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        System.out.println("Error! User not found");
        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public List<User> getUsersByRole(String role) {
        List<User> roleUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equals(role)) {
                roleUsers.add(user);
            }
        }
        return roleUsers;
    }

    private void saveUser(User user) {
        System.out.println("User Saved: " + user.getUsername());
    }
}