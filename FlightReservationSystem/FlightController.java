import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class FlightController {
    private List<Flight> flights;
    private dbManager dbManager;

    public FlightController() {
        this.flights = new ArrayList<>();
        this.dbManager = dbManager.getInstance();
        loadFlightsFromDatabase();
    }

    private void loadFlightsFromDatabase() {
        try {
            this.flights = dbManager.getFlights();
        } catch (SQLException e) {
            System.out.println("Error loading flights");
            this.flights = new ArrayList<>();
        }
    }

    public boolean addFlight(String flightNumber ,String routeId,Airline airline, String origin, String destination,Date flightDate ,Date departureTime, Date arrivalTime, double basePrice, int totalSeats) {
        if (flightNumber == null || flightNumber.trim().isEmpty()) {
            System.out.println("Error: Flight number required");
            return false;
        }

        if (getFlight(flightNumber) != null) {
            System.out.println("Error: Flight already exists");
            return false;
        }

        if (airline == null) {
            System.out.println("Error: Airline required");
            return false;
        }

        if (totalSeats <= 0) {
            System.out.println("Error: Total seats must be positive");
            return false;
        }

        if (basePrice < 0) {
            System.out.println("Error: Base price cannot be negative");
            return false;
        }

        if (departureTime == null || arrivalTime == null || flightDate == null) {
            System.out.println("Error: Date/time required");
            return false;
        }

        if (departureTime.after(arrivalTime)) {
            System.out.println("Error: Departure time cannot be after arrival time");
            return false;
        }

        Flight flight = new Flight(flightNumber, routeId, airline, origin,destination, departureTime, arrivalTime,flightDate, basePrice,totalSeats);
        try {
            boolean saved = dbManager.addFlightToDB(flight);
            if (!saved) {
                System.out.println("Failed to save flight to database");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
        flights.add(flight);
        System.out.println("Flight added: " + flightNumber);
        return true;
    }

    public boolean removeFlight(String flightNumber) {
        if (flightNumber == null || flightNumber.trim().isEmpty()) {
            System.out.println("Error: Flight number required");
            return false;
        }
        try {
            boolean removed = dbManager.removeFlightFromDB(flightNumber);
            if (!removed) {
                System.out.println("Failed to remove flight from database");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }


        for (Flight flight: flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                flights.remove(flight);
                System.out.println("Flight removed: " + flightNumber);
                return true;
            }
        }

        System.out.println("Flight not found: " + flightNumber);
        return false;
    }

    public List<Flight> searchFlights(String origin, String destination, Date date) {
        List<Flight> results = new ArrayList<>();
        for (Flight flight: flights) {
            boolean matches = true;
            if (origin != null && !flight.getOrigin().equalsIgnoreCase(origin)) matches = false;
            if (destination != null && !flight.getDestination().equalsIgnoreCase(destination)) matches = false;
            if (date != null && !flight.getFlightDate().equals(date)) matches = false;

            if (matches) {
                results.add(flight);
            }
        }
        return results;
    }

    public void getFlightDetails(String flightNumber) {
        for (Flight flight: flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                System.out.println("Flight: " + flight.getFlightNumber());
                System.out.println("Airline: " + flight.getAirline());
                System.out.println("Route: " + flight.getOrigin() + " -> " + flight.getDestination());
                System.out.println("Date: " + flight.getFlightDate());
                System.out.println("Time: " + flight.getDepartureTime() + " - " + flight.getArrivalTime());
                System.out.println("Seats: " + flight.getAvailableSeats() + "/" + flight.getTotalSeats());
                System.out.println("Status: " + flight.getStatus());
                return;
            }
        }
        System.out.println("Flight not found");
    }

    public boolean updateSeatAvailability(String flightNumber, String seatId, boolean available) {
        if (flightNumber == null || seatId == null) {
            System.out.println("Error: Flight number or seat ID required");
            return false;
        }

        Flight flight = getFlight(flightNumber);
        if (flight == null) {
            return false;
        }

        return flight.updateSeatAvailability(seatId, available);
    }

    public List<Flight> retrieveFlights() {
        return new ArrayList<>(flights);
    }

    public List<Flight> getFlightByRoute(String routeId){
        List<Flight> routeFlights = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getRouteId().equals(routeId)) {
                routeFlights.add(flight);
            }
        }
        return routeFlights;
    }

    public Flight getFlight(String flightNumber) {
        if (flightNumber == null || flightNumber.isEmpty()) {
            return null;
        }

        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }
}