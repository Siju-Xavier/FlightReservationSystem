import java.util.*;
import java.sql.SQLException;

public class LoginController {
    private Map<String, User> users;
    private Scanner scanner;
    private UserController userController;
    private dbManager dbManager;

    public LoginController() {
        this.scanner = new Scanner(System.in);
        this.userController = new UserController();
        this.dbManager = dbManager.getInstance();
    }

    public void startLoginProcess() {
        System.out.println("\n===Welcome to the Flight Application System Login ===");
        System.out.println("\nSelect any of the following options to proceed:");
        System.out.println("1. Customer Login");
        System.out.println("2. Flight Agent Login");
        System.out.println("3. System Administrator Login");
        System.out.println("4. Guest Access");
        System.out.println("5. Create New Customer Account");


        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                customerLogin();
                break;
            case 2:
                flightAgentLogin();
                break;
            case 3:
                adminLogin();
                break;
            case 4:
                guestLogin();
                break;
            case 5:
                createCustomerAccount();
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    private void customerLogin() {
        System.out.println("\n--- Customer Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = verifyLogin(username, password);
        if (user instanceof Customer) {
            System.out.println("Welcome, " + username + "!");
            showCustomerMenu((Customer) user);
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    private void flightAgentLogin() {
        System.out.println("\n--- Flight Agent Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = verifyLogin(username, password);
        if (user instanceof FlightAgent) {
            System.out.println("Welcome, Agent " + username + "!");
            showFlightAgentMenu((FlightAgent) user);
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    private void adminLogin() {
        System.out.println("\n--- System Administrator Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        SystemAdministrator admin = verifyAdminLogin(username, password);
        if (admin != null) {
            System.out.println("Welcome, Administrator " + username + "!");
            showAdminMenu(admin);
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    private void guestLogin() {
        Customer guest = loginAsGuest();
        System.out.println("Welcome, Guest User!");
        showCustomerMenu(guest);
    }

    private void createCustomerAccount() {
        System.out.println("\n--- Create New Customer Account ---");
        String username;
        while (true){
            System.out.print("Username: ");
            username = scanner.nextLine();
            if (username == null || username.equals("")) {
                System.out.println("Invalid username!");
            } else if (username.length() < 6){
                System.out.println("Invalid username! Username should be atleast 6 characters long.");
            } else if (userController.getUser(username) != null) {
                System.out.println("username already exists. Please choose another one!");
            }
            else {
                break;
            }
        }

        String email;
        while (true){
            System.out.print("Email: ");
            email = scanner.nextLine();
            if (email == null || email.equals("") || !email.contains("@"))  {
                System.out.println("Invalid email!");
            }
            else {
                break;
            }
        }

        String password;
        while (true){
            System.out.print("Password: ");
            password = scanner.nextLine();
            if (password == null || password.equals("") || password.length() < 6)  {
                System.out.println("Invalid password! Password must be atleast 6 characters long.");
            } else {
                break;
            }
        }

        System.out.println("First Name: ");
        String firstName = scanner.nextLine();
        System.out.println("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.println("Phone Number: ");
        String phoneNumber = scanner.nextLine();

        Customer customer = new Customer(username, password, email, phoneNumber, firstName, lastName);
        userController.addUser(customer);
        System.out.println("Account created successfully!");
    }

    private void showCustomerMenu(Customer customer) {
        ProfileController profileController = new ProfileController();

        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. Toggle Promotion Subscription");
            System.out.println("4. Book Flights");
            System.out.println("5. Logout");
            System.out.print("Choose option (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println(profileController.getProfileInfo(customer));
                    break;
                case 2:
                    updateCustomerProfile(customer, profileController);
                    break;
                case 3:profileController.promotionNewsSubscription(customer);
                    break;
                case 4:
                    showBookingMenu(customer);
                    break;
                case 5:
                    customer.logout();
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void updateCustomerProfile(Customer customer, ProfileController profileController) {
        if (customer.isGuestUser()){
            System.out.println("Error: You need to create an account first");
            return;
        }

        System.out.println("\n--- Update Customer Profile ---");
        System.out.println("Username: " + customer.getUsername() + " (cannot be changed)");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Don't pass username parameter anymore
        boolean success = profileController.updatePersonalInfo(customer, firstName, lastName,
                email, phoneNumber, password);

        if (success) {
            System.out.println("Profile updated successfully!");
        } else {
            System.out.println("Profile update failed. Please check your input.");
        }
    }

    private void showFlightAgentMenu(FlightAgent agent) {
        UserController userController = new UserController();
        ProfileController profileController = new ProfileController();
        FlightController flightController = new FlightController();
        BookingController bookingController = new BookingController(new ArrayList<>(), flightController);


        while (true) {
            System.out.println("\n--- Flight Agent Menu ---");
            System.out.println("1. View Schedules");
            System.out.println("2. Create Reservation");
            System.out.println("3. Modify Reservation");
            System.out.println("4. Cancel Reservation");
            System.out.println("5.Manage Customer Data");
            System.out.println("6. View My Info");
            System.out.println("7. Logout");
            System.out.print("Choose option (1-7): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    agent.viewSchedules();
                    break;
                case 2:
                    System.out.print("Customer Username: ");
                    String customerId = scanner.nextLine();
                    System.out.print("Flight Number: ");
                    String flightNumber = scanner.nextLine();
                    System.out.print("Seat Number: ");
                    String seatNumber = scanner.nextLine();
                    agent.createReservation(customerId, flightNumber,seatNumber,bookingController,userController,flightController);
                    break;
                case 3:
                    System.out.print("Booking ID: ");
                    String modifyId = scanner.nextLine();
                    System.out.print("New Flight: ");
                    String newFlightNum = scanner.nextLine();
                    System.out.print("New Seat: ");
                    String newSeatNum = scanner.nextLine();

                    agent.modifyReservation(modifyId, newFlightNum, newSeatNum, bookingController, flightController);
                    break;

                case 4:
                    System.out.print("Booking ID: ");
                    String cancelId = scanner.nextLine();

                    agent.cancelReservation(cancelId, bookingController, userController);
                    break;
                case 5:
                    System.out.println("\n--- Customer Management ---");
                    System.out.print("Customer Username: ");
                    String manageCustomerId = scanner.nextLine();
                    System.out.println("Available Actions:");
                    System.out.println("1. View Profile");
                    System.out.println("2. Update Full Profile");
                    System.out.println("3. Toggle Promotions");
                    System.out.print("Choose action (1-3): ");

                    int actionChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (actionChoice) {
                        case 1:
                            agent.manageCustomerData(manageCustomerId, "view profile", userController, profileController);
                            break;
                        case 2:
                            System.out.print("New First Name: ");
                            String firstName = scanner.nextLine();
                            System.out.print("New Last Name: ");
                            String lastName = scanner.nextLine();
                            System.out.print("New Email: ");
                            String email = scanner.nextLine();
                            System.out.print("New Phone: ");
                            String phone = scanner.nextLine();
                            System.out.print("New Password: ");
                            String password = scanner.nextLine();

                            agent.manageCustomerData(manageCustomerId, "update profile", userController, profileController,
                                    firstName, lastName, email, phone, password);
                            break;
                        case 3:
                            agent.manageCustomerData(manageCustomerId, "toggle promotions", userController, profileController);
                            break;
                        default:
                            System.out.println("Invalid action choice!");
                    }
                    break;
                case 6:
                    System.out.println(agent.getUserInfo());
                    break;
                case 7:
                    agent.logout();
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void showAdminMenu(SystemAdministrator admin) {
        UserController userController = new UserController();
        FlightController flightController = new FlightController();

        while (true) {
            System.out.println("\n--- System Administrator Menu ---");
            System.out.println("1. Add Flight");
            System.out.println("2. Remove Flight");
            System.out.println("3. Update Schedule");
            System.out.println("4. Manage Users");
            System.out.println("5. View My Info");
            System.out.println("6. Logout");
            System.out.print("Choose option (1-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: // Add Flight
                    System.out.print("Flight Number: ");
                    String flightNum = scanner.nextLine();

                    // Get airline (simplified)
                    System.out.print("Airline ID: ");
                    String airlineId = scanner.nextLine();
                    Airline airline = null;
                    try {
                        airline = dbManager.getAirlineByID(airlineId);
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    if (airline == null) {
                        System.out.println("Using default airline");
                        Map<String, String> empty = new HashMap<>();
                        airline = new Airline("Default", "DEF", empty, empty, empty);
                    }

                    System.out.print("Origin: ");
                    String origin = scanner.nextLine();
                    System.out.print("Destination: ");
                    String destination = scanner.nextLine();
                    System.out.print("Base Price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Total Seats: ");
                    int seats = scanner.nextInt();
                    scanner.nextLine();

                    Date now = new Date();
                    boolean added = admin.addFlight(flightNum, "R" + flightNum, airline,
                            origin, destination, now, now, now,
                            price, seats, flightController);
                    if (added) {
                        System.out.println("Flight added");
                    }
                    break;

                case 2: // Remove Flight
                    System.out.print("Flight Number: ");
                    String removeFlight = scanner.nextLine();
                    boolean removed = admin.removeFlight(removeFlight, flightController);
                    if (removed) {
                        System.out.println("Flight removed");
                    }
                    break;

                case 3: // Update Schedule
                    System.out.print("Flight Number: ");
                    String updateFlight = scanner.nextLine();
                    System.out.print("New Schedule: ");
                    String newSchedule = scanner.nextLine();
                    admin.updateSchedule(updateFlight, newSchedule, flightController);
                    break;
                case 4:
                    showUserManagementMenu(userController);
                    break;
                case 5:
                    System.out.println(admin.getUserInfo());
                    break;
                case 6:
                    admin.logout();
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void showUserManagementMenu(UserController userController) {
        while (true) {
            System.out.println("\n--- User Management ---");
            System.out.println("1. Add User");
            System.out.println("2. Update User");
            System.out.println("3. Reset Password");
            System.out.println("4. Delete User");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose option (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 5) break;

            switch (choice) {
                case 1:
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Phone: ");
                    String phone = scanner.nextLine();
                    System.out.print("Role (Cust/Agent/Admin): ");
                    String role = scanner.nextLine();

                    User newUser;
                    if (role.equalsIgnoreCase("Agent")) {
                        System.out.print("Employee ID: ");
                        String empId = scanner.nextLine();
                        System.out.print("Airline: ");
                        String airline = scanner.nextLine();
                        newUser = new FlightAgent(username, password, email, phone, empId, airline);
                    } else if (role.equalsIgnoreCase("Admin")) {
                        newUser = new SystemAdministrator(username, password, email, phone);
                    } else {
                        System.out.print("First Name: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Last Name: ");
                        String lastName = scanner.nextLine();
                        newUser = new Customer(username, password, email, phone, firstName, lastName);
                    }

                    userController.addUser(newUser);
                    break;

                case 2:
                    System.out.print("Username: ");
                    String updateUser = scanner.nextLine();
                    System.out.print("New Email: ");
                    String newEmail = scanner.nextLine();
                    System.out.print("New Phone: ");
                    String newPhone = scanner.nextLine();
                    userController.updateUser(updateUser, newEmail, newPhone);
                    break;

                case 3:
                    System.out.print("Username: ");
                    String resetUser = scanner.nextLine();
                    System.out.print("New Password: ");
                    String newPass = scanner.nextLine();
                    userController.resetPassword(resetUser, newPass);
                    break;

                case 4:
                    System.out.print("Username: ");
                    String deleteUser = scanner.nextLine();
                    userController.deleteUser(deleteUser);
                    break;

                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public User verifyLogin(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        User user = userController.getUser(username);
        if (user == null) {
            return null;
        }

        if (user.login(username, password)) {
            return user;
        }
        return null;
    }

    public SystemAdministrator verifyAdminLogin(String username, String password) {
        User user = verifyLogin(username, password);
        if (user instanceof SystemAdministrator) {
            return (SystemAdministrator) user;
        }
        return null;
    }

    public Customer loginAsGuest() {
        Customer guest = new Customer("guest", "guest", "guest@guest.com", "000-000-0000", "Guest", "User");
        guest.setGuestUser(true);
        return guest;
    }

    public boolean validateAccount(String username, String email, String password) {
        if (username == null || username.trim().isEmpty() || username.length() < 6 ) {
            System.out.println("Username cannot be empty");
            return false;
        }

        if (email == null || !email.contains("@")) {
            System.out.println("Invalid email format");
            return false;
        }

        if (password == null || password.length() < 6) {
            System.out.println("Password must be at least 6 characters long");
            return false;
        }

        if (userController.getUser(username) != null) {
            System.out.println("Username already exists, choose another one");
            return false;
        }

        return true;
    }

    private void showBookingMenu(Customer customer) {
        FlightController flightController = new FlightController();
        BookingController bookingController = new BookingController(new ArrayList<>(), flightController);

        while (true) {
            System.out.println("\n--- Booking Menu ---");
            System.out.println("1. Search Flight");
            System.out.println("2. Make Booking");
            System.out.println("3. View My Bookings");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose option (1-5): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Search Flight");
                    System.out.println("Enter origin: ");
                    String origin = scanner.nextLine();
                    System.out.println("Enter destination: ");
                    String destination = scanner.nextLine();
                    List<Flight> flights = flightController.searchFlights(origin, destination, null);
                    System.out.println("Found " + flights.size() + " flights");

                    for (Flight flight : flights) {
                        List<Seat> availableSeats = flight.getAvailableSeatsList();
                        if (!availableSeats.isEmpty()) {
                            double minPrice = availableSeats.get(0).getPrice();
                            double maxPrice = availableSeats.get(0).getPrice();
                            Map<String, Integer> seatTypeCount = new HashMap<>();

                            for (Seat seat : availableSeats) {
                                minPrice = Math.min(minPrice, seat.getPrice());
                                maxPrice = Math.max(maxPrice, seat.getPrice());
                                seatTypeCount.put(seat.getSeatType(), seatTypeCount.getOrDefault(seat.getSeatType(), 0) + 1);
                            }
                               System.out.println("Flight " + flight.getFlightNumber() + " - " +
                                flight.getOrigin() + " → " + flight.getDestination() +
                                " - Airline: " + flight.getAirline() +
                                " - Available Seats: " + flight.getAvailableSeats());
                               System.out.println("Seat types available: " + seatTypeCount);
                        }else{
                    System.out.println("No availble seats");
                }
                }
                    break;
                case 2:
                    System.out.println("Make Booking");
                    System.out.println("Enter flight number: ");
                    String flightNumber = scanner.nextLine();


                    Flight flight = flightController.getFlight(flightNumber);
                    if (flight == null) {
                        System.out.println("Flight not found!");
                        break;
                    }


                    List<Seat> availableSeats = flight.getAvailableSeatsList();
                    if (availableSeats.isEmpty()) {
                        System.out.println("No available seats on this flight!");
                        break;
                    }

                    System.out.println("Available seats:");
                    for (Seat seat : availableSeats) {
                        System.out.println("Seat: " + seat.getSeatNumber() + " - Type: " +
                                seat.getSeatType() + " - Price: $" + seat.getPrice());
                    }

                    System.out.println("Enter seat number: ");
                    String seatNumber = scanner.nextLine();


                    Seat selectedSeat = null;
                    for (Seat seat : availableSeats) {
                        if (seat.getSeatNumber().equalsIgnoreCase(seatNumber)) {
                            selectedSeat = seat;
                            break;
                        }
                    }

                    if (selectedSeat == null) {
                        System.out.println("Invalid seat number or seat not available!");
                        System.out.println("\n Available seats:");
                        Map<String, List<Seat>> seatsByType = new HashMap<>();
                        for (Seat seat : availableSeats) {
                            seatsByType.computeIfAbsent(seat.getSeatType(), k -> new ArrayList<>()).add(seat);
                        }

                        for (Map.Entry<String, List<Seat>> entry : seatsByType.entrySet()) {
                            System.out.println("Seat type: " + entry.getKey());
                            for (Seat seat : entry.getValue()) {
                                System.out.println(" " + seat.getSeatNumber() + " - $" + seat.getPrice());
                            }
                        }
                        System.out.println("Seat numbers are as follows: E-1,P-1,B-1,F-1  where E= economy , P = premium, B = Business , F = First Class");
                        break;
                    }


                    double actualPrice = selectedSeat.getPrice();
                    Booking booking = bookingController.addBooking(customer, flightNumber, seatNumber, actualPrice);
                    if (booking != null) {
                        System.out.println("Booking successful! Total: $" + actualPrice);
                        System.out.println("Booking ID: " + booking.getBookingId());
                        System.out.println("Booking date: " + booking.getBookingDate());
                        System.out.println("Flight: " + flight.getFlightNumber() + " | " + flight.getOrigin() + " | " + flight.getAirline() + " | " );
                        System.out.println("Route: " + flight.getOrigin() + " | " + flight.getDestination() + " | ");
                        System.out.println("Departure: " + flight.getDepartureTime());
                        System.out.println("Seat: " + selectedSeat.getSeatNumber() + " | " + selectedSeat.getSeatType());
                        System.out.println("Price: " + actualPrice);
                        System.out.println("Status: " + booking.getStatus() );
                    }
                    break;
                case 3:
                    List<Booking> bookings = bookingController.getBookings(customer);
                    System.out.println("\n=== YOUR BOOKINGS ===");
                    System.out.println("Total bookings: " + bookings.size());

                    if (bookings.isEmpty()) {
                        System.out.println("No bookings found.");
                    } else {
                        for (int i = 0; i < bookings.size(); i++) {
                            Booking b = bookings.get(i);
                            System.out.println("\n--- Booking " + (i+1) + " ---");
                            System.out.println("Booking ID: " + b.getBookingId());
                            System.out.println("Flight: " + b.getFlight().getFlightNumber() + " | " +
                                    b.getFlight().getOrigin() + " → " + b.getFlight().getDestination());
                            System.out.println("Airline: " + b.getFlight().getAirline());
                            System.out.println("Date: " + b.getFlight().getFlightDate());
                            System.out.println("Seat: " + b.getSeat().getSeatNumber() + " (" + b.getSeat().getSeatType() + ")");
                            System.out.println("Status: " + b.getStatus());
                            System.out.println("Price: $" + b.getTotalPrice());
                            System.out.println("Booking Date: " + b.getBookingDate());
                        }
                    }
                    break;
                case 4:
                    System.out.println("\n=== CANCEL BOOKING ===");
                    List<Booking> userBookings = bookingController.getBookings(customer);

                    if (userBookings.isEmpty()) {
                        System.out.println("No bookings found to cancel.");
                        break;
                    }

                    System.out.println("Your bookings:");
                    for (int i = 0; i < userBookings.size(); i++) {
                        Booking b = userBookings.get(i);
                        System.out.println((i+1) + ". ID: " + b.getBookingId() +
                                " | Flight: " + b.getFlight().getFlightNumber() +
                                " | Seat: " + b.getSeat().getSeatNumber() +
                                " | Date: " + b.getFlight().getFlightDate());
                    }

                    System.out.print("\nEnter booking NUMBER to cancel (1-" + userBookings.size() + "): ");
                    int bookingChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (bookingChoice < 1 || bookingChoice > userBookings.size()) {
                        System.out.println("Invalid choice.");
                        break;
                    }

                    Booking toCancel = userBookings.get(bookingChoice - 1);
                    boolean canceled = bookingController.cancelBooking(toCancel.getBookingId(), customer);

                    if (canceled) {
                        System.out.println("Booking " + toCancel.getBookingId() + " canceled successfully!");
                    } else {
                        System.out.println("Failed to cancel booking.");
                    }
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}