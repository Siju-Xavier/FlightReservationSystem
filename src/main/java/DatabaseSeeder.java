import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSeeder {
    public static void main(String[] args) {
        System.out.println("Starting database seeder...");
        try (Connection conn = dbManager.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. Add Airlines (Using INSERT IGNORE so it won't crash if they already exist)
            String insertAirlines = "INSERT IGNORE INTO Airline (airline_id, airline_name) VALUES " +
                    "('AC', 'Air Canada'), " +
                    "('WS', 'WestJet'), " +
                    "('UA', 'United Airlines')";
            stmt.executeUpdate(insertAirlines);
            System.out.println("Airlines added.");

            // 2. Add Flights
            String insertFlights = "INSERT IGNORE INTO Flight (flight_number, route_id, airline_id, origin, destination, departure_time, arrival_time, flight_date, base_price, available_seats, total_seats, flight_status) VALUES " +
                    "('AC101', 'YYC-YYZ', 'AC', 'Calgary (YYC)', 'Toronto (YYZ)', '10:00:00', '14:30:00', '2025-05-15', 350.00, 150, 150, 'On Time')," +
                    "('WS202', 'YYC-YVR', 'WS', 'Calgary (YYC)', 'Vancouver (YVR)', '12:00:00', '13:30:00', '2025-05-16', 220.00, 120, 120, 'On Time')," +
                    "('UA303', 'YYC-LAX', 'UA', 'Calgary (YYC)', 'Los Angeles (LAX)', '08:00:00', '11:45:00', '2025-05-17', 450.00, 180, 180, 'Scheduled')," +
                    "('AC404', 'YYZ-LHR', 'AC', 'Toronto (YYZ)', 'London (LHR)', '20:00:00', '08:00:00', '2025-05-18', 850.00, 250, 250, 'On Time')";
            stmt.executeUpdate(insertFlights);
            System.out.println("Flights added.");

            // 3. Add Test User
            String insertUser = "INSERT IGNORE INTO User_ (customer_email, username, customer_password, phone_number, customer_role) VALUES " +
                    "('test@example.com', 'testuser', 'password123', '555-0199', 'Customer')";
            stmt.executeUpdate(insertUser);

            String insertCustomer = "INSERT IGNORE INTO Customer (username, first_name, last_name, promotion, is_guest) VALUES " +
                    "('testuser', 'Test', 'User', FALSE, FALSE)";
            stmt.executeUpdate(insertCustomer);
            System.out.println("Test User added.");

            System.out.println("✅ Database successfully seeded with flights and users!");

        } catch (Exception e) {
            System.err.println("Error seeding database:");
            e.printStackTrace();
        }
    }
}
