import java.sql.SQLException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class BookingController {
    private static final double PRICE_TOLERANCE = 0.01;
    private List<Booking> bookings;
    private FlightController flightController;
    private PaymentController paymentController;
    private dbManager dbManager;

    public BookingController(List<Booking> bookings, FlightController flightController) {
        this.bookings = (bookings != null) ? new ArrayList<>(bookings) : new ArrayList<>();
        this.flightController = flightController;
        this.paymentController = new PaymentController();
        this.dbManager = dbManager.getInstance();
        loadBookingsFromDatabase();
    }
    private void loadBookingsFromDatabase() {
        try {
            this.bookings = dbManager.getBookings();
            System.out.println("Loaded " + bookings.size() + " bookings from database");
        } catch (SQLException e) {
            System.out.println("Error loading bookings from database: " + e.getMessage());
            this.bookings = new ArrayList<>();
        }
    }

    public Booking addBooking(Customer customer, String flightNumber, String seatNumber, double price) {
        if (customer == null || flightNumber == null || seatNumber == null) {
            System.out.println("Error: Customer, flight number, or seat number is null");
            return null;
        }

        Flight flight = flightController.getFlight(flightNumber);
        if (flight == null) {
            System.out.println("Error: Flight not found - " + flightNumber);
            return null;
        }


        Seat seat = null;
        List<Seat> availableSeats = flight.getAvailableSeatsList();
        for (Seat s : availableSeats) {
            if (s.getSeatNumber().equals(seatNumber)) {
                seat = s;
                break;
            }
        }

        if (seat == null || !seat.isAvailable()) {
            System.out.println("Error: Seat is not available or doesn't exist");
            System.out.println("Refunding Payment");
            return null;
        }

        // Verify the price matches the actual seat price

        if (Math.abs(seat.getPrice() - price) > PRICE_TOLERANCE) {
            System.out.println("Error: Price mismatch detected");
            return null;
        }

        Payment payment = new Payment(price, customer);
        boolean paymentSuccess = processPayment(payment);

        if (!paymentSuccess) {
            System.out.println("Error: Payment not successful");
            return null;
        }

        Booking booking = new Booking();
        booking.setBookingId(generateBookingId());
        booking.setCustomer(customer);
        booking.setFlight(flight);
        booking.setSeat(seat);
        booking.setTotalPrice(price);
        booking.setStatus("Confirmed");
        booking.setBookingDate(new Date());

        flight.updateSeatAvailability(seatNumber, false);
        try {
            dbManager.updateSeatAvailability(flightNumber, seatNumber, false);
            System.out.println("Seat " + seatNumber + " marked as unavailable in database");
        } catch (SQLException e) {
            System.out.println("Error updating seat availability in database: " + e.getMessage());
        }
        bookings.add(booking);
        seat.reserveSeat();
        saveBooking(booking);

        customer.getBookingHistory().add("Booking for flight " + flightNumber + ", seat " + seatNumber);

        System.out.println("Booking created successfully for flight " + flightNumber);
        return booking;
    }

    private boolean processPayment(Payment payment) {
        try {
            System.out.println("Processing Payment " + payment.getAmount());
            payment.checkout();
            System.out.println("Payment successfully processed");
            return true;
        } catch (Exception e) {
            System.out.println("Payment not successfully processed " + e.getMessage());
            return false;
        }
    }

    public List<Booking> searchBookings(String search) {
        List<Booking> bookings = new ArrayList<>();
        System.out.println("Searching bookings for : " + search);
        return bookings;
    }

    public boolean updateBooking(String bookingId, Flight newFlight, Seat seat)
    {
        Booking booking = getBooking(bookingId);
        if (booking == null) {
            return false;
        }

        System.out.println("Updating Booking " + bookingId);
        saveBooking(booking);
        return true;

    }

    public boolean cancelBooking(String bookingId, Customer customer) {
        Booking booking = getBooking(bookingId);
        if (booking == null) {
            return false;
        }

        System.out.println("Canceling Booking " + bookingId);
        System.out.println("Refunding Payment : " + booking.getTotalPrice());

        try {
            dbManager.updateBookingStatus(bookingId, "Cancelled");
            System.out.println("Booking status updated in database");
        } catch (SQLException e) {
            System.out.println("Error updating database: " + e.getMessage());
        }

        Flight flight = booking.getFlight();
        if (flight != null && booking.getSeat() != null) {
            String flightNumber = flight.getFlightNumber();
            String seatNumber = booking.getSeat().getSeatNumber();

            // Update in-memory
            flight.updateSeatAvailability(seatNumber, true);

        try {
            dbManager.updateSeatAvailability(flightNumber, seatNumber, true);
            System.out.println("Seat " + seatNumber + " marked as available in database");
        } catch (SQLException e) {
            System.out.println("Error updating seat availability: " + e.getMessage());
        }
    }
        bookings.remove(booking);
        return true;
    }


    public List<Booking> getBookings(Customer customer) {
        List<Booking> customerBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getCustomer().getUsername().equals(customer.getUsername())
                    && !"Cancelled".equals(booking.getStatus())) {  // ← EXCLUDE CANCELLED
                customerBookings.add(booking);
            }
        }
        System.out.println("Found " + customerBookings.size() + " bookings for: " + customer.getUsername());
        return customerBookings;
    }

    public Booking getBooking(String bookingId) {
        if (bookingId == null) {
            return null;
        }

        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                return booking;
            }
        }
        return null;
    }

    private void saveBooking(Booking booking) {
        try {
            boolean success = dbManager.insertBooking(booking);
            if (success) {
                System.out.println("Booking saved to database: " + booking.getBookingId());
            } else {
                System.out.println("Failed to save booking to database");
            }
        } catch (SQLException e) {
            System.out.println("Database error saving booking: " + e.getMessage());
        }
    }

    private void displayBookings(List<Booking> bookings) {
        if (bookings == null) {
            System.out.println("Bookings is null");
        }

        System.out.println("\n--- Booking History ---");
        for (int i = 0; i < bookings.size(); i++) {
            System.out.println(bookings.get(i));
        }
    }
    private String generateBookingId() {
        return "BK" + (System.currentTimeMillis() % 1000000);
    }
    // In BookingController.java
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }
}


