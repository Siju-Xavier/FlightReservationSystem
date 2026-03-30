import java.util.HashMap;
import java.util.Map;

public class AirlineController {
    private Map<String, Airline> airlines;
    public AirlineController() {
        this.airlines = new HashMap<>();
    }

    public Airline getAirline(String airlineId) {
        if (airlineId == null || airlineId.trim().isEmpty()) {
            System.out.println("Error: Airline ID cannot be null or empty");
            return null;
        }
        return airlines.get(airlineId);
    }

    public Map<String, Airline> getListofAirlines() {
        return new HashMap<>(airlines);
    }
    public void displayAirlineInfo(String airlineId) {
        Airline airline = getAirline(airlineId);
        if (airline == null) {
            System.out.println("Airline not found");
            return;
        }
        System.out.println("\n---  Welcome to " + airline.getName() + " ---");
        System.out.println("Airline ID: " + airline.getAirlineID());;
        System.out.println("Baggage Rules: " + airline.getBaggageRules());
        System.out.println("Policies: " + airline.getPolicies());
    }

}