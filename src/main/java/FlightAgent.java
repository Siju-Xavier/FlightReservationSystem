import java.util.*;

public class FlightAgent extends User {
    private String employeeID;
    private String airline;
    private List<String> assignedAirlines;

    public FlightAgent(String username, String password, String email,
                       String phoneNumber, String employeeID, String airline) {
        super(username, password, email, phoneNumber, "FlightAgent");
        this.employeeID = employeeID;
        this.airline = airline;
        this.assignedAirlines = new ArrayList<>();
        this.assignedAirlines.add(airline);
    }
    public boolean manageCustomerData(String customerId, String action,
                                      UserController userController,
                                      ProfileController profileController,
                                      String... additionalParams) {
        if (customerId == null || action == null || userController == null || profileController == null) {
            System.out.println("Error: Invalid parameters provided");
            return false;
        }

        User user = userController.getUser(customerId);
        if (user == null) {
            System.out.println(" Error: Customer not found with ID: " + customerId);
            return false;
        }
        if (!(user instanceof Customer)) {
            System.out.println(" Error: User is not a Customer");
            return false;
        }

        Customer customer = (Customer) user;
        System.out.println("Managing Customer " + customer.getFirstName() + " " + customer.getLastName());
        switch (action.toLowerCase()) {
            case "view profile":
                System.out.println(profileController.getProfileInfo(customer));
                return true;
            case "update profile":
                if (additionalParams.length == 5) {  // Now only 5 params (no username)
                    return profileController.updatePersonalInfo(customer,
                            additionalParams[0],  // firstName
                            additionalParams[1],  // lastName
                            additionalParams[2],  // email
                            additionalParams[3],  // phone
                            additionalParams[4]); // password
                }
            case "toggle promotions":
                return profileController.promotionNewsSubscription(customer);
            default:
                System.out.println("Error: Invalid action provided");
                return false;
        }

    }

    public boolean modifyReservation(String bookingId, String newFlightNumber, String newSeatNumber,
                                     BookingController bookingController, FlightController flightController) {


        System.out.println("Looking for booking: " + bookingId);

        List<Booking> allBookings = bookingController.getAllBookings();
        System.out.println("Total bookings available: " + allBookings.size());

        Booking bookingToModify = null;

        for (Booking booking : allBookings) {
            if (booking.getBookingId().equals(bookingId)) {
                bookingToModify = booking;
                break;
            }
        }

        if (bookingToModify == null) {
            System.out.println("Error: Booking not found");
            return false;
        }

        Customer customer = bookingToModify.getCustomer();
        Flight newFlight = flightController.getFlight(newFlightNumber);

        if (newFlight == null) {
            System.out.println("Error: Flight not found");
            return false;
        }
        System.out.println("Available seats on flight " + newFlightNumber + ":");
        for (Seat seat : newFlight.getAvailableSeatsList()) {
            System.out.println("  " + seat.getSeatNumber() + " - " + seat.getSeatType());
        }

        Seat newSeat = null;
        for (Seat seat : newFlight.getAvailableSeatsList()) {
            if (seat.getSeatNumber().equals(newSeatNumber)) {
                newSeat = seat;
                break;
            }
        }

        if (newSeat == null) {
            System.out.println("Error: Seat not available");
            return false;
        }

        System.out.print("Confirm modification? (yes/no): ");
        Scanner scanner = new Scanner(System.in);
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("yes")) {
            return false;
        }

        bookingController.cancelBooking(bookingId, customer);
        Booking newBooking = bookingController.addBooking(customer, newFlightNumber, newSeatNumber, newSeat.getPrice());

        return newBooking != null;
    }

    public void viewSchedules() {

        System.out.println("Viewing flight schedules for assigned airlines: " + assignedAirlines);
    }

    public boolean cancelReservation(String bookingId, BookingController bookingController, UserController userController) {
        List<Booking> allBookings = bookingController.getAllBookings();
        Booking bookingToCancel = null;

        for (Booking booking : allBookings) {
            if (booking.getBookingId().equals(bookingId)) {
                bookingToCancel = booking;
                break;
            }
        }

        if (bookingToCancel == null) {
            System.out.println("Error: Booking not found");
            return false;
        }

        System.out.print("Confirm cancellation? (yes/no): ");
        Scanner scanner = new Scanner(System.in);
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("yes")) {
            return false;
        }

        boolean success = bookingController.cancelBooking(bookingId, bookingToCancel.getCustomer());
        return success;
    }

    public Booking createReservation(String customerUsername, String flightNumber, String seatNumber,
                                     BookingController bookingController, UserController userController,
                                     FlightController flightController) {

        User user = userController.getUser(customerUsername);
        if (user == null || !(user instanceof Customer)) {
            System.out.println("Error: Customer not found");
            return null;
        }

        Customer customer = (Customer) user;

        Flight flight = flightController.getFlight(flightNumber);
        if (flight == null) {
            System.out.println("Error: Flight not found");
            return null;
        }

        Seat selectedSeat = null;
        for (Seat seat : flight.getAvailableSeatsList()) {
            if (seat.getSeatNumber().equals(seatNumber)) {
                selectedSeat = seat;
                break;
            }
        }

        if (selectedSeat == null) {
            System.out.println("Error: Seat not available");
            return null;
        }

        System.out.print("Confirm reservation? (yes/no): ");
        Scanner scanner = new Scanner(System.in);
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Reservation cancelled");
            return null;
        }

        Booking booking = bookingController.addBooking(customer, flightNumber, seatNumber, selectedSeat.getPrice());

        if (booking != null) {
            System.out.println("Reservation created. Booking ID: " + booking.getBookingId());
        }

        return booking;
    }


    public void addAssignedAirline(String airline) {
        if (!assignedAirlines.contains(airline)) {
            assignedAirlines.add(airline);
            System.out.println("Added " + airline + " to assigned airlines");
        }
    }

    public boolean canManageAirline(String airlineCode) {
        return assignedAirlines.contains(airlineCode);
    }


    public String getEmployeeID()
    {
        return employeeID;
    }
    public void setEmployeeID(String employeeID)
    {
        this.employeeID = employeeID;
    }
    public String getAirline()
    {
        return airline;
    }
    public void setAirline(String airline)
    {
        this.airline = airline;
    }
    public List<String> getAssignedAirlines()
    {
        return assignedAirlines;
    }
    public void setAssignedAirlines(List<String> assignedAirlines)
    {
        this.assignedAirlines = assignedAirlines;
    }

    @Override
    public Map<String, Object> getUserInfo() {
        Map<String, Object> userInfo = super.getUserInfo();
        userInfo.put("employeeID", employeeID);
        userInfo.put("airline", airline);
        userInfo.put("assignedAirlines", assignedAirlines);
        return userInfo;
    }
}