import java.util.*;


public class Flight {

    private Map<String, Seat> seats;
    private String flightNumber;
    private String routeId;
    private Airline airline;
    private String origin;
    private String destination;
    private Date departureTime;
    private Date arrivalTime;
    private Date flightDate;
    private double basePrice;
    private int availableSeats;
    private int totalSeats;
    //The status can be scheduled, Booking Open, Booking Closed, Boarding, Delayed, Cancelled, Departed, Arrived
    private String status;


    public Flight(String flightNumber, String routeId, Airline airline, String origin, String destination
            , Date DepartureTime, Date arrivalTime, Date flightDate, double basePrice, int totalSeats) {
        this.flightNumber = flightNumber;
        this.routeId = routeId;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = DepartureTime;
        this.arrivalTime = arrivalTime;
        this.flightDate = flightDate;
        this.basePrice = basePrice;
        this.totalSeats = totalSeats;
        //Initially available seats is the number of total seats
        this.availableSeats = totalSeats;
        this.seats = new HashMap<>();
        this.status = "Scheduled";
        initializeSeats(totalSeats);
    }

    private void initializeSeats(int totalSeats) {
        String[] seatTypes = {"Economy", "Premium Economy", "Business", "First Class"};
        double[] typePrices = {0.0, 50.0, 150.0, 300.0}; // Price premiums over base price

        int seatsPerType = totalSeats / seatTypes.length;

        for (int i = 0; i < seatTypes.length; i++) {
            for (int j = 1; j <= seatsPerType; j++) {
                String seatNumber = seatTypes[i].charAt(0) + "-" + j;
                double seatPrice = basePrice + typePrices[i];
                Seat seat = new Seat(seatTypes[i], seatNumber, true, seatPrice);
                seats.put(seatNumber, seat);
            }
        }
    }

    public String getFlightNumber() {
        return flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getRouteId(){
        return routeId;
    }

    public void setRouteId(String routeId){
        this.routeId = routeId;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }


    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date DepartureTime) {
        this.departureTime = DepartureTime;
    }


    public Date getArrivalTime() {
        return arrivalTime;
    }


    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }


    public Date getFlightDate() {
        return flightDate;
    }


    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }


    public Airline getAirlineObject() {
        return airline;  // This returns the Airline object
    }

    public String getAirline() {
        if (airline != null) {
            return airline.getName();
        }
        return "Unknown";
    }
    public void setAirline(Airline airline) {
        this.airline = airline;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public boolean updateSeatAvailability(String seatID, boolean available){
        //Get seatID and see if it exists in the flight
        Seat seat  = seats.get(seatID);
        if(seat == null){
            return false;
        }

        boolean availability = seat.isAvailable(); //Check if that seat is available

        //If the availability (found above) and given availability are the same, then the seat availability is up to date... requires no changes
        if (availability == available) {
            return true;
        } else if (availability && !available) { //If availability is true but available is false (in reality it is available but we want to set it to not available)
            availableSeats--; //decrease number of seats available
            seat.reserveSeat(); //use reserveSeat function to set availability of seat to false
            return true;
        } else {
            availableSeats++;
            seat.cancelSeat();
            return true;
        }
    }

    public double getBasePrice(){
        return basePrice;
    }

    public double calculateBasePrice(String seatID) {
        Seat seat = seats.get(seatID);
        if (seat == null) {
            System.out.println("Error: No seat with ID: " + seatID);
            return basePrice;
        }

        double seatPrice = seat.getPrice();
        return seatPrice;
    }
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public List<Seat> getAvailableSeatsList(){
        List<Seat> availableSeatsList = new ArrayList<Seat>();
        for (Seat seat : seats.values()) {
            if (seat.isAvailable()) {
                availableSeatsList.add(seat);
            }
        }
        return availableSeatsList;
    }

    public int getTotalSeats(){
        return totalSeats;
    }
    public void setTotalSeats(int totalSeats){
        this.totalSeats = totalSeats;
    }
    public void setSeatsFromDB(List<Seat> seatList) {
        this.seats.clear();
        for (Seat seat : seatList) {
            this.seats.put(seat.getSeatNumber(), seat);
        }
    }
    public Seat getSeat(String seatNumber) {
        return seats.get(seatNumber);
    }

    public Flight() {
        this.seats = new HashMap<>(); // Add this if you have a no-arg constructor
    }


}
