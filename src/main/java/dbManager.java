import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dbManager {
    //SET UP
    private static final String URL = "jdbc:mysql://localhost:3306/flight_reservation_db";
    private static final String USER = "root";
    private static final String PASS = "root123";

    private static dbManager instance;

    private dbManager() {

    }

    public static dbManager getInstance() {
        if (instance == null) {
            instance = new dbManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    //load data into program
    public Airline getAirlineByID(String airlineID) throws SQLException {
        String airlineSQL = "SELECT * FROM Airline WHERE airline_id = ?";
        String loyaltySQL = "SELECT rule_type, rule FROM Loyalty_points_rules WHERE airline_id = ?";
        String baggageSQL = "SELECT rule_type, rule FROM Baggage_rules WHERE airline_id = ?";
        String policiesSQL = "SELECT rule_type, rule FROM Policies WHERE airline_id = ?";

        String name = null;
        Map<String, String> loyaltyMap = new HashMap<>();
        Map<String, String> baggageMap = new HashMap<>();
        Map<String, String> policiesMap = new HashMap<>();


        try (Connection conn = getConnection()) {
            //fill in name
            try (PreparedStatement stmt = conn.prepareStatement(airlineSQL)) {
                stmt.setString(1, airlineID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        name = rs.getString("airline_name");
                    }
                }
            }

            if (name == null) {
                return null;
            }
            //fill in loyalty rules
            try (PreparedStatement stmt = conn.prepareStatement(loyaltySQL)) {
                stmt.setString(1, airlineID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String rule_type = rs.getString("rule_type");
                        String rule = rs.getString("rule");
                        loyaltyMap.put(rule_type, rule);
                    }
                }
            }
            //fill in baggage rules
            try (PreparedStatement stmt = conn.prepareStatement(baggageSQL)) {
                stmt.setString(1, airlineID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String rule_type = rs.getString("rule_type");
                        String rule = rs.getString("rule");
                        baggageMap.put(rule_type, rule);
                    }

                }
            }
            //fill in policies
            try (PreparedStatement stmt = conn.prepareStatement(policiesSQL)) {
                stmt.setString(1, airlineID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String rule_type = rs.getString("rule_type");
                        String rule = rs.getString("rule");
                        policiesMap.put(rule_type, rule);
                    }

                }
            }
        }
        return new Airline(name, airlineID, loyaltyMap, baggageMap, policiesMap);
    }

    public List<Flight> getFlights() throws SQLException {
        List<Flight> flights = new ArrayList<>();

        String sql = "SELECT * FROM Flight";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                Flight flight = new Flight();

                String airlineID = rs.getString("airline_id");
                Airline airline = getAirlineByID(airlineID);
                flight.setAirline(airline);

                flight.setRouteId(rs.getString("route_id"));
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setBasePrice(rs.getDouble("base_price"));
                flight.setTotalSeats(rs.getInt("total_seats"));
                flight.setAvailableSeats(rs.getInt("available_seats"));
                flight.setOrigin(rs.getString("origin"));
                flight.setDestination(rs.getString("destination"));
                flight.setDepartureTime(rs.getTime("departure_time"));
                flight.setArrivalTime(rs.getTime("arrival_time"));
                flight.setFlightDate(rs.getDate("flight_date"));
                flight.setStatus(rs.getString("flight_status"));

                List<Seat> seatList = getSeats(flight.getFlightNumber());
                flight.setSeatsFromDB(seatList);

                flights.add(flight);
            }
        }
        return flights;
    }

    public List<Seat> getSeats(String flightNumber) throws SQLException {
        List<Seat> seats = new ArrayList<>();
        String seatSQL = "SELECT seat_number, seat_type, is_available, price FROM Seat WHERE flight_number = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(seatSQL)) {
            stmt.setString(1, flightNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String seatType = rs.getString("seat_type");
                    boolean isAvailable = rs.getBoolean("is_available");
                    double price = rs.getDouble("price");
                    String seatNumber = rs.getString("seat_number");

                    Seat seat = new Seat(seatType, seatNumber, isAvailable, price);
                    seats.add(seat);
                }
            }
        }
        return seats;
    }

    public List<String> getBookingHistory(String customerEmail) throws SQLException {
        List<String> bookingHistory = new ArrayList<>();

        String historySQL = """
                SELECT customer_history
                FROM Booking_history
                WHERE customer_email = ?
                ORDER BY booking_history_id
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(historySQL)) {
            stmt.setString(1, customerEmail);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String history = rs.getString("customer_history");
                    bookingHistory.add(history);
                }
            }
        }
        return bookingHistory;
    }

    public List<Customer> getCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();

        String customerSQL = """
                SELECT u.customer_email, u.username, u.customer_password, u.phone_number, u.customer_role,
                c.first_name, c.last_name, c.promotion, c.is_guest
                FROM User_ u
                JOIN Customer c ON u.username = c.username
                WHERE u.customer_role = 'Customer'
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(customerSQL)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String email = rs.getString("customer_email");
                    String password = rs.getString("customer_password");
                    String role = rs.getString("customer_role");
                    String username = rs.getString("username");
                    String phoneNumber = rs.getString("phone_number");

                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    boolean promotion = rs.getBoolean("promotion");
                    boolean isGuest = rs.getBoolean("is_guest");

                    Customer customer = new Customer(username, password, email, phoneNumber, firstName, lastName);
                    customer.setPromotionNewsSubscriber(promotion);
                    customer.setGuestUser(isGuest);

                    List<String> history = getBookingHistory(email);
                    customer.getBookingHistory().addAll(history);

                    customers.add(customer);
                }

                return customers;
            }
        }
    }

    public List<FlightAgent> getFlightAgents() throws SQLException {
        List<FlightAgent> agents = new ArrayList<>();

        String sql = """
        
                SELECT u.customer_email, u.username, u.customer_password, u.phone_number, 
               u.customer_role, u.employee_id, u.airline
        FROM User_ u
        WHERE u.customer_role = 'FlightAgent'
        """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String email = rs.getString("customer_email");
                String username = rs.getString("username");
                String password = rs.getString("customer_password");
                String phone = rs.getString("phone_number");
                String employeeID = rs.getString("employee_id");
                String airline = rs.getString("airline");

                FlightAgent agent = new FlightAgent(username, password, email, phone, employeeID, airline);
                agents.add(agent);
            }
        }
        return agents;
    }


    public List<SystemAdministrator> getSystemAdministrators() throws SQLException {
        List<SystemAdministrator> admins = new ArrayList<>();

        String sql =
                """
        SELECT u.customer_email, u.username, u.customer_password, u.
                phone_number, 
               u.customer_role, u.
                employee_id, u.
                airline
        FROM User_ u
        WHERE u.
                customer_role = 'SystemAdministrator'
        """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String email = rs.getString("customer_email");
                String username = rs.getString("username");
                String password = rs.getString("customer_password");
                String phone = rs.getString("phone_number");

                SystemAdministrator admin = new SystemAdministrator(username, password, email, phone);
                admins.add(admin);
            }
        }
        return admins;
    }


    public Customer getCustomerByEmail(String email) throws SQLException {
        String customerEmailSQL = """
                SELECT u.customer_email, u.username, u.customer_password, u.phone_number, u.customer_role,
                c.first_name, c.last_name, c.promotion, c.is_guest
                FROM User_ u
                JOIN Customer c ON u.username = c.username
                WHERE u.customer_email = ?
                """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(customerEmailSQL)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                String email2 = rs.getString("customer_email");
                String password2 = rs.getString("customer_password");
                String role2 = rs.getString("customer_role");
                String username = rs.getString("username");
                String phoneNumber2 = rs.getString("phone_number");

                String firstName2 = rs.getString("first_name");
                String lastName2 = rs.getString("last_name");
                boolean promotion2 = rs.getBoolean("promotion");
                boolean isGuest2 = rs.getBoolean("is_guest");

                Customer customer = new Customer(username, password2, email2, phoneNumber2, firstName2, lastName2);
                customer.setPromotionNewsSubscriber(promotion2);
                customer.setGuestUser(isGuest2);

                List<String> history = getBookingHistory(email2);
                customer.getBookingHistory().addAll(history);

                return customer;

            }
        }
    }

    //This func helps in get bookings
    public Flight getFlightByNumber(String flightNumber) throws SQLException {
        String flightNumberSQL = "SELECT * FROM Flight WHERE flight_number = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(flightNumberSQL)) {
            stmt.setString(1, flightNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Flight flight = new Flight();

                String airlineID = rs.getString("airline_id");
                Airline airline = getAirlineByID(airlineID);
                flight.setAirline(airline);

                flight.setRouteId(rs.getString("route_id"));
                flight.setFlightNumber(rs.getString("flight_number"));
                flight.setBasePrice(rs.getDouble("base_price"));
                flight.setTotalSeats(rs.getInt("total_seats"));
                flight.setAvailableSeats(rs.getInt("available_seats"));
                flight.setOrigin(rs.getString("origin"));
                flight.setDestination(rs.getString("destination"));
                flight.setDepartureTime(rs.getTime("departure_time"));
                flight.setArrivalTime(rs.getTime("arrival_time"));
                flight.setFlightDate(rs.getDate("flight_date"));
                flight.setStatus(rs.getString("flight_status"));

                List<Seat> seatList = getSeats(flight.getFlightNumber());
                flight.setSeatsFromDB(seatList);



                return flight;
            }
        }
    }

    public List<Booking> getBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();

        String BookingSQL = "SELECT * FROM Booking";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(BookingSQL)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    //customer
                    String customerEmail = rs.getString("customer_email");
                    Customer customer = getCustomerByEmail(customerEmail);
                    booking.setCustomer(customer);
                    //flight
                    String flightNumber = rs.getString("flight_number");
                    Flight flight = getFlightByNumber(flightNumber);
                    booking.setFlight(flight);
                    //seat
                    String seatNumber = rs.getString("seat_number");
                    Seat seat = null;
                    if (flight != null) {
                        seat = flight.getSeat(seatNumber);
                    }
                    booking.setSeat(seat);

                    Date bookingDate = rs.getDate("booking_date");
                    booking.setBookingDate(bookingDate);

                    String bookingId = String.valueOf(rs.getInt("booking_id"));
                    booking.setBookingId(bookingId);

                    String bookingStatus = rs.getString("booking_status");
                    booking.setStatus(bookingStatus);

                    double totalPrice = rs.getDouble("total_price");
                    booking.setTotalPrice(totalPrice);

                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }

    public boolean insertBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO Booking (flight_number, customer_email, seat_number, booking_date, booking_status, total_price) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, booking.getFlight().getFlightNumber());
            stmt.setString(2, booking.getCustomer().getEmail());
            stmt.setString(3, booking.getSeat().getSeatNumber());
            stmt.setDate(4, new java.sql.Date(booking.getBookingDate().getTime()));
            stmt.setString(5, booking.getStatus());
            stmt.setDouble(6, booking.getTotalPrice());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }


    public boolean updateSeatAvailability(String flightNumber, String seatNumber, boolean isAvailable) throws SQLException {
        String sql = "UPDATE Seat SET is_available = ? WHERE flight_number = ? AND seat_number = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, isAvailable);
            stmt.setString(2, flightNumber);
            stmt.setString(3, seatNumber);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }


    public boolean addBookingHistory(String customerEmail, String history) throws SQLException {
        String sql = "INSERT INTO Booking_history (customer_email, customer_history) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerEmail);
            stmt.setString(2, history);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }


// In dbManager.java
    public boolean updateBookingStatus(String bookingId, String newStatus) throws SQLException {
        String sql = "UPDATE Booking SET booking_status = ? WHERE booking_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, Integer.parseInt(bookingId));
            return stmt.executeUpdate() > 0;
        }
    }


    public boolean insertUser(User user) throws SQLException {
        String sql = "INSERT INTO User_ (customer_email, username, customer_password, phone_number, customer_role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getRole());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }


    public boolean insertCustomerDetails(Customer customer) throws SQLException {
        String sql = "INSERT INTO Customer (username, first_name, last_name, promotion, is_guest) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getUsername());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getLastName());
            stmt.setBoolean(4, customer.isPromotionNewsSubscriber());
            stmt.setBoolean(5, customer.isGuestUser());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    public boolean executeTransaction(List<Runnable> operations) throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            for (Runnable op : operations) {

            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }


    public Connection beginTransaction() throws SQLException {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
        return conn;
    }

    public void commitTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.commit();
            conn.setAutoCommit(true);
            conn.close();
        }
    }


    public void rollbackTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.rollback();
            conn.setAutoCommit(

    true);
            conn.close();
        }
    }
    public boolean updateUserProfile(Customer customer, String oldEmail, String username) throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            boolean emailChanged = !oldEmail.equals(customer.getEmail());


            String updateUserSQL = "UPDATE User_ SET customer_email = ?, customer_password = ?, phone_number = ? WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateUserSQL)) {
                stmt.setString(1, customer.getEmail());
                stmt.setString(2, customer.getPassword());
                stmt.setString(3, customer.getPhoneNumber());
                stmt.setString(4, username);
                stmt.executeUpdate();
            }


            String updateCustomerSQL = "UPDATE Customer SET first_name = ?, last_name = ? WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateCustomerSQL)) {
                stmt.setString(1, customer.getFirstName());
                stmt.setString(2, customer.getLastName());
                stmt.setString(3, username);
                stmt.executeUpdate();
            }


            if (emailChanged) {
                String updateHistorySQL = "UPDATE Booking_history SET customer_email = ? WHERE customer_email = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateHistorySQL)) {
                    stmt.setString(1, customer.getEmail());
                    stmt.setString(2, oldEmail);
                    stmt.executeUpdate();
                }

                String updateBookingSQL = "UPDATE Booking SET customer_email = ? WHERE customer_email = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateBookingSQL)) {
                    stmt.setString(1, customer.getEmail());
                    stmt.setString(2, oldEmail);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    public boolean addFlightToDB(Flight flight) throws SQLException {
        String sql = "INSERT INTO Flight (flight_number, route_id, airline_id, origin, destination, departure_time, arrival_time, flight_date, base_price, available_seats, total_seats, flight_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, flight.getFlightNumber());
            stmt.setString(2, flight.getRouteId());

            Airline airline = flight.getAirlineObject();
            if (airline == null) {
                System.out.println("Error: Airline is null");
                return false;
            }
            stmt.setString(3, airline.getAirlineID());

            stmt.setString(4, flight.getOrigin());
            stmt.setString(5, flight.getDestination());
            stmt.setTime(6, new java.sql.Time(flight.getDepartureTime().getTime()));
            stmt.setTime(7, new java.sql.Time(flight.getArrivalTime().getTime()));
            stmt.setDate(8, new java.sql.Date(flight.getFlightDate().getTime()));
            stmt.setDouble(9, flight.getBasePrice());
            stmt.setInt(10, flight.getAvailableSeats());
            stmt.setInt(11, flight.getTotalSeats());
            stmt.setString(12, flight.getStatus());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Create seats for this flight
                addSeatsForFlight(flight.getFlightNumber(), flight.getTotalSeats(), flight.getBasePrice());
                return true;
            }
            return false;
        }
    }

    /**
     * Remove a flight from database
     */
    public boolean removeFlightFromDB(String flightNumber) throws SQLException {
        String sql = "DELETE FROM Flight WHERE flight_number = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, flightNumber);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Create seats for a new flight
     */
    public boolean addSeatsForFlight(String flightNumber, int totalSeats, double basePrice) throws SQLException {
        String[] seatTypes = {"Economy", "Premium Economy", "Business", "First Class"};
        double[] typePrices = {0.0, 50.0, 150.0, 300.0};

        int seatsPerType = totalSeats / seatTypes.length;
        String sql = "INSERT INTO Seat (seat_number, seat_type, is_available, price, flight_number) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < seatTypes.length; i++) {
                for (int j = 1; j <= seatsPerType; j++) {
                    String seatNumber = seatTypes[i].charAt(0) + "-" + j;
                    double seatPrice = basePrice + typePrices[i];

                    stmt.setString(1, seatNumber);
                    stmt.setString(2, seatTypes[i]);
                    stmt.setBoolean(3, true);
                    stmt.setDouble(4, seatPrice);
                    stmt.setString(5, flightNumber);

                    stmt.addBatch();
                }
            }
            stmt.executeBatch();
            return true;
        }
    }

}