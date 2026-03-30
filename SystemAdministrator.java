
import java.sql.SQLException;
import java.util.*;

public class SystemAdministrator extends User {
    private List<String> permissions;
    private dbManager dbManager;

    public SystemAdministrator(String username,String password, String email, String phoneNumber) {
        super(username, password, email, phoneNumber, "Administrator");
        this.permissions = new ArrayList<>();
        this.dbManager = dbManager.getInstance();
        this.permissions.add("manage users");
        this.permissions.add("manage flights");
        this.permissions.add("manage airlines");
        this.permissions.add("system config");
    }


    public boolean addFlight(String flightNumber, String routeId, Airline airline,
                             String origin, String destination, Date flightDate,
                             Date departureTime, Date arrivalTime, double basePrice,
                             int totalSeats, FlightController flightController) {

        return flightController.addFlight(flightNumber, routeId, airline, origin,
                destination, flightDate, departureTime,
                arrivalTime, basePrice, totalSeats);
    }

    public boolean removeFlight(String flightNumber, FlightController flightController) {
        return flightController.removeFlight(flightNumber);
    }



    public void updateSchedule(String flightNumber, String newSchedule, FlightController flightController) {
        Flight flight = flightController.getFlight(flightNumber);
        if (flight != null) {
            System.out.println("Flight " + flightNumber + " schedule updated");
            System.out.println("New schedule: " + newSchedule);
            // Would update in database
        } else {
            System.out.println("Flight not found");
        }
    }


    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    public void addPermission(String permission) {
        if (!permissions.contains(permission)) {
            permissions.add(permission);
        }
    }

    public void removePermission(String permission) {
        permissions.remove(permission);
    }


    public List<String> getPermissions()
    {
        return permissions;
    }
    public void setPermissions(List<String> permissions)
    {
        this.permissions = permissions;
    }

    @Override
    public Map<String, Object> getUserInfo() {
        Map<String, Object> userInfo = super.getUserInfo();
        userInfo.put("permissions", permissions);
        return userInfo;
    }
}